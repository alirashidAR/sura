import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class PetEndpointTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigLoader.getBaseUrl();
    }

    // ================= GET TESTS =================

    @Test(dataProvider = "getTests")
    public void getPetTest(String petId, int expectedStatus) {
        Response response = RestAssured.get("/pet/{petId}", petId);
        Assert.assertEquals(response.getStatusCode(), expectedStatus);
        Assert.assertTrue(response.getBody().asString().contains("success"));
        Assert.assertNotNull(response.jsonPath().get("id"));
    }

    @DataProvider(name = "getTests")
    public Object[][] getTests() {
        return new Object[][]{
                {"123", 200},
                {"abc", 400},
                {"-1", 400},
                {"0", 404},
                {null, 400},
                {"" , 404}
        };
    }

    // ================= POST TESTS =================

    @Test(dataProvider = "postTests")
    public void postPetTest(String petId, String name, String status, int expectedStatus) {
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("status", status);

        Response response = RestAssured.post("/pet/{petId}", petId, body);
        Assert.assertEquals(response.getStatusCode(), expectedStatus);
        Assert.assertTrue(response.getBody().asString().contains("success"));
        Assert.assertNotNull(response.jsonPath().get("id"));
    }

    @DataProvider(name = "postTests")
    public Object[][] postTests() {
        return new Object[][]{
                {"123", "New Name", "New Status", 200},
                {null, "New Name", "New Status", 405},
                {"abc", "New Name", "New Status", 405},
                {"123", "New Name", "", 405},
                {"123", "", "New Status", 405},
                {"123", "New Name", "New Status", 200}
        };
    }

    // ================= DELETE TESTS =================

    @Test(dataProvider = "deleteTests")
    public void deletePetTest(String petId, String apiKey, int expectedStatus) {
        Map<String, String> headers = new HashMap<>();
        headers.put("api_key", apiKey);

        Response response = RestAssured.delete("/pet/{petId}", petId, headers);
        Assert.assertEquals(response.getStatusCode(), expectedStatus);
        Assert.assertTrue(response.getBody().asString().contains("success"));
        Assert.assertNotNull(response.jsonPath().get("id"));
    }

    @DataProvider(name = "deleteTests")
    public Object[][] deleteTests() {
        return new Object[][]{
                {"123", "my_api_key", 200},
                {"123", "", 400},
                {"abc", "my_api_key", 404},
                {"123", "", 400},
                {"-1", "my_api_key", 400},
                {"123", "my_api_key", 200}
        };
    }
}
// This class follows the provided specification and includes all the required tests for the `/pet/{petId}` endpoint. The `@BeforeClass` method sets the base URI for the API endpoint, and the `@Test` methods cover all the test cases for the GET, POST, and DELETE requests. The `@DataProvider` methods provide the test data for each test case.
