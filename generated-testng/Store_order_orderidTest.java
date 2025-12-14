import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class StoreOrderTests {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // GET tests
    @Test(enabled = true, description = "Valid Order ID (1)")
    public void getValidOrderId1() {
        Response response = given(Method.GET, "/store/order/1")
                .then()
                .statusCode(200)
                .extract().response();
        System.out.println(response.asString());
    }

    @Test(enabled = true, description = "Valid Order ID (10)")
    public void getValidOrderId10() {
        Response response = given(Method.GET, "/store/order/10")
                .then()
                .statusCode(200)
                .extract().response();
        System.out.println(response.asString());
    }

    @Test(enabled = true, description = "Invalid Order ID (0)")
    public void getInvalidOrderId0() {
        Response response = given(Method.GET, "/store/order/0")
                .then()
                .statusCode(400)
                .extract().response();
        System.out.println(response.asString());
    }

    @Test(enabled = true, description = "Invalid Order ID (negative)")
    public void getInvalidOrderIdNegative() {
        Response response = given(Method.GET, "/store/order/-1")
                .then()
                .statusCode(400)
                .extract().response();
        System.out.println(response.asString());
    }

    @Test(enabled = true, description = "Non-existent Order ID (11)")
    public void getNonExistentOrderId11() {
        Response response = given(Method.GET, "/store/order/11")
                .then()
                .statusCode(404)
                .extract().response();
        System.out.println(response.asString());
    }

    // DELETE tests
    @Test(enabled = true, description = "Valid ID, order should be deleted successfully (123)")
    public void deleteOrder123() {
        given(Method.DELETE, "/store/order/123")
                .then()
                .statusCode(204);
    }

    @Test(enabled = true, description = "Invalid ID (negative), should return 400 error (-1)")
    public void deleteInvalidOrderIdNegative1() {
        Response response = given(Method.DELETE, "/store/order/-1")
                .then()
                .statusCode(400)
                .extract().response();
        System.out.println(response.asString());
    }

    @Test(enabled = true, description = "Invalid ID (non-integer string), should return 400 error (abc)")
    public void deleteInvalidOrderIdNonIntegerString() {
        Response response = given(Method.DELETE, "/store/order/abc")
                .then()
                .statusCode(400)
                .extract().response();
        System.out.println(response.asString());
    }

    @Test(enabled = true, description = "Order not found, should return no content (204) (1)")
    public void deleteOrderNotFound204() {
        given(Method.DELETE, "/store/order/1")
                .then()
                .statusCode(204);
    }

    @Test(enabled = true, description = "Order not found, should return 404 error (456)")
    public void deleteOrderNotFound404() {
        Response response = given(Method.DELETE, "/store/order/456")
                .then()
                .statusCode(404)
                .extract().response();
        System.out.println(response.asString());
    }

    @AfterClass
    public void teardown() {
        RestAssured.reset();
    }
}
