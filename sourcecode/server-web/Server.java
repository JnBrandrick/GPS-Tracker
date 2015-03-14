/*------------------------------------------------------------------------------------------------------------------
-- SOURCE FILE: Server.java
--
-- PROGRAM: GPS-Tracker
--
-- FUNCTIONS:
--    Server (int port)
--    void run()
--    void addPoint(String message)
--    String getPoint(String message)
--    void die(String message)
--    void main (String [] args)
--    int getNextDivider(String message, int start)
--
-- DATE: March 12, 2015
--
-- REVISIONS: Created March 10, Finished March 12, 2015
--
-- DESIGNER: Michael Chimick
--
-- PROGRAMMER: Michael Chimick
--
-- NOTES:
-- This server receives a formatted string, extracts the data, and stores it into an xml file
--
----------------------------------------------------------------------------------------------------------------------*/

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.String;
import java.util.Arrays;

public class Server extends Thread
{
    private String ClientString;
    private int ClientPort;
    private DatagramSocket ListeningSocket;
    private DatagramPacket dgram;

    private static final int DgramSize = 256;
    private static final int DefaultPort = 8234;

    private byte[] PacketData;
    private InetAddress Addr;

    /*------------------------------------------------------------------------------------------------------------------
    -- FUNCTION: Server
    --
    -- DATE: March 12, 2015
    --
    -- REVISIONS: Created March 10, Finished March 12, 2015
    --
    -- DESIGNER: Michael Chimick
    --
    -- PROGRAMMER: Michael Chimick
    --
    -- INTERFACE: public Server (int port) throws IOException
    --                          int port // port the receive on
    --
    -- RETURNS: N/A
    --
    -- NOTES:
    -- Constructor for the server class
    -- Throws an exception if the port is invalid
    --
    ----------------------------------------------------------------------------------------------------------------------*/
    public Server (int port) throws IOException
    {
        ListeningSocket = new DatagramSocket (port);
    }

    /*------------------------------------------------------------------------------------------------------------------
    -- FUNCTION: run
    --
    -- DATE: March 12, 2015
    --
    -- REVISIONS: Created March 10, Finished March 12, 2015
    --
    -- DESIGNER: Michael Chimick
    --
    -- PROGRAMMER: Michael Chimick
    --
    -- INTERFACE: public void run()
    --
    -- RETURNS: void
    --
    -- NOTES:
    -- Driver for the server
    --
    ----------------------------------------------------------------------------------------------------------------------*/
    public void run()
    {
        while(true)
        {
            PacketData = new byte[DgramSize];
            dgram = new DatagramPacket (PacketData, DgramSize);
            try
            {
                // Listen for datagrams
                System.out.println("Port: " + ListeningSocket.getLocalPort());
                ListeningSocket.receive(dgram);
                Addr = dgram.getAddress();
                ClientPort = dgram.getPort();
                System.out.println ("Datagram from: "+ Addr +":"+ ClientPort);
                
                // Get the client data
                ClientString = new String (PacketData, 0, dgram.getLength());
                System.out.println ("Message: "+ ClientString.trim());
                addPoint(ClientString);
            }
    
            catch (SocketTimeoutException s)
            {
                System.out.println ("Socket timed out!");
                ListeningSocket.close();
                break;
            }
  
            catch(IOException e)
            {
                e.printStackTrace();
                break;
            }
        }
    }

    /*------------------------------------------------------------------------------------------------------------------
    -- FUNCTION: addPoint
    --
    -- DATE: March 12, 2015
    --
    -- REVISIONS: Created March 10, Finished March 12, 2015
    --
    -- DESIGNER: Michael Chimick
    --
    -- PROGRAMMER: Michael Chimick
    --
    -- INTERFACE: public void addPoint(String message)
    --                String message // formatted string containing point data
    --
    -- RETURNS: void
    --
    -- NOTES:
    -- Takes a message string and adds thee data it contains to an xml file
    --
    ----------------------------------------------------------------------------------------------------------------------*/
    public void addPoint(String message)
    {
        FileInputStream in;
        FileOutputStream out;
        int keepLength = 0;
        String point;

        try
        {
            in = new FileInputStream("data.xml");
            keepLength = in.available() - 8;
            byte[] keep = new byte[keepLength];
            in.read(keep);
            in.close();

            out = new FileOutputStream("data.xml");
            point = getPoint(message);
            byte bPoint[] = point.getBytes();

            byte[] result = new byte[keep.length + bPoint.length];
            System.arraycopy(keep, 0, result, 0, keep.length);
            System.arraycopy(bPoint, 0, result, keep.length, bPoint.length);

            out.write(result);
        }
        catch(Exception e)
        {
            System.out.println(e);
            die("addPoint failure");
        }
    }

    /*------------------------------------------------------------------------------------------------------------------
    -- FUNCTION: getPoint
    --
    -- DATE: March 12, 2015
    --
    -- REVISIONS: Created March 10, Finished March 12, 2015
    --
    -- DESIGNER: Michael Chimick
    --
    -- PROGRAMMER: Michael Chimick
    --
    -- INTERFACE: public String getPoint(String message)
    --                String message // formatted string containing point data
    --
    -- RETURNS: String // string formatted for the xml file
    --
    -- NOTES:
    -- Takes a formatted string and reformats it for the xml file
    --
    ----------------------------------------------------------------------------------------------------------------------*/
    public String getPoint(String message)
    {
        String err = "File missing fields";

        String name;
        String ip;
        String time;
        String lat;
        String lon;

        int index = 0;
        int nextIndex = 0;

        // name|ip|time|lat|lon|

        nextIndex = getNextDivider(message, index);
        if (nextIndex < 0) die(err);
        name = message.substring(index, nextIndex);
        index = nextIndex + 1;

        nextIndex = getNextDivider(message, index);
        if (nextIndex < 0) die(err);
        ip = message.substring(index, nextIndex);
        index = nextIndex + 1;

        nextIndex = getNextDivider(message, index);
        if (nextIndex < 0) die(err);
        time = message.substring(index, nextIndex);
        index = nextIndex + 1;

        nextIndex = getNextDivider(message, index);
        if (nextIndex < 0) die(err);
        lat = message.substring(index, nextIndex);
        index = nextIndex + 1;

        nextIndex = getNextDivider(message, index);
        if (nextIndex < 0) die(err);
        lon = message.substring(index, nextIndex);

        return "    <user>\n" +
               "        <name>"      + name + "</name>\n" +
               "        <ip>"        + ip   + "</ip>\n" +
               "        <time>"      + time + "</time>\n" +
               "        <latitude>"  + lat  + "</latitude>\n" +
               "        <longitude>" + lon  + "</longitude>\n" +
               "    </user>\n" +
               "</users>";
    }

    /*------------------------------------------------------------------------------------------------------------------
    -- FUNCTION: getNextDivider
    --
    -- DATE: March 12, 2015
    --
    -- REVISIONS: Created March 10, Finished March 12, 2015
    --
    -- DESIGNER: Michael Chimick
    --
    -- PROGRAMMER: Michael Chimick
    --
    -- INTERFACE: public int getNextDivider(String message, int start)
    --                String message // formatted string containing point data
    --                int start      // point to start parse from
    --
    -- RETURNS: int // index of next divider
    --
    -- NOTES:
    -- Finds the next divider char '|' in the string from the start index
    --
    ----------------------------------------------------------------------------------------------------------------------*/
    public int getNextDivider(String message, int start)
    {
        // field|
        for (int i = start; i < message.length(); i++)
        {
            if (message.charAt(i) == '|') return i;
        }

        return -1;
    }

    /*------------------------------------------------------------------------------------------------------------------
    -- FUNCTION: die
    --
    -- DATE: March 12, 2015
    --
    -- REVISIONS: Created March 10, Finished March 12, 2015
    --
    -- DESIGNER: Michael Chimick
    --
    -- PROGRAMMER: Michael Chimick
    --
    -- INTERFACE: public void die(String message)
    --                String message // death message
    --
    -- RETURNS: void
    --
    -- NOTES:
    -- Prints an error message and kills the program
    --
    ----------------------------------------------------------------------------------------------------------------------*/
    public void die(String message)
    {
        System.out.println(message);
        System.err.println(message);
        System.exit(-1);
    }

    /*------------------------------------------------------------------------------------------------------------------
    -- FUNCTION: main
    --
    -- DATE: March 12, 2015
    --
    -- REVISIONS: Created March 10, Finished March 12, 2015
    --
    -- DESIGNER: Michael Chimick
    --
    -- PROGRAMMER: Michael Chimick
    --
    -- INTERFACE: public static void main (String [] args)
    --                String [] args // command line arguments
    --
    -- RETURNS: void
    --
    -- NOTES:
    -- Takes a user defined port, or assigns the default
    -- Starts the server thread
    --
    ----------------------------------------------------------------------------------------------------------------------*/
    public static void main (String [] args)
    {
        int port = 0;
        if(args.length != 1)
            port = DefaultPort;
        else
            port = Integer.parseInt(args[0]);
       
        try
        {
            Thread t = new Server (port);
            t.start();
        }
        
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}