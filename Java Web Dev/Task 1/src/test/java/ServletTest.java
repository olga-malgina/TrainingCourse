import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Test;


public class ServletTest {

    @Test
    public void getRequestTest() {
        String response = get("/requests/servlet").then().assertThat().statusCode(200).extract().asString();
        assertThat(response, ("Current status of servlet: GET BUTTON CLICKED").equals(response));
    }

    @Test
    public void postRequestTest() {
        String response = post("/requests/servlet").then().assertThat().statusCode(200).extract().asString();
        assertThat(response, ("Current status of servlet: POST BUTTON CLICKED").equals(response));
    }

    @Test
    public void putRequestTest() {
        String response = put("/requests/servlet").then().assertThat().statusCode(200).extract().asString();
        assertThat(response, ("Current status of servlet: PUT BUTTON CLICKED").equals(response));
    }

    @Test
    public void deleteRequestTest() {
        String response = delete("/requests/servlet").then().assertThat().statusCode(200).extract().asString();
        assertThat(response, ("Current status of servlet: DELETE BUTTON CLICKED").equals(response));
    }

}
