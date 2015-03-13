package comteam_chimeragps_tracker.httpsgithub.bigbrother;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.Settings;
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

/*
        Date:       March 7, 2014
        Class:   ClientConnect implements Runnable
        Designer:   Julian Brandrick
        Programmer:  Julian Brandrick

        Description: The responsibility of this class is to create, send on and teardown a UDP
        Socket. Its run method will take the packetData variable and send it to the specified
        server. The send state is done in a separate runnable thread.
 */
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
    private boolean packetSent;

    /*
     * Programmer: Julian Brandrick
     * Designer: Julian Brandrick
     *
     * Constructor: ClientConnect (Context)
     * 
     * Throws:  SocketException       -> Socket could not be initialized.
     *          UnknownHostException  -> Host name could not be found.
     *          NumberFormatException -> Port was not a number.
     * 
     * Notes:
     *  Pulls the host name and port number from the PreferenceHandler, instantiates a 
     *  UDP socket and gets the IP address of the given host name.
     */
    public ClientConnect(Context appContext) {
        context = appContext;
        deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void initSocket() throws SocketException, UnknownHostException, NumberFormatException {
        String[] preferences = PreferenceHandler.checkPreferences(context);

        String host = preferences[0];
        port = Integer.parseInt(preferences[1]);
        
        dgramSocket = new DatagramSocket();
        iAddr = InetAddress.getByName(host);
    }

    /*
     * Programmer: Julian Brandrick
     * Designer: Julian Brandrick
     *
     * Function: packetWasSent (void)
     * 
     * Return: boolean
     *      true  -> Packet was sent
     *      false -> Sending failed
     * 
     * Notes:
     *  Lets the rest of the application know if the latest packet was sent successfully.
     */
    public boolean packetWasSent()
    {
        return packetSent;
    }
    
    public synchronized void setLocalIP()
    {
        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        localIP = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
    }

    public String getLocalIP()
    {
        return localIP;
    }

    /*
     * Programmer: Julian Brandrick
     * Designer: Julian Brandrick
     *
     * Function: setPacketData (String)
     * 
     * Return: void
     * 
     * Notes:
     *  Sets the packet data to be sent to the server.
     */
    public void setPacketData(String time, String latitude, String longitude)
    {
        packetData = deviceID + DELIM + localIP + DELIM + time + DELIM + latitude + DELIM + longitude + DELIM;
    }

    /*
     * Programmer: Julian Brandrick
     * Designer: Julian Brandrick
     *
     * Function: run (void)
     * 
     * Return: void
     * 
     * Notes:
     *  Runs in a thread separate from the UI thread.
     *  
     *  Sends the packet to the server. A global boolean is used to validate if it was 
     *  successful or not.
     *  
     *  packetSend => boolean
     *      true  -> Packet sent successfully
     *      false -> Packet sending failed
     */
    public void run()
    {
        byte[] data = new byte[DGRAM_SIZE];

        System.arraycopy(packetData.getBytes(), 0, data, 0, packetData.length());

        DatagramPacket dgramPacket = new DatagramPacket(data, DGRAM_SIZE, iAddr, port);

        try
        {
            dgramSocket.send(dgramPacket);

            packetSent = true;
        }
        catch(IOException e)
        {
            packetSent = false;
        }
    }

    /*
     * Programmer: Julian Brandrick
     * Designer: Julian Brandrick
     *
     * Function: teardown (void)
     * 
     * Return: void
     * 
     * Notes:
     *  Closes the datagram socket.
     */
    public void teardown()
    {
        dgramSocket.close();
    }

    /*
     * Programmer: Julian Brandrick
     * Designer: Julian Brandrick
     *
     * Function: foundServer (void)
     * 
     * Return: boolean
     *      true  -> Asynchronous task was uninterrupted and the UDP socket was created
     *      false -> Any exceptions occurred during execution
     * 
     * Notes:
     *  Runs the AsyncLookup class and gets the output of its doInBackground method. If the task
     *  was successful, the returned String will be empty. Else it will contain an error phrase.
     */
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
    
    /*
        Date:       March 7, 2014
        AsyncTask:   AsyncLookup
        Designer:   Julian Brandrick
        Programmer:  Julian Brandrick

        Description: This class creates a UDP socket in a background thread and displays any
        exceptions it catches to the user.
    */
    private class AsyncLookup extends AsyncTask<Void, Void, String>
    {

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
