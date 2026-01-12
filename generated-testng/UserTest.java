import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest extends TestBase {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // CREATE USER - VALID REQUEST
    @Test(dataProvider = "createUserValidRequest", description = "Create user with valid request")
    public void createUserValidRequest(String username, String email, String password) {
        given()
                .pathParam("username", username)
                .pathParam("email", email)
                .pathParam("password", password)
                .when()
                .post("/user/{username}/{email}/{password}")
                .then()
                .statusCode(201);
    }

    @DataProvider(name = "createUserValidRequest")
    public Object[][] createUserValidRequest() {
        return new Object[][]{
                {"johnDoe", "johndoe@example.com", "secret"}
        };
    }

    // CREATE USER - MISSING USERNAME
    @Test(dataProvider = "createUserMissingUsername", description = "Create user with missing username")
    public void createUserMissingUsername(String email, String password) {
        given()
                .pathParam("email", email)
                .pathParam("password", password)
                .when()
                .post("/user/{email}/{password}")
                .then()
                .statusCode(400);
    }

    @DataProvider(name = "createUserMissingUsername")
    public Object[][] createUserMissingUsername() {
        return new Object[][]{
                {"johndoe@example.com", "secret"}
        };
    }

    // CREATE USER - INVALID EMAIL
    @Test(dataProvider = "createUserInvalidEmail", description = "Create user with invalid email")
    public void createUserInvalidEmail(String username, String password) {
        given()
                .pathParam("username", username)
                .pathParam("password", password)
                .when()
                .post("/user/{username}/invalid_email/{password}")
                .then()
                .statusCode(400);
    }

    @DataProvider(name = "createUserInvalidEmail")
    public Object[][] createUserInvalidEmail() {
        return new Object[][]{
                {"johnDoe", "secret"}
        };
    }

    // CREATE USER - EMPTY USERNAME
    @Test(dataProvider = "createUserEmptyUsername", description = "Create user with empty username")
    public void createUserEmptyUsername(String email, String password) {
        given()
                .pathParam("email", email)
                .pathParam("password", password)
                .when()
                .post("/user/{email}/{password}")
                .then()
                .statusCode(400);
    }

    @DataProvider(name = "createUserEmptyUsername")
    public Object[][] createUserEmptyUsername() {
        return new Object[][]{
                {"johndoe@example.com", "secret"}
        };
    }

    // CREATE USER - VALID REQUEST WITH CUSTOM HEADERS
    @Test(dataProvider = "createUserValidRequestWithCustomHeaders", description = "Create user with valid request and custom headers")
    public void createUserValidRequestWithCustomHeaders(String username, String email, String password) {
        given()
                .header("Content-Type", "application/json")
                .pathParam("username", username)
                .pathParam("email", email)
                .pathParam("password", password)
                .when()
                .post("/user/{username}/{email}/{password}")
                .then()
                .statusCode(201);
    }

    @DataProvider(name = "createUserValidRequestWithCustomHeaders")
    public Object[][] createUserValidRequestWithCustomHeaders() {
        return new Object[][]{
                {"johnDoe", "johndoe@example.com", "secret"}
        };
    }

    // CREATE USER - INVALID REQUEST BODY
    @Test(dataProvider = "createUserInvalidRequestBody", description = "Create user with invalid request body")
    public void createUserInvalidRequestBody() {
        given()
                .when()
                .post("/user/")
                .then()
                .statusCode(400);
    }

    @DataProvider(name = "createUserInvalidRequestBody")
    public Object[][] createUserInvalidRequestBody() {
        return new Object[]{};
    }
}
