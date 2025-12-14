import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PetImageUploadTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // POST /pet/{petId}/uploadImage

    @Test(description = "POST /pet/{petId}/uploadImage with valid file")
    public void postValidFileUploadTest() {
        RestAssured.given()
                .header("Content-Type", "multipart/form-data")
                .formParam("file", "/path/to/image.jpg")
                .when()
                .post("/pet/1/uploadImage")
                .then()
                .statusCode(200)
                .body("code", 200, "type", "unknown", "message", "pet image uploaded successfully");
    }

    @Test(description = "POST /pet/{petId}/uploadImage with invalid pet ID")
    public void postInvalidPetIdFileUploadTest() {
        RestAssured.given()
                .header("Content-Type", "multipart/form-data")
                .formParam("file", "/path/to/image.jpg")
                .when()
                .post("/pet/-1/uploadImage")
                .then()
                .statusCode(400)
                .body("code", 400, "type", "unknown", "message", "Invalid pet ID");
    }

    @Test(description = "POST /pet/{petId}/uploadImage with empty file name")
    public void postEmptyFileNameFileUploadTest() {
        RestAssured.given()
                .header("Content-Type", "multipart/form-data")
                .formParam("file", "")
                .when()
                .post("/pet/1/uploadImage")
                .then()
                .statusCode(400)
                .body("code", 400, "type", "unknown", "message", "File name is empty");
    }

    @Test(description = "POST /pet/{petId}/uploadImage with additional metadata")
    public void postAdditionalMetadataFileUploadTest() {
        RestAssured.given()
                .header("Content-Type", "multipart/form-data")
                .formParam("additionalMetadata", "some metadata")
                .when()
                .post("/pet/1/uploadImage")
                .then()
                .statusCode(200)
                .body("code", 200, "type", "unknown", "message", "pet image uploaded successfully with additional metadata");
    }

    @Test(description = "POST /pet/{petId}/uploadImage without file")
    public void postWithoutFileUploadTest() {
        RestAssured.given()
                .header("Content-Type", "multipart/form-data")
                .when()
                .post("/pet/1/uploadImage")
                .then()
                .statusCode(400)
                .body("code", 400, "type", "unknown", "message", "File is required");
    }

    @Test(description = "POST /pet/{petId}/uploadImage with invalid request body format")
    public void postInvalidRequestBodyFormatUploadTest() {
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{}")
                .when()
                .post("/pet/1/uploadImage")
                .then()
                .statusCode(400)
                .body("code", 400, "type", "unknown", "message", "Invalid request body format");
    }

    @Test(description = "POST /pet/{petId}/uploadImage with invalid request method")
    public void postInvalidRequestMethodUploadTest() {
        RestAssured.given()
                .header("Content-Type", "multipart/form-data")
                .when()
                .get("/pet/1/uploadImage") // invalid request method
                .then()
                .statusCode(405)
                .body("code", 405, "type", "unknown", "message", "Method not allowed");
    }

}
