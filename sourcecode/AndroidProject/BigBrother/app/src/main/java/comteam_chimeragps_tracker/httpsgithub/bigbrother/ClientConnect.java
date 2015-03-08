package comteam_chimeragps_tracker.httpsgithub.bigbrother;

import android.content.Context;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Packet Format:
 *  name|ip|time|latitude|longitude
 * 
 * Created by Julian on 2015-03-07.
 */
public class ClientConnect implements Runnable {
    private static final int DGRAM_SIZE = 256;
    
    private DatagramSocket dgramSocket;
    private InetAddress iAddr;
    
    private String packetData;
    private int port;
    private boolean packetSent;
    
    public ClientConnect(Context appContext) throws SocketException, UnknownHostException
    {
        String[] preferences = PreferenceHandler.checkPreferences(appContext);
        
        String host = preferences[0];
        port = Integer.parseInt(preferences[1]);
        
        dgramSocket = new DatagramSocket();
        iAddr = InetAddress.getByName(host);
    }
    
    public boolean packetWasSent()
    {
        return packetSent;
    }
    
    public void setPacketData(String data)
    {
        packetData = data;
    }
    
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
}
