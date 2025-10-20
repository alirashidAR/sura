def generate_tests_for_endpoint(endpoint):
    prompt = f"""
    You are an API test case generator. Generate 3-5 detailed test cases (input, expected output, edge cases)
    for this OpenAPI endpoint:

    Method: {endpoint['method']}
    Path: {endpoint['path']}
    Description: {endpoint['description']}
    Parameters: {endpoint['parameters']}
    RequestBody: {endpoint['requestBody']}
    Responses: {endpoint['responses']}

    Return the output as a JSON list of test cases.
    """
    completion = openai.ChatCompletion.create(
        model="gpt-4o",
        messages=[{"role": "user", "content": prompt}]
    )
    return completion.choices[0].message.content

all_tests = []
for endpoint in chunks:
    tests = generate_tests_for_endpoint(endpoint)
    all_tests.append({
        "endpoint": endpoint["path"],
        "method": endpoint["method"],
        "tests": tests
    })

# Save to file
with open("generated_tests.json", "w") as f:
    json.dump(all_tests, f, indent=2)
