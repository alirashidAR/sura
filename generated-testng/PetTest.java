import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PetAPITest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    // POST Tests
    @Test
    public void testPostValidPetObject() {
        Response response = given().contentType(ContentType.JSON)
                .body("{\"id\":12345,\"name\":\"Max\",\"tag\":[\"dog\",\"cute\"]}")
                .when()
                .post("/pet")
                .then()
                .assertThat()
                .statusCode(201);
    }

    @Test
    public void testPostInvalidId() {
        given().contentType(ContentType.JSON)
                .body("{\"id\":null,\"name\":\"Max\",\"tag\":[\"dog\",\"cute\"]}")
                .when()
                .post("/pet")
                .then()
                .assertThat()
                .statusCode(405);
    }

    @Test
    public void testPostInvalidName() {
        given().contentType(ContentType.JSON)
                .body("{\"id\":12345,\"name\":null,\"tag\":[\"dog\",\"cute\"]}")
                .when()
                .post("/pet")
                .then()
                .assertThat()
                .statusCode(405);
    }

    @Test
    public void testPostInvalidTag() {
        given().contentType(ContentType.JSON)
                .body("{\"id\":12345,\"name\":\"Max\",\"tag\":null}")
                .when()
                .post("/pet")
                .then()
                .assertThat()
                .statusCode(405);
    }

    @Test
    public void testPostEmptyPetObject() {
        given().contentType(ContentType.JSON)
                .body("{}")
                .when()
                .post("/pet")
                .then()
                .assertThat()
                .statusCode(405);
    }

    @Test
    public void testPostLargePetObject() {
        given().contentType(ContentType.JSON)
                .body("{\"id\":12345,\"name\":\"Max\",\"tag\":[\"dog\",\"cute\",\"playful\",\"smart\"]}")
                .when()
                .post("/pet")
                .then()
                .assertThat()
                .statusCode(201);
    }

    @Test
    public void testPostExcessiveFields() {
        given().contentType(ContentType.JSON)
                .body("{\"id\":12345,\"name\":\"Max\",\"tag\":[\"dog\",\"cute\",\"playful\",\"smart\",\"large\",\"heavy\"]}")
                .when()
                .post("/pet")
                .then()
                .assertThat()
                .statusCode(405);
    }

    @Test
    public void testPostJsonParsingError() {
        given().contentType(ContentType.JSON)
                .body("{\"id\":12345,\"name\":\"Max\"")
                .when()
                .post("/pet")
                .then()
                .assertThat()
                .statusCode(405);
    }

    // PUT Tests
    @Test
    public void testPutValidPetUpdate() {
        given().contentType(ContentType.JSON)
                .body("{\"id\":1,\"category\":{\"id\":2,\"name\":\"Dog\"},\"name\":\"Buddy\",\"photoUrls\":[\"url1\",\"url2\"],\"tags\":[{\"id\":4,\"name\":\"tag1\"}]}")
                .when()
                .put("/pet/{petId}", 1)
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testPutInvalidID() {
        given().contentType(ContentType.JSON)
                .body("{\"id\":-1,\"category\":{\"id\":2,\"name\":\"Dog\"},\"name\":\"Buddy\",\"photoUrls\":[\"url1\",\"url2\"],\"tags\":[{\"id\":4,\"name\":\"tag1\"}]}")
                .when()
                .put("/pet/{petId}", 1)
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void testPutPetNotfoundUpdate() {
        given().contentType(ContentType.JSON)
                .body("{\"id\":999,\"category\":{\"id\":2,\"name\":\"Dog\"},\"name\":\"Buddy\",\"photoUrls\":[\"url1\",\"url2\"],\"tags\":[{\"id\":4,\"name\":\"tag1\"}]}")
                .when()
                .put("/pet/{petId}", 1)
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void testPutEmptyBodyUpdate() {
        given().contentType(ContentType.JSON)
                .body("{}")
                .when()
                .put("/pet/{petId}", 1)
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void testPutInvalidJsonFormatUpdate() {
        given().contentType(ContentType.JSON)
                .body("{\"id\":\"string\"}")
                .when()
                .put("/pet/{petId}", 1)
                .then()
                .assertThat()
                .statusCode(400);
    }

    @AfterClass
    public void tearDown() {
        RestAssured.reset();
    }
}
