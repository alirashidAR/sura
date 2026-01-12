import io.restassured.RestAssured;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LogoutTest {

    @BeforeClass
    public void setup() {
        // Set the base URI for all API requests
        RestAssured.baseURI = "https://api.example.com";
    }

    // Group test methods by HTTP method using comments

    /**
     * Happy Path - Successful Logout
     */
    @Test(groups = {"GET"}, description = "Successful logout")
    public void happyPathLogout() {
        get("/user/logout").then()
                .statusCode(200)
                .body("", Void.class);
    }

    /**
     * Edge Case - Empty Session
     */
    @Test(groups = {"GET"}, description = "Empty session logout")
    public void emptySessionLogout() {
        get("/user/logout", "", "").then()
                .statusCode(200)
                .body("", Void.class);
    }

    /**
     * Edge Case - Invalid Token
     */
    @Test(groups = {"GET"}, description = "Invalid token logout")
    public void invalidTokenLogout() {
        get("/user/logout").header("Authorization", "InvalidToken").then()
                .statusCode(401)
                .body("error", equalTo("Invalid token provided."));
    }

    /**
     * Edge Case - Missing Authorization Header
     */
    @Test(groups = {"GET"}, description = "Missing auth header logout")
    public void missingAuthHeaderLogout() {
        get("/user/logout").then()
                .statusCode(401)
                .body("error", equalTo("Missing authorization header."));
    }

    /**
     * Edge Case - Token Expiration
     */
    @Test(groups = {"GET"}, description = "Expired token logout")
    public void expiredTokenLogout() {
        get("/user/logout").header("Authorization", "ExpiredToken").then()
                .statusCode(401)
                .body("error", equalTo("Token has expired. Please login again."));
    }

    /**
     * Edge Case - Empty Body
     */
    @Test(groups = {"GET"}, description = "Empty body logout")
    public void emptyBodyLogout() {
        get("/user/logout").then()
                .statusCode(200)
                .body("", Void.class);
    }

    // Group test methods by HTTP method using comments

    /**
     * Edge Case - Invalid Token (PUT)
     */
    @Test(groups = {"PUT"}, description = "Invalid token logout")
    public void invalidTokenLogoutPut() {
        put("/user/logout").header("Authorization", "InvalidToken").then()
                .statusCode(401)
                .body("error", equalTo("Invalid token provided."));
    }

    /**
     * Edge Case - Missing Authorization Header (PUT)
     */
    @Test(groups = {"PUT"}, description = "Missing auth header logout")
    public void missingAuthHeaderLogoutPut() {
        put("/user/logout").then()
                .statusCode(401)
                .body("error", equalTo("Missing authorization header."));
    }

    /**
     * Edge Case - Token Expiration (PUT)
     */
    @Test(groups = {"PUT"}, description = "Expired token logout")
    public void expiredTokenLogoutPut() {
        put("/user/logout").header("Authorization", "ExpiredToken").then()
                .statusCode(401)
                .body("error", equalTo("Token has expired. Please login again."));
    }

    /**
     * Edge Case - Empty Body (PUT)
     */
    @Test(groups = {"PUT"}, description = "Empty body logout")
    public void emptyBodyLogoutPut() {
        put("/user/logout").then()
                .statusCode(200)
                .body("", Void.class);
    }

    // Group test methods by HTTP method using comments

    /**
     * Edge Case - Invalid Token (DELETE)
     */
    @Test(groups = {"DELETE"}, description = "Invalid token logout")
    public void invalidTokenLogoutDelete() {
        delete("/user/logout").header("Authorization", "InvalidToken").then()
                .statusCode(401)
                .body("error", equalTo("Invalid token provided."));
    }

    /**
     * Edge Case - Missing Authorization Header (DELETE)
     */
    @Test(groups = {"DELETE"}, description = "Missing auth header logout")
    public void missingAuthHeaderLogoutDelete() {
        delete("/user/logout").then()
                .statusCode(401)
                .body("error", equalTo("Missing authorization header."));
    }

    /**
     * Edge Case - Token Expiration (DELETE)
     */
    @Test(groups = {"DELETE"}, description = "Expired token logout")
    public void expiredTokenLogoutDelete() {
        delete("/user/logout").header("Authorization", "ExpiredToken").then()
                .statusCode(401)
                .body("error", equalTo("Token has expired. Please login again."));
    }

    /**
     * Edge Case - Empty Body (DELETE)
     */
    @Test(groups = {"DELETE"}, description = "Empty body logout")
    public void emptyBodyLogoutDelete() {
        delete("/user/logout").then()
                .statusCode(200)
                .body("", Void.class);
    }

    // Group test methods by HTTP method using comments

    /**
     * Edge Case - Invalid Token (POST)
     */
    @Test(groups = {"POST"}, description = "Invalid token logout")
    public void invalidTokenLogoutPost() {
        post("/user/logout").header("Authorization", "InvalidToken").then()
                .statusCode(401)
                .body("error", equalTo("Invalid token provided."));
    }

    /**
     * Edge Case - Missing Authorization Header (POST)
     */
    @Test(groups = {"POST"}, description = "Missing auth header logout")
    public void missingAuthHeaderLogoutPost() {
        post("/user/logout").then()
                .statusCode(401)
                .body("error", equalTo("Missing authorization header."));
    }

    /**
     * Edge Case - Token Expiration (POST)
     */
    @Test(groups = {"POST"}, description = "Expired token logout")
    public void expiredTokenLogoutPost() {
        post("/user/logout").header("Authorization", "ExpiredToken").then()
                .statusCode(401)
                .body("error", equalTo("Token has expired. Please login again."));
    }

    /**
     * Edge Case - Empty Body (POST)
     */
    @Test(groups = {"POST"}, description = "Empty body logout")
    public void emptyBodyLogoutPost() {
        post("/user/logout").then()
                .statusCode(200)
                .body("", Void.class);
    }

    private Method get(String path) {
        return RestAssured.given().when().get(path).thenReturn();
    }

    private Method put(String path) {
        return RestAssured.given().when().put(path).thenReturn();
    }

    private Method post(String path) {
        return RestAssured.given().when().post(path).thenReturn();
    }

    private Method delete(String path) {
        return RestAssured.given().when().delete(path).thenReturn();
    }
}
