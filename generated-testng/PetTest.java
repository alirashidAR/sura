import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class PetAPITests {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigLoader.getBaseUrl();
    }

    // ================= GET TESTS =================

    // No GET tests are provided in the test case data

    // ================= POST TESTS =================

    @Test(dataProvider = "postTests")
    public void postPetTest(Map<String, Object> testData) {
        Response response = RestAssured.given()
                .pathParams("pet_id", testData.get("pet_id"))
                .queryParams(testData.get("query_params"))
                .headers(testData.get("headers"))
                .body(testData.get("body"))
                .when()
                .post("/pet");

        Assert.assertEquals(response.getStatusCode(), testData.get("expected_status"));
        Assert.assertTrue(response.getBody().asString().contains("success"));
        Assert.assertNotNull(response.jsonPath().get("id"));
        Assert.assertEquals(response.getTime(), testData.get("expected_time"));
    }

    @DataProvider(name = "postTests")
    public Object[][] postTests() {
        return new Object[][]{
                {
                        "Happy Path",
                        new HashMap<String, Object>() {{
                            put("pet_id", "");
                            put("query_params", "");
                            put("headers", new HashMap<String, String>() {{
                                put("Content-Type", "application/json");
                            }});
                            put("body", new HashMap<String, Object>() {{
                                put("id", 123);
                                put("name", "Buddy");
                                put("tag", "dog");
                            }});
                            put("expected_status", 200);
                            put("expected_time", 100);
                        }},
                        {
                                "Missing Required Field",
                                new HashMap<String, Object>() {{
                                    put("pet_id", "");
                                    put("query_params", "");
                                    put("headers", new HashMap<String, String>() {{
                                        put("Content-Type", "application/json");
                                    }});
                                    put("body", new HashMap<String, Object>() {{
                                        put("name", "Buddy");
                                        put("tag", "dog");
                                    }});
                                    put("expected_status", 400);
                                    put("expected_time", 100);
                                }},
                        },
                        {
                                "Invalid Request Body",
                                new HashMap<String, Object>() {{
                                    put("pet_id", "");
                                    put("query_params", "");
                                    put("headers", new HashMap<String, String>() {{
                                        put("Content-Type", "application/json");
                                    }});
                                    put("body", new HashMap<String, Object>() {{
                                        put("id", "123");
                                        put("name", "Buddy");
                                        put("tag", "dog");
                                    }});
                                    put("expected_status", 400);
                                    put("expected_time", 100);
                                }},
                        },
                        {
                                "Invalid Content-Type",
                                new HashMap<String, Object>() {{
                                    put("pet_id", "");
                                    put("query_params", "");
                                    put("headers", new HashMap<String, String>() {{
                                        put("Content-Type", "text/plain");
                                    }});
                                    put("body", new HashMap<String, Object>() {{
                                        put("id", 123);
                                        put("name", "Buddy");
                                        put("tag", "dog");
                                    }});
                                    put("expected_status", 415);
                                    put("expected_time", 100);
                                }},
                        },
                        {
                                "Method Not Allowed",
                                new HashMap<String, Object>() {{
                                    put("pet_id", "");
                                    put("query_params", "");
                                    put("headers", new HashMap<String, String>() {{
                                        put("Content-Type", "application/json");
                                    }});
                                    put("body", new HashMap<String, Object>() {{
                                        put("id", 123);
                                        put("name", "Buddy");
                                        put("tag", "dog");
                                    }});
                                    put("expected_status", 405);
                                    put("expected_time", 100);
                                }},
                        }
                }
        };
    }

    // ================= PUT TESTS =================

    @Test(dataProvider = "putTests")
    public void putPetTest(Map<String, Object> testData) {
        Response response = RestAssured.given()
                .pathParams("pet_id", testData.get("pet_id"))
                .queryParams(testData.get("query_params"))
                .headers(testData.get("headers"))
                .body(testData.get("body"))
                .when()
                .put("/pet");

        Assert.assertEquals(response.getStatusCode(), testData.get("expected_status"));
        Assert.assertTrue(response.getBody().asString().contains("success"));
        Assert.assertNotNull(response.jsonPath().get("id"));
        Assert.assertEquals(response.getTime(), testData.get("expected_time"));
    }

    @DataProvider(name = "putTests")
    public Object[][] putTests() {
        return new Object[][]{
                {
                        "Happy Path - Pet Added",
                        new HashMap<String, Object>() {{
                            put("pet_id", 123);
                            put("query_params", "");
                            put("headers", new HashMap<String, String>() {{
                                put("Content-Type", "application/json");
                            }});
                            put("body", new HashMap<String, Object>() {{
                                put("id", 123);
                                put("name", "Fido");
                                put("tag", "dog");
                            }});
                            put("expected_status", 200);
                            put("expected_time", 100);
                        }},
                        {
                                "Missing Required Field - Name",
                                new HashMap<String, Object>() {{
                                    put("pet_id", 123);
                                    put("query_params", "");
                                    put("headers", new HashMap<String, String>() {{
                                        put("Content-Type", "application/json");
                                    }});
                                    put("body", new HashMap<String, Object>() {{
                                        put("id", 123);
                                        put("tag", "dog");
                                    }});
                                    put("expected_status", 400);
                                    put("expected_time", 100);
                                }},
                        },
                        {
                                "Invalid Pet Object - Bad JSON",
                                new HashMap<String, Object>() {{
                                    put("pet_id", 123);
                                    put("query_params", "");
                                    put("headers", new HashMap<String, String>() {{
                                        put("Content-Type", "application/json");
                                    }});
                                    put("body", "Invalid JSON");
                                    put("expected_status", 400);
                                    put("expected_time", 100);
                                }},
                        },
                        {
                                "Resource Not Found - Pet ID",
                                new HashMap<String, Object>() {{
                                    put("pet_id", 123);
                                    put("query_params", "");
                                    put("headers", new HashMap<String, String>() {{
                                        put("Content-Type", "application/json");
                                    }});
                                    put("body", new HashMap<String, Object>() {{
                                        put("id", 123);
                                        put("name", "Fido");
                                        put("tag", "dog");
                                    }});
                                    put("expected_status", 404);
                                    put("expected_time", 100);
                                }},
                        },
                        {
                                "Method Not Allowed",
                                new HashMap<String, Object>() {{
                                    put("pet_id", 123);
                                    put("query_params", "");
                                    put("headers", new HashMap<String, String>() {{
                                        put("Content-Type", "application/json");
                                    }});
                                    put("body", new HashMap<String, Object>() {{
                                        put("id", 123);
                                        put("name", "Fido");
                                        put("tag", "dog");
                                    }});
                                    put("expected_status", 405);
                                    put("expected_time", 100);
                                }},
                        },
                        {
                                "Empty Body",
                                new HashMap<String, Object>() {{
                                    put("pet_id", 123);
                                    put("query_params", "");
                                    put("headers", new HashMap<String, String>() {{
                                        put("Content-Type", "application/json");
                                    }});
                                    put("body", new HashMap<String, Object>() {{
                                        put("id", 123);
                                        put("name", "Fido");
                                        put("tag", "dog");
                                    }});
                                    put("expected_status", 400);
                                    put("expected_time", 100);
                                }},
                        }
                }
        };
    }
}
// Note that I've assumed that the `ConfigLoader` class is already implemented and provides the `getBaseUrl()` method. Also, I've used the `HashMap` class to represent the test data, as it's easier to work with than the `Object[][]` array.
