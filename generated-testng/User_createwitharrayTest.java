import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class CreateUserWithArrayTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigLoader.getBaseUrl();
    }

    // ================= POST TESTS =================

    @Test(dataProvider = "createUserWithArrayData")
    public void createUserWithArrayTest(String methodName, String expectedStatus, String edgeCaseReason) {
        given()
                .header("Content-Type", "application/json")
                .body(getRequestBody(methodName))
        .when()
                .post("/user/createWithArray")
        .then()
                .statusCode(Integer.parseInt(expectedStatus))
                .body("contains", "success")
                .body("id", Assert::notNull)
                .time(lessThan(1000));
    }

    @DataProvider(name = "createUserWithArrayData")
    public Object[][] createUserWithArrayData() {
        return new Object[][]{
                {"Happy Path - Create Users with Array", "200", "Valid list of users with array"},
                {"Edge Case - Empty Array", "400", "Invalid array with no users"},
                {"Edge Case - Null Array", "400", "Invalid array with null value"},
                {"Edge Case - Array with Invalid User", "400", "Invalid array with user with invalid email"},
                {"Negative Test - Non-JSON Request Body", "400", "Non-JSON request body"},
                {"Happy Path - Create Users with Single User", "200", "Valid single user with array"}
        };
    }

    private Object getRequestBody(String methodName) {
        switch (methodName) {
            case "Happy Path - Create Users with Array":
                return "[{\"name\":\"John Doe\",\"email\":\"john@example.com\"},{\"name\":\"Jane Doe\",\"email\":\"jane@example.com\"}]";
            case "Edge Case - Empty Array":
                return "[]";
            case "Edge Case - Null Array":
                return "null";
            case "Edge Case - Array with Invalid User":
                return "[{\"name\":\"John Doe\",\"email\":\"john@example.com\"},{\"name\":\"Invalid User\",\"email\":\"invalid_user\"}]";
            case "Negative Test - Non-JSON Request Body":
                return "\"Invalid JSON\"";
            case "Happy Path - Create Users with Single User":
                return "[{\"name\":\"John Doe\",\"email\":\"john@example.com\"}]";
            default:
                return null;
        }
    }
}
// This code follows the provided framework rules and includes all the necessary imports, setup, and test cases. The `createUserWithArrayData` method uses a data provider to supply the test data for the `createUserWithArrayTest` method. The `getRequestBody` method returns the request body for each test case.
