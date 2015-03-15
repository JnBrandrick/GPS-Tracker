package comteam_chimeragps_tracker.httpsgithub.bigbrother;

/**********************************************************************
 **  SOURCE FILE:  TrackingCentral.java -  Java file for the central tracking
 **
 **  PROGRAM:    Android GPS // Big Brother
 **
 **  FUNCTIONS:
 **             boolean onOptionsItemSelected(MenuItem item)
 **             boolean onCreateOptionsMenu(Menu menu)
 **             void onCreate(Bundle savedInstanceState)
 **             void showAll(View view)
 **             void toggleTracking(View view)
 **             void changeServer(View view)
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
 ** This Activity Lets a user turn tracking on or off and also has
 lets a user change the server go to the ShowMap Activity or the AcquireHost
 activity.
 *************************************************************************/

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class TrackingCentral extends ActionBarActivity
{

    boolean tracking;
    private final String START_TRACKING = "Start Tracking";
    private final String STOP_TRACKING = "Stop Tracking";

    /*****************************************************************************
     * Function: onCreate
     * Date March 4, 2015
     * Revision:
     *
     * Designer: Jeff Bayntun
     *
     *Programmer: Jeff Bayntun
     *
     *Interface: void onCreate(Bundle savedInstanceState)
     *              Bundle savedInstanceState -- state before last close
     *Returns:
     *         void
     *
     * Notes:
     * Creates the tracking activity
     **************************************************************************/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_central);

        // need to check if tracking or not so toggle tracking button can be done
        // might want to do that in AcquireHost to see what app to load...
        tracking = (PreferenceHandler.getPreference(this, PreferenceHandler.TRACKING_PREFERENCE).equals("true"));

        Button toggle = (Button)findViewById(R.id.toggleTracking);
        String t = (tracking) ? STOP_TRACKING:START_TRACKING;
        toggle.setText(t);
    }


    /*****************************************************************************
     * Function: onCreateOptionsMenu
     * Date March 4, 2015
     * Revision:
     *
     * Designer: Jeff Bayntun
     *
     *Programmer: Jeff Bayntun
     *
     *Interface: boolean onCreateOptionsMenu(Menu menu)
     *              Menu menu -- Menu bar
     *
     * Returns:
     *          bool -- inflate fail or success
     *
     * Notes:
     *  Creates the options menu bar
     **************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tracking_central, menu);
        return true;
    }


    /*****************************************************************************
     * Function: onOptionsItemSelected
     * Date March 4, 2015
     * Revision:
     *
     * Designer: Jeff Bayntun
     *
     *Programmer: Jeff Bayntun
     *
     *Interface: boolean  onOptionsItemSelected(MenuItem item)
     *              MenuItem item -- Item selected in the menu
     *
     * Returns:
     *          bool -- inflate fail or success
     *
     * Notes:
     *  onClick method for any of the menu bar items
     **************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /*****************************************************************************
     * Function: toggleTracking
     * Date March 4, 2015
     * Revision:
     *
     * Designer: Jeff Bayntun
     *
     *Programmer: Jeff Bayntun
     *
     *Interface: void toggleTracking(View v)
     *          View view -- calling view
     *
     * Returns:
     *          void
     *
     * Notes:
     * starts or stops tracking service
     **************************************************************************/
    public void toggleTracking(View view)
    {
        // start or stop tracking
        if(tracking)
        {
            //stop service
            Intent i= new Intent(this, TrackingService.class);
            stopService(i);
            PreferenceHandler.setPreference(this, PreferenceHandler.TRACKING_PREFERENCE, "false");
        }
        else
        {
            //start service
            Intent i= new Intent(this, TrackingService.class);
            PreferenceHandler.setPreference(this, PreferenceHandler.TRACKING_PREFERENCE, "true");
            startService(i);
        }

        // update button
        tracking = !tracking;
        Button toggle = (Button)findViewById(R.id.toggleTracking);
        String t = (tracking) ? STOP_TRACKING:START_TRACKING;
        toggle.setText(t);
    }


    /*****************************************************************************
     * Function: changeServer
     * Date March 4, 2015
     * Revision:
     *
     * Designer: Jeff Bayntun
     *
     *Programmer: Jeff Bayntun
     *
     *Interface: void changeServer(View v)
     *          View view -- calling view
     *
     * Returns:
     *          void
     *
     * Notes:
     * opens AcquireHost activity
     **************************************************************************/
    public void changeServer(View view)
    {
        // go back to AcquireHost activity
        Intent i= new Intent(this, TrackingService.class);
        stopService(i);
        PreferenceHandler.setPreference(this, PreferenceHandler.TRACKING_PREFERENCE, "false");

        Intent myIntent = new Intent(this,AcquireHost.class);
        startActivity(myIntent);
        finish();
    }


    /*****************************************************************************
     * Function: showAll
     * Date March 4, 2015
     * Revision:
     *
     * Designer: Jeff Bayntun
     *
     *Programmer: Jeff Bayntun
     *
     *Interface: void showAll(View view)
     *          View view -- calling view
     *
     * Returns:
     *          void
     *
     * Notes:
     * Load the website
     **************************************************************************/
    public void showAll(View view)
    {
        // open in-app browser activity to show all users results
      //  Intent myIntent = new Intent(this,ShowMap.class);
      //  startActivity(myIntent);

        String url = "http://" + PreferenceHandler.getPreference(this, PreferenceHandler.HOST_PREFERENCE);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
