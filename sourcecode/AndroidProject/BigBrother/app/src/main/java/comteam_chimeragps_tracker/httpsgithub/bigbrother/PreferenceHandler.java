package comteam_chimeragps_tracker.httpsgithub.bigbrother;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jeffrey on 3/3/2015.
 */
public class PreferenceHandler {

    static final String PreferenceName = "bigbropref";
    static final String HOST = "HOST";
    static final String PORT = "PORT";
    static final String USER = "USER";

   static String[] checkPreferences(Context c)
   {
       String[] p = new String[3];
       SharedPreferences sharedpreferences = c.getSharedPreferences(PreferenceName, Context.MODE_WORLD_READABLE);
       p[0] = sharedpreferences.getString(HOST, "");
       p[0] = sharedpreferences.getString(PORT, "");
       p[0] = sharedpreferences.getString(USER, "");
       
       return p;
   }

    static void addPreferences(Context c, String host, String port, String user)
    {
        SharedPreferences sharedpreferences = c.getSharedPreferences(PreferenceName, Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(HOST, host);
        editor.putString(PORT, port);
        editor.putString(USER, user);
        editor.commit();
    }
}
