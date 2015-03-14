package comteam_chimeragps_tracker.httpsgithub.bigbrother;

/***********************************************************************
 **  SOURCE FILE: PreferenceHandler.java -  Java file for helpers
 **
 **  PROGRAM:    Android GPS // Big Brother
 **
 **  FUNCTIONS:
 **            String[] checkPreferences(Context c)
 **            void addPreferences(Context c, String host, String port)
 **            String getPreference(Context c, String key)
 **            void setPreference(Context c, String key, String value)
 **
 **
 **
 **
 **
 **  DATE:      March 4, 2014
 **
 **
 **  DESIGNER:    Jeff Bayntun
 **
 **
 **  PROGRAMMER: Jeff Bayntun
 **
 **  NOTES:
 ** This Static class handles the interaction with the Android
 Shared Preferences, which is used as a blue colored database.
 ***************************************************************************/

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jeffrey on 3/3/2015.
 */
public class PreferenceHandler
{

    public static final String TRACKING_PREFERENCE = "TRACKING";
    public static final String HOST_PREFERENCE = "HOST";
    public static final String PORT_PREFERENCE = "PORT";


    static final String PreferenceName = "bigbropref";


    /*****************************************************************************
     * Function: onCreate
     * Date March 4, 2015
     * Revision:
     *
     * Designer: Jeff Bayntun
     *
     *Programmer: Jeff Bayntun
     *
     *Interface: String[] checkPreferences(Context c)
     *             Context c -- Current app context
     *Returns:
     *         String[] -- Array of shared preferences
     *
     * Notes:
     * checks for current host and port preferences, returns array with these values
     **************************************************************************/
   static String[] checkPreferences(Context c)
   {
       String[] p = new String[2];
       SharedPreferences sharedpreferences = c.getSharedPreferences(PreferenceName, Context.MODE_WORLD_READABLE);
       p[0] = sharedpreferences.getString(HOST_PREFERENCE, "");
       p[1] = sharedpreferences.getString(PORT_PREFERENCE, "");

       return p;
   }


    /*****************************************************************************
     * Function: addPreferences
     * Date March 4, 2015
     * Revision:
     *
     * Designer: Jeff Bayntun
     *
     *Programmer: Jeff Bayntun
     *
     *Interface: void addPreferences(Context c, String host, String port)
     *             Context c -- Current app context
     *             String host -- host name
     *             String port -- port name
     *Returns:
     *         void
     *
     * Notes:
     * adds given host and port preferences to the shared preferences file
     **************************************************************************/
    static void addPreferences(Context c, String host, String port)
    {
        SharedPreferences sharedpreferences = c.getSharedPreferences(PreferenceName, Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(HOST_PREFERENCE, host);
        editor.putString(PORT_PREFERENCE, port);
        editor.commit();
    }


    /*****************************************************************************
     * Function: addPreferences
     * Date March 4, 2015
     * Revision:
     *
     * Designer: Jeff Bayntun
     *
     *Programmer: Jeff Bayntun
     *
     *Interface: String getPreference(Context c, String key)
     *             Context c -- Current app context
     *             String key -- Shared preference key
     *Returns:
     *        String -- Preferences
     *
     * Notes:
     * returns preference for this key, or "" if no preference found
     **************************************************************************/
    static String getPreference(Context c, String key)
    {
        SharedPreferences sharedpreferences = c.getSharedPreferences(PreferenceName, Context.MODE_WORLD_READABLE);
        return sharedpreferences.getString(key, "");
    }



    /*****************************************************************************
     * Function: addPreferences
     * Date March 4, 2015
     * Revision:
     *
     * Designer: Jeff Bayntun
     *
     *Programmer: Jeff Bayntun
     *
     *Interface: void setPreference(Context c, String key, String value))
     *             Context c -- Current app context
     *             String key -- Shared preference key
     *             string value -- Value to set in the preference
     *Returns:
     *        String -- Preferences
     *
     * Notes:
     * Sets preference for this key
     **************************************************************************/
    static void setPreference(Context c, String key, String value)
    {
        SharedPreferences sharedpreferences = c.getSharedPreferences(PreferenceName, Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
