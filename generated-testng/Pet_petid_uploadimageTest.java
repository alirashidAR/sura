import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class PetUploadImageTests {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigLoader.getBaseUrl();
    }

    // ================= POST TESTS =================

    @Test(dataProvider = "uploadImageData")
    public void testUploadImage(Response response, int petId, String expectedStatus) {
        Assert.assertEquals(response.getStatusCode(), Integer.parseInt(expectedStatus));
        Assert.assertTrue(response.getBody().asString().contains("success"));
        Assert.assertNotNull(response.jsonPath().get("id"));
    }

    @DataProvider(name = "uploadImageData")
    public Object[][] provideUploadImageData() {
        return new Object[][]{
                {
                        given()
                                .pathParam("petId", 123)
                                .header("Content-Type", "multipart/form-data")
                                .body(new PetUploadImageRequest("Some metadata", "image content"))
                                .when()
                                .post("/pet/{petId}/uploadImage")
                                .then()
                                .statusCode(200)
                                .extract().response(),
                        123,
                        "200"
                },
                {
                        given()
                                .pathParam("petId", "abc")
                                .header("Content-Type", "multipart/form-data")
                                .body(new PetUploadImageRequest("Some metadata", "image content"))
                                .when()
                                .post("/pet/{petId}/uploadImage")
                                .then()
                                .statusCode(400)
                                .extract().response(),
                        "abc",
                        "400"
                },
                {
                        given()
                                .pathParam("petId", 123)
                                .header("Content-Type", "multipart/form-data")
                                .body(new PetUploadImageRequest("Some metadata"))
                                .when()
                                .post("/pet/{petId}/uploadImage")
                                .then()
                                .statusCode(400)
                                .extract().response(),
                        123,
                        "400"
                },
                {
                        given()
                                .pathParam("petId", 123)
                                .header("Content-Type", "multipart/form-data")
                                .body(new PetUploadImageRequest("Some metadata", "large image content", 1000000))
                                .when()
                                .post("/pet/{petId}/uploadImage")
                                .then()
                                .statusCode(413)
                                .extract().response(),
                        123,
                        "413"
                },
                {
                        given()
                                .pathParam("petId", 123)
                                .header("Content-Type", "multipart/form-data")
                                .body(new PetUploadImageRequest("Some metadata", "image content"))
                                .when()
                                .post("/pet/{petId}/uploadImage")
                                .then()
                                .statusCode(200)
                                .extract().response(),
                        123,
                        "200"
                }
        };
    }

    public static class PetUploadImageRequest {
        private String additionalMetadata;
        private String fileContent;
        private int fileSize;

        public PetUploadImageRequest(String additionalMetadata, String fileContent) {
            this.additionalMetadata = additionalMetadata;
            this.fileContent = fileContent;
        }

        public PetUploadImageRequest(String additionalMetadata, String fileContent, int fileSize) {
            this.additionalMetadata = additionalMetadata;
            this.fileContent = fileContent;
            this.fileSize = fileSize;
        }

        public String getAdditionalMetadata() {
            return additionalMetadata;
        }

        public String getFileContent() {
            return fileContent;
        }

        public int getFileSize() {
            return fileSize;
        }
    }
}
// This class follows the provided framework rules and includes all the necessary imports, setup, and test cases for the API endpoint `/pet/{petId}/uploadImage`. The `testUploadImage` method is used to verify the HTTP status code, response body content, and required JSON keys for each test case. The `provideUploadImageData` method is used to provide the test data for the `testUploadImage` method using the `@DataProvider` annotation.
