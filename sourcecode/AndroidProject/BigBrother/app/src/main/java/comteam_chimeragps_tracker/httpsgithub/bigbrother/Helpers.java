package comteam_chimeragps_tracker.httpsgithub.bigbrother;

/**********************************************************************
 **  SOURCE FILE:  Helpers.java -  Java file for helpers
 **
 **  PROGRAM:    Android GPS // Big Brother
 **
 **  FUNCTIONS:
 **             void serviceToast(Context c, String text, int duration)
 **              void makeToast(Activity c, String text, int duration)
 **
 **  DATE:      March 5, 2014
 **
 **
 **  DESIGNER:    Jeff Bayntun
 **
 **
 **  PROGRAMMER: Jeff Bayntun
 **
 **  NOTES:
 **  Helper functions
 *************************************************************************/

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jeffrey on 3/5/2015.
 */
public class Helpers {

    /*****************************************************************************
     * Function: makeToast
     * Date March 5, 2015
     * Revision:
     *
     * Designer: Jeff Bayntun
     *
     *Programmer: Jeff Bayntun
     *
     *Interface: void makeToast(Activity c, String text, int duration)
     *              Activity c -- Current activity
     *              String text -- toast text
     *              int duration -- duration of the toast
     *
     *Returns:
     *         void
     *
     * Notes:
     *  Creates a fancy custom toast
     **************************************************************************/
    public static void makeToast(Activity c, String text, int duration)
    {
        LayoutInflater inflater = LayoutInflater.from(c);
        View customToastroot = inflater.inflate(R.layout.custom_toast, null);
        Toast customtoast=new Toast(c);

        TextView t = (TextView)customToastroot.findViewById(R.id.toastView);
        t.setText(text);

        customtoast.setView(customToastroot);
        customtoast.setGravity(Gravity.CENTER,0, 0);
        customtoast.setDuration(duration);
        customtoast.show();
    }

    /*****************************************************************************
     * Function: serviceToast
     * Date March 5, 2015
     * Revision:
     *
     * Designer: Jeff Bayntun
     *
     *Programmer: Jeff Bayntun
     *
     *Interface: void serviceToast(Context c, String text, int duration)
     *              Activity c -- Current activity
     *              String text -- toast text
     *              int duration -- duration of the toast
     *
     *Returns:
     *         void
     *
     * Notes:
     *  Creates a toast on the screen.
     **************************************************************************/
    public static void serviceToast(Context c, String text, int duration)
    {
        Toast t = Toast.makeText(c, text, duration);
        t.setGravity(Gravity.CENTER,0, 0);
        t.show();
    }
}
