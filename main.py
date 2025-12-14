import os
import json
import time
import logging
from typing import List, Dict, Any, Union

# Import TypeAdapter from pydantic to correctly generate schema for List[T]
from pydantic import BaseModel, Field, ValidationError, TypeAdapter 

from google import genai
from google.genai import types
from google.genai.errors import APIError

# --- Setup Basic Logging (FIXED) ---
# Correct format string: '%(message)s' instead of '%(message:s]'
logging.basicConfig(level=logging.INFO, format='[%(levelname)s] %(message)s')
logger = logging.getLogger(__name__)

# --- Pydantic Schema Definitions (Unchanged) ---

class TestCase(BaseModel):
    """Schema for a single generated API test case."""
    testId: str = Field(..., description="Unique ID for traceability (e.g., SURA_POST_0001).")
    description: str = Field(..., description="A clear, concise description of the test objective.")
    scenarioType: str = Field(..., description="Type: 'Functional_Valid', 'Functional_Invalid', 'Fuzzing_Security', 'Edge_Case'.")
    inputData: dict = Field(..., description="The full JSON body or query parameters used for the request.") 
    expectedStatusCode: int = Field(..., description="The expected HTTP status code (e.g., 200, 400, 500).")
    expectedValidation: str = Field(..., description="A natural language description of the expected response body validation.")

class EndpointTestResults(BaseModel):
    """Schema for tests generated for a single API endpoint."""
    endpoint: str = Field(..., description="The API path (e.g., /users/{id}).")
    method: str = Field(..., description="The HTTP method (e.g., GET, POST).")
    tests: List[TestCase]

# ----------------------------------------------------------------------------------

class TestGenerationEngine:
    def __init__(self, openapi_path: str = "openapi.json"):
        """Initializes the engine, loads policy, and configures the LLM."""
        api_key = os.getenv("GEMINI_API_KEY")
        if not api_key:
            raise RuntimeError("GEMINI_API_KEY environment variable not set. Please obtain one and set it.")

        self.client = genai.Client(api_key=api_key)
        self.model = "gemini-2.5-flash"
        self.openapi = self._load_openapi(openapi_path)
        self.chunks = self._build_endpoint_chunks()
        self.test_counter = 0

    def _load_openapi(self, path: str) -> Dict[str, Any]:
        """Loads and validates the OpenAPI JSON file."""
        try:
            with open(path, "r", encoding="utf-8") as f:
                return json.load(f)
        except FileNotFoundError:
            raise FileNotFoundError(f"OpenAPI file not found at: {path}")
        except json.JSONDecodeError:
            raise ValueError(f"OpenAPI file at {path} is not valid JSON.")

    def _build_endpoint_chunks(self) -> List[Dict[str, Any]]:
        """Parses the OpenAPI spec into structured chunks for the LLM."""
        chunks = []
        for path, methods in self.openapi.get("paths", {}).items():
            for method, details in methods.items():
                if isinstance(details, dict):
                    chunks.append({
                        "method": method.upper(),
                        "path": path,
                        "description": details.get("description", "No description provided."),
                        "parameters": details.get("parameters", []),
                        "requestBody": details.get("requestBody", {}),
                        "responses": details.get("responses", {}),
                        "definitions": self.openapi.get("definitions", {}) 
                    })
        return chunks

    def _generate_test_id(self, method: str) -> str:
        """Generates a unique, traceable ID for a test case (per SURA requirements)."""
        self.test_counter += 1
        return f"SURA_{method}_{self.test_counter:04d}"

    def _generate_tests_for_endpoint(self, endpoint: Dict[str, Any], max_retries: int = 3) -> List[TestCase]:
        """
        Uses Gemini to generate test cases by forcing JSON output via the prompt 
        and manually validating it with Pydantic.
        """
        system_instruction = (
            "You are an expert SDET (Software Development Engineer in Test) for REST APIs. "
            "Your task is to generate 5 comprehensive test cases for the provided OpenAPI endpoint "
            "specification. Ensure a mix of Functional_Valid, Functional_Invalid, and Fuzzing_Security "
            "scenarios. "
            "CRITICAL: Output ONLY a single JSON array that conforms to the required structure. "
            "Do not include any other text, explanations, or markdown fences."
        )
        
        # FIX: Use TypeAdapter to correctly get the schema for List[TestCase]
        test_case_adapter = TypeAdapter(List[TestCase])
        test_case_list_schema = test_case_adapter.json_schema()

        prompt = (
            "Generate 5 test cases based on the following API endpoint specification:\n\n"
            f"**Endpoint Method:** {endpoint['method']}\n"
            f"**Endpoint Path:** {endpoint['path']}\n"
            f"**Description:** {endpoint['description']}\n"
            f"**Query/Path Parameters:** {json.dumps(endpoint['parameters'])}\n"
            f"**Request Body Schema:** {json.dumps(endpoint['requestBody'])}\n"
            f"**Expected Responses:** {json.dumps(endpoint['responses'])}\n\n"
            "--- REQUIRED JSON OUTPUT FORMAT ---\n"
            "The output MUST be a JSON array (list) of objects. Each object MUST adhere to this JSON Schema:\n"
            f"{json.dumps(test_case_list_schema, indent=2)}\n\n"
            "CRITICAL: For the 'testId' field, use the format 'SURA_[METHOD]_[COUNT]', where [METHOD] is the HTTP method and [COUNT] is a unique four-digit incrementing number (e.g., SURA_POST_0001, SURA_GET_0002)."
        )

        for attempt in range(max_retries):
            try:
                # 1. CALL GEMINI (WITHOUT response_schema to bypass the SDK error)
                response = self.client.models.generate_content(
                    model=self.model,
                    contents=prompt,
                    config=types.GenerateContentConfig(
                        system_instruction=system_instruction,
                        temperature=0.4,
                    ),
                )
                
                # 2. EXTRACT AND PARSE RAW JSON STRING
                raw_json_text = response.text.strip()
                
                # Clean up markdown fences if the model added them despite the system instruction
                if raw_json_text.startswith("```"):
                    # Split only once on the markdown fence
                    raw_json_text = raw_json_text.split("```", 2)[1].strip()
                    if raw_json_text.lower().startswith("json"):
                         raw_json_text = raw_json_text[4:].strip()
                
                raw_tests = json.loads(raw_json_text)
                
                # 3. MANUAL PYDANTIC VALIDATION AND ID FIXING
                validated_tests = []
                for raw_test in raw_tests:
                    raw_test['testId'] = self._generate_test_id(endpoint['method'])
                    validated_tests.append(TestCase(**raw_test))

                return validated_tests
                
            except APIError as e:
                logger.error(f"Gemini API Error for {endpoint['path']}: {e}")
                time.sleep(2 ** attempt)
            except (json.JSONDecodeError, KeyError, ValidationError) as e:
                logger.error(f"Structured output validation failed for {endpoint['path']} (Attempt {attempt+1}): {e}")
                logger.debug(f"Raw model output that failed: {response.text}") 
                time.sleep(2 ** attempt)
            except Exception as e:
                logger.error(f"General Error processing endpoint {endpoint['path']}: {e}")
                time.sleep(2 ** attempt)
                
        logger.error(f"Failed to generate tests for {endpoint['path']} after {max_retries} attempts.")
        return []

    def run_full_generation(self, output_file: str = "sura_generated_tests.json") -> None:
        """Generates tests for all endpoints and saves them to a file."""
        logger.info(f"Starting test generation for {len(self.chunks)} endpoints using {self.model}...")
        
        all_test_results: List[Dict[str, Any]] = []
        
        for endpoint in self.chunks:
            self.test_counter = 0 
            
            generated_tests = self._generate_tests_for_endpoint(endpoint)
            
            result = EndpointTestResults(
                endpoint=endpoint["path"],
                method=endpoint["method"],
                tests=generated_tests
            )
            
            all_test_results.append(result.model_dump())
            logger.info(f"Generated {len(generated_tests)} tests for {endpoint['method']} {endpoint['path']}.")
            
        with open(output_file, "w", encoding="utf-8") as f:
            json.dump(all_test_results, f, indent=2, ensure_ascii=False)

        logger.info(f"--- Generation Complete! ---")
        logger.info(f"All generated tests saved to {output_file}")


if __name__ == "__main__":
    try:
        engine = TestGenerationEngine(openapi_path="openapi.json")
        engine.run_full_generation()
    except Exception as e:
        logger.error(f"FATAL ERROR: {e}")