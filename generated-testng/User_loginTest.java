import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.time.LocalDateTime.now;

public class UserLoginTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // GET Tests

    @Test(description = "Valid Login")
    public void testValidLogin() {
        given(Method.GET, "/user/login?username=valid_user&password=correct_password").
                then().statusCode(200).
                body("X-Expires-After", equalTo(now().format(ofPattern("yyyy-MM-dd HH:mm:ss")))).
                body("X-Rate-Limit", equalTo("100")).
                body("token", equalTo("$.schema"));
    }

    @Test(description = "Empty Username")
    public void testEmptyUsername() {
        given(Method.GET, "/user/login?username=&password=correct_password").
                then().statusCode(400);
    }

    @Test(description = "Empty Password")
    public void testEmptyPassword() {
        given(Method.GET, "/user/login?username=valid_user&password=").
                then().statusCode(400);
    }

    @Test(description = "Non-Alphanumeric Username")
    public void testNonAlphanumericUsername() {
        given(Method.GET, "/user/login?username=@#$%^&*()&password=correct_password").
                then().statusCode(400);
    }

    // POST Tests

    @Test(description = "Valid Login with Body")
    public void testValidLoginWithBody() {
        JsonPath response = given(Method.POST, "/user/login",
                        "{\"username\":\"valid_user\",\"password\":\"correct_password\"}").
                asJson();
        String token = response.getString("token");
        assertThat(token, equalTo("$.schema"));
    }

    @Test(description = "Invalid Password with Body")
    public void testInvalidPasswordWithBody() {
        JsonPath response = given(Method.POST, "/user/login",
                        "{\"username\":\"valid_user\",\"password\":\"incorrect_password\"}").
                asJson();
        String token = response.getString("token");
        assertThat(token, equalTo(""));
    }

    @Test(description = "Empty Username with Body")
    public void testEmptyUsernameWithBody() {
        JsonPath response = given(Method.POST, "/user/login",
                        "{\"username\":\"\",\"password\":\"correct_password\"}").
                asJson();
        String token = response.getString("token");
        assertThat(token, equalTo(""));
    }

    @Test(description = "Empty Password with Body")
    public void testEmptyPasswordWithBody() {
        JsonPath response = given(Method.POST, "/user/login",
                        "{\"username\":\"valid_user\",\"password\":\"\"}").
                asJson();
        String token = response.getString("token");
        assertThat(token, equalTo(""));
    }

    @Test(description = "Non-Alphanumeric Username with Body")
    public void testNonAlphanumericUsernameWithBody() {
        JsonPath response = given(Method.POST, "/user/login",
                        "{\"username\":\"@#$%^&*()\"," +
                                "\"password\":\"correct_password\"}").
                asJson();
        String token = response.getString("token");
        assertThat(token, equalTo(""));
    }

    // PUT Tests

    @Test(description = "Valid Login with Path Param")
    public void testValidLoginWithPathParam() {
        given(Method.PUT, "/user/login/{username}/{password}",
                        "valid_user", "correct_password").
                then().statusCode(200);
    }

    @Test(description = "Invalid Password with Path Param")
    public void testInvalidPasswordWithPathParam() {
        given(Method.PUT, "/user/login/{username}/{password}",
                        "valid_user", "incorrect_password").
                then().statusCode(400);
    }

    @Test(description = "Empty Username with Path Param")
    public void testEmptyUsernameWithPathParam() {
        given(Method.PUT, "/user/login/{username}/{password}",
                        "", "correct_password").
                then().statusCode(400);
    }

    @Test(description = "Empty Password with Path Param")
    public void testEmptyPasswordWithPathParam() {
        given(Method.PUT, "/user/login/{username}/{password}",
                        "valid_user", "").
                then().statusCode(400);
    }

    @Test(description = "Non-Alphanumeric Username with Path Param")
    public void testNonAlphanumericUsernameWithPathParam() {
        given(Method.PUT, "/user/login/{username}/{password}",
                        "@#$%^&*()", "correct_password").
                then().statusCode(400);
    }

    // DELETE Tests

    @Test(description = "Valid Login with Body and Delete")
    public void testValidLoginWithBodyAndDelete() {
        JsonPath response = given(Method.POST, "/user/login",
                        "{\"username\":\"valid_user\",\"password\":\"correct_password\"}").
                asJson();
        String token = response.getString("token");
        given(Method.DELETE, "/user/{token}",
                        token).
                then().statusCode(200);
    }

    @Test(description = "Invalid Password with Body and Delete")
    public void testInvalidPasswordWithBodyAndDelete() {
        JsonPath response = given(Method.POST, "/user/login",
                        "{\"username\":\"valid_user\",\"password\":\"incorrect_password\"}").
                asJson();
        String token = response.getString("token");
        given(Method.DELETE, "/user/{token}",
                        token).
                then().statusCode(400);
    }

    @Test(description = "Empty Username with Body and Delete")
    public void testEmptyUsernameWithBodyAndDelete() {
        JsonPath response = given(Method.POST, "/user/login",
                        "{\"username\":\"\",\"password\":\"correct_password\"}").
                asJson();
        String token = response.getString("token");
        given(Method.DELETE, "/user/{token}",
                        token).
                then().statusCode(400);
    }

    @Test(description = "Empty Password with Body and Delete")
    public void testEmptyPasswordWithBodyAndDelete() {
        JsonPath response = given(Method.POST, "/user/login",
                        "{\"username\":\"valid_user\",\"password\":\"\"}").
                asJson();
        String token = response.getString("token");
        given(Method.DELETE, "/user/{token}",
                        token).
                then().statusCode(400);
    }

    @Test(description = "Non-Alphanumeric Username with Body and Delete")
    public void testNonAlphanumericUsernameWithBodyAndDelete() {
        JsonPath response = given(Method.POST, "/user/login",
                        "{\"username\":\"@#$%^&*()\"," +
                                "\"password\":\"correct_password\"}").
                asJson();
        String token = response.getString("token");
        given(Method.DELETE, "/user/{token}",
                        token).
                then().statusCode(400);
    }
}
