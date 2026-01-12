import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PetAPITests {

    @BeforeClass
    public void setup() {
        // Setup Rest Assured with the base URI
        RestAssured.baseURI = "https://api.example.com";
    }

    @DataProvider(name = "petData")
    public Object[][] dataProvider() {
        return new Object[][]{
                {"Valid Pet PUT Request", "{\"id\":1,\"category\":{\"name\":\"Dogs\"},\"name\":\"Max\",\"photoUrls\":[\"http://example.com/image1.jpg\",\"http://example.com/image2.jpg\"],\"tags\":[{\"name\":\"tag1\"},{\"name\":\"tag2\"}]}"},
                {"Invalid Pet PUT Request - Missing ID", ""},
                {"Invalid Pet PUT Request - Invalid Category", "{\"id\":1,\"category\":{\"name\":\"Invalid Category\"},\"name\":\"Max\",\"photoUrls\":[\"http://example.com/image1.jpg\",\"http://example.com/image2.jpg\"],\"tags\":[{\"name\":\"tag1\"},{\"name\":\"tag2\"}]}"},
                {"Pet PUT Request with Invalid Tags", "{\"id\":1,\"category\":{\"name\":\"Dogs\"},\"name\":\"Max\",\"photoUrls\":[\"http://example.com/image1.jpg\",\"http://example.com/image2.jpg\"],\"tags\":[{\"name\":\"tag1\"},{\"name\":\"Invalid Tag\"}]}"},
                {"Non-Existent Pet PUT Request", "{\"id\":1,\"category\":{\"name\":\"Dogs\"},\"name\":\"Max\",\"photoUrls\":[\"http://example.com/image1.jpg\",\"http://example.com/image2.jpg\"],\"tags\":[{\"name\":\"tag1\"},{\"name\":\"tag2\"}]}"}
        };
    }

    @Test(dataProvider = "petData", dataProviderClass = PetAPITests.class, groups = {"General"})
    public void putPet(String testName, String requestBody) {
        // PUT /pet
        RestAssured.given()
                .header("Content-Type", "application/json")
                .pathParam("id", 1)
                .body(requestBody)
                .when().put("/pet/{id}")
                .then().statusCode(Integer.parseInt(getExpectedStatus(testName)));
    }

    @Test(dataProvider = "petData", dataProviderClass = PetAPITests.class, groups = {"General"})
    public void getPet(String testName, String requestBody) {
        // GET /pet
        RestAssured.given()
                .header("Content-Type", "application/json")
                .queryParam("id", 1)
                .body(requestBody)
                .when().get("/pet/{id}")
                .then().statusCode(Integer.parseInt(getExpectedStatus(testName)));
    }

    @Test(dataProvider = "petData", dataProviderClass = PetAPITests.class, groups = {"General"})
    public void postPet(String testName, String requestBody) {
        // POST /pet
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when().post("/pet")
                .then().statusCode(Integer.parseInt(getExpectedStatus(testName)));
    }

    @Test(dataProvider = "petData", dataProviderClass = PetAPITests.class, groups = {"General"})
    public void deletePet(String testName, String requestBody) {
        // DELETE /pet
        RestAssured.given()
                .header("Content-Type", "application/json")
                .pathParam("id", 1)
                .body(requestBody)
                .when().delete("/pet/{id}")
                .then().statusCode(Integer.parseInt(getExpectedStatus(testName)));
    }

    private int getExpectedStatus(String testName) {
        if (testName.equals("Valid Pet PUT Request")) return 200;
        else if (testName.equals("Invalid Pet PUT Request - Missing ID")) return 400;
        else if (testName.equals("Invalid Pet PUT Request - Invalid Category")) return 400;
        else if (testName.equals("Pet PUT Request with Invalid Tags")) return 400;
        else if (testName.equals("Non-Existent Pet PUT Request")) return 404;
        else return 200;
    }
}
