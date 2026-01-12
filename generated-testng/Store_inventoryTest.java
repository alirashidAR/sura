import io.qameta.allure.Allure;
import io.restassured.http.Method;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class StoreInventoryTest {

    @BeforeClass
    public void setup() {
        // Setup base URI for API calls
        baseURI = "https://api.example.com";
    }

    /**
     * Test methods grouped by HTTP method
     */
    // GET Methods

    @DataProvider(name = "get-methods")
    public Object[][] provideGetMethodsData() {
        return new Object[][]{
                {"", 200, ""},
                {"InvalidToken", 401, "Missing authorization header"},
                {null, 401, "Missing authorization header"},
                {"filter=", 200, ""}
        };
    }

    @Test(dataProvider = "get-methods")
    public void testGetInventory(String queryParam, int expectedStatus, String edgeCaseReason) {
        // Test GET /store/inventory with different input data
        given()
                .param("filter", queryParam)
                .when()
                .get("/store/inventory")
                .then()
                .statusCode(expectedStatus);
    }

    // POST Methods

    @DataProvider(name = "post-methods")
    public Object[][] providePostMethodsData() {
        return new Object[][]{
                {"largeQuantity", 201, ""},
                {"smallQuantity", 400, "Invalid quantity"}
        };
    }

    @Test(dataProvider = "post-methods")
    public void testPostInventory(String body) {
        // Test POST /store/inventory with different input data
        given()
                .body(body)
                .when()
                .post("/store/inventory")
                .then()
                .statusCode(201);
    }

    // PUT Methods

    @DataProvider(name = "put-methods")
    public Object[][] providePutMethodsData() {
        return new Object[][]{
                {"largeQuantity", 200, ""},
                {"smallQuantity", 400, "Invalid quantity"}
        };
    }

    @Test(dataProvider = "put-methods")
    public void testPutInventory(String body) {
        // Test PUT /store/inventory with different input data
        given()
                .body(body)
                .when()
                .put("/store/inventory")
                .then()
                .statusCode(200);
    }

    // DELETE Methods

    @DataProvider(name = "delete-methods")
    public Object[][] provideDeleteMethodsData() {
        return new Object[][]{
                {"largeQuantity", 204, ""},
                {"smallQuantity", 400, "Invalid quantity"}
        };
    }

    @Test(dataProvider = "delete-methods")
    public void testDeleteInventory(String body) {
        // Test DELETE /store/inventory with different input data
        given()
                .body(body)
                .when()
                .delete("/store/inventory")
                .then()
                .statusCode(204);
    }
}
