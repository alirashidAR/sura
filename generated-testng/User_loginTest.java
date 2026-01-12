import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue);

public class LoginTest extends TestBase {

    @BeforeClass
    public void setup() {
        baseURI = "https://api.example.com";
    }

    // GROUP 1 - GET TESTS
    /**
     * Valid login test case for GET method
     */
    @Test(description = "Valid login test case")
    public void validLoginGetTest() {
        given()
                .pathParam("username", "johnDoe")
                .pathParam("password", "mySecretPassword")
                .when()
                .get("/user/login?username={username}&password={password}")
                .then()
                .statusCode(200)
                .body("X-Expires-After", equalTo("2024-05-01T12:00:00Z"))
                .body("X-Rate-Limit", equalTo(100))
                .body("token", notNullValue());
    }

    /**
     * Invalid password test case for GET method
     */
    @Test(description = "Invalid password test case")
    public void invalidPasswordGetTest() {
        given()
                .pathParam("username", "johnDoe")
                .pathParam("password", "wrongPassword")
                .when()
                .get("/user/login?username={username}&password={password}")
                .then()
                .statusCode(400)
                .body(equalTo(null));
    }

    /**
     * Empty password test case for GET method
     */
    @Test(description = "Empty password test case")
    public void emptyPasswordGetTest() {
        given()
                .pathParam("username", "johnDoe")
                .pathParam("password", "")
                .when()
                .get("/user/login?username={username}&password={password}")
                .then()
                .statusCode(400)
                .body(equalTo(null));
    }

    /**
     * Empty username test case for GET method
     */
    @Test(description = "Empty username test case")
    public void emptyUsernameGetTest() {
        given()
                .pathParam("username", "")
                .pathParam("password", "mySecretPassword")
                .when()
                .get("/user/login?username={username}&password={password}")
                .then()
                .statusCode(400)
                .body(equalTo(null));
    }

    /**
     * Non-string username test case for GET method
     */
    @Test(description = "Non-string username test case")
    public void nonStringUsernameGetTest() {
        given()
                .pathParam("username", 12345)
                .pathParam("password", "mySecretPassword")
                .when()
                .get("/user/login?username={username}&password={password}")
                .then()
                .statusCode(400)
                .body(equalTo(null));
    }

    // GROUP 2 - POST TESTS
    /**
     * Valid login test case for POST method
     */
    @Test(description = "Valid login test case")
    public void validLoginPostTest() {
        given()
                .header("Content-Type", ContentType.JSON.toString())
                .body("{\"username\":\"johnDoe\",\"password\":\"mySecretPassword\"}")
                .when()
                .post("/user/login")
                .then()
                .statusCode(200)
                .body("X-Expires-After", equalTo("2024-05-01T12:00:00Z"))
                .body("X-Rate-Limit", equalTo(100))
                .body("token", notNullValue());
    }

    /**
     * Invalid password test case for POST method
     */
    @Test(description = "Invalid password test case")
    public void invalidPasswordPostTest() {
        given()
                .header("Content-Type", ContentType.JSON.toString())
                .body("{\"username\":\"johnDoe\",\"password\":\"wrongPassword\"}")
                .when()
                .post("/user/login")
                .then()
                .statusCode(400)
                .body(equalTo(null));
    }

    /**
     * Empty password test case for POST method
     */
    @Test(description = "Empty password test case")
    public void emptyPasswordPostTest() {
        given()
                .header("Content-Type", ContentType.JSON.toString())
                .body("{\"username\":\"johnDoe\",\"password\":\"\"}")
                .when()
                .post("/user/login")
                .then()
                .statusCode(400)
                .body(equalTo(null));
    }

    /**
     * Empty username test case for POST method
     */
    @Test(description = "Empty username test case")
    public void emptyUsernamePostTest() {
        given()
                .header("Content-Type", ContentType.JSON.toString())
                .body("{\"username\":\"\",\"password\":\"mySecretPassword\"}")
                .when()
                .post("/user/login")
                .then()
                .statusCode(400)
                .body(equalTo(null));
    }

    /**
     * Non-string username test case for POST method
     */
    @Test(description = "Non-string username test case")
    public void nonStringUsernamePostTest() {
        given()
                .header("Content-Type", ContentType.JSON.toString())
                .body("{\"username\":12345,\"password\":\"mySecretPassword\"}")
                .when()
                .post("/user/login")
                .then()
                .statusCode(400)
                .body(equalTo(null));
    }

    // GROUP 3 - PUT TESTS
    /**
     * Valid login test case for PUT method
     */
    @Test(description = "Valid login test case")
    public void validLoginPutTest() {
        given()
                .header("Content-Type", ContentType.JSON.toString())
                .body("{\"username\":\"johnDoe\",\"password\":\"mySecretPassword\"}")
                .when()
                .put("/user/login")
                .then()
                .statusCode(200)
                .body("X-Expires-After", equalTo("2024-05-01T12:00:00Z"))
                .body("X-Rate-Limit", equalTo(100))
                .body("token", notNullValue());
    }

    /**
     * Invalid password test case for PUT method
     */
    @Test(description = "Invalid password test case")
    public void invalidPasswordPutTest() {
        given()
                .header("Content-Type", ContentType.JSON.toString())
                .body("{\"username\":\"johnDoe\",\"password\":\"wrongPassword\"}")
                .when()
                .put("/user/login")
                .then()
                .statusCode(400)
                .body(equalTo(null));
    }

    /**
     * Empty password test case for PUT method
     */
    @Test(description = "Empty password test case")
    public void emptyPasswordPutTest() {
        given()
                .header("Content-Type", ContentType.JSON.toString())
                .body("{\"username\":\"johnDoe\",\"password\":\"\"}")
                .when()
                .put("/user/login")
                .then()
                .statusCode(400)
                .body(equalTo(null));
    }

    /**
     * Empty username test case for PUT method
     */
    @Test(description = "Empty username test case")
    public void emptyUsernamePutTest() {
        given()
                .header("Content-Type", ContentType.JSON.toString())
                .body("{\"username\":\"\",\"password\":\"mySecretPassword\"}")
                .when()
                .put("/user/login")
                .then()
                .statusCode(400)
                .body(equalTo(null));
    }

    /**
     * Non-string username test case for PUT method
     */
    @Test(description = "Non-string username test case")
    public void nonStringUsernamePutTest() {
        given()
                .header("Content-Type", ContentType.JSON.toString())
                .body("{\"username\":12345,\"password\":\"mySecretPassword\"}")
                .when()
                .put("/user/login")
                .then()
                .statusCode(400)
                .body(equalTo(null));
    }

    // GROUP 4 - DELETE TESTS
    /**
     * Valid login test case for DELETE method
     */
    @Test(description = "Valid login test case")
    public void validLoginDeleteTest() {
        given()
                .header("Content-Type", ContentType.JSON.toString())
                .body("{\"username\":\"johnDoe\",\"password\":\"mySecretPassword\"}")
                .when()
                .delete("/user/login")
                .then()
                .statusCode(200)
                .body("X-Expires-After", equalTo("2024-05-01T12:00:00Z"))
                .body("X-Rate-Limit", equalTo(100))
                .body("token", notNullValue());
    }

    /**
     * Invalid password test case for DELETE method
     */
    @Test(description = "Invalid password test case")
    public void invalidPasswordDeleteTest() {
        given()
                .header("Content-Type", ContentType.JSON.toString())
                .body("{\"username\":\"johnDoe\",\"password\":\"wrongPassword\"}")
                .when()
                .delete("/user/login")
                .then()
                .statusCode(400)
                .body(equalTo(null));
    }

    /**
     * Empty password test case for DELETE method
     */
    @Test(description = "Empty password test case")
    public void emptyPasswordDeleteTest() {
        given()
                .header("Content-Type", ContentType.JSON.toString())
                .body("{\"username\":\"johnDoe\",\"password\":\"\"}")
                .when()
                .delete("/user/login")
                .then()
                .statusCode(400)
                .body(equalTo(null));
    }

    /**
     * Empty username test case for DELETE method
     */
    @Test(description = "Empty username test case")
    public void emptyUsernameDeleteTest() {
        given()
                .header("Content-Type", ContentType.JSON.toString())
                .body("{\"username\":\"\",\"password\":\"mySecretPassword\"}")
                .when()
                .delete("/user/login")
                .then()
                .statusCode(400)
                .body(equalTo(null));
    }

    /**
     * Non-string username test case for DELETE method
     */
    @Test(description = "Non-string username test case")
    public void nonStringUsernameDeleteTest() {
        given()
                .header("Content-Type", ContentType.JSON.toString())
                .body("{\"username\":12345,\"password\":\"mySecretPassword\"}")
                .when()
                .delete("/user/login")
                .then()
                .statusCode(400)
                .body(equalTo(null));
    }
