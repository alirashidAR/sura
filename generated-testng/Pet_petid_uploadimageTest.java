import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PetImageUploadTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // Grouping test methods by HTTP method

    /**
     * Positive scenarios for POST /pet/{petId}/uploadImage
     */
    @DataProvider(name = "postTestData")
    public Object[][] postTestDataProvider() {
        return new Object[][]{
                {
                        1,
                        null,
                        null,
                        "{\"file\":{\"filename\":\"valid_image.jpg\",\"size\":100000,\"type\":\"image/jpeg\"}}"
                },
                {
                        1,
                        null,
                        null,
                        "{\"file\":{\"filename\":\"large_image.jpg\",\"size\":1000000,\"type\":\"image/jpeg\"}}"
                }
        };
    }

    /**
     * Negative scenarios for POST /pet/{petId}/uploadImage
     */
    @DataProvider(name = "postNegativeTestData")
    public Object[][] postNegativeTestData() {
        return new Object[][]{
                {
                        -1,
                        null,
                        null,
                        "{\"file\":{\"filename\":\"valid_image.jpg\",\"size\":100000,\"type\":\"image/jpeg\"}}"
                },
                {
                        1,
                        null,
                        null,
                        "{\"file\":{\"filename\":\"invalid_image.exe\",\"size\":100000,\"type\":\"application/octet-stream\"}}"
                }
        };
    }

    /**
     * Edge cases for POST /pet/{petId}/uploadImage
     */
    @DataProvider(name = "postEdgeCaseData")
    public Object[][] postEdgeCaseTestData() {
        return new Object[][]{
                {
                        1,
                        null,
                        null,
                        "{\"file\":{\"filename\":\"large_image.jpg\",\"size\":1000000,\"type\":\"image/jpeg\"}}"
                }
        };
    }

    @Test(dataProvider = "postTestData")
    public void testUploadImageValidPetId(int petId, Object queryParam, String header, String request) {
        given()
                .baseUri(RestAssured.baseURI)
                .pathParam("petId", petId)
                .body(request)
                .when()
                .post("/pet/{petId}/uploadImage")
                .then()
                .statusCode(200);
    }

    @Test(dataProvider = "postEdgeCaseData")
    public void testUploadImageLargeFile(int petId, Object queryParam, String header, String request) {
        given()
                .baseUri(RestAssured.baseURI)
                .pathParam("petId", petId)
                .body(request)
                .when()
                .post("/pet/{petId}/uploadImage")
                .then()
                .statusCode(413);
    }

    @Test(dataProvider = "postNegativeTestData")
    public void testUploadImageInvalidPetId(int petId, Object queryParam, String header, String request) {
        given()
                .baseUri(RestAssured.baseURI)
                .pathParam("petId", petId)
                .body(request)
                .when()
                .post("/pet/{petId}/uploadImage")
                .then()
                .statusCode(404);
    }

    @Test(dataProvider = "postNegativeTestData")
    public void testUploadImageInvalidFileType(int petId, Object queryParam, String header, String request) {
        given()
                .baseUri(RestAssured.baseURI)
                .pathParam("petId", petId)
                .body(request)
                .when()
                .post("/pet/{petId}/uploadImage")
                .then()
                .statusCode(400);
    }

    @Test
    public void testUploadImageWithoutPetId() {
        given()
                .baseUri(RestAssured.baseURI)
                .pathParam("petId", -1)
                .body("")
                .when()
                .post("/pet/{petId}/uploadImage")
                .then()
                .statusCode(400);
    }

    @Test
    public void testUploadImageWithoutAdditionalMetadata() {
        given()
                .baseUri(RestAssured.baseURI)
                .pathParam("petId", 1)
                .body("")
                .when()
                .post("/pet/{petId}/uploadImage")
                .then()
                .statusCode(200);
    }
}
