import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // POST Tests

    @Test(description = "POST /user with valid data")
    public void postUserValidData() {
        given()
                .header("Authorization", "Bearer token")
                .body("{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"}")
                .when()
                .post("/user")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(1), "name", equalTo("John Doe"), "email", equalTo("john.doe@example.com"));
    }

    @Test(description = "POST /user with empty request body")
    public void postUserEmptyRequestBody() {
        given()
                .header("Authorization", "Bearer token")
                .body("{}")
                .when()
                .post("/user")
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("error", equalTo("Invalid request"));
    }

    @Test(description = "POST /user with missing required fields")
    public void postUserMissingRequiredFields() {
        given()
                .header("Authorization", "Bearer token")
                .body("{\"email\":\"john.doe@example.com\"}")
                .when()
                .post("/user")
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("error", equalTo("Missing required fields"));
    }

    @Test(description = "POST /user with invalid authorization token")
    public void postUserInvalidAuthorizationToken() {
        given()
                .header("Authorization", "Bearer invalid_token")
                .body("{\"name\":\"Jane Doe\",\"email\":\"jane.doe@example.com\"}")
                .when()
                .post("/user")
                .then()
                .statusCode(401);
    }

    @Test(description = "POST /user with empty name and email fields")
    public void postUserEmptyNameAndEmailFields() {
        given()
                .header("Authorization", "Bearer token")
                .body("{\"name\":\"\",\"email\":\"\"}")
                .when()
                .post("/user")
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("error", equalTo("Invalid request"));
    }

    // PUT Tests

    @Test(description = "PUT /user with valid data")
    public void putUserValidData() {
        given()
                .header("Authorization", "Bearer token")
                .pathParam("id", 1)
                .body("{\"name\":\"Jane Doe\",\"email\":\"jane.doe@example.com\"}")
                .when()
                .put("/user/{id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(1), "name", equalTo("Jane Doe"), "email", equalTo("jane.doe@example.com"));
    }

    @Test(description = "PUT /user with empty request body")
    public void putUserEmptyRequestBody() {
        given()
                .header("Authorization", "Bearer token")
                .pathParam("id", 1)
                .body("{}")
                .when()
                .put("/user/{id}")
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("error", equalTo("Invalid request"));
    }

    @Test(description = "PUT /user with missing required fields")
    public void putUserMissingRequiredFields() {
        given()
                .header("Authorization", "Bearer token")
                .pathParam("id", 1)
                .body("{\"email\":\"john.doe@example.com\"}")
                .when()
                .put("/user/{id}")
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("error", equalTo("Missing required fields"));
    }

    @Test(description = "PUT /user with invalid authorization token")
    public void putUserInvalidAuthorizationToken() {
        given()
                .header("Authorization", "Bearer invalid_token")
                .pathParam("id", 1)
                .body("{\"name\":\"Jane Doe\",\"email\":\"jane.doe@example.com\"}")
                .when()
                .put("/user/{id}")
                .then()
                .statusCode(401);
    }

    @Test(description = "PUT /user with empty name and email fields")
    public void putUserEmptyNameAndEmailFields() {
        given()
                .header("Authorization", "Bearer token")
                .pathParam("id", 1)
                .body("{\"name\":\"\",\"email\":\"\"}")
                .when()
                .put("/user/{id}")
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("error", equalTo("Invalid request"));
    }

    // GET Tests

    @Test(description = "GET /users with valid data")
    public void getUsersValidData() {
        given()
                .header("Authorization", "Bearer token")
                .when()
                .get("/user")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test(description = "GET /users with invalid authorization token")
    public void getUsersInvalidAuthorizationToken() {
        given()
                .header("Authorization", "Bearer invalid_token")
                .when()
                .get("/user")
                .then()
                .statusCode(401);
    }

    // DELETE Tests

    @Test(description = "DELETE /user/{id} with valid id")
    public void deleteUserValidId() {
        given()
                .header("Authorization", "Bearer token")
                .pathParam("id", 1)
                .when()
                .delete("/user/{id}")
                .then()
                .statusCode(200);
    }

    @Test(description = "DELETE /user/{id} with invalid id")
    public void deleteUserInvalidId() {
        given()
                .header("Authorization", "Bearer token")
                .pathParam("id", 999)
                .when()
                .delete("/user/{id}")
                .then()
                .statusCode(404);
    }

}
