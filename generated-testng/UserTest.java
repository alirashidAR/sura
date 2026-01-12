import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class UserAPITest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // POST tests
    /**
     * Test case: Create a new user with valid token.
     */
    @Test(description = "Create a new user with valid token.")
    public void testCase1_CreateUserWithValidToken() {
        given().
                header("Authorization", "Bearer valid-token").
                body("{\"username\":\"johnDoe\",\"email\":\"johndoe@example.com\",\"password\":\"password123\"}").
        when().
                post("/user").
        then().
                assertThat().
                statusCode(201).
                contentType(ContentType.JSON).
                body("username", equalTo("johnDoe")).
                body("email", equalTo("johndoe@example.com"));
    }

    /**
     * Test case: Create a new user with invalid token format.
     */
    @Test(description = "Create a new user with invalid token format.")
    public void testCase2_CreateUserWithInvalidTokenFormat() {
        given().
                header("Authorization", "Bearer invalid-token-format").
                body("{\"username\":\"johnDoe\",\"email\":\"johndoe@example.com\",\"password\":\"password123\"}").
        when().
                post("/user").
        then().
                assertThat().
                statusCode(401).
                contentType(ContentType.JSON).
                body("error", equalTo("Invalid token format"));
    }

    /**
     * Test case: Create a new user with missing username.
     */
    @Test(description = "Create a new user with missing username.")
    public void testCase3_CreateUserWithMissingUsername() {
        given().
                header("Authorization", "Bearer valid-token").
                body("{\"email\":\"johndoe@example.com\",\"password\":\"password123\"}").
        when().
                post("/user").
        then().
                assertThat().
                statusCode(400).
                contentType(ContentType.JSON).
                body("error", equalTo("Username is required"));
    }

    /**
     * Test case: Create a new user with missing email.
     */
    @Test(description = "Create a new user with missing email.")
    public void testCase4_CreateUserWithMissingEmail() {
        given().
                header("Authorization", "Bearer valid-token").
                body("{\"username\":\"johnDoe\",\"password\":\"password123\"}").
        when().
                post("/user").
        then().
                assertThat().
                statusCode(400).
                contentType(ContentType.JSON).
                body("error", equalTo("Email is required"));
    }

    /**
     * Test case: Create a new user with missing password.
     */
    @Test(description = "Create a new user with missing password.")
    public void testCase5_CreateUserWithMissingPassword() {
        given().
                header("Authorization", "Bearer valid-token").
                body("{\"username\":\"johnDoe\",\"email\":\"johndoe@example.com\"}").
        when().
                post("/user").
        then().
                assertThat().
                statusCode(400).
                contentType(ContentType.JSON).
                body("error", equalTo("Password is required"));
    }

    /**
     * Test case: Create a new user with duplicate username.
     */
    @Test(description = "Create a new user with duplicate username.")
    public void testCase6_CreateUserWithDuplicateUsername() {
        given().
                header("Authorization", "Bearer valid-token").
                body("{\"username\":\"johnDoe\",\"email\":\"johndoe@example.com\",\"password\":\"password123\"}").
        when().
                post("/user").
        then().
                assertThat().
                statusCode(409).
                contentType(ContentType.JSON).
                body("error", equalTo("Username already exists"));
    }

    /**
     * Test case: Create a new user without token.
     */
    @Test(description = "Create a new user without token.")
    public void testCase7_CreateUserWithoutToken() {
        given().
                body("{\"username\":\"johnDoe\",\"email\":\"johndoe@example.com\",\"password\":\"password123\"}").
        when().
                post("/user").
        then().
                assertThat().
                statusCode(401).
                contentType(ContentType.JSON).
                body("error", equalTo("Unauthorized"));
    }

    /**
     * Test case: Create a new user with invalid token.
     */
    @Test(description = "Create a new user with invalid token.")
    public void testCase8_CreateUserWithInvalidToken() {
        given().
                header("Authorization", "Bearer invalid-token").
                body("{\"username\":\"johnDoe\",\"email\":\"johndoe@example.com\",\"password\":\"password123\"}").
        when().
                post("/user").
        then().
                assertThat().
                statusCode(401).
                contentType(ContentType.JSON).
                body("error", equalTo("Unauthorized"));
    }

    /**
     * Test case: Create a new user with invalid token format.
     */
    @Test(description = "Create a new user with invalid token format.")
    public void testCase9_CreateUserWithInvalidTokenFormat() {
        given().
                header("Authorization", "Bearer invalid-token-format").
                body("{\"username\":\"johnDoe\",\"email\":\"johndoe@example.com\",\"password\":\"password123\"}").
        when().
                post("/user").
        then().
                assertThat().
                statusCode(401).
                contentType(ContentType.JSON).
                body("error", equalTo("Invalid token format"));
    }
}
