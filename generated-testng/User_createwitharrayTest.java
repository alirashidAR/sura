import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class CreateUserArrayTest {

    @BeforeClass
    public void setUp() {
        baseURI = "https://api.example.com";
    }

    // POST tests
    @Test
    public void createUserArrayWithValidData() {
        String[] body = new String[] {
                "{\"name\":\"John Doe\",\"email\":\"john@example.com\"}",
                "{\"name\":\"Jane Doe\",\"email\":\"jane@example.com\"}"
        };

        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/user/createWithArray");

        response.then().statusCode(201);
    }

    @Test
    public void createUserArrayWithInvalidEmails() {
        String[] body = new String[] {
                "{\"name\":\"John Doe\",\"email\":\"\"}",
                "{\"name\":\"\",\"email\":\"jane@example.com\"}"
        };

        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/user/createWithArray");

        response.then().statusCode(201);
    }

    @Test
    public void createUserArrayWithTwoEmptyUsers() {
        String[] body = new String[] {
                "{\"name\":\"\",\"email\":\"\"}",
                "{\"name\":\"\",\"email\":\"\"}"
        };

        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/user/createWithArray");

        response.then().statusCode(201);
    }

    @Test
    public void createUserArrayWithMultipleUsers() {
        String[] body = new String[] {
                "{\"name\":\"John Doe\",\"email\":\"john@example.com\"}",
                "{\"name\":\"Jane Doe\",\"email\":\"jane@example.com\"}",
                "{\"name\":\"Bob Smith\",\"email\":\"bob@example.com\"}"
        };

        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/user/createWithArray");

        response.then().statusCode(201);
    }

    @Test
    public void createUserArrayWithSingleEmptyUser() {
        String[] body = new String[] {
                "{\"name\":\"\",\"email\":\"\"}"
        };

        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/user/createWithArray");

        response.then().statusCode(201);
    }

    // DELETE tests
    @Test
    public void deleteUserArray() {
        String[] body = new String[] {
                "{\"id\":1,\"name\":\"John Doe\",\"email\":\"john@example.com\"}",
                "{\"id\":2,\"name\":\"Jane Doe\",\"email\":\"jane@example.com\"}"
        };

        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .delete("/user/createWithArray");

        response.then().statusCode(200);
    }

    // PUT tests
    @Test
    public void updateUserArray() {
        String[] body = new String[] {
                "{\"id\":1,\"name\":\"John Doe\",\"email\":\"john@example.com\"}",
                "{\"id\":2,\"name\":\"Jane Doe\",\"email\":\"jane@example.com\"}"
        };

        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put("/user/createWithArray");

        response.then().statusCode(200);
    }

    // GET tests
    @Test
    public void getUserArray() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/user/createWithArray");

        response.then().statusCode(200);
    }
}
