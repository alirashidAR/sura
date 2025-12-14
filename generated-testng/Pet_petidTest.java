import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PetAPITest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // Group test methods by HTTP method using comments
    /* GET Methods */
    @Test(description = "Test Case 1: Valid Pet ID")
    public void testValidPetID_GET() {
        int petId = 123;
        String response = RestAssured.get("/pet/{petId}", petId).asString();
        // Validate HTTP status code
        org.testng.Assert.assertEquals(200, RestAssured.getStatusCode());
    }

    @Test(description = "Large Valid Pet ID (> 1000)")
    public void testValidPetID_LARGE_GET() {
        int petId = 1234;
        String response = RestAssured.get("/pet/{petId}", petId).asString();
        // Validate HTTP status code
        org.testng.Assert.assertEquals(200, RestAssured.getStatusCode());
    }

    @Test(description = "Invalid Pet ID")
    public void testInvalidPetID_GET() {
        int petId = -1;
        String response = RestAssured.get("/pet/{petId}", petId).asString();
        // Validate HTTP status code
        org.testng.Assert.assertEquals(404, RestAssured.getStatusCode());
    }

    @Test(description = "Large Invalid Pet ID (-1000)")
    public void testInvalidPetID_LARGE_GET() {
        int petId = -1234;
        String response = RestAssured.get("/pet/{petId}", petId).asString();
        // Validate HTTP status code
        org.testng.Assert.assertEquals(404, RestAssured.getStatusCode());
    }

    @Test(description = "Missing Pet ID")
    public void testMissingPetID_GET() {
        int petId = 0;
        String response = RestAssured.get("/pet/{petId}", petId).asString();
        // Validate HTTP status code
        org.testng.Assert.assertEquals(404, RestAssured.getStatusCode());
    }

    @Test(description = "Large Pet ID (> 1000000)")
    public void testLargePetID_GET() {
        int petId = 1000000;
        String response = RestAssured.get("/pet/{petId}", petId).asString();
        // Validate HTTP status code
        org.testng.Assert.assertEquals(404, RestAssured.getStatusCode());
    }

    @Test(description = "Zero Pet ID")
    public void testZeroPetID_GET() {
        int petId = 0;
        String response = RestAssured.get("/pet/{petId}", petId).asString();
        // Validate HTTP status code
        org.testng.Assert.assertEquals(404, RestAssured.getStatusCode());
    }

    /* POST Methods */
    @Test(description = "Test Case 1 - Valid Pet ID and Name")
    public void testValidPetID_POST() {
        int petId = 12345;
        String response = RestAssured.given()
                .contentType("application/json")
                .body("{\"name\":\"New Pet Name\",\"status\":\"available\"}")
                .post("/pet/{petId}", petId).asString();
        // Validate HTTP status code
        org.testng.Assert.assertEquals(200, RestAssured.getStatusCode());
    }

    @Test(description = "Test Case 2 - Valid Pet ID and Status")
    public void testValidPetID_STATUS_POST() {
        int petId = 67890;
        String response = RestAssured.given()
                .contentType("application/json")
                .body("{\"name\":\"New Pet Name\",\"status\":\"available\"}")
                .post("/pet/{petId}", petId).asString();
        // Validate HTTP status code
        org.testng.Assert.assertEquals(200, RestAssured.getStatusCode());
    }

    @Test(description = "Test Case 3 - Invalid Pet ID (empty)")
    public void testInvalidPetID_EMPTY_POST() {
        String response = RestAssured.given()
                .contentType("application/json")
                .body("{\"name\":\"New Pet Name\",\"status\":\"available\"}")
                .post("/pet/").asString();
        // Validate HTTP status code
        org.testng.Assert.assertEquals(405, RestAssured.getStatusCode());
    }

    @Test(description = "Test Case 4 - Valid Pet ID, Name and Status")
    public void testValidPetID_NAME_STATUS_POST() {
        int petId = 12345;
        String response = RestAssured.given()
                .contentType("application/json")
                .body("{\"name\":\"New Pet Name\",\"status\":\"available\"}")
                .post("/pet/{petId}", petId).asString();
        // Validate HTTP status code
        org.testng.Assert.assertEquals(200, RestAssured.getStatusCode());
    }

    @Test(description = "Test Case 5 - Missing Pet ID")
    public void testMissingPetID_POST() {
        String response = RestAssured.given()
                .contentType("application/json")
                .body("{\"name\":\"New Pet Name\",\"status\":\"available\"}")
                .post("/pet/").asString();
        // Validate HTTP status code
        org.testng.Assert.assertEquals(405, RestAssured.getStatusCode());
    }

    /* PUT Methods */
    @Test(description = "Test Case 1 - Valid Pet ID and Status")
    public void testValidPetID_STATUS_PUT() {
        int petId = 12345;
        String response = RestAssured.given()
                .contentType("application/json")
                .body("{\"name\":\"New Pet Name\",\"status\":\"available\"}")
                .put("/pet/{petId}", petId).asString();
        // Validate HTTP status code
        org.testng.Assert.assertEquals(200, RestAssured.getStatusCode());
    }

    @Test(description = "Test Case 2 - Invalid Pet ID (empty)")
    public void testInvalidPetID_EMPTY_PUT() {
        String response = RestAssured.given()
                .contentType("application/json")
                .body("{\"name\":\"New Pet Name\",\"status\":\"available\"}")
                .put("/pet/").asString();
        // Validate HTTP status code
        org.testng.Assert.assertEquals(405, RestAssured.getStatusCode());
    }

    /* DELETE Methods */
    @Test(description = "Test Case 1 - Valid Pet ID")
    public void testValidPetID_DELETE() {
        int petId = 12345;
        String response = RestAssured.delete("/pet/{petId}", petId).asString();
        // Validate HTTP status code
        org.testng.Assert.assertEquals(200, RestAssured.getStatusCode());
    }

    @Test(description = "Test Case 2 - Invalid Pet ID (empty)")
    public void testInvalidPetID_EMPTY_DELETE() {
        String response = RestAssured.delete("/pet/").asString();
        // Validate HTTP status code
        org.testng.Assert.assertEquals(405, RestAssured.getStatusCode());
    }
}
