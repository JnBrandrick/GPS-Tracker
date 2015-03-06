package comteam_chimeragps_tracker.httpsgithub.bigbrother;
/*
        Date:       March 4, 2014
        Activity:   Tracking Central
        Designer:   Jeff Bayntun
        Programmer:  Jeff Bayntun

        Description: This Activity Lets a user turn tracking on or off and also has
        lets a user change the server go to the ShowMap Activity or the AcquireHost
        activity.

 */
import android.content.Context;

/*
        Date:       March 4, 2014
        Activity:   Tracking Central
        Designer:   Jeff Bayntun
        Programmer:  Jeff Bayntun

        Description: This Activity Lets a user turn tracking on or off and also has
        lets a user change the server go to the ShowMap Activity or the AcquireHost
        activity.

 */
public class LocationHandler {

    Context context;

    LocationHandler(Context c)
    {
        context = c;
        buildGoogleApiClient(context);

    }

    protected synchronized void buildGoogleApiClient(Context context)
    {
      /*  GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(context)
                .addOnConnectionFailedListener(context)
                .addApi(LocationServices.API)
                .build(); */
    }


}
