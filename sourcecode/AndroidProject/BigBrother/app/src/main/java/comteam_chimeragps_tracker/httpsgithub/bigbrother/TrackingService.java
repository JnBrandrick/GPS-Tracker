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
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;


public class TrackingService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    boolean quit;
    Context context;

    private final long FASTEST_INTERVAL = 5 * 1000;
    private final long NORMAL_INTERVAL = 10 * 1000;

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

    @Override
    public void onDestroy() {
        quit = true;
        Helpers.serviceToast(this, "service done", Toast.LENGTH_SHORT);
        PreferenceHandler.setPreference(this, PreferenceHandler.TRACKING_PREFERENCE, "false");

        super.onDestroy();
    }



    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler
            implements  GoogleApiClient.ConnectionCallbacks,
                        GoogleApiClient.OnConnectionFailedListener,
            LocationListener {

        private GoogleApiClient mGoogleApiClient;
        private Location mLastLocation;
        private Location mCurrentLocation;
        private String mLastUpdateTime;
        private LocationRequest mLocationRequest;

        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg)
        {

            buildGoogleApiClient();
            createLocationRequest();

            Log.d("buildApi", "aaaaaaaaaaaaaaaaaaaa");
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            int count = 0;
            while (!quit)
            {
                Log.d("hello ", " " + (++count));

                synchronized (this)
                {

                    try
                    {
                        wait(3*1000);
                    }
                    catch (Exception e)
                    {
                        Log.d("Tracking Service Error", " Problems");
                        stopSelf(msg.arg1);

                    }

                }

            }
        }

        protected void createLocationRequest() {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(NORMAL_INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        }

        protected synchronized void buildGoogleApiClient()
        {
            Log.d("buildApi", "xxxxxxxxxxxxxxxxxx");
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        private void sendLocation (Location l, String time)
        {
            Log.d("Latitude", String.valueOf(l.getLatitude()) );
            Log.d("Longitude", String.valueOf(l.getLongitude()) );
        }

        @Override
        public void onLocationChanged(Location location) {
            mCurrentLocation = location;
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            sendLocation(location, mLastUpdateTime);
        }


        @Override
        public void onConnected(Bundle connectionHint)
        {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                Log.d("Latitude", String.valueOf(mLastLocation.getLatitude()) );
                Log.d("Longitude", String.valueOf(mLastLocation.getLongitude()) );
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

        }

        @Override
        public void onConnectionSuspended(int cause)
        {

        }

        @Override
        public void onConnectionFailed(ConnectionResult result)
        {

        }
    }

}

