import os
import re


def extract_patterns_from_repo(repo_path: str):
    """
    Extract high-level design and testing patterns from a Java repository.
    Returns a list of human-readable pattern descriptions suitable for vector DB storage.
    """

    extracted_patterns = set()

    # -------- Canonical Pattern Descriptions --------
    design_pattern_map = {
        "Singleton": "Singleton Pattern: Ensures a class has only one instance and provides a global access point. Commonly used for configuration or token managers.",
        "Factory": "Factory Pattern: Creates objects without exposing instantiation logic, useful for building request specifications or clients.",
        "Strategy": "Strategy Pattern: Encapsulates interchangeable behaviors, useful for different validation or authentication strategies.",
        "Template": "Template Method Pattern: Defines a common workflow in a base class while allowing subclasses to override specific steps.",
        "Builder": "Builder Pattern: Constructs complex objects step by step, commonly used for request payload creation.",
        "Facade": "Facade Pattern: Provides a simplified interface over complex subsystems, often used to wrap API calls.",
        "Observer": "Observer Pattern: Establishes a one-to-many dependency so dependent objects are notified of state changes.",
        "Decorator": "Decorator Pattern: Dynamically adds behavior to objects without modifying existing code.",
        "Command": "Command Pattern: Encapsulates a request as an object, allowing parameterization and queuing of requests."
    }

    # -------- Traverse Repo --------
    for root, _, files in os.walk(repo_path):
        for file in files:
            if not file.endswith(".java"):
                continue

            file_path = os.path.join(root, file)

            try:
                with open(file_path, "r", encoding="utf-8", errors="ignore") as f:
                    code = f.read()
            except Exception:
                continue

            # -------- Software Design Pattern Signals --------
            for keyword, description in design_pattern_map.items():
                if keyword in code or keyword.lower() in code.lower():
                    extracted_patterns.add(description)

            # -------- Testing / Framework-Specific Patterns --------

            if "class BaseTest" in code or "extends BaseTest" in code:
                extracted_patterns.add(
                    "Template Method Pattern (Testing): Shared setup and teardown logic implemented via a BaseTest class."
                )

            if "RequestSpecification" in code or "RequestSpecBuilder" in code:
                extracted_patterns.add(
                    "Specification Pattern: Reusable RequestSpecification used to standardize API requests."
                )

            if "@DataProvider" in code:
                extracted_patterns.add(
                    "DataProvider Pattern: Use TestNG @DataProvider to execute the same API test with multiple input datasets for data-driven testing."
                )

            if "private static" in code and "getInstance" in code:
                extracted_patterns.add(
                    "Singleton Pattern (Testing): Centralized configuration or token manager implemented as a singleton."
                )

            if ".builder()" in code or "Builder" in code:
                extracted_patterns.add(
                    "Builder Pattern (Testing): Fluent construction of request payloads or configuration objects."
                )

            if "Service" in file or "ApiClient" in file or "Client" in file:
                extracted_patterns.add(
                    "Facade Pattern (Testing): API calls abstracted into service or client classes instead of being written directly in test methods."
                )

    return list(extracted_patterns)
