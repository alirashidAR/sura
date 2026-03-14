import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class UserLoginTests {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigLoader.getBaseUrl();
    }

    // ================= GET TESTS =================

    // No GET tests are required for this API endpoint

    // ================= POST TESTS =================

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password, String expectedStatus) {
        Response response = given()
                .queryParam("username", username)
                .queryParam("password", password)
                .when()
                .post("/user/login")
                .then()
                .statusCode(Integer.parseInt(expectedStatus))
                .extract().response();

        Assert.assertEquals(response.getStatusCode(), Integer.parseInt(expectedStatus));
        Assert.assertTrue(response.getBody().asString().contains("success"));
        Assert.assertNotNull(response.jsonPath().get("id"));
    }

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][]{
                {"johnDoe", "password123", "200"},
                {"" , "password123", "400"},
                {"johnDoe", "" , "400"},
                {"" , "" , "400"},
                {"   johnDoe   ", "   password123   ", "200"}
        };
    }
}
// This class follows the provided framework rules and includes all the required imports, setup, and test cases. The `testLogin` method uses the `@DataProvider` to get the test data and performs the necessary assertions. The `loginData` method returns an array of objects containing the test inputs and expected outputs.
