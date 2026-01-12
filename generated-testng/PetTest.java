import io.restassured.RestAssured;
import io.restassured.http.Method;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PetAPITest {

    @BeforeClass(alwaysRun = true)
    public void setup() {
        // Set Base URI
        RestAssured.baseURI = "https://api.example.com";
    }

    @Test(priority = 0, groups = {"POST"})
    public void validPetObjectPost() {
        // Valid Pet Object
        RestAssured.given(Method.POST, "/pet")
                .body("{\"id\":123,\"category\":{\"id\":1,\"name\":\"string\"},\"name\":\"Max\",\"photoUrls\":[\"string\"],\"tags\":[{\"id\":1,\"name\":\"string\"}]}")
                .then()
                .statusCode(200)
                .log().all();
    }

    @Test(priority = 0, groups = {"POST"})
    public void invalidPetObjectPost() {
        // Invalid Pet Object (missing id)
        RestAssured.given(Method.POST, "/pet")
                .body("{\"category\":{\"id\":1,\"name\":\"string\"},\"name\":\"Max\",\"photoUrls\":[\"string\"],\"tags\":[{\"id\":1,\"name\":\"string\"}]}")
                .then()
                .statusCode(405)
                .log().all();
    }

    @Test(priority = 0, groups = {"POST"})
    public void validPetObjectWithEmptyPhotoUrlsArrayPost() {
        // Valid Pet Object with empty photoUrls array
        RestAssured.given(Method.POST, "/pet")
                .body("{\"id\":123,\"category\":{\"id\":1,\"name\":\"string\"},\"name\":\"Max\",\"photoUrls\":[],\"tags\":[{\"id\":1,\"name\":\"string\"}]}")
                .then()
                .statusCode(200)
                .log().all();
    }

    @Test(priority = 0, groups = {"POST"})
    public void validPetObjectWithInvalidCategoryPost() {
        // Valid Pet Object with invalid category
        RestAssured.given(Method.POST, "/pet")
                .body("{\"id\":123,\"category\":{},\"name\":\"Max\",\"photoUrls\":[\"string\"],\"tags\":[{\"id\":1,\"name\":\"string\"}]}")
                .then()
                .statusCode(200)
                .log().all();
    }

    @Test(priority = 0, groups = {"POST"})
    public void invalidPetObjectCategoryIsNotAnArrayPost() {
        // Invalid Pet Object (category is not an array)
        RestAssured.given(Method.POST, "/pet")
                .body("{\"id\":123,\"category\":\"string\",\"name\":\"Max\",\"photoUrls\":[\"string\"],\"tags\":[{\"id\":1,\"name\":\"string\"}]}")
                .then()
                .statusCode(405)
                .log().all();
    }

    @Test(priority = 1, groups = {"PUT"})
    public void validPetCreationPut() {
        // Valid Pet Creation
        RestAssured.given(Method.PUT, "/pet")
                .header("Content-Type", "application/json")
                .body("{\"id\":\"12345\",\"name\":\"Fido\",\"tag\":[{\"name\":\"Lost\"},{\"name\":\"Male\"}]}")
                .then()
                .statusCode(200)
                .log().all();
    }

    @Test(priority = 1, groups = {"PUT"})
    public void invalidIdSuppliedPut() {
        // Invalid ID Supplied
        RestAssured.given(Method.PUT, "/pet")
                .header("Content-Type", "application/json")
                .body("{\"id\":\"\",\"name\":\"Fido\",\"tag\":[{\"name\":\"Lost\"},{\"name\":\"Male\"}]}")
                .then()
                .statusCode(400)
                .log().all();
    }

    @Test(priority = 1, groups = {"PUT"})
    public void petNotFoundPut() {
        // Pet Not Found
        RestAssured.given(Method.PUT, "/pet")
                .header("Content-Type", "application/json")
                .body("{\"id\":\"99999\",\"name\":\"Fido\",\"tag\":[{\"name\":\"Lost\"},{\"name\":\"Male\"}]}")
                .then()
                .statusCode(404)
                .log().all();
    }

    @Test(priority = 1, groups = {"PUT"})
    public void validationExceptionMissingNamePut() {
        // Validation Exception (Missing Name)
        RestAssured.given(Method.PUT, "/pet")
                .header("Content-Type", "application/json")
                .body("{\"id\":\"12345\",\"tag\":[{\"name\":\"Lost\"},{\"name\":\"Male\"}]}")
                .then()
                .statusCode(405)
                .log().all();
    }

    @Test(priority = 1, groups = {"PUT"})
    public void validationExceptionEmptyNamePut() {
        // Validation Exception (Empty Name)
        RestAssured.given(Method.PUT, "/pet")
                .header("Content-Type", "application/json")
                .body("{\"id\":\"12345\",\"name\":\"\",\"tag\":[{\"name\":\"Lost\"},{\"name\":\"Male\"}]}")
                .then()
                .statusCode(405)
                .log().all();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        // Nothing to do
    }
}
