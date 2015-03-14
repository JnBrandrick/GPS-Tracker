package comteam_chimeragps_tracker.httpsgithub.bigbrother;

/**********************************************************************
 **  SOURCE FILE: ClientConnect.java -  UDP backend of the app
 **
 **  PROGRAM:    Android GPS // Big Brother
 **
 **  FUNCTIONS:
 **             ClientConnect(Context appContext)
 **             void initSocket()
 **             void setLocalIP()
 **             void setPacketData(String time, String latitude, String longitude)
 **             void run()
 **             void teardown()
 **             boolean foundServer()
 **             String doInBackground(Void... params)
 **             void onPostExecute(String result)
 **
 **  DATE:      March 5, 2014
 **
 **
 **  DESIGNER:    Julian Brandrick
 **
 **
 **  PROGRAMMER: Julian Brandrick
 **
 **  NOTES:
 **  Intiailizes the connection to the server
 *************************************************************************/

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/*****************************************************************************
 * Class:   ClientConnect implements Runnable
 * Date March 7, 2015
 * Revision:
 *
 * Designer: Julian Brandrick
 *
 *Programmer: Julian Brandrick
 *
 *
 * Notes:
 * The responsibility of this class is to create, send on and teardown a UDP
 Socket. Its run method will take the packetData variable and send it to the specified
 server. The send state is done in a separate runnable thread.
 **************************************************************************/
public class ClientConnect implements Runnable {
    private static final int  DGRAM_SIZE = 256;
    private static final char DELIM      = '|';

    private DatagramSocket dgramSocket;
    private InetAddress    iAddr;
    private Context        context;

    private String deviceID;
    private String localIP;
    private String packetData;
    private int    port;



    /*****************************************************************************
     * Function:   clientConnect
     * Date March 7, 2015
     * Revision:
     *
     * Designer: Julian Brandrick
     *
     *Programmer: Julian Brandrick
     *
     *Interface: public ClientConnect (Contex appContext)
     *           Context appContext -- Application
     *Returns:
     *       N/A (Constructor)
     *
     * Notes:
     *  Initializes the context and deviceID.
     **************************************************************************/
    public ClientConnect(Context appContext) {
        context = appContext;
        deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }




    /*****************************************************************************
     * Function:   initSocket
     * Date March 7, 2015
     * Revision:
     *
     * Designer: Julian Brandrick
     *
     *Programmer: Julian Brandrick
     *
     *Interface:  void initSocket()
     *
     *
     *Throws:  SocketException       -> Socket could not be initialized.
     *          UnknownHostException  -> Host name could not be found.
     *          NumberFormatException -> Port was not a number.
     *
     *Returns:
     *        void
     *
     * Notes:
     *   Pulls the host name and port number from the PreferenceHandler, instantiates a
     *  UDP socket and gets the IP address of the given host name.
     **************************************************************************/
    private void initSocket() throws SocketException, UnknownHostException, NumberFormatException {
        String[] preferences = PreferenceHandler.checkPreferences(context);

        String host = preferences[0];
        port = Integer.parseInt(preferences[1]);
        
        dgramSocket = new DatagramSocket();
        iAddr = InetAddress.getByName(host);
    }




    /*****************************************************************************
     * Function: setLocalIP
     * Date March 7, 2015
     * Revision:
     *
     *Designer: CYB (StackOverflow user name)
     *
     *Programmer: Julian Brandrick
     *
     *Interface:  void setLocalIP()
     *
     *
     *Returns:
     *        void
     *
     * Notes:
     * Finds the IP of the current device and stores it in dotted decimal notation.
     **************************************************************************/
    private void setLocalIP()
    {
        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        localIP = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
    }


    /*****************************************************************************
     * Function: setPacketData
     * Date March 7, 2015
     * Revision:
     *
     *Designer: Julian Brandrick
     *
     *Programmer: Julian Brandrick
     *
     *Interface:  void setPacketData(String time, String latitude, String longitude)
     *              String time -- time the device is sending at
     *              String latitude -- Latitude of device
     *              String longitude -- Longitude of device
     *
     *
     *Returns:
     *        void
     *
     * Notes:
     *  Sets the packet data to be sent to the server.
     *  The format is:
     *      name|local ip|time|latitude|longitude|
     **************************************************************************/
    public void setPacketData(String time, String latitude, String longitude)
    {
        packetData = deviceID + DELIM + localIP + DELIM + time + DELIM + latitude + DELIM + longitude + DELIM;
    }



    /*****************************************************************************
     * Function: run
     * Date March 7, 2015
     * Revision:
     *
     *Designer: Julian Brandrick
     *
     *Programmer: Julian Brandrick
     *
     *Interface:  void run()
     *
     *
     *Returns:
     *        void
     *
     * Notes:
     * Sends the data to the server in a datagram via UDP.
     *  This is accomplished in a separate thread.
     **************************************************************************/
    public void run()
    {
        byte[] data = new byte[DGRAM_SIZE];

        System.arraycopy(packetData.getBytes(), 0, data, 0, packetData.length());

        DatagramPacket dgramPacket = new DatagramPacket(data, DGRAM_SIZE, iAddr, port);

        try
        {
            dgramSocket.send(dgramPacket);
        }
        catch(IOException e)
        {
            Log.e("Run Exception: ", e.getMessage());
        }
    }



    /*****************************************************************************
     * Function:   teardown
     * Date March 7, 2015
     *
     * Revision:
     *
     * Designer: Julian Brandrick
     *
     *Programmer: Julian Brandrick
     *
     *Interface: void teardown()
     *
     *Returns:
     *      void
     *
     * Notes:
     *  Closes the datagram socket.
     **************************************************************************/
    public void teardown()
    {
        dgramSocket.close();
    }


    /*****************************************************************************
     * Function:  foundServer
     * Date March 7, 2015
     *
     * Revision:
     *
     * Designer: Julian Brandrick
     *
     *Programmer: Julian Brandrick
     *
     *Interface: boolean foundServer()
     *
     *Returns:
     *      boolean
     *      true  -> Asynchronous task was uninterrupted and the UDP socket was created
     *      false -> Any exceptions occurred during execution
     *
     * Notes:
     *  Runs the AsyncLookup class and gets the output of its doInBackground method. If the task
     *  was successful, the returned String will be empty. Else it will contain an error message.
     *******************************************************************************************/
    public boolean foundServer()
    {
        try
        {
            String result = new AsyncLookup().execute().get(500, TimeUnit.MILLISECONDS);

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



    /*****************************************************************************
     * Class: AsyncLookup
     * Date March 7, 2015
     *
     * Revision:
     *
     * Designer: Julian Brandrick
     *
     *Programmer: Julian Brandrick
     *
     *
     * Notes:
     *  This class creates a UDP socket in a background thread and displays any
     * exceptions it catches to the user.
     **************************************************************************/
    private class AsyncLookup extends AsyncTask<Void, Void, String>
    {




        /*****************************************************************************
         * Function:  doInBackground
         * Date March 7, 2015
         *
         * Revision:
         *
         * Designer: Julian Brandrick
         *
         *Programmer: Julian Brandrick
         *
         *Interface: String doInBackground(Void... params)
         *            params -- List of parameters
         *
         *Returns:
         *     String
         *
         * Notes:
         *  Do in background functionality of the connection
         *******************************************************************************************/
        @Override
        protected String doInBackground(Void... params) {
            try
            {
                initSocket();

                setLocalIP();
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


        /*****************************************************************************
         * Function:  onPostExecute
         * Date March 7, 2015
         *
         * Revision:
         *
         * Designer: Julian Brandrick
         *
         *Programmer: Julian Brandrick
         *
         *Interface: void onPostExecute(String result)
         *              String result -- Result of the background service
         *
         *
         *Returns:
         *     String
         *
         * Notes:
         * Occurs after the execution of the service.
         *******************************************************************************************/
        @Override
        protected void onPostExecute(String result)
        {
            if(!result.equals(""))
            {
                Helpers.makeToast((Activity)context, result, Toast.LENGTH_LONG);
            }
        }
    }
}
