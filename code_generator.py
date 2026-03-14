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
def generate_testNg_code(path, method_blocks):

    prompt = f"""
    ROLE
    You are a senior Java QA Automation Engineer who writes production-ready
    API automation tests using TestNG and RestAssured.

    GOAL
    Generate ONE complete Java TestNG class for the API endpoint provided.

    The generated code must compile and follow enterprise automation standards.

    ----------------------------------------

    FRAMEWORK RULES

    1. Only ONE Java class per API PATH
    2. All HTTP methods for the path must be inside the SAME class
    3. If multiple test cases exist for the same method:
    - Use TestNG @DataProvider
    - Do NOT create multiple @Test methods
    4. Group tests by HTTP method

    Example structure:

    // ================= GET TESTS =================

    // ================= POST TESTS =================

    ----------------------------------------

    TECH STACK

    Java 11+
    TestNG
    RestAssured

    ----------------------------------------

    CONFIGURATION RULES

    The base URL MUST NOT be hardcoded.

    Assume there is a utility class named ConfigLoader.

    Use it in setup:

    @BeforeClass
    public void setup() {{
        RestAssured.baseURI = ConfigLoader.getBaseUrl();
    }}

    Do NOT implement ConfigLoader.

    ----------------------------------------

    REQUEST HANDLING

    Correctly support:

    - Path parameters
    - Query parameters
    - Headers
    - Request body
    - Multipart requests if present

    ----------------------------------------

    ASSERTION RULES (MANDATORY)

    Every test MUST verify:

    1. HTTP status code
    2. Response body content
    3. Required JSON keys
    4. Error messages for negative tests
    5. Response time

    Examples:

    Assert.assertEquals(response.getStatusCode(), expectedStatus);

    Assert.assertTrue(response.getBody().asString().contains("success"));

    Assert.assertNotNull(response.jsonPath().get("id"));

    ----------------------------------------

    DATA PROVIDER RULES

    If multiple tests exist for a method:

    Use:

    @DataProvider(name = "methodNameData")

    Return Object[][] containing test inputs and expected outputs.

    ----------------------------------------

    IMPORTS REQUIRED

    Ensure these imports exist:

    import io.restassured.RestAssured;
    import io.restassured.response.Response;
    import org.testng.Assert;
    import org.testng.annotations.BeforeClass;
    import org.testng.annotations.DataProvider;
    import org.testng.annotations.Test;

    ----------------------------------------

    OUTPUT FORMAT

    Return ONLY valid Java code.
    If you gave any explanations or notes, give them as java comments INSIDE the class, NEVER outside.

    Do NOT include:
    - markdown
    - explanations
    - text outside the Java class
    - Notes or TODOs outside the class

    ----------------------------------------

    API PATH
    {path}

    TEST CASE DATA
    {json.dumps(method_blocks, indent=2)}

    """
    response = ollama.chat(
        model="incept5/llama3.1-claude",
        messages=[{"role": "user", "content": prompt}],
        options={
            "temperature": 0.1,
            "num_predict": 2048
        }
    )
    return response["message"]["content"]

# Utility to identify if a line of code is Java or not (to comment out explanations)

def is_java_code(line):
    line = line.strip()

    if not line:
        return False

    java_patterns = [
        r'^\s*package ',
        r'^\s*import ',
        r'^\s*public ',
        r'^\s*private ',
        r'^\s*protected ',
        r'^\s*class ',
        r'^\s*@',
        r'[{}();=]',
        r'\('
    ]

    for pattern in java_patterns:
        if re.search(pattern, line):
            return True

    return False

def comment_explanations(directory):
    for filename in os.listdir(directory):
        file_path = os.path.join(directory, filename)

        if not os.path.isfile(file_path):
            continue

        with open(file_path, "r", encoding="utf-8") as f:
            lines = f.readlines()

        # -------- TOP SCAN --------
        i = 0
        while i < len(lines):
            stripped = lines[i].lstrip()

            if stripped.startswith("import ") or stripped.startswith("public "):
                break

            if stripped and not stripped.startswith("//"):
                lines[i] = "// " + lines[i]

            i += 1

        # -------- BOTTOM SCAN --------
        j = len(lines) - 1
        while j >= 0:
            stripped = lines[j].lstrip()

            if stripped.startswith("}"):
                break

            if stripped and not stripped.startswith("//"):
                lines[j] = "// " + lines[j]

            j -= 1

        with open(file_path, "w", encoding="utf-8") as f:
            f.writelines(lines)

        print(f"Processed: {filename}")
# Removing Java markdown from the 1st and last line of the files

def clean_generated_java(directory):
    for filename in os.listdir(directory):
        file_path = os.path.join(directory, filename)

        # Process only files
        if not os.path.isfile(file_path):
            continue

        with open(file_path, "r", encoding="utf-8") as f:
            content = f.read()

        # Remove markdown code fences if present
        content = re.sub(r"```java\s*", "", content)
        content = re.sub(r"```\s*", "", content)

        # Remove accidental leading explanations sometimes produced by LLMs
        content = re.sub(r"^Here is.*?\n", "", content)

        # Trim whitespace
        content = content.strip() + "\n"

        with open(file_path, "w", encoding="utf-8") as f:
            f.write(content)

        print(f"Cleaned: {filename}")
 
# MAIN
 
def main():
    with open("generated_tests.json", "r", encoding="utf-8") as f:
        all_tests = json.load(f)

    OUTPUT_DIR = "generated-testng"
    os.makedirs(OUTPUT_DIR, exist_ok=True)

    grouped_paths = group_by_path(all_tests)

    for path, method_blocks in grouped_paths.items():
        java_code = generate_testNg_code(path, method_blocks)

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
    clean_generated_java(OUTPUT_DIR)
    #Comment out explanations outside Java code
    comment_explanations(OUTPUT_DIR)

if __name__ == "__main__":
    main("config.properties")
