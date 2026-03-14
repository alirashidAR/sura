from fastapi import FastAPI, UploadFile, File, Form
from fastapi.responses import FileResponse
from fastapi.middleware.cors import CORSMiddleware

from test import main as generate_tests_cases
from code_generator import main as generate_code_testNG

import os
import shutil
#Api That Takes JSON OpenAPI file and config file as input and returns ZIP of generated TestNG Java code

app = FastAPI(title="Test Case Generation")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.post("/generate/")
async def generate_tests(
    openapi_file: UploadFile = File(...),
):
    # ---------- Save OpenAPI JSON ----------
    openapi_content = await openapi_file.read()
    openapi_path = "uploaded_openapi.json"

    with open(openapi_path, "wb") as f:
        f.write(openapi_content)

    # ---------- Save Config file ----------

    # ---------- Clean generated-testng directory ----------
    dir_path = "generated-testng"
    os.makedirs(dir_path, exist_ok=True)

    for filename in os.listdir(dir_path):
        file_path = os.path.join(dir_path, filename)
        if os.path.isfile(file_path):
            os.remove(file_path)

    # ---------- Generate tests ----------
    generate_tests_cases(openapi_path)

    # ---------- Generate code ----------
    generate_code_testNG()

     # ---------- Zip directory ----------
    zip_name = "generated-testng"
    zip_path = shutil.make_archive(zip_name, "zip", dir_path)
    # ---------- Return ZIP ----------
    return FileResponse(
        path=zip_path,
        media_type="application/zip",
        filename="generated-testng.zip"
    )



@app.get("/")
def root():
    return {"message": "Test Case Generation API is running."}

# uvicorn app:app --reload --port 8080