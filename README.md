# Automated API Test Case Generation using RAG + LLM (TestNG)

##  Overview

This project is an **end-to-end automated API test case generation system** built using:

- **OpenAPI (Swagger) specifications**
- **Large Language Models (LLMs) via Ollama**
- **Retrieval-Augmented Generation (RAG)**
- **Vector Database (Qdrant)**
- **TestNG + RestAssured (Java)**

The system automatically:
1. Reads an OpenAPI document
2. Generates structured API test cases
3. Uses design-pattern knowledge learned from public repositories
4. Converts test cases into clean, compilable **TestNG Java code**
5. Enforces **DataProvider-based data-driven testing**
6. Reuses validated outputs using a **fast-path cache**
7. Exposes everything through a **FastAPI backend**

---

## Key Features Implemented

###  OpenAPI-Driven Test Generation
- Parses all API paths and HTTP methods
- Handles path params, query params, headers, body, and responses

###  Retrieval-Augmented Generation (RAG)
- Extracts design patterns from public Java testing frameworks
- Stores pattern knowledge in **Qdrant Vector DB**
- Retrieves relevant patterns per endpoint at runtime

###  Design Pattern Awareness
Patterns applied during generation:
- Template Method
- Facade
- Builder
- Singleton
- Specification
- **DataProvider (client-required)**

###  DataProvider Enforcement
- If multiple test cases exist for the same HTTP method:
  - Uses **TestNG @DataProvider**
  - Prevents duplicate test logic
  - Generates scalable, maintainable tests

###  Fast-Path Cache (Performance Optimization)
- Successful test case generations are cached
- Subsequent runs skip LLM calls entirely
- Reduces generation time from **seconds to milliseconds**

###  Fully Automated Backend API
- Upload OpenAPI + config file
- Receive a ZIP containing generated TestNG Java tests

---

## 🗂️ Project Directory Structure

```
SURA/
│
├── app.py
├── test.py
├── code_generator.py
│
├── rag/
│   └── qdrant_retrieve.py
│
├── patterns/
│   ├── extract_patterns.py
│   ├── ingestion_repo_to_qdart.py
│   └── qdrant_store.py
│
├── cache/
│   └── testcase_cache.py
│
├── generated-testng/
├── generated_tests.json
├── generated-testng.zip
│
├── openapi.json
├── uploaded_openapi.json
├── config.properties
│
├── requirements.txt
└── README.md
```

---

## ⚙️ How to Run

### Start Qdrant
```bash
docker run -p 6333:6333 qdrant/qdrant
```

### Start Ollama
```bash
ollama serve
ollama pull nomic-embed-text
ollama pull incept5/llama3.1-claude
```

### Run Backend
```bash
uvicorn app:app --reload
```

---

## 📈 Performance

| Scenario | Time |
|--------|------|
| First run | 2–5 sec per endpoint |
| Cached run | < 50 ms per endpoint |

---

## 📌 Summary

This repository showcases a **production-grade, pattern-aware, RAG-powered system** for automated API test generation, suitable for enterprise QA automation and research.
