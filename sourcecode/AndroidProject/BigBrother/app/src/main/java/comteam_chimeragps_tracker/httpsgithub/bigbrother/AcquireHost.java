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

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AcquireHost extends ActionBarActivity {
    private ClientConnect clientNet;

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
        EditText[] views = new EditText[2];
        views[0] = (EditText)findViewById(R.id.hostEdit);
        views[1] = (EditText)findViewById(R.id.portEdit);

        for(int i = 0; i < 2; i++)
        {
            views[i].setText("");
        }
    }

    public void submitConnection(View view)
    {
        String host, port;
        boolean connected = false;

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

        //try to connect
        //connected = true;
        
        if(!foundServer())
        {
            return;
        }
        
        //display error if any
        //if(!connected)
        //{
            // do a message box
        //    Helpers.makeToast(this, "Error Connecting, verify host information", Toast.LENGTH_LONG);
        //    return;
        //}

        //open other view
        Intent myIntent = new Intent(this, TrackingCentral.class);
        startActivity(myIntent);
        finish();
    }

    boolean allFields()
    {
        Button submit = (Button) findViewById(R.id.submit);
        EditText h = (EditText)findViewById(R.id.hostEdit);
        EditText p = (EditText)findViewById(R.id.portEdit);

        if (h.getText().toString().trim().length() > 0
                && p.getText().toString().trim().length() > 0)
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
    
    public boolean foundServer()
    {
        try
        {
            String result = new AsyncLookup().execute(this).get(500, TimeUnit.MILLISECONDS);
            
            return result.equals("");
        }
        catch(CancellationException e)
        {
            // Task was cancelled
            return false;
        }
        catch(ExecutionException e)
        {
            // Exception was thrown inside task
            return false;
        }
        catch(InterruptedException e)
        {
            // Waiting thread has interrupted the task
            return false;
        }
        catch(TimeoutException e)
        {
            // Timeout has expired
            return false;
        }
    }
    
    private class AsyncLookup extends AsyncTask<Context, Void, String>
    {

        @Override
        protected String doInBackground(Context... params) {
            try
            {
                clientNet = new ClientConnect(params[0]);
            }
            catch(SocketException e)
            {
                return "Error Connecting, could not connect to socket";
            }
            catch(UnknownHostException e)
            {
                return "Error Connecting, could not resolve host name";
            }
            catch(NumberFormatException e)
            {
                return "Port must be a number";
            }
            
            return "";
        }
        
        @Override
        protected void onPostExecute(String result)
        {
            if(!result.equals(""))
            {
                Helpers.makeToast(AcquireHost.this, result, Toast.LENGTH_LONG);
            }
        }
    }
}