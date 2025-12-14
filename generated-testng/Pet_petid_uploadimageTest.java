import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PetUploadImageTest {

    private String BASE_URL = "https://api.example.com";

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    /**
     * GROUP 1: GET
     */
    // No test cases for GET method

    /**
     * GROUP 2: POST
     */

    @Test
    public void testCaseSuccessfulImageUpload() {
        given()
                .header("Content-Type", "multipart/form-data")
                .formParam("file.filename", "image.jpg")
                .formParam("file.content", "binary data for image.jpg")
                .pathParam("petId", 1)
        .when()
                .post("/pet/{petId}/uploadImage")
        .then()
                .assertThat().statusCode(200);
    }

    @Test
    public void testCaseInvalidPetId() {
        given()
                .header("Content-Type", "multipart/form-data")
                .formParam("file.filename", "image.jpg")
                .formParam("file.content", "binary data for image.jpg")
                .pathParam("petId", "abc")
        .when()
                .post("/pet/{petId}/uploadImage")
        .then()
                .assertThat().statusCode(400);
    }

    @Test
    public void testCaseMissingFileInRequestBody() {
        given()
                .header("Content-Type", "multipart/form-data")
        .when()
                .post("/pet/1/uploadImage")
        .then()
                .assertThat().statusCode(400);
    }

    @Test
    public void testCaseInvalidFileType() {
        given()
                .header("Content-Type", "multipart/form-data")
                .formParam("file.filename", "image.mp3")
                .formParam("file.content", "binary data for image.mp3")
                .pathParam("petId", 1)
        .when()
                .post("/pet/{petId}/uploadImage")
        .then()
                .assertThat().statusCode(400);
    }

    @Test
    public void testCaseFileSizeExceededLimit() {
        given()
                .header("Content-Type", "multipart/form-data")
                .formParam("file.filename", "image.jpg")
                .formParam("file.content", "large binary data for image.jpg (exceeding server limit)")
                .pathParam("petId", 1)
        .when()
                .post("/pet/{petId}/uploadImage")
        .then()
                .assertThat().statusCode(400);
    }

    /**
     * GROUP 3: PUT
     */
    // No test cases for PUT method

    /**
     * GROUP 4: DELETE
     */
    // No test cases for DELETE method
}
