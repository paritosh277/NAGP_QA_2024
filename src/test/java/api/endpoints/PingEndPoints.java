package api.endpoints;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class PingEndPoints {

    public static Response getHealthCheck(){
        Response response = when()
                .get(Routes.ping_url);
        return response;
    }
}
