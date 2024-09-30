package api.testcases;

import api.endpoints.AuthEndPoints;
import api.endpoints.BookingEndPoints;
import api.endpoints.PingEndPoints;
import api.payload.AuthRequestPayload;
import api.payload.BookingDates;
import api.payload.BookingRequestPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.equalTo;

public class APITest {

    Faker faker;

    BookingRequestPayload bookingRequestPayload;

    BookingDates bookingdates;

    AuthRequestPayload authRequestPayload;

    ObjectMapper objectMapper;

    String authToken;

    int bookingIdGeneratedFromCreateBooking;

    Logger log;


    @BeforeClass
    public void generateTestData() throws JsonProcessingException {

        log = LogManager.getLogger("Restful-BookerAutomation Framework");
        faker = new Faker();
        bookingRequestPayload = new BookingRequestPayload();
        bookingdates = new BookingDates();

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        log.info("Setting Dynamic Data in Request Payload");
        bookingRequestPayload.setFirstname(faker.name().firstName());
        bookingRequestPayload.setLastname(faker.name().lastName());
        bookingRequestPayload.setTotalprice(faker.number().numberBetween(100, 500));
        bookingRequestPayload.setDepositpaid(true);
        bookingdates.setCheckin(currentDateTime.format(formatter));
        bookingdates.setCheckout(currentDateTime.format(formatter));
        bookingRequestPayload.setBookingdates(bookingdates);
        bookingRequestPayload.setAdditionalneeds("None");



        log.info("Get Auth Token from Auth API");
        authRequestPayload = new AuthRequestPayload();
        authRequestPayload.setUsername("admin");
        authRequestPayload.setPassword("password123");
        Response response = AuthEndPoints.createToken(authRequestPayload);
        response.then().log().all();
        authToken = response.then().extract().path("token");
        log.info("Token value is - "+authToken);
    }

    @Test (priority = 1)
    public void testHealthCheckAPIs(){
        log.info("Testing Health Check API");
        Response response = PingEndPoints.getHealthCheck();
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(), 201);
    }


    @Test (priority = 2)
    public void testGetAllBookingIDs(){
        log.info("Testing Get All Booking IDs API");
        Response response = BookingEndPoints.getAllBookingIds();
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(),200);
    }


    @Test (priority = 3)
    public void testCreateBooking(){
        log.info("Testing Create Booking API");
        Response response = BookingEndPoints.createBooking(bookingRequestPayload);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(),200);
        bookingIdGeneratedFromCreateBooking = response.then().extract().path("bookingid");
        log.info("Booking Id Generated from Create Booking- "+ bookingIdGeneratedFromCreateBooking);
    }


    @Test(priority = 4)
    public void testGetBookingDetailsById(){
        log.info("Testing Get Booking Details By Id API");
        Response response = BookingEndPoints.getBookingDetailsById(bookingIdGeneratedFromCreateBooking);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(),200);
    }


    @Test (priority = 5)
    public void testUpdateBooking(){
        log.info("Testing Update Booking API");
        String updatedFirstName = faker.name().firstName();

        log.info("Updating Request Payload with Updated First Name");
        bookingRequestPayload.setFirstname(updatedFirstName);
        Response response = BookingEndPoints.updateBooking(bookingIdGeneratedFromCreateBooking,bookingRequestPayload, authToken);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(),200);

        Response responsePostUpdate = BookingEndPoints.getBookingDetailsById(bookingIdGeneratedFromCreateBooking);
        responsePostUpdate.then().log().all();

        log.info("Verifying First Name is updated or not");
        responsePostUpdate.then().body("firstname",equalTo(updatedFirstName));

    }

    @Test (priority = 6)
    public void testPartialUpdateBooking(){
        log.info("Testing Partial Update Booking API");
        String updatedLastName = faker.name().lastName();

        log.info("Updating Request Payload with Updated Last Name");
        bookingRequestPayload.setLastname(updatedLastName);
        Response response = BookingEndPoints.partialUpdateBooking(bookingIdGeneratedFromCreateBooking,bookingRequestPayload, authToken);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(),200);

        Response responsePostUpdate = BookingEndPoints.getBookingDetailsById(bookingIdGeneratedFromCreateBooking);
        responsePostUpdate.then().log().all();
        log.info("Verifying Last Name is updated or not");
        responsePostUpdate.then().body("lastname",equalTo(updatedLastName));

    }

    @Test (priority = 7)
    public void testDeleteBooking(){
        log.info("Testing Delete Booking API");
        Response response = BookingEndPoints.deleteBooking(bookingIdGeneratedFromCreateBooking, authToken);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(),201);

        Response responseAfterDelete = BookingEndPoints.getBookingDetailsById(bookingIdGeneratedFromCreateBooking);
        responseAfterDelete.then().log().all();
        log.info("Verifying Id is deleted or not");
        //404 error code should be returned by get booking detail by ID API
        Assert.assertEquals(responseAfterDelete.getStatusCode(),404);
    }


}
