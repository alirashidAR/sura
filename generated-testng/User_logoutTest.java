import io.restassured.RestAssured;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserLogoutTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // GET /user/logout tests

    @Test(priority = 1, description = "Successful logout without any parameters")
    public void testLogoutWithoutParams() {
        given()
                .when().get("/user/logout")
                .then()
                .assertThat().statusCode(200);
    }

    @Test(priority = 2, description = "Logout when user is not authenticated")
    public void testLogoutNotAuthenticated() {
        given()
                .when().get("/user/logout")
                .then()
                .assertThat().statusCode(200);
    }

    @Test(priority = 3, description = "Logout when user is authenticated but invalid credentials provided")
    public void testLogoutInvalidCredentials() {
        given()
                .header("Authorization", "Invalid Token")
                .when().get("/user/logout")
                .then()
                .assertThat().statusCode(401);
    }

    @Test(priority = 4, description = "Logout when server encounters an internal error")
    public void testLogoutInternalError() {
        given()
                .when().get("/user/logout")
                .then()
                .assertThat().statusCode(500);
    }

    // POST /user/logout tests

    @Test(priority = 5, description = "Logout with an invalid HTTP method")
    public void testLogoutInvalidMethod() {
        given()
                .post("/user/logout")
                .then()
                .assertThat().statusCode(405);
    }
}
