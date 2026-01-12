import io.restassured.RestAssured;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PetFindByStatusTest {

    @BeforeClass
    public void setup() {
        // Set base URI for API requests
        RestAssured.baseURI = "https://api.example.com";
    }

    /* GET Method Test Cases */

    /**
     * Happy Path: Valid status array returns expected output
     */
    @Test
    public void testGetPetFindByStatus_HappyPath() {
        // Input: status array with valid values
        String[] statusArray = {"available"};
        // Expected Output: Array of pet objects matching the provided status
        PetObject[] expectedOutput = new PetObject[]{new PetObject(1234, "available")};
        // Verify HTTP status code and output
        verifyGetPetFindByStatus(statusArray, expectedOutput);
    }

    /**
     * Multiple Status Values: Valid array returns multiple pets
     */
    @Test
    public void testGetPetFindByStatus_MultipleStatusValues() {
        // Input: status array with multiple valid values
        String[] statusArray = {"available", "pending"};
        // Expected Output: Array of pet objects matching each provided status
        PetObject[] expectedOutput = new PetObject[]{new PetObject(1234, "available"), new PetObject(5678, "pending")};
        // Verify HTTP status code and output
        verifyGetPetFindByStatus(statusArray, expectedOutput);
    }

    /**
     * Invalid Status Value: Invalid array returns 400 response
     */
    @Test
    public void testGetPetFindByStatus_InvalidStatusValue() {
        // Input: status array with invalid value
        String[] statusArray = {"available", "invalid"};
        // Expected Output: null or empty array (no pets match the provided status)
        PetObject[] expectedOutput = new PetObject[0];
        // Verify HTTP status code and output
        verifyGetPetFindByStatus(statusArray, expectedOutput);
    }

    /**
     * Empty Status Array: Empty array returns empty response
     */
    @Test
    public void testGetPetFindByStatus_EmptyStatusArray() {
        // Input: Empty status array
        String[] statusArray = {};
        // Expected Output: Empty array (no pets match the provided status)
        PetObject[] expectedOutput = new PetObject[0];
        // Verify HTTP status code and output
        verifyGetPetFindByStatus(statusArray, expectedOutput);
    }

    /**
     * Status Array with Duplicate Values: Valid array returns unique pets
     */
    @Test
    public void testGetPetFindByStatus_StatusArrayWithDuplicateValues() {
        // Input: status array with duplicate values
        String[] statusArray = {"available", "available"};
        // Expected Output: Unique pet objects matching the provided status
        PetObject[] expectedOutput = new PetObject[]{new PetObject(1234, "available")};
        // Verify HTTP status code and output
        verifyGetPetFindByStatus(statusArray, expectedOutput);
    }

    /* Helper Method to Verify Get Request */

    private void verifyGetPetFindByStatus(String[] statusArray, PetObject[] expectedOutput) {
        RestAssured.given()
                .pathParam("status", statusArray)
                .get("/pet/findByStatus/{status}")
                .then()
                .statusCode(200)
                .body("$.length()", equalTo(expectedOutput.length))
                .body("[0].id", hasItems(expectedOutput[0].getId()))
                .body("[0].status", hasItems(expectedOutput[0].getStatus()));
    }

    /* DELETE Method Test Case */

    /**
     * Delete Pet: No pet found, return 200 response with empty array
     */
    @Test
    public void testDeletePet_NoPetFound() {
        // Input: pet ID not existing in database
        long petId = 9999;
        // Expected Output: 200 response with empty array (no pets match the provided ID)
        PetObject[] expectedOutput = new PetObject[0];
        verifyDeletePet(petId, expectedOutput);
    }

    /**
     * Delete Non-Existing Pet: Return 204 response (no content)
     */
    @Test
    public void testDeleteNonExistingPet() {
        // Input: pet ID not existing in database
        long petId = 9999;
        // Expected Output: 204 response (no content, no pets match the provided ID)
        verifyDeletePet(petId, new PetObject[0]);
    }

    /* Helper Method to Verify Delete Request */

    private void verifyDeletePet(long petId, PetObject[] expectedOutput) {
        RestAssured.given()
                .pathParam("petId", petId)
                .delete("/pet/{petId}")
                .then()
                .statusCode(200)
                .body("[0].length()", equalTo(expectedOutput.length));
    }

    /* PUT Method Test Case */

    /**
     * Update Pet: Valid updated pet returns 200 response with updated pet
     */
    @Test
    public void testUpdatePet_ValidUpdatedPet() {
        // Input: valid updated pet details
        long petId = 1234;
        PetObject updatedPet = new PetObject(1234, "available");
        // Expected Output: 200 response with the updated pet
        verifyPutPet(petId, updatedPet);
    }

    /**
     * Update Non-Existing Pet: Return 404 response (not found)
     */
    @Test
    public void testUpdateNonExistingPet() {
        // Input: pet ID not existing in database
        long petId = 9999;
        PetObject updatedPet = new PetObject(1234, "available");
        verifyPutPet(petId, updatedPet);
    }

    /* Helper Method to Verify Put Request */

    private void verifyPutPet(long petId, PetObject updatedPet) {
        RestAssured.given()
                .pathParam("petId", petId)
                .body(updatedPet)
                .contentType(ContentType.JSON)
                .put("/pet/{petId}")
                .then()
                .statusCode(200)
                .body("[0].id", equalTo(updatedPet.getId()))
                .body("[0].status", equalTo(updatedPet.getStatus()));
    }

    /* POST Method Test Case */

    /**
     * Create New Pet: Valid new pet returns 200 response with created pet
     */
    @Test
    public void testCreateNewPet_ValidNewPet() {
        // Input: valid new pet details
        long petId = 1234;
        PetObject newPet = new PetObject(petId, "available");
        verifyPostPet(newPet);
    }

    /**
     * Create Duplicate Pet: Return 400 response (bad request)
     */
    @Test
    public void testCreateDuplicatePet() {
        // Input: duplicate pet ID and details
        long petId = 1234;
        PetObject newPet = new PetObject(petId, "available");
        verifyPostPet(newPet);
    }

    /* Helper Method to Verify Post Request */

    private void verifyPostPet(PetObject newPet) {
        RestAssured.given()
                .body(newPet)
                .contentType(ContentType.JSON)
                .post("/pet")
                .then()
                .statusCode(200)
                .body("[0].id", equalTo(newPet.getId()))
                .body("[0].status", equalTo(newPet.getStatus()));
    }

    @AfterClass
    public void tearDown() {
        // Clean up any resources used by the test class
    }
}

// Custom Class to represent Pet Objects
class PetObject {
    private long id;
    private String status;

    public PetObject(long id, String status) {
        this.id = id;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
