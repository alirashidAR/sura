import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserApiTest {

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://api.example.com";
    }

    @Test(description = "Update user profile successfully")
    public void putUserByUsernameUsername123ValidProfileUpdate() {
        // Given
        String username = "username123";
        String name = "John Doe";
        String email = "johndoe@example.com";

        // When
        int responseCode = RestAssured.given()
                .header("Authorization", "Bearer token")
                .body("{\"name\":\"" + name + "\",\"email\":\"" + email + "\"}")
                .when()
                .put("/user/{username}", username)
                .getStatusCode();

        // Then
        assert responseCode == 200;
    }

    @Test(description = "User not found when updating profile")
    public void putUserByUsernameInvalidUsernameUserNotFound() {
        // Given
        String invalidUsername = "invalid_username";

        // When
        int responseCode = RestAssured.given()
                .header("Authorization", "Bearer token")
                .body("{\"name\":\"John Doe\",\"email\":\"johndoe@example.com\"}")
                .when()
                .put("/user/{username}", invalidUsername)
                .getStatusCode();

        // Then
        assert responseCode == 404;
    }

    @Test(description = "Invalid user supplied when updating profile")
    public void putUserByUsernameUsername123InvalidUserObject() {
        // Given
        String username = "username123";

        // When
        int responseCode = RestAssured.given()
                .header("Authorization", "Bearer token")
                .body("{\"name\":null,\"email\":\"johndoe@example.com\"}")
                .when()
                .put("/user/{username}", username)
                .getStatusCode();

        // Then
        assert responseCode == 400;
    }

    @Test(description = "Unauthorized when updating profile without authentication")
    public void putUserByUsernameUsername123NoAuthentication() {
        // Given
        String username = "username123";

        // When
        int responseCode = RestAssured.given()
                .body("{\"name\":\"John Doe\",\"email\":\"johndoe@example.com\"}")
                .when()
                .put("/user/{username}", username)
                .getStatusCode();

        // Then
        assert responseCode == 401;
    }

    @Test(description = "Unauthorized when updating profile with invalid authentication token")
    public void putUserByUsernameUsername123InvalidAuthenticationToken() {
        // Given
        String username = "username123";

        // When
        int responseCode = RestAssured.given()
                .header("Authorization", "Bearer invalid_token")
                .body("{\"name\":\"John Doe\",\"email\":\"johndoe@example.com\"}")
                .when()
                .put("/user/{username}", username)
                .getStatusCode();

        // Then
        assert responseCode == 401;
    }

    @Test(description = "The request should be processed successfully and return a no-content response")
    public void deleteUserByUsernameJohnDoeValidDeleteRequestByLoggedInUser() {
        // Given
        String username = "johnDoe";

        // When
        int responseCode = RestAssured.given()
                .header("Authorization", "Bearer validToken")
                .when()
                .delete("/user/{username}", username)
                .getStatusCode();

        // Then
        assert responseCode == 204;
    }

    @Test(description = "The request should return a bad request response due to an invalid username")
    public void deleteUserByUsernameInvalidUsernameBadRequest() {
        // Given
        String invalidUsername = "invalidUsername";

        // When
        int responseCode = RestAssured.given()
                .header("Authorization", "Bearer invalidToken")
                .when()
                .delete("/user/{username}", invalidUsername)
                .getStatusCode();

        // Then
        assert responseCode == 400;
    }

    @Test(description = "The request should return a not found response due to an unknown user")
    public void deleteUserByUsernameJohnDoeUserNotFound() {
        // Given
        String username = "johnDoe";

        // When
        int responseCode = RestAssured.given()
                .header("Authorization", "Bearer validToken")
                .when()
                .delete("/user/{username}", username)
                .getStatusCode();

        // Then
        assert responseCode == 404;
    }

    @Test(description = "The request should not be processed due to missing authentication")
    public void deleteUserByUsernameJohnDoeNoAuthentication() {
        // Given
        String username = "johnDoe";

        // When
        int responseCode = RestAssured.given()
                .when()
                .delete("/user/{username}", username)
                .getStatusCode();

        // Then
        assert responseCode == null;
    }

    @Test(description = "The request should not be processed due to a missing or empty username parameter")
    public void deleteUserByUsernameEmptyUsernameMissingUsername() {
        // Given
        String username = "";

        // When
        int responseCode = RestAssured.given()
                .header("Authorization", "Bearer validToken")
                .when()
                .delete("/user/{username}", username)
                .getStatusCode();

        // Then
        assert responseCode == null;
    }
}
