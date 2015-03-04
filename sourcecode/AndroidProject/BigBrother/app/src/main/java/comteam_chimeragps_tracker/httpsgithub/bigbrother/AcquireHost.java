package comteam_chimeragps_tracker.httpsgithub.bigbrother;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AcquireHost extends ActionBarActivity {

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
            }
        }

        setFocusListeners(views);

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

    public void submitConnection(View view)
    {
        String user, host, port;
        boolean connected = false;
        Toast.makeText(getApplicationContext(), "Dingle",
                Toast.LENGTH_LONG).show();

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
        Toast.makeText(getApplicationContext(), "Go to new activity here",
                Toast.LENGTH_LONG).show();


    }

    void setFocusListeners(EditText[] views)
    {
        for(EditText e: views)
        {
            e.setOnFocusChangeListener(new View.OnFocusChangeListener()
            {

                @Override
                public void onFocusChange(View v, boolean hasFocus)
                {

                    if (hasFocus)
                    {
                        return;
                    }

                    Button submit = (Button) findViewById(R.id.submit);
                    EditText h = (EditText)findViewById(R.id.hostEdit);
                    EditText p = (EditText)findViewById(R.id.portEdit);
                    EditText u = (EditText)findViewById(R.id.userEdit);

                    if (h.getText().toString().trim().length() > 0
                            && p.getText().toString().trim().length() > 0
                            && u.getText().toString().trim().length() > 0)
                    {
                        //enable submit button, set color green
                        submit.setEnabled(true);
                        submit.setBackgroundColor(getResources().getColor(R.color.Chartreuse));
                    }
                    else
                    {
                        // disable submit, set color red
                        submit.setEnabled(false);
                        submit.setBackgroundColor(getResources().getColor(R.color.win8_red));
                    }
                }
            });
        }
    }
}
