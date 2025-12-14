import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class OrderApiTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // GROUP 1: GET Tests
    @Test(description = "Valid Order ID")
    public void validOrderId() {
        int orderId = 1;
        String path = "/store/order/" + orderId;
        int statusCode = get(path, orderId).getStatusCode();
        assert statusCode == 200 : "Expected status code 200 but got " + statusCode;
    }

    @Test(description = "Invalid Order ID - Too Low")
    public void invalidOrderIdTooLow() {
        int orderId = 0;
        String path = "/store/order/" + orderId;
        get(path, orderId).then().statusCode(400);
    }

    @Test(description = "Invalid Order ID - Too High")
    public void invalidOrderIdTooHigh() {
        int orderId = 11;
        String path = "/store/order/" + orderId;
        get(path, orderId).then().statusCode(400);
    }

    @Test(description = "Order ID Not Found")
    public void orderNotFound() {
        int orderId = 9999;
        String path = "/store/order/" + orderId;
        get(path, orderId).then().statusCode(404);
    }

    @Test(description = "Non-Integer Order ID")
    public void nonIntegerOrderId() {
        String orderId = "non-int";
        String path = "/store/order/" + orderId;
        get(path, orderId).then().statusCode(400);
    }

    private Response get(String path, int orderId) {
        return RestAssured.given()
                .pathParam("orderId", orderId)
                .when()
                .get(path)
                .then()
                .contentType(ContentType.JSON)
                .extract().response();
    }

    // GROUP 2: DELETE Tests
    @Test(description = "Valid integer ID (positive)")
    public void validIntegerIdPositive() {
        int orderId = 1;
        String path = "/store/order/" + orderId;
        int statusCode = delete(path, orderId).getStatusCode();
        assert statusCode == 200 : "Expected status code 200 but got " + statusCode;
    }

    @Test(description = "Valid integer ID (negative)")
    public void validIntegerIdNegative() {
        int orderId = -1;
        String path = "/store/order/" + orderId;
        delete(path, orderId).then().statusCode(400);
    }

    @Test(description = "Invalid integer ID (non-numeric)")
    public void invalidIntegerIdNonNumeric() {
        String orderId = "abc";
        String path = "/store/order/" + orderId;
        delete(path, orderId).then().statusCode(400);
    }

    @Test(description = "Order does not exist")
    public void orderDoesNotExist() {
        int orderId = 1;
        String path = "/store/order/" + orderId;
        delete(path, orderId).then().statusCode(404);
    }

    @Test(description = "Invalid integer ID (zero)")
    public void invalidIntegerIdZero() {
        int orderId = 0;
        String path = "/store/order/" + orderId;
        delete(path, orderId).then().statusCode(400);
    }

    private Response delete(String path, Object orderId) {
        return RestAssured.given()
                .pathParam("orderId", orderId)
                .when()
                .delete(path)
                .then()
                .contentType(ContentType.JSON)
                .extract().response();
    }
}
