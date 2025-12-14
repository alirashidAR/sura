import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CreateUserArrayTest {

    private static final String BASE_URL = "https://api.example.com";
    private Response response;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    // POST tests
    @Test(dataProvider = "happyPath", dataProviderClass = DataProviders.class)
    public void tc001HappyPath(String body) {
        response = given().contentType(ContentType.JSON).body(body).when().post("/user/createWithArray");
        System.out.println("Expected output for TC-001: Status - 201, Message - Users created successfully");
        assert response.statusCode() == 201;
    }

    @Test(dataProvider = "singleUser", dataProviderClass = DataProviders.class)
    public void tc002SingleUser(String body) {
        response = given().contentType(ContentType.JSON).body(body).when().post("/user/createWithArray");
        System.out.println("Expected output for TC-002: Status - 201, Message - User created successfully");
        assert response.statusCode() == 201;
    }

    @Test(dataProvider = "invalidUser", dataProviderClass = DataProviders.class)
    public void tc003InvalidUser(String body) {
        response = given().contentType(ContentType.JSON).body(body).when().post("/user/createWithArray");
        System.out.println("Expected output for TC-003: Status - 400, Error - Invalid user data: name cannot be null");
        assert response.statusCode() == 400;
    }

    @Test(dataProvider = "emptyArray", dataProviderClass = DataProviders.class)
    public void tc004EmptyArray(String body) {
        response = given().contentType(ContentType.JSON).body(body).when().post("/user/createWithArray");
        System.out.println("Expected output for TC-004: Status - 400, Error - Invalid request body: empty array of users");
        assert response.statusCode() == 400;
    }

    // GET tests
    @Test
    public void tc005GetAllUsers() {
        response = given().when().get("/user/createWithArray");
        System.out.println("Expected output for TC-005: Status - 200");
        assert response.statusCode() == 200;
    }

    // PUT tests
    @Test(dataProvider = "singleUser", dataProviderClass = DataProviders.class)
    public void tc006UpdateSingleUser(String body) {
        response = given().contentType(ContentType.JSON).body(body).when().put("/user/createWithArray");
        System.out.println("Expected output for TC-006: Status - 200, Message - User updated successfully");
        assert response.statusCode() == 200;
    }

    // DELETE tests
    @Test(dataProvider = "singleUser", dataProviderClass = DataProviders.class)
    public void tc007DeleteSingleUser(String body) {
        response = given().contentType(ContentType.JSON).body(body).when().delete("/user/createWithArray");
        System.out.println("Expected output for TC-007: Status - 200, Message - User deleted successfully");
        assert response.statusCode() == 200;
    }

    @AfterClass
    public void tearDown() {
        response = null;
    }
}

class DataProviders {
    public String[] happyPath() {
        return new String[] {"[{\"name\":\"John\",\"email\":\"john@example.com\"},{\"name\":\"Jane\",\"email\":\"jane@example.com\"}]"};
    }

    public String[] singleUser() {
        return new String[] {"{\"name\":\"John\",\"email\":\"john@example.com\"}"};
    }

    public String[] invalidUser() {
        return new String[] {"{\"name\":null,\"email\":\"john@example.com\"}"};
    }

    public String[] emptyArray() {
        return new String[] {"[]"};
    }
}
