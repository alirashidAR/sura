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

## Repositories Used for Pattern Extraction

This project extracts design patterns and best practices from the following public repositories:

| Repository | GitHub Link | Purpose |
|---|---|---|
| **DesignPatterns** | [rahulshettyacademy/DesignPatterns](https://github.com/rahulshettyacademy/DesignPatterns) | Design pattern implementations in Java |
| **RestAssuredTestNGFramework** | [omprakashchavan01/RestAssuredTestNGFramework](https://github.com/omprakashchavan01/RestAssuredTestNGFramework) | REST API test automation framework with TestNG |
| **restassured-complete-basic-example** | [eliasnogueira/restassured-complete-basic-example](https://github.com/eliasnogueira/restassured-complete-basic-example) | Complete REST Assured testing examples |
| **restassured-testng-examples** | [anhtester/restassured-testng-examples](https://github.com/anhtester/restassured-testng-examples) | REST Assured with TestNG examples |

These repositories are stored locally in the `/repo` directory and are used for pattern analysis and knowledge extraction.

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

## ⚙️ Setup & Installation

### Prerequisites
1. **Python 3.8+**
2. **Java JDK 11+** (for compiling generated tests)
3. **Docker** (for Qdrant)
4. **Git** (for cloning repositories)

### Step 1: Clone Repository Dependencies

The pattern extraction requires the referenced repositories. Clone them into the `repos/` directory:

```bash
cd repos/

# Clone all required repositories
git clone https://github.com/rahulshettyacademy/DesignPatterns.git
git clone https://github.com/omprakashchavan01/RestAssuredTestNGFramework.git
git clone https://github.com/eliasnogueira/restassured-complete-basic-example.git
git clone https://github.com/anhtester/restassured-testng-examples.git

cd ..
```

### Step 2: Install Python Dependencies

```bash
pip install -r requirements.txt
```

### Step 3: Start Qdrant Vector Database

```bash
docker run -p 6333:6333 -v qdrant_storage:/qdrant/storage qdrant/qdrant
```

Verify Qdrant is running: http://localhost:6333/health

### Step 4: Extract & Inject Patterns into Qdrant

This step extracts design patterns and testing best practices from the cloned repositories and stores them in the Qdrant vector database.

```bash
# Navigate to patterns directory
cd patterns/

# Run pattern extraction and injection
python ingestion_repo_to_qdart.py

cd ..
```

**Expected Output:**
```
Extracting patterns from DesignPatterns
Stored pattern: Factory Pattern: Creates objects...
Stored pattern: Singleton Pattern: Ensures a class...
Extracting patterns from RestAssuredTestNGFramework
Stored pattern: DataProvider Pattern: Use TestNG @DataProvider...
...
[Collection created/already exists]
```

### Step 5: Start Ollama (for LLM-based generation)

In a **new terminal**:

```bash
# Start Ollama server
ollama serve

# In another terminal, pull required models
ollama pull nomic-embed-text
ollama pull incept5/lamma3.1-claude  # or another model of your choice
```

### Step 6: Run FastAPI Backend

In a **new terminal**:

```bash
uvicorn app:app --reload
```

The API will be available at: http://localhost:8000

---

## 🚀 How to Use the API

### Method 1: Interactive Swagger UI (Easiest)

1. Ensure the FastAPI backend is running: `uvicorn app:app --reload`
2. Open your browser: **http://localhost:8000/docs**
3. Click on **`POST /generate/`** to expand the endpoint
4. Click **"Try it out"**
5. Click **"Choose File"** and select your OpenAPI/Swagger JSON file
6. Click **"Execute"**
7. The server will generate tests and automatically download `generated-testng.zip`

### Method 2: Using Postman (Recommended for Testing)

**Step-by-Step Guide:**

#### Step 1: Open Postman
- Launch **Postman** application
- Click **File** → **New** → **Request** (or click **+** to create a new tab)

#### Step 2: Configure Request URL
- Change method dropdown from **GET** to **POST**
- Enter URL: `http://localhost:8000/generate/`

#### Step 3: Set Request Body
1. Click the **Body** tab
2. Select **form-data** radio button
3. Add a new row:
   - **Key:** `openapi_file`
   - **Value type:** Change from "Text" to **File** (click the dropdown on the right)
   - **File selection:** Click the file icon → Select your OpenAPI JSON file

**Your Body section should look like:**
```
form-data:
  openapi_file [File] : /path/to/openapi.json
```

#### Step 4: Set Headers (Optional - Auto-added)
- Headers tab will automatically show: `Content-Type: multipart/form-data`
- No additional headers needed

#### Step 5: Send Request
1. Click the blue **Send** button
2. Wait for the response (10-30 seconds depending on OpenAPI complexity)

#### Step 6: Download ZIP Response
Once the request completes:
1. Look at the **Response** panel at the bottom
2. Click **Save Response** button (right side of response panel)
3. Choose **Save to a file**
4. Name it: `generated-testng.zip`
5. Click **Save**

**Expected Response Headers:**
```
Content-Type: application/zip
Content-Disposition: attachment; filename="generated-testng.zip"
Status: 200 OK
```

#### Visual Reference - Postman UI Layout

```
┌─────────────────────────────────────────────────────────────────┐
│  Postman  [Tabs]  Documentation  Testing  Mock Server Help      │
├─────────────────────────────────────────────────────────────────┤
│  [POST ▼] http://localhost:8000/generate/        [Send] [Save]  │
├─────────────────────────────────────────────────────────────────┤
│ Params | Authorization | Headers | Body | Pre-request | Tests   │
├─────────────────────────────────────────────────────────────────┤
│ ◉ form-data  ○ raw  ○ binary  ○ x-www-form-urlencoded         │
│                                                                 │
│ KEY                 VALUE                    [+] [−]           │
│ openapi_file [File▼] /path/to/openapi.json  [browse]          │
│                                                                 │
├─────────────────────────────────────────────────────────────────┤
│ RESPONSE PANEL (Bottom)                                          │
│ Status: 200 OK    Time: 15.432 seconds    Size: 250 KB         │
│ [Body] [Cookies] [Headers] [Tests]                              │
│ ZIP file received - [Save Response ▼]                           │
└─────────────────────────────────────────────────────────────────┘
```

---

### Alternative: Using cURL

```bash
curl -X POST \
  -F "openapi_file=@/path/to/your/openapi.json" \
  http://localhost:8000/generate/ \
  --output generated-testng.zip
```

### Using Python Requests

```python
import requests

with open('openapi.json', 'rb') as f:
    files = {'openapi_file': f}
    response = requests.post('http://localhost:8000/generate/', files=files)
    
    with open('generated-testng.zip', 'wb') as z:
        z.write(response.content)
```

---

## 📦 ZIP File Generation Process

### What Happens When You Hit `/generate/`

```
POST Request (openapi_file)
        ↓
1. Save uploaded OpenAPI JSON to: uploaded_openapi.json
        ↓
2. Clean generated-testng/ directory (remove old files)
        ↓
3. Call generate_tests_cases() → test.py
   - Parse OpenAPI specification
   - Extract API endpoints, methods, parameters
   - Generate test case JSON
   - Save to: generated_tests.json
        ↓
4. Call generate_code_testNG() → code_generator.py
   - Query Qdrant for relevant design patterns
   - Use Ollama LLM to generate test code
   - Convert to compilable TestNG Java classes
   - Save individual .java files to: generated-testng/
        ↓
5. Create ZIP archive using shutil.make_archive()
   - Input: generated-testng/ directory
   - Output: generated-testng.zip
   - Contains all generated Test*.java files
        ↓
6. Return FileResponse with ZIP file
   - browser automatically downloads: generated-testng.zip
```

### ZIP File Contents

The downloaded `generated-testng.zip` contains:

```
generated-testng/
├── PetTest.java
├── Pet_findbystatusTest.java
├── Pet_findbytagsTest.java
├── Pet_petidTest.java
└── ... (one test file per API endpoint)
```

Each `.java` file is:
- ✅ Fully compilable
- ✅ Ready to run with TestNG
- ✅ Uses REST Assured for HTTP calls
- ✅ Includes @DataProvider for data-driven testing
- ✅ Follows design patterns extracted from Qdrant

---

## 🏗️ System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           USER INTERFACE                                     │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  Swagger UI (localhost:8000/docs) / Postman / cURL / Python API    │    │
│  │                         (HTTP POST to /generate/)                    │    │
│  └──────────────────────────┬──────────────────────────────────────────┘    │
└─────────────────────────────┼──────────────────────────────────────────────┘
                              │
                       ┌──────▼─────────┐
                       │   FastAPI      │
                       │   (app.py)     │
                       │ /generate/     │
                       └──────┬─────────┘
                              │
                ┌─────────────┼─────────────┐
                │             │             │
         ┌──────▼────┐  ┌────▼──────┐ ┌───▼────────────┐
         │   Save    │  │  Clean    │ │  Generate     │
         │  OpenAPI  │  │   Dir     │ │ Test Cases    │
         │  JSON     │  │  (old)    │ │(test.py)      │
         └──────┬────┘  └────┬──────┘ └───┬────────────┘
                │             │            │
                └─────────────┼────────────┘
                              │
                    ┌─────────▼──────────────┐
                    │   Generate Java       │
                    │   Code (code_gen.py)  │
                    └─────────┬──────────────┘
                              │
         ┌────────────────────┼────────────────────┐
         │                    │                    │
    ┌────▼────────┐  ┌───────▼────────┐  ┌──────▼─────────┐
    │   Query     │  │  Ollama LLM    │  │ REST Assured   │
    │   Qdrant    │  │   Generation   │  │ Code Template  │
    │(Patterns)   │  │                │  │                │
    └────┬────────┘  └───────┬────────┘  └──────┬─────────┘
         │                   │                   │
         └───────────────────┼───────────────────┘
                             │
              ┌──────────────▼─────────────────┐
              │  Create .java Files in:        │
              │  generated-testng/             │
              │  - PetTest.java                │
              │  - Pet_findbystatusTest.java   │
              │  - Pet_petidTest.java          │
              │  - etc.                        │
              └──────────────┬──────────────────┘
                             │
              ┌──────────────▼──────────────────┐
              │   Create ZIP Archive           │
              │   shutil.make_archive()         │
              │   Output: generated-testng.zip  │
              └──────────────┬──────────────────┘
                             │
         ┌───────────────────▼───────────────────┐
         │      Return FileResponse              │
         │ (Browser downloads generated-testng.zip)
         │                                       │
         └───────────────────┬───────────────────┘
                             │
                      ┌──────▼──────┐
                      │   User      │
                      │ Downloads   │
                      │   ZIP       │
                      └─────────────┘
```

### Key Components

| Component | Technology | Purpose |
|-----------|-----------|---------|
| **API Server** | FastAPI | Handles HTTP requests & file uploads |
| **Test Generator** | Python | Parses OpenAPI & generates test cases |
| **Code Generator** | Python + Ollama | Converts test cases to TestNG Java |
| **Pattern Store** | Qdrant + Ollama | Retrieves design patterns via RAG |
| **Archive Creator** | Python shutil | Creates downloadable ZIP file |
| **Cache Layer** | JSON files | Stores generated tests for rapid re-generation |

---

## 📊 Execution Flow Diagram

```
1. Upload OpenAPI JSON via POST /generate/
        ↓
2. Load design patterns from Qdrant (RAG retrieval)
        ↓
3. Generate test cases using LLM (Ollama)
        ↓
4. Cache successful generations (optional)
        ↓
5. Convert to compilable TestNG Java code
        ↓
6. Zip all generated test files
        ↓
7. Return ZIP file to browser/client
```

---

## ⚙️ Troubleshooting

| Issue | Solution |
|-------|----------|
| Qdrant connection refused | Ensure `docker run -p 6333:6333 qdrant/qdrant` is running |
| Pattern injection fails | Verify all repos are cloned in `repos/` directory |
| Ollama connection error | Ensure `ollama serve` is running in a separate terminal |
| LLM model not found | Run `ollama pull llama2` (or preferred model) |
| Cache issues | Delete `cache/testcase_cache.json` to reset cache |

---

## 📈 Performance

| Scenario | Time |
|--------|------|
| First run | 2–5 sec per endpoint |
| Cached run | < 50 ms per endpoint |

---

## 📌 Summary

This repository showcases a **production-grade, pattern-aware, RAG-powered system** for automated API test generation, suitable for enterprise QA automation and research.
