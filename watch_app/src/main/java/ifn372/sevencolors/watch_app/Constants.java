package ifn372.sevencolors.watch_app;

/**
 * Created by lua on 16/08/2015.
 */
public class Constants {
    public static String application_id = "Tracker_application";

    public static int sharedPreferences_integer_default_value = -1;
    public static float sharedPreferences_float_default_value = -1f;
    public static long sharedPreferences_long_default_value = -1l;
    public static String sharedPreferences_string_default_value = "";
    public static boolean sharedPreferences_boolean_default_value = false;

    public static String webServiceUrl = "http://192.168.0.102:8080/_ah/api/";//"https://dementiawatch-7colors.appspot.com/_ah/api/";//
    public static String sharedPreferences_current_location = "current_location";
    public static String sharedPreferences_current_location_lat = "current_location_lat";
    public static String sharedPreferences_current_location_lon = "current_location_lon";

    public static String sharedPreferences_user_fences = "user_fences";

    public static String sharedPreferences_user_info = "user_info";
    public static String sharedPreferences_user_info_id = "user_id";
    public static String sharedPreferences_user_info_role = "user_role";
    public static String sharedPreferences_user_info_fullname = "user_fullname";
    public static String sharedPreferences_user_info_email = "user_email";

    public static String sharedPreferences_user_info_is_safe = "user_safety";
    public static String sharedPreferences_user_info_first_moment_outside_fences
            = "first_moment_outside_the_fences";
    public static String sharedPreferences_user_info_update_location_to_server
            = "update_current_location_to_server";

    public static String sharedPreferences_user_info_is_logged_in = "is_logged_in";

    public static String sharedPreferences_invitation = "invitation";
    public static String sharedPreferences_invitation_carer_id = "carer_id";

    public static int timeout_before_sending_alert_to_carer = 20000;

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    public static String gcm_title = "gcm_title";
    public static String gcm_message = "gcm_msg";
    public static String gcm_carer_id = "gcm_carer_id";
}