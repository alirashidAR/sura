import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class StoreOrderAPITest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    @DataProvider(name = "getData")
    public Object[][] getData() {
        return new Object[][]{
                {"1", 200},
                {"-1", 400},
                {"11", 400},
                {"11", 404},
                {null, 400}
        };
    }

    @DataProvider(name = "deleteData")
    public Object[][] deleteData() {
        return new Object[][]{
                {"123", 200},
                {"abc", 400},
                {"-123", 400},
                {"0", 400},
                {"999", 404},
                {null, 405}
        };
    }

    @Test(dataProvider = "getData")
    public void getStoreOrderAPITest(String orderId, int expectedStatus) {
        RestAssured.given()
                .pathParam("orderId", orderId)
        .when()
                .get("/store/order/{orderId}")
        .then()
                .log().all()
                .statusCode(expectedStatus);
    }

    @Test(dataProvider = "deleteData")
    public void deleteStoreOrderAPITest(String orderId, int expectedStatus) {
        RestAssured.given()
                .pathParam("orderId", orderId)
        .when()
                .delete("/store/order/{orderId}")
        .then()
                .log().all()
                .statusCode(expectedStatus);
    }
}
