import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class PetFindByStatusTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // Grouping test methods by HTTP method

    /**
     * Happy path and edge cases for GET /pet/findByStatus
     */
    @DataProvider(name = "getFindByStatus")
    public Object[][] getFindByStatus() {
        return new Object[][]{
                {"happy_path_status_available", null, "available"},
                {"edge_case_multiple_status_values", true, "available,pending"},
                {"negative_test_empty_status", false, ""},
                {"negative_test_invalid_status", false, "available,invalid"},
                {"negative_test_missing_status", false, ""},
                {"happy_path_default_status_available", false, ""}
        };
    }

    @Test(dataProvider = "getFindByStatus")
    public void testGetFindByStatus(String testCaseName, Boolean multipleStatusValues, String status) {
        given().when()
                .queryParam("status", status)
                .when().get("/pet/findByStatus")
                .then().statusCode((testCaseName.contains("available") || testCaseName.contains("default")) ? 200 : 400);
    }

    /**
     * Happy path and edge cases for POST /pet
     */
    @DataProvider(name = "postPet")
    public Object[][] postPet() {
        return new Object[][]{
                {"happy_path_status_available", "available"},
                {"edge_case_multiple_status_values", "available,pending"}
        };
    }

    @Test(dataProvider = "postPet")
    public void testPostPet(String testCaseName, String status) {
        given().when()
                .queryParam("status", status)
                .when().post("/pet/findByStatus")
                .then().statusCode((testCaseName.contains("available") || testCaseName.contains("default")) ? 200 : 400);
    }

    /**
     * Edge cases for PUT /pet/{id}
     */
    @DataProvider(name = "putPetId")
    public Object[][] putPetId() {
        return new Object[][]{
                {"happy_path_status_available", 123, "available"},
                {"edge_case_multiple_status_values", 456, "available,pending"}
        };
    }

    @Test(dataProvider = "putPetId")
    public void testPutPetId(String testCaseName, int id, String status) {
        given().when()
                .pathParam("id", id)
                .queryParam("status", status)
                .when().put("/pet/{id}")
                .then().statusCode((testCaseName.contains("available") || testCaseName.contains("default")) ? 200 : 400);
    }

    /**
     * Edge cases for DELETE /pet/{id}
     */
    @DataProvider(name = "deletePetId")
    public Object[][] deletePetId() {
        return new Object[][]{
                {"happy_path_status_available", 123},
                {"edge_case_multiple_status_values", 456}
        };
    }

    @Test(dataProvider = "deletePetId")
    public void testDeletePetId(String testCaseName, int id) {
        given().when()
                .pathParam("id", id)
                .when().delete("/pet/{id}")
                .then().statusCode((testCaseName.contains("available") || testCaseName.contains("default")) ? 200 : 400);
    }
}
