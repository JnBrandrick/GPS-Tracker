package comteam_chimeragps_tracker.httpsgithub.bigbrother;
/*
        Date:       March 4, 2014
        Activity:   ShowMap
        Designer:   Jeff Bayntun
        Programmer:  Jeff Bayntun

        Description: This Activity Loads a web page to show either this users
        progress or all users progress.

 */


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;


public class ShowMap extends ActionBarActivity {

    private WebView web = null;
    private boolean all = false;

    private String ALL_PAGE;
    private String MY_PAGE;
    private final String MY_HISTORY = "View My History";
    private final String ALL_HISTORY = "View All History";

    /*
     * Programmer: Jeff Bayntun
     * Designer: Jeff Bayntun
     *
     * Function: onCreate
     *
     *
     * Notes:
     *  opens browser in this activity, loading page based on which button
     * was selected (boolean in intent)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);

        ALL_PAGE = "http://" + PreferenceHandler.getPreference(this, PreferenceHandler.HOST_PREFERENCE);
        MY_PAGE = ALL_PAGE + "/" + "myid";

        // get bundle with info for correct webview
        Intent intent = getIntent();
        all = intent.getExtras().getBoolean("all");
        String url = (all) ? ALL_PAGE: MY_PAGE;

        // set button
        String text = (all) ? MY_HISTORY: ALL_HISTORY;
        Button button = (Button)findViewById(R.id.switchMap);
        button.setText(text);

        //open that webview
        web = (WebView) findViewById(R.id.webView);
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new MyBrowser());
        open(url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_map, menu);
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
    * Function: openTracking
    *
    *
    * Notes:
    *  return to tracking activity
    */
    public void openTracking(View view)
    {
        // open in-app browser activity to show this users results
        Intent myIntent = new Intent(this,TrackingCentral.class);
        startActivity(myIntent);
    }

    /*
    * Programmer: Jeff Bayntun
    * Designer: Jeff Bayntun
    *
    * Function: onCreate
    *
    *
    * Notes:
    *  toggles website displayed
    */
    public void switchMap(View view)
    {
        all = !all;

        String text = (all) ? MY_HISTORY: ALL_HISTORY;
        Button button = (Button)findViewById(R.id.switchMap);
        button.setText(text);

        String url = (all) ? ALL_PAGE: MY_PAGE;
        open(url);
    }

    public void open(String url) {
        web.getSettings().setLoadsImagesAutomatically(true);
        web.getSettings().setJavaScriptEnabled(true);
        // web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        web.loadUrl(url);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
