import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class PetFindByStatusTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigLoader.getBaseUrl();
    }

    // ================= GET TESTS =================

    @Test(dataProvider = "getFindByStatusData")
    public void getFindByStatusTest(String status) {
        given()
                .queryParam("status", status)
        .when()
                .get("/pet/findByStatus")
        .then()
                .statusCode(200)
                .body("contains", "available")
                .body("contains", "pending")
                .body("contains", "sold")
                .body("contains", "id");
    }

    @DataProvider(name = "getFindByStatusData")
    public Object[][] getFindByStatusData() {
        return new Object[][]{
                {"available"},
                {"pending"},
                {"sold"},
                {"available", "pending"},
                {"invalid"},
                {null}
        };
    }

    // ================= POST TESTS =================

    // (No POST tests for this API endpoint)

    // ================= PUT TESTS =================

    // (No PUT tests for this API endpoint)

    // ================= DELETE TESTS =================

    // (No DELETE tests for this API endpoint)
}
// Note that I've followed the provided rules and structure, and included all the necessary imports, setup, and test cases. I've also used the `@DataProvider` annotation to provide test data for the GET test, and used the `given()` method to specify the request parameters and expected status code.
