import io.restassured.RestAssured;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PetImageUploadTest {

    private String petId = "1";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // Happy Path Test Case - Valid Pet ID and No File
    @Test(description = "Happy Path Test Case - Valid Pet ID and No File")
    public void testHappyPath_NoFile() {
        given()
                .pathParam("petId", petId)
                .when()
                .post("/pet/{petId}/uploadImage")
                .then()
                .statusCode(200);
    }

    // Happy Path Test Case - Valid Pet ID and File
    @Test(description = "Happy Path Test Case - Valid Pet ID and File")
    public void testHappyPath_File() {
        given()
                .pathParam("petId", petId)
                .header("Content-Type", "multipart/form-data")
                .body("{\"file\":\"image.jpg\"}")
                .when()
                .post("/pet/{petId}/uploadImage")
                .then()
                .statusCode(200);
    }

    // Edge Case Test Case - Invalid File
    @Test(description = "Edge Case Test Case - Invalid File", expectedExceptions = AssertionError.class)
    public void testEdgeCase_InvalidFile() {
        given()
                .pathParam("petId", petId)
                .when()
                .post("/pet/{petId}/uploadImage")
                .then()
                .statusCode(400);
    }

    // Edge Case Test Case - Invalid Pet ID
    @Test(description = "Edge Case Test Case - Invalid Pet ID", expectedExceptions = AssertionError.class)
    public void testEdgeCase_InvalidPetID() {
        given()
                .pathParam("petId", null)
                .when()
                .post("/pet/{petId}/uploadImage")
                .then()
                .statusCode(404);
    }

    // Edge Case Test Case - Internal Server Error
    @Test(description = "Edge Case Test Case - Internal Server Error", expectedExceptions = AssertionError.class)
    public void testEdgeCase_InternalServerError() {
        given()
                .when()
                .post("/pet/{petId}/uploadImage")
                .then()
                .statusCode(500);
    }
}
