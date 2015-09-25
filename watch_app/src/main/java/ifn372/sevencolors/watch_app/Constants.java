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

    public static String webServiceUrl = "http://192.168.0.106:8080/_ah/api/";//"https://dementiawatch-7colors.appspot.com/_ah/api/";//
    public static String sharedPreferences_current_location = "current_location";
    public static String sharedPreferences_current_location_lat = "current_location_lat";
    public static String sharedPreferences_current_location_lon = "current_location_lon";

    public static String sharedPreferences_user_fences = "user_fences";

    public static String sharedPreferences_user_info = "user_info";
    public static String sharedPreferences_user_info_id = "user_id";
    public static String sharedPreferences_user_info_role = "user_role";
    public static String sharedPreferences_user_info_fullname = "user_fullname";
    public static String sharedPreferences_user_info_email = "user_email";
}