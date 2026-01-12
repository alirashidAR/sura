import ollama
import json
import os
import re
from collections import defaultdict

 
# Read base URL from config file
 
def get_base_url(file_path):
    with open(file_path, "r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith("#"):
                continue
            if line.startswith("api.base.url="):
                return line.split("=", 1)[1]
    return None


 
# Normalize test cases
 
def normalize_tests(tests):
    if isinstance(tests, list):
        return tests

    if isinstance(tests, str):
        # Remove markdown code fences if present
        match = re.search(r"```(?:json)?\s*(.*?)\s*```", tests, re.DOTALL)
        if match:
            tests = match.group(1)

        try:
            return json.loads(tests)
        except Exception:
            return []

    return []


 
# Group ALL methods by PATH
 
def group_by_path(all_tests):
    grouped = defaultdict(list)

    for item in all_tests:
        endpoint = item.get("endpoint")
        method = item.get("method")

        tests = normalize_tests(item.get("tests", []))
        if not tests:
            continue

        grouped[endpoint].append({
            "method": method,
            "tests": tests,
            "patterns_used": item.get("patterns_used", [])
        })

    return grouped


 
# Generate TestNG Java code
 
def generate_testNg_code(path, method_blocks, baseUrl):
    """
    method_blocks = [
        { "method": "GET", "tests": [...] },
        { "method": "POST", "tests": [...] }
    ]
    """

    prompt = f"""
You are a senior Java QA Automation Engineer.

TASK:
Generate ONE complete and compilable Java TestNG test class
for the API path below.

IMPORTANT RULES (MUST FOLLOW STRICTLY):
- Generate ONLY ONE Java class per API PATH
- Include ALL HTTP METHODS (GET, POST, PUT, DELETE) in the SAME class
- If MORE THAN ONE test case exists for the SAME HTTP METHOD:
  - You MUST use TestNG @DataProvider
  - You MUST NOT create multiple @Test methods for the same HTTP method
  - You MUST reuse the same test logic with different input data
- Use @Test(dataProvider = "...") when applicable
- Use DataProvider to cover positive, negative, and edge cases
- Use Java 11+
- Use TestNG
- Use RestAssured
- Use @BeforeClass for setup
- Validate HTTP status codes at minimum
- Correctly handle path params, query params, headers, request body, and multipart if present
- Follow Java naming conventions
- DO NOT include explanations or markdown
- OUTPUT ONLY VALID JAVA CODE
- DO NOT RETURN ANYTHING OTHER THAN THE JAVA CODE
- If needed, add remarks ONLY as Java comments inside the code
- Add comments above test methods explaining which edge case is covered

BASE URL:
{baseUrl}

API PATH:
{path}

METHODS AND TEST CASES (JSON INPUT):
{json.dumps(method_blocks, indent=2)}

IMPLEMENTATION NOTES:
- Use RestAssured.baseURI for setup
- Use pathParam() for {{pathVariables}}
- Group test methods by HTTP method using Java comments
"""

    response = ollama.chat(
        model="incept5/llama3.1-claude",
        messages=[{"role": "user", "content": prompt}],
    )

    return response["message"]["content"]

# Removing Java markdown from the 1st and last line of the files

def remove_first_and_last_line(directory):
    for filename in os.listdir(directory):
        file_path = os.path.join(directory, filename)

        # Process only files (skip folders)
        if not os.path.isfile(file_path):
            continue

        with open(file_path, "r", encoding="utf-8") as f:
            lines = f.readlines()

        # Skip files that are too small
        if len(lines) <= 2:
            print(f"Skipped (too short): {filename}")
            continue

        # Remove first and last line
        new_lines = lines[1:-1]

        with open(file_path, "w", encoding="utf-8") as f:
            f.writelines(new_lines)

        print(f"Updated: {filename}")
 
# MAIN
 
def main(config_file):
    with open("generated_tests.json", "r", encoding="utf-8") as f:
        all_tests = json.load(f)

    baseUrl = get_base_url(config_file)
    if not baseUrl:
        raise RuntimeError("Base URL not found in config.properties")

    OUTPUT_DIR = "generated-testng"
    os.makedirs(OUTPUT_DIR, exist_ok=True)

    grouped_paths = group_by_path(all_tests)

    for path, method_blocks in grouped_paths.items():
        java_code = generate_testNg_code(path, method_blocks, baseUrl)

        safe_path = (
            path.strip("/")
            .replace("/", "_")
            .replace("{", "")
            .replace("}", "")
        )

        file_name = f"{safe_path.capitalize()}Test.java"
        file_path = os.path.join(OUTPUT_DIR, file_name)

        with open(file_path, "w", encoding="utf-8") as f:
            f.write(java_code)

        print(f"Generated: {file_name}")
    #Clean up Java markdown fences from generated files
    remove_first_and_last_line(OUTPUT_DIR)

if __name__ == "__main__":
    main("config.properties")
