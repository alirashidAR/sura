import io.restassured.RestAssured;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PetFindByTagsTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
    }

    /**
     * Grouping test methods by HTTP method using comments
     */

    // GET Method Tests

    @Test(description = "Valid multiple tags query parameter")
    public void tc_1_ValidMultipleTagsQueryParameter() {
        given()
                .queryParam("tags", "tag1, tag2").
                when().
                get("/pet/findByTags").
                then().
                statusCode(200).
                body("id[0]", equalTo(123)).
                body("name[0]", equalTo("Pet 1")).
                body("photoUrls[0]", equalTo("url1")).
                body("tags[0]", equalTo("tag1, tag2"));
    }

    @Test(description = "Empty query parameter")
    public void tc_2_EmptyQueryParameter() {
        given()
                .queryParam("tags", "").
                when().
                get("/pet/findByTags").
                then().
                statusCode(200).
                body("length()", equalTo(0));
    }

    @Test(description = "Invalid tag value in query parameter (missing comma separator)")
    public void tc_3_InvalidTagValueInQueryParameter() {
        given()
                .queryParam("tags", "tag1 tag2").
                when().
                get("/pet/findByTags").
                then().
                statusCode(400);
    }

    @Test(description = "Multiple tags with duplicate values in query parameter")
    public void tc_4_MultipleTagsWithDuplicateValuesInQueryParameter() {
        given()
                .queryParam("tags", "tag1, tag2, tag2").
                when().
                get("/pet/findByTags").
                then().
                statusCode(200).
                body("id[0]", equalTo(123)).
                body("name[0]", equalTo("Pet 1")).
                body("photoUrls[0]", equalTo("url1")).
                body("tags[0]", equalTo("tag1, tag2"));
    }

    @Test(description = "No tags provided in query parameter (required=true)")
    public void tc_5_NoTagsProvidedInQueryParameter() {
        given()
                .when().
                get("/pet/findByTags").
                then().
                statusCode(400);
    }
}
