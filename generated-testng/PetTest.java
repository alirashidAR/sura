import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class PetApiTest {

    private String basePath = "/pet";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // Group test methods by HTTP method using comments

    /**
     * Test Cases for GET Method
     */

    @Test(description = "Test Case 6: Get Pet with Valid ID")
    public void getPetWithValidId() {
        given()
                .when()
                .get(basePath + "/12345")
                .then()
                .statusCode(200);
    }

    @Test(description = "Test Case 7: Get Pet with Invalid ID")
    public void getPetWithInvalidId() {
        given()
                .when()
                .get(basePath + "/99999")
                .then()
                .statusCode(404);
    }

    // Group test methods by HTTP method using comments

    /**
     * Test Cases for POST Method
     */

    @Test(description = "Test Case 8: Create Pet with Valid Object")
    public void createPetWithValidObject() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"id\":12345,\"name\":\"Max\",\"tag\":\"dog\"}")
                .when()
                .post(basePath)
                .then()
                .statusCode(201);
    }

    @Test(description = "Test Case 9: Create Pet with Invalid Object")
    public void createPetWithInvalidObject() {
        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .when()
                .post(basePath)
                .then()
                .statusCode(400);
    }

    // Group test methods by HTTP method using comments

    /**
     * Test Cases for PUT Method
     */

    @Test(description = "Test Case 10: Update Pet with Valid Object")
    public void updatePetWithValidObject() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"id\":12345,\"name\":\"Max\",\"tag\":\"dog\"}")
                .when()
                .put(basePath + "/12345")
                .then()
                .statusCode(200);
    }

    @Test(description = "Test Case 11: Update Pet with Invalid Object")
    public void updatePetWithInvalidObject() {
        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .when()
                .put(basePath + "/12345")
                .then()
                .statusCode(400);
    }

    @Test(description = "Test Case 12: Update Pet with Valid Object and Optional Fields")
    public void updatePetWithValidObjectAndOptionalFields() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"id\":12345,\"name\":\"Max\",\"tag\":\"dog\",\"status\":\"available\"}")
                .when()
                .put(basePath + "/12345")
                .then()
                .statusCode(200);
    }

    @Test(description = "Test Case 13: Update Pet with Invalid Content-Type Header")
    public void updatePetWithInvalidContentTypeHeader() {
        given()
                .contentType(ContentType.TEXT)
                .body("")
                .when()
                .put(basePath + "/12345")
                .then()
                .statusCode(405);
    }

    @Test(description = "Test Case 14: Update Pet with No Body")
    public void updatePetWithNoBody() {
        given()
                .when()
                .put(basePath + "/12345")
                .then()
                .statusCode(400);
    }

    // Group test methods by HTTP method using comments

    /**
     * Test Cases for DELETE Method
     */

    @Test(description = "Test Case 15: Delete Pet with Valid ID")
    public void deletePetWithValidId() {
        given()
                .when()
                .delete(basePath + "/12345")
                .then()
                .statusCode(200);
    }

    @Test(description = "Test Case 16: Delete Pet with Invalid ID")
    public void deletePetWithInvalidId() {
        given()
                .when()
                .delete(basePath + "/99999")
                .then()
                .statusCode(404);
    }
}
