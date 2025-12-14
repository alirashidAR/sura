import io.restassured.RestAssured;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class StoreInventoryTest {

    private String baseURI = "https://api.example.com";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseURI;
    }

    // GET Tests

    @Test(description = "Get all inventory items")
    public void getInventoryItems() {
        given()
                .when().get("/store/inventory")
                .then()
                .statusCode(200)
                .body("data.statusCodes[0]", equalTo(200));
    }

    @Test(description = "Get inventory item with valid quantity parameter", expectedExceptions = AssertionError.class)
    public void getInventoryItemWithValidQuantity() {
        given()
                .when().get("/store/inventory?qty=50")
                .then()
                .statusCode(200)
                .body("data.statusCodes[0]", equalTo(200))
                .body("data.quantities[0]", equalTo(50));
    }

    @Test(description = "Get inventory item with invalid quantity parameter", expectedExceptions = AssertionError.class)
    public void getInventoryItemWithInvalidQuantity() {
        given()
                .when().get("/store/inventory?quantity=100")
                .then()
                .statusCode(400)
                .body("error", equalTo("Invalid quantity parameter"));
    }

    @Test(description = "Get inventory item with invalid query parameter", expectedExceptions = AssertionError.class)
    public void getInventoryItemWithInvalidQuery() {
        given()
                .when().get("/store/inventory?invalidParam=true")
                .then()
                .statusCode(400)
                .body("error", equalTo("Invalid query parameter"));
    }

    @Test(description = "Get inventory item with invalid quantity and query parameters", expectedExceptions = AssertionError.class)
    public void getInventoryItemWithInvalidQuantityAndQuery() {
        given()
                .when().get("/store/inventory?quantity=100&invalidParam=true")
                .then()
                .statusCode(400)
                .body("error", equalTo("Invalid query parameter"));
    }

    @Test(description = "Get inventory item with conflicting quantity parameters", expectedExceptions = AssertionError.class)
    public void getInventoryItemWithConflictingQuantity() {
        given()
                .when().get("/store/inventory?qty=50&quantity=100")
                .then()
                .statusCode(400)
                .body("error", equalTo("Invalid quantity parameter"));
    }

    // POST Tests

    @Test(description = "POST is not implemented for this path", expectedExceptions = AssertionError.class)
    public void postInventoryItem() {
        given()
                .when().post("/store/inventory")
                .then()
                .statusCode(405);
    }

    // PUT Tests

    @Test(description = "PUT is not implemented for this path", expectedExceptions = AssertionError.class)
    public void putInventoryItem() {
        given()
                .when().put("/store/inventory")
                .then()
                .statusCode(405);
    }

    // DELETE Tests

    @Test(description = "DELETE is not implemented for this path", expectedExceptions = AssertionError.class)
    public void deleteInventoryItem() {
        given()
                .when().delete("/store/inventory")
                .then()
                .statusCode(405);
    }

    @AfterClass
    public void cleanup() {
        // Nothing to clean up
    }
}
