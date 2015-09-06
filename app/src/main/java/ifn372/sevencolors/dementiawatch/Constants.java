package ifn372.sevencolors.dementiawatch;

/**
 * Created by lua on 16/08/2015.
 */
public class Constants {
    public static String webServiceUrl = "http://192.168.0.106:8080/_ah/api/";//"https://dementiawatch-7colors.appspot.com/_ah/api/";

    public static String application_id = "Tracker_application"; //for Log class

    public static int sharedPreferences_integer_default_value = -1;
    public static float sharedPreferences_float_default_value = -1f;
    public static long sharedPreferences_long_default_value = -1l;

    public static String sharedPreferences_user_info = "user_info";
    public static String sharedPreferences_user_info_id = "user_id";
    public static String sharedPreferences_user_info_role = "user_role";

    public static String create_new_fence_intent_data = "create_new_fence_intent_data";

    public static String patients_lost_intent_data = "patients_lost_intent_data";
}