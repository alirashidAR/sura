import io.restassured.RestAssured;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CreateUserArrayTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    /**
     * Group test methods by HTTP method using comments
     */
    
    // POST tests
    @Test(priority = 1)
    public void createUsersValidData() {
        // Test case: Create multiple users with valid data
        RestAssured.given()
                .when().post("/user/createWithArray")
                .then().statusCode(201)
                .body("users[0].name", hasItem("John Doe"))
                .body("users[0].email", hasItem("john.doe@example.com"))
                .body("users[1].name", hasItem("Jane Doe"))
                .body("users[1].email", hasItem("jane.doe@example.com"));
    }

    @Test(priority = 2)
    public void createUsersInvalidData() {
        // Test case: Create multiple users with invalid data
        RestAssured.given()
                .when().post("/user/createWithArray")
                .then().statusCode(400)
                .body("errors[0].message", hasItem("Name is required"))
                .body("errors[1].message", hasItem("Email is required"));
    }

    @Test(priority = 3)
    public void emptyInputArray() {
        // Test case: Empty input array
        RestAssured.given()
                .when().post("/user/createWithArray")
                .then().statusCode(204);
    }

    // GET tests
    @Test(priority = 4)
    public void getCreatedUsers() {
        // Test case: Get created users
        RestAssured.given()
                .when().get("/users")
                .then().statusCode(200)
                .body("users[0].name", hasItem("John Doe"))
                .body("users[1].email", hasItem("jane.doe@example.com"));
    }

    // PUT tests
    @Test(priority = 5)
    public void updateExistingUser() {
        // Test case: Update existing user
        RestAssured.given()
                .when().put("/user/{id}", 1, "{name=John Doe Updated}")
                .then().statusCode(200);
    }

    @Test(priority = 6)
    public void updateUserInvalidData() {
        // Test case: Update existing user with invalid data
        RestAssured.given()
                .when().put("/user/{id}", 1, "{name=}")
                .then().statusCode(400);
    }

    // DELETE tests
    @Test(priority = 7)
    public void deleteUser() {
        // Test case: Delete existing user
        RestAssured.given()
                .when().delete("/user/{id}", 1)
                .then().statusCode(204);
    }
    
    @AfterClass
    public void tearDown() {
        
    }
}
