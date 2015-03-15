package comteam_chimeragps_tracker.httpsgithub.bigbrother;
/**********************************************************************
 **  SOURCE FILE:  TrackingService.java -  Java file for the GPS service
 **
 **  PROGRAM:    Android GPS // Big Brother
 **
 **  FUNCTIONS:
 **             void onCreate(Bundle savedInstanceState)
 **             void onProviderDisabled(String provider)
 **             void onProviderEnabled(String provider)
 **             void onStatusChanged(String provider, int status, Bundle extras)
 **             void onLocationChanged(Location location)
 **             void sendLocation (Location l, String time)
 **             void handleMessage(Message msg)
 **             void onDestroy()
 **             IBinder onBind(Intent intent)
 **             int onStartCommand(Intent intent, int flags, int startId)
 **
 **
 **
 **  DATE:      March 4, 2014
 **
 **
 **  DESIGNER:    Jeff Bayntun with example code from developer.android.com
 **
 **
 **  PROGRAMMER: Jeff Bayntun
 **
 **  NOTES:
 ** This Activity Lets a user turn tracking on or off and also has
 lets a user change the server go to the ShowMap Activity or the AcquireHost
 activity.
 *************************************************************************/


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class TrackingService extends Service
{
    private ClientConnect clientNet;
    
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    boolean quit;
    Context context;
    private android.location.LocationListener mListener;
    private LocationManager locationManager;

    private final long INTERVAL = 3 * 60 * 1000; // atleast 3 min intervals
    private final float DISTANCE = 500; // atleast 500m distance

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
     *  creates the tracking service, starts a thread to handle location finding.
     **************************************************************************/
    @Override
    public void onCreate()
    {
        context = getBaseContext();
        //Create thread for the service
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        PreferenceHandler.setPreference(this, PreferenceHandler.TRACKING_PREFERENCE, "true");

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        clientNet = new ClientConnect(this.getApplicationContext());

        clientNet.foundServer();
    }


    /*****************************************************************************
     * Function: onStartCommand
     * Date March 4, 2015
     * Revision:
     *
     * Designer: Jeff Bayntun
     *
     *Programmer: Jeff Bayntun
     *
     *Interface: int onStartCommand(Intent intent, int flags, int startId)
     *             Intent intent -- Intent from the starter
     *             int flags -- Flags for the service
     *             int startId -- Which request we're stopping when we finish
     *Returns:
     *         int -- value for restarting
     *
     * Notes:
     *  starts the service when issued the command.
     **************************************************************************/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Helpers.serviceToast(this, "service starting", Toast.LENGTH_SHORT);

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }


    /*****************************************************************************
     * Function: onBind
     * Date March 4, 2015
     * Revision:
     *
     * Designer: Jeff Bayntun
     *
     *Programmer: Jeff Bayntun
     *
     *Interface: IBinder onBind(Intent intent)
     *              Intent intent -- Intent to bind on
     *Returns:
     *         IBinder -- null in this scenario (overriding purposes)
     *
     * Notes:
     *  Mandatory for services. We are not using this.
     **************************************************************************/
    @Override
    public IBinder onBind(Intent intent)
    {
        // We don't provide binding, so return null
        return null;
    }


    /*****************************************************************************
     * Function: onDestroy
     * Date March 4, 2015
     * Revision:
     *
     * Designer: Jeff Bayntun
     *
     *Programmer: Jeff Bayntun
     *
     *Interface: void onDestroy()
     *Returns:
     *         void
     *
     * Notes:
     *  destroys service. stops thread and unregisters from the location manager.
     **************************************************************************/
    @Override
    public void onDestroy() {
        quit = true;
        locationManager.removeUpdates(mListener);
        mServiceLooper.quit();
        Helpers.serviceToast(this, "service done", Toast.LENGTH_SHORT);
        PreferenceHandler.setPreference(this, PreferenceHandler.TRACKING_PREFERENCE, "false");

        clientNet.teardown();
        
        super.onDestroy();
    }



    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler
    {
        private Location mLocation;
        private Criteria mCriteria;
        private String mProvider;

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        /***********************************************************************
         * Function: handleMessage
         * Date March 4, 2015
         * Revision:
         *
         * Designer: Jeff Bayntun
         *
         *Programmer: Jeff Bayntun
         *
         *Interface: void handleMessage(Message msg)
         *              Message msg -- Message received from the service
         *
         *Returns:
         *         void
         *
         * Notes:
         *  creates location manager and listener for location changes.
         *  whenever location listener is invoked, sendLocation is called
         **************************************************************************/
        @Override
        public void handleMessage(Message msg)
        {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            mListener = new MyLocationListener();
            mCriteria = new Criteria();
            mCriteria.setAccuracy(Criteria.ACCURACY_MEDIUM );
            mProvider = locationManager.getBestProvider(mCriteria, false);
            mLocation = locationManager.getLastKnownLocation(mProvider);
            locationManager.requestLocationUpdates(mProvider, INTERVAL, DISTANCE, mListener);

            if (mLocation != null)
            {
              mListener.onLocationChanged(mLocation);
            }
        }

        /***********************************************************************
         * Function: sendLocation
         * Date March 4, 2015
         * Revision:
         *
         * Designer: Jeff Bayntun
         *
         *Programmer: Jeff Bayntun
         *
         *Interface: void sendLocation (Location l, String time)
         *              location l -- location data
         *              String time -- time data
         *
         *Returns:
         *         void
         *
         * Notes:
         * Sends the location to the the client
         ***************************************************************************/
        private void sendLocation (Location l, String time)
        {
            Log.d("Latitude", String.valueOf(l.getLatitude()) );
            Log.d("Longitude", String.valueOf(l.getLongitude()) );

            clientNet.setPacketData(time, String.valueOf(l.getLatitude()), String.valueOf(l.getLongitude()));

            new Thread(clientNet).start();
        }

        private class MyLocationListener implements android.location.LocationListener
        {
            Long time;
            Date myDate;

            /***********************************************************************
             * Function: onLocationChanged
             * Date March 4, 2015
             * Revision:
             *
             * Designer: Jeff Bayntun
             *
             *Programmer: Jeff Bayntun & Rhea Lauzon
             *
             *Interface: void onLocationChanged(Location location)
             *             Location location -- new location
             *
             *Returns:
             *         void
             *
             * Notes:
             *  Gets current location, calls sendLocations.
             **************************************************************************/
            @Override
            public void onLocationChanged(Location location) {
                // Initialize the location fields
                mLocation = location;
                time = location.getTime();

                //Retrieve current time
                Calendar cal = Calendar.getInstance();

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                dateFormat.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));

                sendLocation(mLocation, dateFormat.format(cal.getTime()));

            }


            /***********************************************************************
             * Function: onStatusChanged
             * Date March 4, 2015
             * Revision:
             *
             * Designer: Jeff Bayntun
             *
             *Programmer: Jeff Bayntun & Rhea Lauzon
             *
             *Interface: void onStatusChanged(String provider, int status, Bundle extras)
             *           String provider -- The provider that has changed
             *           int status -- new status
             *           Bundle extras -- extras with the status changed
             *
             *Returns:
             *         void
             *
             * Notes:
             *  Occurs when the status of the service has changed
             **************************************************************************/
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {
                Log.d(provider, ""+ status);
            }


            /***********************************************************************
             * Function: onProviderEnabled
             * Date March 4, 2015
             * Revision:
             *
             * Designer: Jeff Bayntun
             *
             *Programmer: Jeff Bayntun
             *
             *Interface: void onProviderEnabled(String provider)
             *           String provider -- The provider that is enabled
             *
             *Returns:
             *         void
             *
             * Notes:
             *  Occurs when the provider becomes enabled
             **************************************************************************/
            @Override
            public void onProviderEnabled(String provider)
            {
                Log.d(provider, "enabled");
            }


            /***********************************************************************
             * Function: onProviderDisabled
             * Date March 4, 2015
             * Revision:
             *
             * Designer: Jeff Bayntun
             *
             *Programmer: Jeff Bayntun
             *
             *Interface: void onProviderDisabled(String provider)
             *           String provider -- The provider that is now enabled
             *
             *Returns:
             *         void
             *
             * Notes:
             *  Occurs when the provider becomes disabled
             **************************************************************************/
            @Override
            public void onProviderDisabled(String provider)
            {
                Log.d(provider, "disabled");
            }
        }
    }

}

