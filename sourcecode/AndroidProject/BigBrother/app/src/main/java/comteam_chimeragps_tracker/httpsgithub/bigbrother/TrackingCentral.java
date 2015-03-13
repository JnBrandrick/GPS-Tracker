package comteam_chimeragps_tracker.httpsgithub.bigbrother;
/*
        Date:       March 4, 2014
        Activity:   Tracking Central
        Designer:   Jeff Bayntun
        Programmer:  Jeff Bayntun

        Description: This Activity Lets a user turn tracking on or off and also has
        lets a user change the server go to the ShowMap Activity or the AcquireHost
        activity.

 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class TrackingCentral extends ActionBarActivity {

    boolean tracking;
    private final String START_TRACKING = "Start Tracking";
    private final String STOP_TRACKING = "Stop Tracking";
    /*
    * Programmer: Jeff Bayntun
    * Designer: Jeff Bayntun
    *
    * Function: onCreate
    *
    *
    * Notes:
    *  creates tracking activity
    */
    @Override
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tracking_central, menu);
        return true;
    }

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

    /*
    * Programmer: Jeff Bayntun
    * Designer: Jeff Bayntun
    *
    * Function: toggleTracking
    *
    *
    * Notes:
    *  starts or stops tracking service
    */
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

    /*
    * Programmer: Jeff Bayntun
    * Designer: Jeff Bayntun
    *
    * Function: changeServer
    *
    *
    * Notes:
    *  opens AcquireHost activity
    */
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

    /*
    * Programmer: Jeff Bayntun
    * Designer: Jeff Bayntun
    *
    * Function: showMine
    *
    *
    * Notes:
    *  loads Page for this users history
    */
    public void showMine(View view)
    {
        // open in-app browser activity to show this users results
        Intent myIntent = new Intent(this,ShowMap.class);
        myIntent.putExtra("all", false);
        startActivity(myIntent);
    }

    /*
    * Programmer: Jeff Bayntun
    * Designer: Jeff Bayntun
    *
    * Function: showMine
    *
    *
    * Notes:
    *  loads Page all histories
    */
    public void showAll(View view)
    {
        // open in-app browser activity to show all users results
        Intent myIntent = new Intent(this,ShowMap.class);
        myIntent.putExtra("all", true);
        startActivity(myIntent);
    }
}
