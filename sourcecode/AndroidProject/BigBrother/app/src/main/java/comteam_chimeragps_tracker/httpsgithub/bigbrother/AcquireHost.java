package comteam_chimeragps_tracker.httpsgithub.bigbrother;
/*
        Date:       March 4, 2014
        Activity:   AcquireHost
        Designer:   Jeff Bayntun
        Programmer:  Jeff Bayntun

        Description: This Activity allows the user to input host information.
        When the user submits this information, a connection is attempted.  If
        the connection succeeds, the user proceeds to the Activity Tracking Central.

 */

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

    private final TextWatcher  mTextEditorWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {}

        public void onTextChanged(CharSequence s, int start, int before, int count)
        {}

        public void afterTextChanged(Editable s)
        {
            allFields();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acquire_host);

        String[] pref = PreferenceHandler.checkPreferences(this);

        EditText[] views = new EditText[3];
        views[0] = (EditText)findViewById(R.id.hostEdit);
        views[1] = (EditText)findViewById(R.id.portEdit);
        views[2] = (EditText)findViewById(R.id.userEdit);


        if(pref.length == 3) {
            for(int i = 0; i < 3; i++)
            {
                views[i].setText(pref[i]);
                views[i].setSelection(views[i].getText().length());
                views[i].addTextChangedListener(mTextEditorWatcher);
            }
        }

        allFields();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_acquire_host, menu);
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

    public void clearFields(View view)
    {
        EditText[] views = new EditText[3];
        views[0] = (EditText)findViewById(R.id.hostEdit);
        views[1] = (EditText)findViewById(R.id.portEdit);
        views[2] = (EditText)findViewById(R.id.userEdit);

        for(int i = 0; i < 3; i++)
        {
            views[i].setText("");
        }
    }

    public void submitConnection(View view)
    {
        String user, host, port;
        boolean connected = false;

        if(!allFields())
        {
            Toast.makeText(getApplicationContext(), "You must fill in all values to proceed.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // get username, host and port
        EditText h = (EditText)findViewById(R.id.hostEdit);
        EditText p = (EditText)findViewById(R.id.portEdit);
        EditText u = (EditText)findViewById(R.id.userEdit);

        user = u.getText().toString();
        host = h.getText().toString();
        port = p.getText().toString();

        // add it to shared preferences
        PreferenceHandler.addPreferences(this, host, port, user);

        //try to connect
        connected = true;
        //display error if any
        if(!connected)
        {
            // do a message box
            Toast.makeText(getApplicationContext(), "Error Connecting, verify host information",
                    Toast.LENGTH_LONG).show();
            return;
        }

        //open other view
        Intent myIntent = new Intent(this, TrackingCentral.class);
        startActivity(myIntent);
    }

    boolean allFields()
    {
        Button submit = (Button) findViewById(R.id.submit);
        EditText h = (EditText)findViewById(R.id.hostEdit);
        EditText p = (EditText)findViewById(R.id.portEdit);
        EditText u = (EditText)findViewById(R.id.userEdit);

        if (h.getText().toString().trim().length() > 0
                && p.getText().toString().trim().length() > 0
                && u.getText().toString().trim().length() > 0)
        {
            //set color green
            submit.setBackgroundColor(getResources().getColor(R.color.GoodSubmit));
            return true;
        }
        else
        {
            // set color red
            submit.setBackgroundColor(getResources().getColor(R.color.BadSubmit));
            return false;
        }
    }
}
