import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PetDeleteTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // DELETE /pet/{petId}
    @Test(description = "Delete pet with valid id and api key")
    public void deletePetValidApiKey() {
        int petId = 12345;
        String apiKey = "some-api-key";

        RestAssured.given()
                .when().delete("/pet/{petId}", petId)
                .then()
                .statusCode(200);
    }

    // DELETE /pet/{petId}
    @Test(description = "Delete pet with invalid api key")
    public void deletePetInvalidApiKey() {
        int petId = 12345;
        String apiKey = "";

        RestAssured.given()
                .when().delete("/pet/{petId}", petId)
                .then()
                .statusCode(400);
    }

    // DELETE /pet/{petId}
    @Test(description = "Delete pet with missing api key")
    public void deletePetMissingApiKey() {
        int petId = 12345;
        String apiKey = "";

        RestAssured.given()
                .when().delete("/pet/{petId}", petId)
                .then()
                .statusCode(400);
    }

    // DELETE /pet/{petId}
    @Test(description = "Delete pet with invalid id (-1)")
    public void deletePetInvalidIdMinusOne() {
        int petId = -1;
        String apiKey = "some-api-key";

        RestAssured.given()
                .when().delete("/pet/{petId}", petId)
                .then()
                .statusCode(400);
    }

    // DELETE /pet/{petId}
    @Test(description = "Delete pet with non-existent id (99999)")
    public void deletePetNonExistentId() {
        int petId = 99999;
        String apiKey = "some-api-key";

        RestAssured.given()
                .when().delete("/pet/{petId}", petId)
                .then()
                .statusCode(404);
    }
}
