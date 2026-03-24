# `repos/` Folder - Design Pattern & Testing Knowledge Base

## 📌 Purpose

This folder contains **cloned public Java testing frameworks** that serve as the **knowledge base** for the SURA system's pattern extraction and RAG (Retrieval-Augmented Generation) pipeline.

## 🎯 What This Folder Does

### 1. **Design Pattern Source**
The repositories in this folder are analyzed to extract:
- ✅ Design patterns (Singleton, Factory, Builder, Facade, etc.)
- ✅ Testing patterns (DataProvider, Template Method, Specifications)
- ✅ REST API automation best practices
- ✅ TestNG & REST Assured usage conventions

### 2. **LLM Context & Knowledge Injection**
When you generate new tests via `/generate/` endpoint:
- The system queries patterns extracted from these repos
- Ollama LLM uses these patterns to improve code generation
- Generated tests follow **proven, production-quality patterns**
- Reduces boilerplate and improves code consistency

### 3. **Vector Database Population**
The `ingestion_repo_to_qdart.py` script:
```
repos/ (source code)
    ↓
Scans all .java files
    ↓
Identifies design patterns
    ↓
Embeds with Ollama (nomic-embed-text)
    ↓
Stores in Qdrant vector database
```

---

## 📂 Repositories Included

| Repository | Purpose | GitHub Link |
|---|---|---|
| **DesignPatterns** | Pure design pattern implementations in Java | [rahulshettyacademy/DesignPatterns](https://github.com/rahulshettyacademy/DesignPatterns) |
| **RestAssuredTestNGFramework** | Complete REST API test automation framework | [omprakashchavan01/RestAssuredTestNGFramework](https://github.com/omprakashchavan01/RestAssuredTestNGFramework) |
| **restassured-complete-basic-example** | End-to-end REST Assured + TestNG examples | [eliasnogueira/restassured-complete-basic-example](https://github.com/eliasnogueira/restassured-complete-basic-example) |
| **restassured-testng-examples** | More REST Assured testing patterns & examples | [anhtester/restassured-testng-examples](https://github.com/anhtester/restassured-testng-examples) |

---

## 🚀 How to Set Up This Folder

### Clone All Repositories

```bash
cd repos/

# Clone all required repositories
git clone https://github.com/rahulshettyacademy/DesignPatterns.git
git clone https://github.com/omprakashchavan01/RestAssuredTestNGFramework.git
git clone https://github.com/eliasnogueira/restassured-complete-basic-example.git
git clone https://github.com/anhtester/restassured-testng-examples.git

cd ..
```

---

## 🔄 Data Flow

```
repos/
  ├── DesignPatterns/
  │   └── src/main/java/... (design pattern examples)
  │
  ├── RestAssuredTestNGFramework/
  │   └── src/test/java/... (test framework code)
  │
  ├── restassured-complete-basic-example/
  │   └── src/test/java/... (test examples)
  │
  └── restassured-testng-examples/
      └── src/test/java/... (more test examples)
          ↓
    patterns/ingestion_repo_to_qdart.py
          ↓
    patterns/extract_patterns.py
    ├─ Identifies: @DataProvider, RequestSpecification, Singleton, Builder...
    ├─ Extracts: "DataProvider Pattern: Use TestNG @DataProvider..."
    ├─ Embeds with Ollama: Creates 768-dim vectors
    └─ Stores in Qdrant
          ↓
    api.py /generate/ endpoint
    ├─ User uploads OpenAPI.json
    ├─ Query Qdrant for relevant patterns
    ├─ Pass patterns to Ollama LLM as context
    ├─ LLM generates pattern-aware test code
    └─ Return ZIP with generated TestNG tests
```

---

## 📊 Pattern Extraction Details

### Extracted Design Patterns

```
Singleton Pattern
├─ Ensures only one instance of a class
├─ Used for: Configuration managers, Token managers
└─ Signal: "new Singleton()" or "getInstance()"

Factory Pattern
├─ Creates objects without exposing instantiation logic
├─ Used for: Request builders, Client creation
└─ Signal: "Factory", "@Factory", "create()" methods

Builder Pattern
├─ Constructs complex objects step by step
├─ Used for: REST request payload construction
└─ Signal: "Builder", ".withXxx()", ".build()"

Specification Pattern
├─ Reusable REST request specifications
├─ Used for: RequestSpecification, RequestSpecBuilder
└─ Signal: "RequestSpecification", "RequestSpecBuilder"

DataProvider Pattern
├─ Test data parameterization with TestNG
├─ Used for: Data-driven testing, Multiple test scenarios
└─ Signal: "@DataProvider", "public Object[][] data()"
```

### Testing Patterns Identified

```
Template Method (Testing)
├─ Shared setup/teardown in BaseTest class
├─ Used for: Common test initialization
└─ Signal: "class BaseTest", "extends BaseTest"

Facade Pattern (Testing)
├─ Simplified API interface over complex HTTP calls
├─ Used for: API wrapper classes
└─ Signal: "RestAssured", "sendRequest()", "verifyResponse()"
```

---

## ⚙️ How the LLM Uses This Knowledge

### Example Generation Process

**Input:** Your OpenAPI specification for a Pet Store API

**Process:**
1. System parses: GET `/pets/{petId}` endpoint
2. Queries Qdrant: "What patterns exist for GET requests with path params?"
3. Retrieves:
   - "RequestSpecification Pattern: Use RequestSpecBuilder..."
   - "DataProvider Pattern: Multiple test data sets..."
   - "Template Method Pattern: Shared setup in BaseTest..."
4. Ollama LLM generates:
   ```java
   @Test(dataProvider = "petIdData")
   public void testGetPetById(String petId, int expectedStatus) {
       given()
           .spec(requestSpec)
           .pathParam("petId", petId)
       .when()
           .get("/pets/{petId}")
       .then()
           .statusCode(expectedStatus);
   }
   ```

---

## 🎓 Why This Matters

### Without `repos/` folder:
❌ LLM generates generic, inefficient tests  
❌ No consistency with industry best practices  
❌ Tests may not follow established patterns  
❌ Code quality varies  

### With `repos/` folder:
✅ Generated tests follow proven design patterns  
✅ Code quality stays consistent  
✅ Tests are production-ready  
✅ Easy to integrate into CI/CD pipelines  

---

## 📝 Quick Reference

| Content | Location | Purpose |
|---------|----------|---------|
| **Source Java Code** | `repos/*/src/` | Pattern extraction source |
| **Extraction Script** | `../patterns/extract_patterns.py` | Finds patterns in Java files |
| **Storage Script** | `../patterns/qdrant_store.py` | Embeds & stores in Qdrant |
| **Injection Script** | `../patterns/ingestion_repo_to_qdart.py` | Coordinates extraction & storage |

---

## ✅ Status Check

To verify this folder is properly set up:

1. **Check if all 4 repos are cloned:**
   ```bash
   ls -la repos/
   # Should show: DesignPatterns, RestAssuredTestNGFramework, 
   #              restassured-complete-basic-example, restassured-testng-examples
   ```

2. **Check if patterns were injected into Qdrant:**
   - Run: `python patterns/ingestion_repo_to_qdart.py`
   - Visit: http://localhost:6333/dashboard
   - Verify: Collection `api_test_patterns` exists with vectors

3. **Test generation with patterns:**
   - Upload an OpenAPI file via `/generate/`
   - Check generated tests follow extracted patterns

---

## 🔗 Related Files

- Main project: [../README.md](../README.md)
- Pattern extraction: [../patterns/ingestion_repo_to_qdart.py](../patterns/ingestion_repo_to_qdart.py)
- API endpoint: [../app.py](../app.py)
- Vector DB config: [../patterns/qdrant_store.py](../patterns/qdrant_store.py)
