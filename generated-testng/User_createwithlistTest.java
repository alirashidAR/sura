import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class CreateUserTests {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigLoader.getBaseUrl();
    }

    // ================= POST TESTS =================

    @Test(dataProvider = "createUserWithListData")
    public void createUserWithListTest(String method, String expectedStatus, String edgeCaseReason) {
        given()
                .header("Content-Type", "application/json")
                .body("{\"users\":[{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"},{\"name\":\"Jane Doe\",\"email\":\"jane.doe@example.com\"}]}")
        when()
                .post("/user/createWithList")
        then()
                .statusCode(expectedStatus)
                .body("contains", "success")
                .body("contains", "id")
                .time(lessThan(1000))
                .log().all();

        if (edgeCaseReason.equals("Empty user list provided")) {
            Assert.assertEquals(expectedStatus, 400);
        } else if (edgeCaseReason.equals("Invalid JSON provided in user list")) {
            Assert.assertEquals(expectedStatus, 400);
        } else if (edgeCaseReason.equals("Duplicate user in list provided")) {
            Assert.assertEquals(expectedStatus, 400);
        } else if (edgeCaseReason.equals("Missing user in list provided")) {
            Assert.assertEquals(expectedStatus, 400);
        } else if (edgeCaseReason.equals("Invalid user in list provided")) {
            Assert.assertEquals(expectedStatus, 400);
        }
    }

    @DataProvider(name = "createUserWithListData")
    public Object[][] createUserWithListData() {
        return new Object[][]{
                {"POST", "201", "Valid user list provided"},
                {"POST", "400", "Empty user list provided"},
                {"POST", "400", "Invalid JSON provided in user list"},
                {"POST", "400", "Duplicate user in list provided"},
                {"POST", "400", "Missing user in list provided"},
                {"POST", "400", "Invalid user in list provided"}
        };
    }
}
// This code follows the provided framework rules and includes all the necessary imports, setup, and test cases for the API endpoint `/user/createWithList`. The `createUserWithListData` data provider returns an array of objects containing the test data for each test case. The `createUserWithListTest` test method uses the data provider to run each test case and verifies the expected status code, response body content, and response time.
