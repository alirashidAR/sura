# /D:/test-generation/test.py - corrected + cleaned JSON

import os
import json
import time
import re

import openai
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity

# Use an environment variable for the API key
openai.api_key = "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"
if not openai.api_key:
    raise RuntimeError("OPENAI_API_KEY environment variable not set")

# Load OpenAPI JSON
with open("openapi.json", "r", encoding="utf-8") as f:
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


# Generate tests for an endpoint; parse JSON if possible
def generate_tests_for_endpoint(endpoint, model="gpt-4o", max_retries=2):
    prompt = (
        "You are an API test case generator. Generate 3-5 detailed test cases "
        "(input, expected output, edge cases) for this OpenAPI endpoint. "
        "Return the output as a JSON array of test case objects.\n\n"
        f"Method: {endpoint['method']}\n"
        f"Path: {endpoint['path']}\n"
        f"Description: {endpoint['description']}\n"
        f"Parameters: {json.dumps(endpoint['parameters'])}\n"
        f"RequestBody: {json.dumps(endpoint['requestBody'])}\n"
        f"Responses: {json.dumps(endpoint['responses'])}\n"
    )
    for attempt in range(max_retries):
        try:
            completion = openai.ChatCompletion.create(
                model=model,
                messages=[{"role": "user", "content": prompt}],
                temperature=0.2,
            )
            content = completion["choices"][0]["message"]["content"]

            # Remove Markdown fences if present
            match = re.search(r"```json\s*(.*?)\s*```", content, re.DOTALL)
            if match:
                content = match.group(1)

            try:
                return json.loads(content)
            except json.JSONDecodeError:
                return content  # fallback
        except Exception:
            if attempt + 1 == max_retries:
                raise
            time.sleep(1)
    return None

# Generate tests for all endpoints and save to file
all_tests = []
for endpoint in chunks:
    tests = generate_tests_for_endpoint(endpoint)
    all_tests.append({
        "endpoint": endpoint["path"],
        "method": endpoint["method"],
        "tests": tests
    })

with open("generated_tests.json", "w", encoding="utf-8") as f:
    json.dump(all_tests, f, indent=2, ensure_ascii=False)

print("Generated tests saved as clean JSON to generated_tests.json")
