import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;

public class PetFindByStatusTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // GET Tests

    @Test(description = "GET /pet/findByStatus - available pets")
    public void testGetAvailablePets() {
        RestAssured.given()
                .pathParam("status", "available")
                .get("/pet/findByStatus/{status}")
                .then()
                .statusCode(200)
                .body(equalTo("[{\"id\":1,\"name\":\"Max\",\"status\":\"available\"}]"));
    }

    @Test(description = "GET /pet/findByStatus - pending or sold pets")
    public void testGetPendingOrSoldPets() {
        RestAssured.given()
                .pathParam("status", "pending,sold")
                .get("/pet/findByStatus/{status}")
                .then()
                .statusCode(200)
                .body(equalTo("[]"));
    }

    @Test(description = "GET /pet/FindByStatus - duplicate available pets")
    public void testGetDuplicateAvailablePets() {
        RestAssured.given()
                .pathParam("status", "available,available")
                .get("/pet/findByStatus/{status}")
                .then()
                .statusCode(200)
                .body(equalTo("[{\"id\":1,\"name\":\"Max\",\"status\":\"available\"}]"));
    }

    @Test(description = "GET /pet/FindByStatus - pending and available pets")
    public void testGetPendingAndAvailablePets() {
        RestAssured.given()
                .pathParam("status", "pending,available")
                .get("/pet/FindByStatus/{status}")
                .then()
                .statusCode(200)
                .body(equalTo("[{\"id\":2,\"name\":\"Bella\",\"status\":\"available\"}]"));
    }

    @Test(description = "GET /pet/findByStatus - invalid status", expectedExceptions = IllegalArgumentException.class)
    public void testGetInvalidStatus() {
        RestAssured.given()
                .pathParam("status", "invalid_status")
                .get("/pet/FindByStatus/{status}")
                .then()
                .statusCode(400);
    }

    // POST Tests

    @Test(description = "POST /pet - create new pet available")
    public void testPostAvailablePet() {
        RestAssured.given()
                .contentType(JSON)
                .body("{\"id\":1,\"name\":\"Max\",\"status\":\"available\"}")
                .post("/pet")
                .then()
                .statusCode(200);
    }

    @Test(description = "POST /pet - create new pet pending or sold", expectedExceptions = IllegalArgumentException.class)
    public void testPostPendingOrSoldPet() {
        RestAssured.given()
                .contentType(JSON)
                .body("{\"id\":1,\"name\":\"Max\",\"status\":\"pending,sold\"}")
                .post("/pet")
                .then()
                .statusCode(400);
    }

    @Test(description = "POST /pet - create new pet with duplicate available status", expectedExceptions = IllegalArgumentException.class)
    public void testPostDuplicateAvailablePet() {
        RestAssured.given()
                .contentType(JSON)
                .body("{\"id\":1,\"name\":\"Max\",\"status\":\"available,available\"}")
                .post("/pet")
                .then()
                .statusCode(400);
    }

    @Test(description = "POST /pet - create new pet with pending and available status", expectedExceptions = IllegalArgumentException.class)
    public void testPostPendingAndAvailablePet() {
        RestAssured.given()
                .contentType(JSON)
                .body("{\"id\":1,\"name\":\"Max\",\"status\":\"pending,available\"}")
                .post("/pet")
                .then()
                .statusCode(400);
    }

    @Test(description = "POST /pet - create new pet with invalid status", expectedExceptions = IllegalArgumentException.class)
    public void testPostInvalidStatus() {
        RestAssured.given()
                .contentType(JSON)
                .body("{\"id\":1,\"name\":\"Max\",\"status\":\"invalid_status\"}")
                .post("/pet")
                .then()
                .statusCode(400);
    }

    // PUT Tests

    @Test(description = "PUT /pet - update existing pet available", expectedExceptions = IllegalArgumentException.class)
    public void testPutAvailablePet() {
        RestAssured.given()
                .contentType(JSON)
                .body("{\"id\":1,\"name\":\"Max\",\"status\":\"available\"}")
                .put("/pet")
                .then()
                .statusCode(200);
    }

    @Test(description = "PUT /pet - update existing pet pending or sold", expectedExceptions = IllegalArgumentException.class)
    public void testPutPendingOrSoldPet() {
        RestAssured.given()
                .contentType(JSON)
                .body("{\"id\":1,\"name\":\"Max\",\"status\":\"pending,sold\"}")
                .put("/pet")
                .then()
                .statusCode(400);
    }

    @Test(description = "PUT /pet - update existing pet with duplicate available status", expectedExceptions = IllegalArgumentException.class)
    public void testPutDuplicateAvailablePet() {
        RestAssured.given()
                .contentType(JSON)
                .body("{\"id\":1,\"name\":\"Max\",\"status\":\"available,available\"}")
                .put("/pet")
                .then()
                .statusCode(400);
    }

    @Test(description = "PUT /pet - update existing pet with pending and available status", expectedExceptions = IllegalArgumentException.class)
    public void testPutPendingAndAvailablePet() {
        RestAssured.given()
                .contentType(JSON)
                .body("{\"id\":1,\"name\":\"Max\",\"status\":\"pending,available\"}")
                .put("/pet")
                .then()
                .statusCode(400);
    }

    @Test(description = "PUT /pet - update existing pet with invalid status", expectedExceptions = IllegalArgumentException.class)
    public void testPutInvalidStatus() {
        RestAssured.given()
                .contentType(JSON)
                .body("{\"id\":1,\"name\":\"Max\",\"status\":\"invalid_status\"}")
                .put("/pet")
                .then()
                .statusCode(400);
    }

    // DELETE Tests

    @Test(description = "DELETE /pet - delete existing pet available")
    public void testDeleteAvailablePet() {
        RestAssured.given()
                .pathParam("status", "available")
                .delete("/pet/{status}")
                .then()
                .statusCode(200);
    }

    @Test(description = "DELETE /pet - delete existing pet pending or sold", expectedExceptions = IllegalArgumentException.class)
    public void testDeletePendingOrSoldPet() {
        RestAssured.given()
                .pathParam("status", "pending,sold")
                .delete("/pet/{status}")
                .then()
                .statusCode(400);
    }

    @Test(description = "DELETE /pet - delete existing pet with duplicate available status", expectedExceptions = IllegalArgumentException.class)
    public void testDeleteDuplicateAvailablePet() {
        RestAssured.given()
                .pathParam("status", "available,available")
                .delete("/pet/{status}")
                .then()
                .statusCode(400);
    }

    @Test(description = "DELETE /pet - delete existing pet with pending and available status", expectedExceptions = IllegalArgumentException.class)
    public void testDeletePendingAndAvailablePet() {
        RestAssured.given()
                .pathParam("status", "pending,available")
                .delete("/pet/{status}")
                .then()
                .statusCode(400);
    }

    @Test(description = "DELETE /pet - delete existing pet with invalid status", expectedExceptions = IllegalArgumentException.class)
    public void testDeleteInvalidStatus() {
        RestAssured.given()
                .pathParam("status", "invalid_status")
                .delete("/pet/{status}")
                .then()
                .statusCode(400);
    }
}
