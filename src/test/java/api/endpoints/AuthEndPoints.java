package api.endpoints;

import api.payload.AuthRequestPayload;
import api.payload.BookingRequestPayload;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthEndPoints {

    public static Response createToken(AuthRequestPayload authRequestPayload){
        Response response = given()
                .contentType(ContentType.JSON)
                .body(authRequestPayload)
                .when()
                .post(Routes.auth_url);

        return response;

    }
}
