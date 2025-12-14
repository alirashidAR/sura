import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // Grouped by HTTP method

    // POST tests
    @Test(groups = {"POST"})
    public void testValidUserCreation() {
        // Test case 1: Valid User Creation
        RequestSpecification request = RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"username\":\"johnDoe\",\"email\":\"johndoe@example.com\",\"password\":\"password123\"}");
        Response response = request.post("/user");
        int statusCode = response.getStatusCode();
        if (statusCode == 201) {
            System.out.println("Test case 1: Valid User Creation - passed");
        } else {
            System.out.println("Test case 1: Valid User Creation - failed with status code " + statusCode);
        }
    }

    @Test(groups = {"POST"})
    public void testEmptyUserCreation() {
        // Test case 2: Empty User Creation
        RequestSpecification request = RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{}");
        Response response = request.post("/user");
        int statusCode = response.getStatusCode();
        if (statusCode == 400) {
            System.out.println("Test case 2: Empty User Creation - passed");
        } else {
            System.out.println("Test case 2: Empty User Creation - failed with status code " + statusCode);
        }
    }

    @Test(groups = {"POST"})
    public void testDuplicateUserCreation() {
        // Test case 3: Duplicate User Creation
        RequestSpecification request = RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"username\":\"johnDoe\",\"email\":\"johndoe@example.com\",\"password\":\"password123\"}");
        Response response = request.post("/user");
        int statusCode = response.getStatusCode();
        if (statusCode == 409) {
            System.out.println("Test case 3: Duplicate User Creation - passed");
        } else {
            System.out.println("Test case 3: Duplicate User Creation - failed with status code " + statusCode);
        }
    }

    // GET tests
    @Test(groups = {"GET"})
    public void testGetUser() {
        RequestSpecification request = RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("username", "johnDoe");
        Response response = request.get("/user/{username}");
        int statusCode = response.getStatusCode();
        if (statusCode == 200) {
            System.out.println("Test case: Get User - passed");
        } else {
            System.out.println("Test case: Get User - failed with status code " + statusCode);
        }
    }

    @Test(groups = {"GET"})
    public void testGetUserNotFound() {
        RequestSpecification request = RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("username", "non-existent-user");
        Response response = request.get("/user/{username}");
        int statusCode = response.getStatusCode();
        if (statusCode == 404) {
            System.out.println("Test case: Get User Not Found - passed");
        } else {
            System.out.println("Test case: Get User Not Found - failed with status code " + statusCode);
        }
    }

    // PUT tests
    @Test(groups = {"PUT"})
    public void testUpdateUser() {
        RequestSpecification request = RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"username\":\"johnDoe\",\"email\":\"johndoe@example.com\",\"password\":\"password123\"}")
                .pathParam("username", "johnDoe");
        Response response = request.put("/user/{username}");
        int statusCode = response.getStatusCode();
        if (statusCode == 200) {
            System.out.println("Test case: Update User - passed");
        } else {
            System.out.println("Test case: Update User - failed with status code " + statusCode);
        }
    }

    @Test(groups = {"PUT"})
    public void testUpdateUserNotFound() {
        RequestSpecification request = RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"username\":\"johnDoe\",\"email\":\"johndoe@example.com\",\"password\":\"password123\"}")
                .pathParam("username", "non-existent-user");
        Response response = request.put("/user/{username}");
        int statusCode = response.getStatusCode();
        if (statusCode == 404) {
            System.out.println("Test case: Update User Not Found - passed");
        } else {
            System.out.println("Test case: Update User Not Found - failed with status code " + statusCode);
        }
    }

    // DELETE tests
    @Test(groups = {"DELETE"})
    public void testDeleteUser() {
        RequestSpecification request = RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("username", "johnDoe");
        Response response = request.delete("/user/{username}");
        int statusCode = response.getStatusCode();
        if (statusCode == 200) {
            System.out.println("Test case: Delete User - passed");
        } else {
            System.out.println("Test case: Delete User - failed with status code " + statusCode);
        }
    }

    @Test(groups = {"DELETE"})
    public void testDeleteUserNotFound() {
        RequestSpecification request = RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("username", "non-existent-user");
        Response response = request.delete("/user/{username}");
        int statusCode = response.getStatusCode();
        if (statusCode == 404) {
            System.out.println("Test case: Delete User Not Found - passed");
        } else {
            System.out.println("Test case: Delete User Not Found - failed with status code " + statusCode);
        }
    }

    @AfterClass
    public void cleanup() {
        // Cleanup code if needed
    }
}
