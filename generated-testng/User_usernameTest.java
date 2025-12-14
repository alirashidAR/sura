import io.restassured.RestAssured;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.isA, equalTo;

public class UserAPITests {

    private String baseUri = "https://api.example.com";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseUri;
    }

    // GET tests
    @Test(description = "/user/{username} (happy path)")
    public void testGetUserHappyPath() {
        given()
                .pathParam("username", "user1")
                .when()
                .get("/user/{username}")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test(description = "/user/ (empty username)")
    public void testGetUserEmptyUsername() {
        given()
                .pathParam("username", "")
                .when()
                .get("/user/{username}")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test(description = "/user/user!@# (username with special characters)")
    public void testGetUserSpecialCharacters() {
        given()
                .pathParam("username", "user!@#")
                .when()
                .get("/user/{username}")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test(description = "/user/non-existent-user (not found)")
    public void testGetUserNotFound() {
        given()
                .pathParam("username", "non-existent-user")
                .when()
                .get("/user/{username}")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test(description = "/user/null-username (400 error)")
    public void testGetUserNullUsername() {
        given()
                .pathParam("username", null)
                .when()
                .get("/user/{username}")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test(description = "/user/user1 (caching test)")
    public void testGetUserCaching() {
        given()
                .pathParam("username", "user1")
                .header("Cache-Control", "max-age=3600")
                .when()
                .get("/user/{username}")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test(description = "/user/user1 (concurrent requests)")
    public void testGetUserConcurrentRequests() {
        given()
                .pathParam("username", "user1")
                .header("X-Forwarded-For", "192.168.1.100")
                .when()
                .get("/user/{username}")
                .then()
                .assertThat()
                .statusCode(200);
    }

    // PUT tests
    @Test(description = "Valid Update Request")
    public void testPutUserValidUpdate() {
        given()
                .pathParam("username", "johnDoe")
                .body("{\"email\":\"john.doe@example.com\",\"phone\":"+ "\"+1-555-555-5555\"}")
                .when()
                .put("/user/{username}")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test(description = "Invalid Update Request - Missing Body")
    public void testPutUserMissingBody() {
        given()
                .pathParam("username", "johnDoe")
                .when()
                .put("/user/{username}")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test(description = "Invalid Update Request - Invalid User Object")
    public void testPutUserInvalidObject() {
        given()
                .pathParam("username", "johnDoe")
                .body("{\"email\":null,\"phone\":\"invalid phone number\"}")
                .when()
                .put("/user/{username}")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test(description = "UnAuthorized Update Request - Different Username")
    public void testPutUserDifferentUsername() {
        given()
                .pathParam("username", "janeDoe")
                .body("{\"email\":\"john.doe@example.com\",\"phone\":"+ "\"+1-555-555-5555\"}")
                .when()
                .put("/user/{username}")
                .then()
                .assertThat()
                .statusCode(401);
    }

    @Test(description = "UnAuthorized Update Request - No Authorization Header")
    public void testPutUserNoAuthHeader() {
        given()
                .pathParam("username", "johnDoe")
                .body("{\"email\":\"john.doe@example.com\",\"phone\":"+ "\"+1-555-555-5555\"}")
                .when()
                .put("/user/{username}")
                .then()
                .assertThat()
                .statusCode(401);
    }

    // DELETE tests
    @Test(description = "Valid Delete Request")
    public void testDeleteUserHappyPath() {
        given()
                .pathParam("username", "johnDoe")
                .when()
                .delete("/user/{username}")
                .then()
                .assertThat()
                .statusCode(204);
    }

    @Test(description = "Invalid Username")
    public void testDeleteUserInvalidUsername() {
        given()
                .pathParam("username", "invalidUsername")
                .when()
                .delete("/user/{username}")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test(description = "User Not Found")
    public void testDeleteUserNotFound() {
        given()
                .pathParam("username", "nonExistentUser")
                .when()
                .delete("/user/{username}")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test(description = "Delete Request with Empty Username")
    public void testDeleteUserEmptyUsername() {
        given()
                .pathParam("username", "")
                .when()
                .delete("/user/{username}")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test(description = "Delete Request with Null Username")
    public void testDeleteUserNullUsername() {
        given()
                .pathParam("username", null)
                .when()
                .delete("/user/{username}")
                .then()
                .assertThat()
                .statusCode(400);
    }
}
