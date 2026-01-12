import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class PetFinderTagsApiTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    /**
     * Grouping test methods by HTTP method using Java comments
     */
    // GET Methods
    @DataProvider(name = "getValidRequests")
    public Object[][] getValidRequests() {
        return new Object[][]{
                {"tag1", "tag2", "tag3"},
                {"tag1"}
        };
    }

    @DataProvider(name = "getErrorRequests")
    public Object[][] getErrorRequests() {
        return new Object[][]{
                {new String[]{}, ""} // Empty tags array
                , {1, 2, 3}            // Tags array contains non-string values
        };
    }

    @Test(dataProvider = "getValidRequests", description = "Valid GET request with multiple tags")
    public void testValidGetRequestWithMultipleTags(String... tags) {
        given()
                .queryParam("tags", tags)
                .header("Content-Type", "application/json")
                .when()
                .get("/pet/findByTags")
                .then()
                .statusCode(200);
    }

    @Test(dataProvider = "getErrorRequests", description = "Invalid GET request with empty or non-string values")
    public void testInvalidGetRequest(String... tags) {
        given()
                .queryParam("tags", tags)
                .header("Content-Type", "application/json")
                .when()
                .get("/pet/findByTags")
                .then()
                .statusCode(400);
    }

    @Test(description = "Valid GET request with no tags provided")
    public void testValidGetRequestWithNoTags() {
        given()
                .queryParam("tags", "")
                .header("Content-Type", "application/json")
                .when()
                .get("/pet/findByTags")
                .then()
                .statusCode(200);
    }

    // POST Methods
    @Test(description = "Valid POST request with multiple tags")
    public void testValidPostRequestWithMultipleTags() {
        given()
                .queryParam("tags", "tag1, tag2, tag3")
                .header("Content-Type", "application/json")
                .when()
                .post("/pet/findByTags")
                .then()
                .statusCode(200);
    }

    @Test(description = "Invalid POST request with empty tags")
    public void testInvalidPostRequestWithEmptyTags() {
        given()
                .queryParam("tags", "")
                .header("Content-Type", "application/json")
                .when()
                .post("/pet/findByTags")
                .then()
                .statusCode(400);
    }

    @Test(description = "Valid POST request with single tag")
    public void testValidPostRequestWithSingleTag() {
        given()
                .queryParam("tags", "tag1")
                .header("Content-Type", "application/json")
                .when()
                .post("/pet/findByTags")
                .then()
                .statusCode(200);
    }

    @Test(description = "Invalid POST request with multiple tags containing non-string values")
    public void testInvalidPostRequestWithMultipleTagsContainingNonStringValues() {
        given()
                .queryParam("tags", "1, 2, 3")
                .header("Content-Type", "application/json")
                .when()
                .post("/pet/findByTags")
                .then()
                .statusCode(400);
    }

    @Test(description = "Valid POST request with no tags provided")
    public void testValidPostRequestWithNoTags() {
        given()
                .queryParam("tags", "")
                .header("Content-Type", "application/json")
                .when()
                .post("/pet/findByTags")
                .then()
                .statusCode(200);
    }

    // PUT Methods
    @Test(description = "Valid PUT request with multiple tags")
    public void testValidPutRequestWithMultipleTags() {
        given()
                .queryParam("tags", "tag1, tag2, tag3")
                .header("Content-Type", "application/json")
                .when()
                .put("/pet/findByTags")
                .then()
                .statusCode(200);
    }

    @Test(description = "Invalid PUT request with empty tags")
    public void testInvalidPutRequestWithEmptyTags() {
        given()
                .queryParam("tags", "")
                .header("Content-Type", "application/json")
                .when()
                .put("/pet/findByTags")
                .then()
                .statusCode(400);
    }

    @Test(description = "Valid PUT request with single tag")
    public void testValidPutRequestWithSingleTag() {
        given()
                .queryParam("tags", "tag1")
                .header("Content-Type", "application/json")
                .when()
                .put("/pet/findByTags")
                .then()
                .statusCode(200);
    }

    @Test(description = "Invalid PUT request with multiple tags containing non-string values")
    public void testInvalidPutRequestWithMultipleTagsContainingNonStringValues() {
        given()
                .queryParam("tags", "1, 2, 3")
                .header("Content-Type", "application/json")
                .when()
                .put("/pet/findByTags")
                .then()
                .statusCode(400);
    }

    @Test(description = "Valid PUT request with no tags provided")
    public void testValidPutRequestWithNoTags() {
        given()
                .queryParam("tags", "")
                .header("Content-Type", "application/json")
                .when()
                .put("/pet/findByTags")
                .then()
                .statusCode(200);
    }

    // DELETE Methods
    @Test(description = "Valid DELETE request with multiple tags")
    public void testValidDeleteRequestWithMultipleTags() {
        given()
                .queryParam("tags", "tag1, tag2, tag3")
                .header("Content-Type", "application/json")
                .when()
                .delete("/pet/findByTags")
                .then()
                .statusCode(200);
    }

    @Test(description = "Invalid DELETE request with empty tags")
    public void testInvalidDeleteRequestWithEmptyTags() {
        given()
                .queryParam("tags", "")
                .header("Content-Type", "application/json")
                .when()
                .delete("/pet/findByTags")
                .then()
                .statusCode(400);
    }

    @Test(description = "Valid DELETE request with single tag")
    public void testValidDeleteRequestWithSingleTag() {
        given()
                .queryParam("tags", "tag1")
                .header("Content-Type", "application/json")
                .when()
                .delete("/pet/findByTags")
                .then()
                .statusCode(200);
    }

    @Test(description = "Invalid DELETE request with multiple tags containing non-string values")
    public void testInvalidDeleteRequestWithMultipleTagsContainingNonStringValues() {
        given()
                .queryParam("tags", "1, 2, 3")
                .header("Content-Type", "application/json")
                .when()
                .delete("/pet/findByTags")
                .then()
                .statusCode(400);
    }

    @Test(description = "Valid DELETE request with no tags provided")
    public void testValidDeleteRequestWithNoTags() {
        given()
                .queryParam("tags", "")
                .header("Content-Type", "application/json")
                .when()
                .delete("/pet/findByTags")
                .then()
                .statusCode(200);
    }
}
