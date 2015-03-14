package comteam_chimeragps_tracker.httpsgithub.bigbrother;
/*
        Date:       March 4, 2014
        Activity:   Tracking Central
        Designer:   Jeff Bayntun with example code from developer.android.com
        Programmer:  Jeff Bayntun

        Description: This Activity Lets a user turn tracking on or off and also has
        lets a user change the server go to the ShowMap Activity or the AcquireHost
        activity.

 */

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


public class TrackingService extends Service {
    private ClientConnect clientNet;
    
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    boolean quit;
    Context context;
    private android.location.LocationListener mListener;
    private LocationManager locationManager;

    private final long INTERVAL = 15 * 60 * 1000; // every 15 minutes
    private final float DISTANCE = 500; // every 500m
    /*
    * Programmer: Jeff Bayntun
    * Designer: Jeff Bayntun
    *
    * Function: onCreate
    *
    *
    * Notes:
    *  creates the tracking service, starts a thread to handle location finding.
    *
    */
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

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    /*
    * Programmer: Jeff Bayntun
    * Designer: Jeff Bayntun
    *
    * Function: onDestroy
    *
    *
    * Notes:
    *  destroys service. stops thread and unregisters from the location manager.
    *
    */
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
        /*
    * Programmer: Jeff Bayntun
    * Designer: Jeff Bayntun
    *
    * Function: handleMessage
    *
    *
    * Notes:
    *  creates location manager and listener for location changes.
    *  whenever location listener is invoked, sendLocation is called
    *
    */
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

        // Send goes here
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
            @Override
            /*
            * Programmer: Jeff Bayntun & Rhea Lauzon
            * Designer: Jeff Bayntun
            *
            * Function: onLocationChanged
            *
            *
            * Notes:
            *  gets current location, calls sendLocation
            *
            */
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

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {
                Log.d(provider, ""+ status);
            }

            @Override
            public void onProviderEnabled(String provider)
            {
                Log.d(provider, "enabled");
            }

            @Override
            public void onProviderDisabled(String provider)
            {
                Log.d(provider, "disabled");
            }
        }
    }

}

