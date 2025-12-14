import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserLoginTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // GROUP GET TESTS BY HTTP METHOD
    /**
     * Test case for getting user login response with valid credentials
     */
    @Test
    public void testGetValidUserLoginResponse() {
        Response response = given().queryParam("username", "test_user").queryParam("password", "test_password")
                .when()
                .get("/user/login");

        then()
                .statusCode(200)
                .body("headers.X-Expires-After", equalTo("2024-04-30T12:00:00Z"))
                .body("headers.X-Rate-Limit", equalTo(100));
    }

    /**
     * Test case for getting user login response with empty username and password
     */
    @Test
    public void testGetInvalidUserLoginResponse() {
        Response response = given().queryParam("username", "").queryParam("password", "")
                .when()
                .get("/user/login");

        then()
                .statusCode(400)
                .body("message", equalTo("Invalid username/password supplied"));
    }

    /**
     * Test case for getting user login response with null username and password
     */
    @Test
    public void testGetNullUserLoginResponse() {
        Response response = given().queryParam("username", (Object) null).queryParam("password", (Object) null)
                .when()
                .get("/user/login");

        then()
                .statusCode(400)
                .body("message", equalTo("Invalid username/password supplied"));
    }

    /**
     * Test case for getting user login response with valid username and empty password
     */
    @Test
    public void testGetValidUsernameEmptyPasswordUserLoginResponse() {
        Response response = given().queryParam("username", "test_user").queryParam("password", "")
                .when()
                .get("/user/login");

        then()
                .statusCode(400)
                .body("message", equalTo("Invalid username/password supplied"));
    }

    /**
     * Test case for getting user login response with empty username and valid password
     */
    @Test
    public void testGetEmptyUsernameValidPasswordUserLoginResponse() {
        Response response = given().queryParam("username", "").queryParam("password", "test_password")
                .when()
                .get("/user/login");

        then()
                .statusCode(400)
                .body("message", equalTo("Invalid username/password supplied"));
    }

    /**
     * Test case for getting user login response with valid credentials and weak password
     */
    @Test
    public void testGetValidUserLoginResponseWithWeakPassword() {
        Response response = given().queryParam("username", "test_user").queryParam("password", "123456")
                .when()
                .get("/user/login");

        then()
                .statusCode(200)
                .body("headers.X-Expires-After", equalTo("2024-04-30T12:00:00Z"))
                .body("headers.X-Rate-Limit", equalTo(100));
    }
}
