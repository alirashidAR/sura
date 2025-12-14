import json
import os
from jinja2 import Environment, FileSystemLoader

# --- Configuration ---
GENERATED_TEST_DATA_FILE = "sura_generated_tests.json"
TEMPLATE_FILE = "test_script_template.jinja2"
OUTPUT_DIR = "generated_pytest_scripts"
# ---------------------

def generate_pytest_scripts():
    """
    Reads the structured JSON tests and generates executable Pytest scripts.
    """
    if not os.path.exists(GENERATED_TEST_DATA_FILE):
        print(f"[ERROR] Test data file not found: {GENERATED_TEST_DATA_FILE}")
        print("Please run the TestGenerationEngine first to create this file.")
        return

    # 1. Setup Jinja2 Environment
    file_loader = FileSystemLoader(os.path.dirname(os.path.abspath(__file__)))
    env = Environment(loader=file_loader)
    
    # Add the standard 'tojson' filter which is useful for embedding data
    env.filters['tojson'] = json.dumps
    
    template = env.get_template(TEMPLATE_FILE)

    # 2. Load the Generated Test Data
    with open(GENERATED_TEST_DATA_FILE, "r", encoding="utf-8") as f:
        all_test_results = json.load(f)

    # Create output directory
    os.makedirs(OUTPUT_DIR, exist_ok=True)
    
    print(f"[INFO] Found {len(all_test_results)} endpoints in the JSON data.")
    
    # 3. Render and Save Scripts
    for endpoint_data in all_test_results:
        # Generate a clean filename (e.g., test_users_id_POST.py)
        # We ensure the filename starts with 'test_' for Pytest discovery
        path_name = endpoint_data['endpoint'].strip('/').replace('/', '_').replace('{', '').replace('}', '')
        method_name = endpoint_data['method'].upper()
        filename = f"test_{path_name}_{method_name}.py"
        output_path = os.path.join(OUTPUT_DIR, filename)
        
        # Render the template with the endpoint's data
        rendered_script = template.render(endpoint_data=endpoint_data)
        
        # Save the executable Python script
        with open(output_path, "w", encoding="utf-8") as f:
            f.write(rendered_script)
            
        print(f"[SUCCESS] Generated script for {endpoint_data['endpoint']} -> {output_path}")

if __name__ == "__main__":
    generate_pytest_scripts()