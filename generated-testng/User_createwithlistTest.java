import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class CreateUserTest {

    @BeforeClass
    public void setup() {
        // Setup Rest Assured to use the correct base URI and headers
        RestAssured.baseURI = "https://api.example.com";
    }

    @Test(groups = {"POST"})
    public void createMultipleUsersWithValidData() {
        given()
                .contentType(ContentType.JSON)
                .body("[{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"},{\"name\":\"Jane Doe\",\"email\":\"jane.doe@example.com\"}]")
                .when()
                .post("/user/createWithList")
                .then()
                .statusCode(200);
    }

    @Test(groups = {"POST"})
    public void createZeroOrOneUserEdgeCases() {
        given()
                .contentType(ContentType.JSON)
                .body("[[]]")
                .when()
                .post("/user/createWithList")
                .then()
                .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"}")
                .when()
                .post("/user/createWithList")
                .then()
                .statusCode(200);
    }

    @Test(groups = {"POST"})
    public void createUserWithInvalidDataSingleUser() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Invalid User\",\"email\":\"invalid_email\"}")
                .when()
                .post("/user/createWithList")
                .then()
                .statusCode(200);
    }

    @Test(groups = {"POST"})
    public void createUserWithValidDataAndAnInvalidEdgeCaseSingleUser() {
        given()
                .contentType(ContentType.JSON)
                .body("[{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"},{\"name\":\"\",\"email\":\"\"}]")
                .when()
                .post("/user/createWithList")
                .then()
                .statusCode(200);
    }

    @Test(groups = {"POST"})
    public void emptyListOfUsersReturnError() {
        given()
                .contentType(ContentType.JSON)
                .body("[[]]")
                .when()
                .post("/user/createWithList")
                .then()
                .statusCode(400);
    }
}
