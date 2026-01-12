import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class PetTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    private Object[][] getTestData() {
        return new Object[][]{
                {123, null},
                {-123, null},
                {null, null}
        };
    }

    @Test(dataProvider = "getTestData")
    public void testGetPet(int petId, int expectedStatus) {
        given()
                .pathParam("petId", petId)
                .when()
                .get("/pet/{petId}")
                .then()
                .assertThat().statusCode(expectedStatus);
    }

    @Test(dataProvider = "getTestData")
    public void testUpdatePet(int petId, int expectedStatus) {
        given()
                .pathParam("petId", petId)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(new Pet(123, "Max"))
                .when()
                .patch("/pet/{petId}")
                .then()
                .assertThat().statusCode(expectedStatus);
    }

    @Test(dataProvider = "getTestData")
    public void testDeletePet(int petId, int expectedStatus) {
        given()
                .pathParam("petId", petId)
                .header("api_key", "abc")
                .when()
                .delete("/pet/{petId}")
                .then()
                .assertThat().statusCode(expectedStatus);
    }

    private class Pet {
        public int id;
        public String name;

        public Pet(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
