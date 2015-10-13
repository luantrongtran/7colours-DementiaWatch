package ifn372.sevencolors.dementiawatch;

/**
 * Created by lua on 16/08/2015.
 */
public class Constants {
    public static String webServiceUrl = "http://172.19.34.165:8080/_ah/api/";//"https://dementiawatch-7colors.appspot.com/_ah/api/"
    public static String application_id = "Tracker_application"; //for Log class

    public static int sharedPreferences_integer_default_value = -1;
    public static float sharedPreferences_float_default_value = -1f;
    public static long sharedPreferences_long_default_value = -1l;
    public static String sharedPreferences_string_default_value = "";

    public static String sharedPreferences_user_info = "user_info";
    public static String sharedPreferences_user_info_id = "user_id";
    public static String sharedPreferences_user_info_role = "user_role";
    public static String sharedPreferences_user_info_fullname = "user_fullname";
    public static String sharedPreferences_user_info_email = "user_email";
    public static String sharedPreferences_user_info_profile_picture = "user_profile_picture";

    public static String create_new_fence_intent_data = "create_new_fence_intent_data";

    public static String patients_lost_intent_data = "patients_lost_intent_data";

    public static String sharedPreferences_current_location = "current_location";
    public static String sharedPreferences_current_location_lat = "current_location_lat";
    public static String sharedPreferences_current_location_lon = "current_location_lon";

    public static String sharedPreferences_temporary_fences = "temporary_fences";

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
}