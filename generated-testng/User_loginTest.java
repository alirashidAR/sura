import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class UserLoginAPITest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    @DataProvider(name = "validLoginTestData")
    public Object[][] validLoginTestData() {
        return new Object[][]{
                {"test_user", "test_password"},
                {"empty_username", ""},
                {"missing_username", "test_password"},
                {"missing_password", "test_user"}
        };
    }

    @DataProvider(name = "invalidLoginTestData")
    public Object[][] invalidLoginTestData() {
        return new Object[][]{
                {"invalid_password", "test_user"},
                {"empty_password", "test_user"},
                {"empty_username", ""},
                {"empty_password", ""}
        };
    }

    /* GET /user/login tests */

    @Test(dataProvider = "validLoginTestData")
    public void validGetUserLoginTest(String username, String password) {
        // Valid Login Test
        given()
                .queryParam("username", username)
                .queryParam("password", password)
                .header("Content-Type", "application/json").
        when().
                get("/user/login").
        then().statusCode(200);
    }

    @Test(dataProvider = "invalidLoginTestData")
    public void invalidGetUserLoginTest(String username, String password) {
        // Invalid Credentials Test
        given()
                .queryParam("username", username)
                .queryParam("password", password)
                .header("Content-Type", "application/json").
        when().
                get("/user/login").
        then().statusCode(400);
    }

    /* POST /user/login tests */

    @Test(dataProvider = "validLoginTestData")
    public void validPostUserLoginTest(String username, String password) {
        // Valid Login Test
        given()
                .queryParam("username", username)
                .queryParam("password", password)
                .header("Content-Type", "application/json").
        when().
                post("/user/login").
        then().statusCode(200);
    }

    @Test(dataProvider = "invalidLoginTestData")
    public void invalidPostUserLoginTest(String username, String password) {
        // Invalid Credentials Test
        given()
                .queryParam("username", username)
                .queryParam("password", password)
                .header("Content-Type", "application/json").
        when().
                post("/user/login").
        then().statusCode(400);
    }

    /* PUT /user/login tests */

    @Test(dataProvider = "validLoginTestData")
    public void validPutUserLoginTest(String username, String password) {
        // Valid Login Test
        given()
                .queryParam("username", username)
                .queryParam("password", password)
                .header("Content-Type", "application/json").
        when().
                put("/user/login").
        then().statusCode(200);
    }

    @Test(dataProvider = "invalidLoginTestData")
    public void invalidPutUserLoginTest(String username, String password) {
        // Invalid Credentials Test
        given()
                .queryParam("username", username)
                .queryParam("password", password)
                .header("Content-Type", "application/json").
        when().
                put("/user/login").
        then().statusCode(400);
    }

    /* DELETE /user/login tests */

    @Test(dataProvider = "validLoginTestData")
    public void validDeleteUserLoginTest(String username, String password) {
        // Valid Login Test
        given()
                .queryParam("username", username)
                .queryParam("password", password)
                .header("Content-Type", "application/json").
        when().
                delete("/user/login").
        then().statusCode(200);
    }

    @Test(dataProvider = "invalidLoginTestData")
    public void invalidDeleteUserLoginTest(String username, String password) {
        // Invalid Credentials Test
        given()
                .queryParam("username", username)
                .queryParam("password", password)
                .header("Content-Type", "application/json").
        when().
                delete("/user/login").
        then().statusCode(400);
    }
}
