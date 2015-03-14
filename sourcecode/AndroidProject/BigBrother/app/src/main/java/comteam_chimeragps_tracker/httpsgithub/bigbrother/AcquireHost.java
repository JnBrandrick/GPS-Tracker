package comteam_chimeragps_tracker.httpsgithub.bigbrother;


/**********************************************************************
 **  SOURCE FILE:  AcquireHost.java -  Java file for the host page
 **
 **  PROGRAM:    Android GPS // Big Brother
 **
 **  FUNCTIONS:
 **             boolean onOptionsItemSelected(MenuItem item)
 **             boolean onCreateOptionsMenu(Menu menu)
 **             void onCreate(Bundle savedInstanceState)
 **             void submitConnection(View view)
 **             boolean allFields()
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
 ** This Activity allows the user to input host information.
 ** When the user submits this information, a connection is attempted.  If
 ** the connection succeeds, the user proceeds to the Activity Tracking Central.
 *************************************************************************/
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AcquireHost extends ActionBarActivity {

    private final TextWatcher  mTextEditorWatcher = new TextWatcher()
    {

        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {}

        public void onTextChanged(CharSequence s, int start, int before, int count)
        {}

        public void afterTextChanged(Editable s)
        {
            allFields();
        }
    };

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
     * Creates the AcquireHost activity, pre-populating the last used host
     *  and port into the GUI
     **************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acquire_host);

        // already connected to a host and tracking, open that activity
        if(PreferenceHandler.getPreference(this, PreferenceHandler.TRACKING_PREFERENCE).equals("true"))
        {
            Intent myIntent = new Intent(this, TrackingCentral.class);
            startActivity(myIntent);
            finish();
        }

        String[] pref = PreferenceHandler.checkPreferences(this);

        EditText[] views = new EditText[2];
        views[0] = (EditText)findViewById(R.id.hostEdit);
        views[1] = (EditText)findViewById(R.id.portEdit);


        if(pref.length == 2) {
            for(int i = 0; i < 2; i++)
            {
                views[i].setText(pref[i]);
                views[i].setSelection(views[i].getText().length());
                views[i].addTextChangedListener(mTextEditorWatcher);
            }
        }

        allFields();
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
        getMenuInflater().inflate(R.menu.menu_acquire_host, menu);
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

    /*
     * Programmer: Jeff Bayntun
     * Designer: Jeff Bayntun
     *
     * Function: clearFields
     *
     *
     * Notes:
     *  Listener for button to clear all input fields in this Activity
     */
    public void clearFields(View view)
    {
        EditText[] views = new EditText[2];
        views[0] = (EditText)findViewById(R.id.hostEdit);
        views[1] = (EditText)findViewById(R.id.portEdit);

        for(int i = 0; i < 2; i++)
        {
            views[i].setText("");
        }
    }


    /*****************************************************************************
     * Function: submitConnection
     * Date March 4, 2015
     * Revision:
     *
     * Designer: Jeff Bayntun
     *
     *Programmer: Jeff Bayntun,  Julian Brandrick
     *
     *Interface: void submitConnection(View view)
     *              View view -- Calling view
     *
     * Returns:
     *          bool -- inflate fail or success
     *
     * Notes:
     * Listener for button submit button.  Validates input, verifies host
     *  is reachable, then opens Tracking Service Activity
     **************************************************************************/
    public void submitConnection(View view)
    {
        ClientConnect clientNet;
        String host, port;

        if(!allFields())
        {
            Helpers.makeToast(this, "Must specify a host and port!", Toast.LENGTH_LONG);
            return;
        }

        // get username, host and port
        EditText h = (EditText)findViewById(R.id.hostEdit);
        EditText p = (EditText)findViewById(R.id.portEdit);

        host = h.getText().toString();
        port = p.getText().toString();

        // add it to shared preferences
        PreferenceHandler.addPreferences(this, host, port);

        clientNet = new ClientConnect(this);
        
        //try to connect
        if(!clientNet.foundServer())
        {
            return;
        }
        
        clientNet.teardown();
        
        //open other view
        Intent myIntent = new Intent(this, TrackingCentral.class);
        startActivity(myIntent);
        finish();
    }




    /*****************************************************************************
     * Function: allFields
     * Date March 4, 2015
     * Revision:
     *
     * Designer: Jeff Bayntun
     *
     *Programmer: Jeff Bayntun,  Julian Brandrick
     *
     *Interface: void allFields()
     *
     * Returns:
     *         boolean -- true if the fields are all set
     *                    false if a field is empty
     *
     * Notes:
     * ensures all fields have been filled in, toggles color of submit button
     **************************************************************************/
    boolean allFields()
    {
        Button submit = (Button) findViewById(R.id.submit);
        EditText h = (EditText)findViewById(R.id.hostEdit);
        EditText p = (EditText)findViewById(R.id.portEdit);

        if (h.getText().toString().trim().length() > 0
                && p.getText().toString().trim().length() > 0)
        {
            //set color green
            submit.setBackgroundResource(R.drawable.blue_button);
            return true;
        }
        else
        {
            // set color red
            submit.setBackgroundResource(R.drawable.red_button);
            return false;
        }
    }

}
