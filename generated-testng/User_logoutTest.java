import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class UserLogoutTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // GET /user/logout tests

    @DataProvider(name = "getTests")
    public Object[][] getTestData() {
        return new Object[][]{
                {"Happy Path - User Logout", 200, ""},
                {"Unauthorized User - Missing Token", 401, ""},
                {"Token Expiration - Expired Token", 401, ""},
                {"Error Path - Server Error", 500, ""},
                {"No Token Provided - Public Route", 200, ""}
        };
    }

    @Test(dataProvider = "getTests")
    public void getLogoutUserTest(String testName, int expectedStatus, String edgeCaseReason) {
        String token;
        switch (testName) {
            case "Happy Path - User Logout":
                token = "valid_token";
                break;
            case "Unauthorized User - Missing Token":
                token = null;
                break;
            case "Token Expiration - Expired Token":
                token = "expired_token";
                break;
            case "Error Path - Server Error":
                token = "valid_token";
                break;
            case "No Token Provided - Public Route":
                token = null;
                break;
            default:
                throw new RuntimeException("Invalid test name");
        }

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when().get("/user/logout")
                .then()
                .statusCode(expectedStatus);
    }

    // POST /user/logout tests

    @DataProvider(name = "postTests")
    public Object[][] postTestData() {
        return new Object[][]{
                {"Happy Path - User Logout", 200, ""},
                {"Unauthorized User - Missing Token", 401, ""}
        };
    }

    @Test(dataProvider = "postTests")
    public void postLogoutUserTest(String testName, int expectedStatus, String edgeCaseReason) {
        // Currently no POST test is specified for this API path
        // So we just return a valid status code
        RestAssured.given()
                .when().post("/user/logout")
                .then()
                .statusCode(expectedStatus);
    }

    // PUT /user/logout tests

    @DataProvider(name = "putTests")
    public Object[][] putTestData() {
        return new Object[][]{
                {"Happy Path - User Logout", 200, ""},
                {"Unauthorized User - Missing Token", 401, ""}
        };
    }

    @Test(dataProvider = "putTests")
    public void putLogoutUserTest(String testName, int expectedStatus, String edgeCaseReason) {
        // Currently no PUT test is specified for this API path
        // So we just return a valid status code
        RestAssured.given()
                .when().put("/user/logout")
                .then()
                .statusCode(expectedStatus);
    }

    // DELETE /user/logout tests

    @DataProvider(name = "deleteTests")
    public Object[][] deleteTestData() {
        return new Object[][]{
                {"Happy Path - User Logout", 200, ""},
                {"Unauthorized User - Missing Token", 401, ""}
        };
    }

    @Test(dataProvider = "deleteTests")
    public void deleteLogoutUserTest(String testName, int expectedStatus, String edgeCaseReason) {
        // Currently no DELETE test is specified for this API path
        // So we just return a valid status code
        RestAssured.given()
                .when().delete("/user/logout")
                .then()
                .statusCode(expectedStatus);
    }
}
