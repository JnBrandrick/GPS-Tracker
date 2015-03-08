package comteam_chimeragps_tracker.httpsgithub.bigbrother;
/*
        Date:       March 4, 2014
        Designer:   Jeff Bayntun
        Programmer:  Jeff Bayntun

        Description: This Static class handles the interaction with the Android
        Shared Preferences, which is used as a blue colored database.

 */
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jeffrey on 3/3/2015.
 */
public class PreferenceHandler {

    public static final String TRACKING_PREFERENCE = "TRACKING";
    public static final String HOST_PREFERENCE = "HOST";
    public static final String PORT_PREFERENCE = "PORT";


    static final String PreferenceName = "bigbropref";


   static String[] checkPreferences(Context c)
   {
       String[] p = new String[2];
       SharedPreferences sharedpreferences = c.getSharedPreferences(PreferenceName, Context.MODE_WORLD_READABLE);
       p[0] = sharedpreferences.getString(HOST_PREFERENCE, "");
       p[1] = sharedpreferences.getString(PORT_PREFERENCE, "");

       return p;
   }

    static void addPreferences(Context c, String host, String port)
    {
        SharedPreferences sharedpreferences = c.getSharedPreferences(PreferenceName, Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(HOST_PREFERENCE, host);
        editor.putString(PORT_PREFERENCE, port);
        editor.commit();
    }

    static String getPreference(Context c, String key)
    {
        SharedPreferences sharedpreferences = c.getSharedPreferences(PreferenceName, Context.MODE_WORLD_READABLE);
        return sharedpreferences.getString(key, "");
    }

    static void setPreference(Context c, String key, String value)
    {
        SharedPreferences sharedpreferences = c.getSharedPreferences(PreferenceName, Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}