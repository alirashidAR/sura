import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PetFindByTagsTest {

    private String baseUrl = "https://api.example.com";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseUrl;
    }

    // Test Case 1 - Valid Tags (GET)
    @Test
    public void testCase1_ValidTags() {
        given().queryParam("tags", "tag1, tag2")
                .when()
                .get("/pet/findByTags")
                .then()
                .statusCode(200)
                .body("[0].id", equalTo(1))
                .body("[0].name", equalTo("Pet 1"))
                .body("[1].id", equalTo(2))
                .body("[1].name", equalTo("Pet 2"));
    }

    // Test Case 2 - Invalid Tag Format (GET)
    @Test
    public void testCase2_InvalidTagFormat() {
        given().queryParam("tags", "tag1, invalidTag")
                .when()
                .get("/pet/findByTags")
                .then()
                .statusCode(400);
    }

    // Test Case 3 - Edge Case: Invalid Tag Value (non-string) (GET)
    @Test
    public void testCase2_EdgeCase_InvalidTagValueNonString() {
        given().queryParam("tags", "tag1, 123")
                .when()
                .get("/pet/findByTags")
                .then()
                .statusCode(400);
    }

    // Test Case 3 - Empty Tags (GET)
    @Test
    public void testCase3_EmptyTags() {
        given().queryParam("tags", "")
                .when()
                .get("/pet/findByTags")
                .then()
                .statusCode(200)
                .body(equalTo("["))
                .body("[].id", equalTo(null));
    }

    // Test Case 4 - Multiple Tags with Comma Separation (GET)
    @Test
    public void testCase4_MultipleTagsCommaSeparation() {
        given().queryParam("tags", "tag1, tag2, tag3")
                .when()
                .get("/pet/findByTags")
                .then()
                .statusCode(200)
                .body("[0].id", equalTo(1))
                .body("[0].name", equalTo("Pet 1"))
                .body("[1].id", equalTo(2))
                .body("[1].name", equalTo("Pet 2"))
                .body("[2].id", equalTo(3))
                .body("[2].name", equalTo("Pet 3"));
    }

    // Test Case 5 - Duplicate Tags (GET)
    @Test
    public void testCase5_DuplicateTags() {
        given().queryParam("tags", "tag1, tag1")
                .when()
                .get("/pet/findByTags")
                .then()
                .statusCode(200)
                .body("[0].id", equalTo(1))
                .body("[0].name", equalTo("Pet 1"))
                .body("[1].id", equalTo(2))
                .body("[1].name", equalTo("Pet 2"));
    }
}
