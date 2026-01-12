import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PetAPITest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // POST /pet/{petId}
    @Test(description = "POST /pet/123 with invalid input")
    public void testPostPetInvalidInput() {
        given()
                .contentType(ContentType.JSON)
                .header("Content-Type", "application/json")
                .when()
                .post("/pet/123")
                .then()
                .statusCode(405);
    }

    @Test(description = "POST /pet/123 with valid petId and updated name")
    public void testPostPetValidInputName() {
        given()
                .contentType(ContentType.JSON)
                .header("Content-Type", "application/json")
                .formParam("name", "Max")
                .when()
                .post("/pet/123")
                .then()
                .statusCode(200);
    }

    @Test(description = "POST /pet/123 with valid petId and empty name")
    public void testPostPetValidInputEmptyName() {
        given()
                .contentType(ContentType.JSON)
                .header("Content-Type", "application/json")
                .formParam("name", "")
                .when()
                .post("/pet/123")
                .then()
                .statusCode(200);
    }

    @Test(description = "POST /pet/123 with valid petId and empty status")
    public void testPostPetValidInputEmptyStatus() {
        given()
                .contentType(ContentType.JSON)
                .header("Content-Type", "application/json")
                .formParam("status", "")
                .when()
                .post("/pet/123")
                .then()
                .statusCode(200);
    }

    @Test(description = "POST /pet/123 with valid petId and empty name and status")
    public void testPostPetValidInputEmptyNameStatus() {
        given()
                .contentType(ContentType.JSON)
                .header("Content-Type", "application/json")
                .formParam("name", "")
                .formParam("status", "")
                .when()
                .post("/pet/123")
                .then()
                .statusCode(200);
    }

    // GET /pet/{petId}
    @Test(description = "GET /pet/123 with valid petId")
    public void testGetPetValidInput() {
        given()
                .contentType(ContentType.JSON)
                .header("Content-Type", "application/json")
                .when()
                .get("/pet/123")
                .then()
                .statusCode(200);
    }

    @Test(description = "GET /pet/123 with invalid petId")
    public void testGetPetInvalidInput() {
        given()
                .contentType(ContentType.JSON)
                .header("Content-Type", "application/json")
                .when()
                .get("/pet/999")
                .then()
                .statusCode(404);
    }

    // PUT /pet/{petId}
    @Test(description = "PUT /pet/123 with valid petId and updated name")
    public void testPutPetValidInputName() {
        given()
                .contentType(ContentType.JSON)
                .header("Content-Type", "application/json")
                .formParam("name", "Max")
                .when()
                .put("/pet/123")
                .then()
                .statusCode(200);
    }

    @Test(description = "PUT /pet/123 with valid petId and empty name")
    public void testPutPetValidInputEmptyName() {
        given()
                .contentType(ContentType.JSON)
                .header("Content-Type", "application/json")
                .formParam("name", "")
                .when()
                .put("/pet/123")
                .then()
                .statusCode(200);
    }

    @Test(description = "PUT /pet/123 with valid petId and empty status")
    public void testPutPetValidInputEmptyStatus() {
        given()
                .contentType(ContentType.JSON)
                .header("Content-Type", "application/json")
                .formParam("status", "")
                .when()
                .put("/pet/123")
                .then()
                .statusCode(200);
    }

    @Test(description = "PUT /pet/123 with valid petId and empty name and status")
    public void testPutPetValidInputEmptyNameStatus() {
        given()
                .contentType(ContentType.JSON)
                .header("Content-Type", "application/json")
                .formParam("name", "")
                .formParam("status", "")
                .when()
                .put("/pet/123")
                .then()
                .statusCode(200);
    }

    // DELETE /pet/{petId}
    @Test(description = "DELETE /pet/123 with valid petId")
    public void testDeletePetValidInput() {
        given()
                .contentType(ContentType.JSON)
                .header("Content-Type", "application/json")
                .when()
                .delete("/pet/123")
                .then()
                .statusCode(200);
    }

    @Test(description = "DELETE /pet/999 with invalid petId")
    public void testDeletePetInvalidInput() {
        given()
                .contentType(ContentType.JSON)
                .header("Content-Type", "application/json")
                .when()
                .delete("/pet/999")
                .then()
                .statusCode(404);
    }
}
