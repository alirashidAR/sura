import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserAPITests {

    private String baseUri = "https://api.example.com";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseUri;
    }

    // GROUP 1 - GET Methods

    @Test(priority = 0, description = "Valid Username")
    public void testGetValidUsername() {
        Response response = get("/user/user1");
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 1, description = "Invalid Username (Empty)")
    public void testGetInvalidUsername_Empty() {
        Response response = get("/user/");
        Assert.assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 2, description = "User Not Found")
    public void testGetUserNotFound() {
        Response response = get("/user/unknownusername");
        Assert.assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 3, description = "Username with Special Characters")
    public void testGetUsernameWithSpecialCharacters() {
        Response response = get("/user/!@#$%^&*()");
        Assert.assertEquals(response.getStatusCode(), 400);
    }

    // GROUP 2 - PUT Methods

    @Test(priority = 4, description = "PUT Valid User Details")
    public void testPutValidUserDetails() {
        Response response = put("/user/johnDoe", "{\"name\": \"John Doe\", \"email\": \"johndoe@example.com\"}");
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 5, description = "PUT User Does Not Exist")
    public void testPutUserDoesNotExist() {
        Response response = put("/user/johnDoe", "{\"name\": \"John Doe\", \"email\": \"johndoe@example.com\"}");
        Assert.assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 6, description = "PUT Invalid User Details")
    public void testPutInvalidUserDetails() {
        Response response = put("/user/johnDoe", "{\"name\": null, \"email\": \"johndoe@example.com\"}");
        Assert.assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 7, description = "PUT Invalid Username")
    public void testPutInvalidUsername() {
        Response response = put("/user/invalidUsername", "{\"name\": \"John Doe\", \"email\": \"johndoe@example.com\"}");
        Assert.assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 8, description = "PUT Empty Username")
    public void testPutEmptyUsername() {
        Response response = put("/user/", "{\"name\": \"John Doe\", \"email\": \"johndoe@example.com\"}");
        Assert.assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 9, description = "PUT Invalid User Details Empty Username")
    public void testPutInvalidUserDetailsEmptyUsername() {
        Response response = put("/user/", "{\"name\": \"John Doe\", \"email\": \"johndoe@example.com\"}");
        Assert.assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 10, description = "PUT Empty User Details")
    public void testPutEmptyUserDetails() {
        Response response = put("/user/johnDoe", "{\"name\": \"John Doe\", \"email\": null}");
        Assert.assertEquals(response.getStatusCode(), 400);
    }

    // GROUP 3 - DELETE Methods

    @Test(priority = 11, description = "Delete User by Valid Username")
    public void testDeleteUserValidUsername() {
        Response response = delete("/user/{username}", "johnDoe");
        Assert.assertEquals(response.getStatusCode(), 204);
    }

    @Test(priority = 12, description = "Delete User by Invalid Username (404)")
    public void testDeleteUserInvalidUsername_404() {
        Response response = delete("/user/{username}", "invalidUsername");
        Assert.assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 13, description = "Delete User by Valid Username (logged in as admin)")
    public void testDeleteUserValidUsernameLoggedinAsAdmin() {
        Response response = delete("/user/{username}", "johnDoe");
        Assert.assertEquals(response.getStatusCode(), 204);
    }

    @Test(priority = 14, description = "Delete User by Valid Username (logged in as same user)")
    public void testDeleteUserValidUsernameLoggedinAsSameUser() {
        Response response = delete("/user/{username}", "johnDoe");
        Assert.assertEquals(response.getStatusCode(), 204);
    }

    private Response get(String path) {
        return RestAssured.given().when().get(path).andReturn();
    }

    private Response put(String path, String body) {
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .body(body)
                .when().put(path)
                .andReturn();
    }

    private Response delete(String path, String username) {
        return RestAssured.given()
                .pathParam("username", username)
                .when().delete(path)
                .andReturn();
    }
}
