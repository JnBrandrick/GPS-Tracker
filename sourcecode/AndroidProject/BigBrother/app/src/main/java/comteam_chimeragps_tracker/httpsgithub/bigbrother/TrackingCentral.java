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
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class TrackingCentral extends ActionBarActivity {

    static boolean tracking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_central);

        // need to check if tracking or not so toggle tracking button can be done
        // might want to do that in AcquireHost to see what app to load...
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

    public void toggleTracking(View view)
    {
        Toast.makeText(getApplicationContext(), "Toggle button pressed.",
                Toast.LENGTH_LONG).show();
        //see if currently tracking

        // start or stop tracking

        // update button
    }

    public void changeServer(View view)
    {
        // go back to AcquireHost activity
        Intent myIntent = new Intent(this,AcquireHost.class);
        startActivity(myIntent);
    }

    public void showMine(View view)
    {
        // open in-app browser activity to show this users results
        Intent myIntent = new Intent(this,ShowMap.class);
        myIntent.putExtra("all", false);
        startActivity(myIntent);
    }

    public void showAll(View view)
    {
        // open in-app browser activity to show all users results
        Intent myIntent = new Intent(this,ShowMap.class);
        myIntent.putExtra("all", true);
        startActivity(myIntent);
    }
}
