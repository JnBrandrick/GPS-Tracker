package comteam_chimeragps_tracker.httpsgithub.bigbrother;

import android.content.Context;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

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
    private static final int DGRAM_SIZE = 256;
    
    private DatagramSocket dgramSocket;
    private InetAddress iAddr;
    
    private String packetData;
    private int port;
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
    public ClientConnect(Context appContext) throws SocketException, UnknownHostException, NumberFormatException
    {
        String[] preferences = PreferenceHandler.checkPreferences(appContext);
        
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
    public void setPacketData(String data)
    {
        packetData = data;
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
}
