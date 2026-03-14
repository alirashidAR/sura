import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class UserApiTests {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigLoader.getBaseUrl();
    }

    // ================= GET TESTS =================

    @Test(dataProvider = "getTests")
    public void testGetUser(String username, int expectedStatus) {
        Response response = given()
                .pathParam("username", username)
                .when()
                .get("/user/{username}")
                .then()
                .statusCode(expectedStatus)
                .extract().response();

        Assert.assertEquals(response.getStatusCode(), expectedStatus);
        Assert.assertTrue(response.getBody().asString().contains("success"));
        Assert.assertNotNull(response.jsonPath().get("id"));
    }

    @DataProvider(name = "getTests")
    public Object[][] getTests() {
        return new Object[][]{
                {"user1", 200},
                {"non_existent_user", 404},
                {"invalid#username", 400},
                {"" , 400},
                {null, 400}
        };
    }

    // ================= PUT TESTS =================

    @Test(dataProvider = "putTests")
    public void testPutUser(String username, String token, int expectedStatus) {
        Response response = given()
                .pathParam("username", username)
                .header("Authorization", "Bearer " + token)
                .when()
                .put("/user/{username}")
                .then()
                .statusCode(expectedStatus)
                .extract().response();

        Assert.assertEquals(response.getStatusCode(), expectedStatus);
        Assert.assertTrue(response.getBody().asString().contains("success"));
        Assert.assertNotNull(response.jsonPath().get("id"));
    }

    @DataProvider(name = "putTests")
    public Object[][] putTests() {
        return new Object[][]{
                {"johnDoe", "valid_token", 200},
                {"johnDoe", "invalid_token", 401},
                {"", "valid_token", 400},
                {"johnDoe", "valid_token", 400}
        };
    }

    // ================= DELETE TESTS =================

    @Test(dataProvider = "deleteTests")
    public void testDeleteUser(String username, String token, int expectedStatus) {
        Response response = given()
                .pathParam("username", username)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/user/{username}")
                .then()
                .statusCode(expectedStatus)
                .extract().response();

        Assert.assertEquals(response.getStatusCode(), expectedStatus);
        Assert.assertTrue(response.getBody().asString().contains("success"));
        Assert.assertNotNull(response.jsonPath().get("id"));
    }

    @DataProvider(name = "deleteTests")
    public Object[][] deleteTests() {
        return new Object[][]{
                {"johndoe", "logged-in-user-token", 200},
                {"johndoe", "unauthenticated-user-token", 401},
                {"nonexistentuser", "logged-in-user-token", 404},
                {"invalidusername", "logged-in-user-token", 400},
                {null, "logged-in-user-token", 400}
        };
    }
}
// Note that I've assumed that the `ConfigLoader` class is responsible for loading the base URL for the API, and that the `given()` method is used to create a new request. I've also assumed that the `when()` method is used to specify the HTTP method (e.g. GET, PUT, DELETE), and that the `then()` method is used to specify the expected response.
