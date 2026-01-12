# /D:/test-generation/test.py - corrected + cleaned JSON
import json
import time
import re
from rag.qdrant_retrieve import retrieve_patterns
import ollama
from cache.test_cache import get_cached, store_cached

def endpoint_signature(endpoint):
    return f"{endpoint['method']}:{endpoint['path']}:{bool(endpoint['requestBody'])}"


def generate_tests_for_endpoint(endpoint, model="incept5/llama3.1-claude", max_retries=2):
    patterns = retrieve_patterns(endpoint["path"])
    if not patterns:
        pattern_text = "- No specific patterns found. Follow standard API testing best practices."

    else:
        pattern_text = "\n".join(
            [f"- {p.get('pattern_text', '')}" for p in patterns]
        )

    pattern_names = list(
        {p.get("pattern_type", "General") for p in patterns}
    )

    prompt = f"""
You are an expert API test case generator.

REFERENCE TESTING PATTERNS:
{pattern_text}

TASK:
Generate 5-6 API test cases as a JSON ARRAY.

Each test case MUST include:
- name
- request (path_params, query_params, headers, body)
- expected_status
- edge_case_reason

ENDPOINT DETAILS:
Method: {endpoint['method']}
Path: {endpoint['path']}
Description: {endpoint['description']}
Parameters: {json.dumps(endpoint['parameters'])}
RequestBody: {json.dumps(endpoint['requestBody'])}
Responses: {list(endpoint['responses'].keys())}

IMPORTANT:
- Follow the reference patterns strictly
- Return ONLY valid JSON
- No markdown
"""

    for attempt in range(max_retries):
        try:
            signature = endpoint_signature(endpoint)
            cached = get_cached(signature)
            
            if cached:
                print(f"[FAST-PATH] Using cached test cases for {signature}")
                return cached
            completion = ollama.chat(
                model=model,
                messages=[{"role": "user", "content": prompt}],
            )

            # content = completion["message"]["content"]
            content = completion["message"]["content"]

            if not content or not content.strip():
                    raise ValueError("Empty LLM response")
            match = re.search(r"```json\s*(.*?)\s*```", content, re.DOTALL)
            if match:
                content = match.group(1)
            if not content.strip().startswith("["):
                raise ValueError("Non-JSON response")
            try:
                tests_json = json.loads(content)
                result = {
                    "tests": tests_json,
                    "patterns_used": pattern_names
                }
                store_cached(signature, result)
                return result
            except json.JSONDecodeError:
                print(f"[WARN] Non-JSON response for {endpoint['path']}")
                return {
                    "tests": [],
                    "patterns_used": pattern_names
                }
        except Exception as e:
            if attempt + 1 == max_retries:
                print(f"Failed for {endpoint['path']}: {e}")
                return {
                    "tests": [],
                    "patterns_used": pattern_names
                }

            time.sleep(1)

# Generate tests for all endpoints and save to file
def main(file):
    # Load OpenAPI JSON
    with open(file, "r", encoding="utf-8") as f:
        openapi = json.load(f)

    # Build chunks from paths
    chunks = []
    for path, methods in openapi.get("paths", {}).items():
        for method, details in methods.items():
            desc = details.get("description") or ""
            chunks.append({
                "method": method.upper(),
                "path": path,
                "description": desc,
                "parameters": details.get("parameters", []),
                "requestBody": details.get("requestBody", {}),
                "responses": details.get("responses", {})
            })


    all_tests = []
    #path wise chuncking
    for endpoint in chunks:
        tests = generate_tests_for_endpoint(endpoint)

        all_tests.append({
            "endpoint": endpoint["path"],
            "method": endpoint["method"],
            "patterns_used": tests["patterns_used"],
            "tests": tests["tests"]
        })

    with open("generated_tests.json", "w", encoding="utf-8") as f:
        json.dump(all_tests, f, indent=2, ensure_ascii=False)

    print("Generated tests saved as clean JSON to generated_tests.json")    
     

if __name__ == "__main__":
    main("openapi.json")