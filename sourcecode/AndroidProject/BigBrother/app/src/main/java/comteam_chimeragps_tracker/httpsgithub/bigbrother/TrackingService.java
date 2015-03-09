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
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;


public class TrackingService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    boolean quit;

    @Override
    public void onCreate()
    {
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
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
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
    }

}

