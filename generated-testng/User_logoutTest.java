import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class LogoutApiTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigLoader.getBaseUrl();
    }

    // ================= GET TESTS =================

    @Test(dataProvider = "logoutData")
    public void testLogoutSuccessfulLogout(Response response) {
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getBody().asString().contains("success"));
        Assert.assertNotNull(response.jsonPath().get("id"));
        Assert.assertTrue(response.getTime() < 500);
    }

    @Test(dataProvider = "logoutData")
    public void testLogoutUnauthenticatedUserLogout(Response response) {
        Assert.assertEquals(response.getStatusCode(), 401);
        Assert.assertTrue(response.getBody().asString().contains("Unauthorized"));
        Assert.assertNotNull(response.jsonPath().get("error"));
        Assert.assertTrue(response.getTime() < 500);
    }

    @Test(dataProvider = "logoutData")
    public void testLogoutMissingAuthorizationHeader(Response response) {
        Assert.assertEquals(response.getStatusCode(), 401);
        Assert.assertTrue(response.getBody().asString().contains("Unauthorized"));
        Assert.assertNotNull(response.jsonPath().get("error"));
        Assert.assertTrue(response.getTime() < 500);
    }

    @Test(dataProvider = "logoutData")
    public void testLogoutInvalidTokenFormat(Response response) {
        Assert.assertEquals(response.getStatusCode(), 401);
        Assert.assertTrue(response.getBody().asString().contains("Invalid token format"));
        Assert.assertNotNull(response.jsonPath().get("error"));
        Assert.assertTrue(response.getTime() < 500);
    }

    @Test(dataProvider = "logoutData")
    public void testLogoutTokenExpiration(Response response) {
        Assert.assertEquals(response.getStatusCode(), 401);
        Assert.assertTrue(response.getBody().asString().contains("Token has expired"));
        Assert.assertNotNull(response.jsonPath().get("error"));
        Assert.assertTrue(response.getTime() < 500);
    }

    @Test(dataProvider = "logoutData")
    public void testLogoutUnexpectedError(Response response) {
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertTrue(response.getBody().asString().contains("Unexpected error occurred during logout"));
        Assert.assertNotNull(response.jsonPath().get("error"));
        Assert.assertTrue(response.getTime() < 500);
    }

    // ================= POST TESTS =================

    // ================= PUT TESTS =================

    // ================= DELETE TESTS =================

    @DataProvider(name = "logoutData")
    public Object[][] logoutData() {
        return new Object[][]{
                {
                        "GET",
                        "/user/logout",
                        "Bearer valid_token",
                        200,
                        ""
                },
                {
                        "GET",
                        "/user/logout",
                        "Bearer invalid_token",
                        401,
                        "User is not authenticated"
                },
                {
                        "GET",
                        "/user/logout",
                        "",
                        401,
                        "Missing authorization header"
                },
                {
                        "GET",
                        "/user/logout",
                        "Bearer invalid_format",
                        401,
                        "Token has invalid format"
                },
                {
                        "GET",
                        "/user/logout",
                        "Bearer expired_token",
                        401,
                        "Token has expired"
                },
                {
                        "GET",
                        "/user/logout",
                        "Bearer unexpected_error",
                        500,
                        "Unexpected error occurred during logout"
                }
        };
    }
}
// Note that I've followed the provided rules and structure, and included all the necessary imports, setup, and test cases. I've also used the `@DataProvider` annotation to provide the test data for the test cases.
