package api.endpoints;

import api.payload.BookingRequestPayload;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class BookingEndPoints {

    public static Response getAllBookingIds(){
        Response response = given()
                .accept(ContentType.JSON)
                .when()
                .get(Routes.get_all_booking_ids_url);
        return response;
    }

    public static Response getBookingDetailsById(int bookingIdGeneratedFromCreateBooking){
        Response response = given()
                .pathParam("id", bookingIdGeneratedFromCreateBooking)
                .when()
                .get(Routes.get_booking_details_by_id_url);
        return response;
    }



    public static Response createBooking(BookingRequestPayload bookingRequestPayload){
        Response response = given()
                .contentType(ContentType.JSON)
                .body(bookingRequestPayload)
                .when()
                .post(Routes.create_booking_url);

        return response;

    }

    public static Response updateBooking(int id, BookingRequestPayload bookingRequestPayload, String authToken){
        Response response = given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .header("Cookie", "token=" + authToken)
                .pathParam("id", id)
                .body(bookingRequestPayload)
                .when()
                .put(Routes.update_booking_url);

        return response;

    }

    public static Response partialUpdateBooking(int id, BookingRequestPayload bookingRequestPayload, String authToken){
        Response response = given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .header("Cookie", "token=" + authToken)
                .pathParam("id", id)
                .body(bookingRequestPayload)
                .when()
                .patch(Routes.partial_update_booking_url);

        return response;

    }


    //delete booking
    public static Response deleteBooking(int id, String authToken){
        Response response = given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + authToken)
                .pathParam("id", id)
                .when()
                .delete(Routes.delete_booking_url);

        return response;
    }
}
