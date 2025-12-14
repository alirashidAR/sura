import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class StoreOrderTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // POST /store/order Happy Path Test Case
    @Test(priority = 1)
    public void postStoreOrderHappyPath() {
        String jsonBody = "{\"petId\":12345,\"quantity\":2,\"shipDate\":\"2024-04-15\"}";
        RestAssured.given()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/store/order")
                .then()
                .statusCode(201);
    }

    // POST /store/order Quantity is zero
    @Test(priority = 2)
    public void postStoreOrderQuantityIsZero() {
        String jsonBody = "{\"petId\":12345,\"quantity\":0,\"shipDate\":\"2024-04-15\"}";
        RestAssured.given()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/store/order")
                .then()
                .statusCode(400);
    }

    // POST /store/order Invalid ship date
    @Test(priority = 3)
    public void postStoreOrderInvalidShipDate() {
        String jsonBody = "{\"petId\":12345,\"quantity\":2,\"shipDate\":\"2023-01-01\"}";
        RestAssured.given()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/store/order")
                .then()
                .statusCode(400);
    }

    // POST /store/order Missing required fields
    @Test(priority = 4)
    public void postStoreOrderMissingRequiredFields() {
        String jsonBody = "{\"quantity\":2,\"shipDate\":\"2024-04-15\"}";
        RestAssured.given()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/store/order")
                .then()
                .statusCode(400);
    }

    // POST /store/order Edge Case: petId is null
    @Test(priority = 5)
    public void postStoreOrderEdgeCasePetIdIsNull() {
        String jsonBody = "{\"petId\":null,\"quantity\":2,\"shipDate\":\"2024-04-15\"}";
        RestAssured.given()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/store/order")
                .then()
                .statusCode(201);
    }

    // POST /store/order Edge Case: quantity is a negative number
    @Test(priority = 6)
    public void postStoreOrderEdgeCaseQuantityIsANegativeNumber() {
        String jsonBody = "{\"petId\":12345,\"quantity\":-2,\"shipDate\":\"2024-04-15\"}";
        RestAssured.given()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/store/order")
                .then()
                .statusCode(400);
    }

    // GET /store/order
    @Test(priority = 7)
    public void getStoreOrder() {
        RestAssured.given()
                .when()
                .get("/store/order")
                .then()
                .statusCode(200);
    }

    // PUT /store/order
    @Test(priority = 8)
    public void putStoreOrder() {
        String jsonBody = "{\"petId\":12345,\"quantity\":2,\"shipDate\":\"2024-04-15\"}";
        RestAssured.given()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .put("/store/order")
                .then()
                .statusCode(200);
    }

    // DELETE /store/order
    @Test(priority = 9)
    public void deleteStoreOrder() {
        RestAssured.given()
                .when()
                .delete("/store/order")
                .then()
                .statusCode(204);
    }
}
