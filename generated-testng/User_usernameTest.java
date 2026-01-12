import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class UserTests {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    @DataProvider(name = "PUT_user_data")
    public Object[][] provideData() {
        return new Object[][]{
                {"johnDoe", Map.of("name", "John Doe", "email", "johndoe@example.com")},
                {null, Map.of()},
                {"johnDoe", Map.of("name", "John Doe", "email", "johndoe.example.com")},
                {"nonExistentUser", Map.of("name", "John Doe", "email", "johndoe@example.com")},
                {"johnDoe", Map.of("name", "John Doe", "email", "johndoe@example.com")},
                {"johnDoe", Map.of()}
        };
    }

    @Test(dataProvider = "PUT_user_data")
    public void putUserTest(String username, Map<String, String> requestBody) {
        String body = requestBody != null ? requestBody.toString() : "";
        RestAssured.given()
                .pathParam("username", username)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put("/user/{username}")
                .then()
                .statusCode(requestBody == null || !requestBody.containsKey("name") ? 400 : (username.equals("nonExistentUser") ? 404 : 200));
    }

    @DataProvider(name = "GET_user_data")
    public Object[][] provideGetUserTestData() {
        return new Object[][]{
                {"johnDoe"},
                {"nonExistentUser"}
        };
    }

    @Test(dataProvider = "GET_user_data")
    public void getUserTest(String username) {
        RestAssured.given()
                .pathParam("username", username)
                .when()
                .get("/user/{username}")
                .then()
                .statusCode(username.equals("nonExistentUser") ? 404 : 200);
    }

    @DataProvider(name = "POST_user_data")
    public Object[][] providePostUserData() {
        return new Object[][]{
                {Map.of("name", "John Doe", "email", "johndoe@example.com")},
                {Map.of()}
        };
    }

    @Test(dataProvider = "POST_user_data")
    public void postUserTest(Map<String, String> requestBody) {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody != null ? requestBody.toString() : "")
                .when()
                .post("/user")
                .then()
                .statusCode(requestBody == null || !requestBody.containsKey("name") ? 400 : 201);
    }

    @DataProvider(name = "DELETE_user_data")
    public Object[][] provideDeleteUserTestData() {
        return new Object[][]{
                {"johnDoe"},
                {"nonExistentUser"}
        };
    }

    @Test(dataProvider = "DELETE_user_data")
    public void deleteUserTest(String username) {
        RestAssured.given()
                .pathParam("username", username)
                .when()
                .delete("/user/{username}")
                .then()
                .statusCode(username.equals("nonExistentUser") ? 404 : 200);
    }
}
