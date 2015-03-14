package comteam_chimeragps_tracker.httpsgithub.bigbrother;
/**********************************************************************
 **  SOURCE FILE:  ShowMap.java -  Java file for the map screen
 **
 **  PROGRAM:    Android GPS // Big Brother
 **
 **  FUNCTIONS:
 **             void open(String url)
 **             boolean shouldOverrideUrlLoading(WebView view, String url)
 **             void openTracking(View view)
 **             boolean onOptionsItemSelected(MenuItem item)
 **             boolean onCreateOptionsMenu(Menu menu)
 **             void onCreate(Bundle savedInstanceState)
 **             boolean shouldOverrideUrlLoading(WebView view, String url)
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
 **  This Activity Loads a web page to show either this users
 progress or all users progress.
 *************************************************************************/


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class ShowMap extends ActionBarActivity {

    private WebView web = null;
    private boolean all = false;

    private String ALL_PAGE;
    private String MY_PAGE;
    private final String MY_HISTORY = "View My History";
    private final String ALL_HISTORY = "View All History";

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
     *  opens browser in this activity, loading page based on which button
     * was selected (boolean in intent)
     **************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);

        ALL_PAGE = "http://" + PreferenceHandler.getPreference(this, PreferenceHandler.HOST_PREFERENCE);
        MY_PAGE = ALL_PAGE + "/" + "chimera.datacom.me";

        // get bundle with info for correct webview
        Intent intent = getIntent();
        all = intent.getExtras().getBoolean("all");
        String url = (all) ? ALL_PAGE: MY_PAGE;


        //open that webview
        web = (WebView) findViewById(R.id.webView);
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new MyBrowser());
        open(url);
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
        getMenuInflater().inflate(R.menu.menu_show_map, menu);
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
     * Function: openTracking
     * Date March 4, 2015
     * Revision:
     *
     * Designer: Jeff Bayntun
     *
     *Programmer: Jeff Bayntun
     *
     *Interface:  void openTracking(View view)
     *             View view -- Calling view
     *
     * Returns:
     *          void
     *
     * Notes:
     *  onClick method for any of the menu bar items
     **************************************************************************/
    public void openTracking(View view)
    {
        // open in-app browser activity to show this users results
        Intent myIntent = new Intent(this,TrackingCentral.class);
        startActivity(myIntent);
    }


    /*****************************************************************************
     * Function: open
     * Date March 4, 2015
     * Revision:
     *
     * Designer: Jeff Bayntun
     *
     *Programmer: Jeff Bayntun
     *
     *Interface:  void open(String url)
     *             String url -- website URL
     *
     * Returns:
     *          void
     *
     * Notes:
     *  opens the specified webpage in the web view
     **************************************************************************/
    public void open(String url) {
        web.getSettings().setLoadsImagesAutomatically(true);
        web.getSettings().setJavaScriptEnabled(true);
        // web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        web.loadUrl(url);
    }


    private class MyBrowser extends WebViewClient
    {

        /*****************************************************************************
         * Function: shouldOverrideUrlLoading
         * Date March 4, 2015
         * Revision:
         *
         * Designer: Jeff Bayntun
         *
         *Programmer: Jeff Bayntun
         *
         *Interface:  boolean shouldOverrideUrlLoading(WebView view, String url)
         *             WebView view -- Webview to load a page
         *             String url -- URL to laod in the view
         *
         * Returns:
         *          boolean -- True or false on the loading
         *
         * Notes:
         *  Opens the URL in the webview
         **************************************************************************/
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
