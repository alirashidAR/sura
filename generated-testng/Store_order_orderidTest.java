import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class StoreOrderTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // DELETE /store/order/{orderId}
    @Test(priority = 1, description = "Valid Order ID")
    public void deleteValidOrderId() {
        Response response = RestAssured.given()
                .delete("/store/order/123");

        Assert.assertEquals(response.statusCode(), 204);
        Assert.assertNull(response.jsonPath().getString("message"));
    }

    // DELETE /store/order/{orderId}
    @Test(priority = 2, description = "Invalid Order ID (negative)")
    public void deleteInvalidOrderIdNegative() {
        Response response = RestAssured.given()
                .delete("/store/order/-1");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Invalid ID supplied");
    }

    // DELETE /store/order/{orderId}
    @Test(priority = 3, description = "Invalid Order ID (non-integer)")
    public void deleteInvalidOrderIdNonInteger() {
        Response response = RestAssured.given()
                .delete("/store/order/abc");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Invalid ID supplied");
    }

    // DELETE /store/order/{orderId}
    @Test(priority = 4, description = "Order not found")
    public void deleteOrderNotFound() {
        Response response = RestAssured.given()
                .delete("/store/order/99999");

        Assert.assertEquals(response.statusCode(), 404);
        Assert.assertEquals(response.jsonPath().getString("message"), "Order not found");
    }

    // DELETE /store/order/{orderId}
    @Test(priority = 5, description = "OrderId missing")
    public void deleteOrderIdMissing() {
        Response response = RestAssured.given()
                .delete("/store/order");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Invalid ID supplied");
    }
}
