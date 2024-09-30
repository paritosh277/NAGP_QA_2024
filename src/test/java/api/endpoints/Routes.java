package api.endpoints;

public class Routes {

    public static String base_url = "https://restful-booker.herokuapp.com";

    public static String auth_url = base_url + "/auth";

    public static String ping_url = base_url + "/ping";

    public static String get_all_booking_ids_url = base_url + "/booking";

    public static String get_booking_details_by_id_url = base_url + "/booking/{id}";

    public static String create_booking_url = base_url + "/booking";

    public static String update_booking_url = base_url + "/booking/{id}";

    public static String partial_update_booking_url = base_url + "/booking/{id}";

    public static String delete_booking_url = base_url + "/booking/{id}";
}
