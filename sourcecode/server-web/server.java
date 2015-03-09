/*---------------------------------------------------------------------------------------
--  Source File:        udps.java - a simple Java UDP (multi-threaded) echo server
--
--  Classes:        udps        - public class
--              DatagramSocket  - java.net
--              DatagramPacket  - java.net
--              
--  Methods:
--              getAddress      (DatagramPacket Class)
--              getPort         (DatagramPacket Class)
--              getLength       (DatagramPacket Class)
--              getLocalPort        (DatagramSocket Class)
--              setSoTimeout        (DatagramSocket Class)
--              send            (DatagramSocket Class)
--              receive         (DatagramSocket Class)
--              
--
--  Date:           February 8, 2014
--
--  Revisions:      (Date and Description)
--                  
--  Designer:       Aman Abdulla
--              
--  Programmer:     Aman Abdulla
--
--  Notes:
--  The program illustrates the use of the java.net package to implement a basic
--  echo server.The server is multi-threaded so every new client connection is 
--  handled by a separate thread.
--  
--  The application receives a string from an echo client and simply sends back after 
--  displaying it. 
--
--  Generate the class file and run it as follows:
--          javac udps
--          java udps <server port>
---------------------------------------------------------------------------------------*/

import java.net.*;
import java.io.*;

public class udps extends Thread
{
    String ClientString;
    int ClientPort;
    private DatagramSocket ListeningSocket;
    DatagramPacket dgram;
    static final int DgramSize = 1024;
    byte[] PacketData;
    InetAddress Addr;

    public udps (int port) throws IOException
    {
        ListeningSocket = new DatagramSocket (port);
        //ListeningSocket.setSoTimeout(30000); // set a 20 second timeout
    }

    public void run()
    {
        while(true)
        {
          PacketData = new byte[DgramSize];
          dgram = new DatagramPacket (PacketData, DgramSize);
          try
          {
              // Listen for datagrams
              //System.out.println ("Listening on port: " + ListeningSocket.getLocalPort());
              ListeningSocket.receive(dgram);
              //Addr = dgram.getAddress();
              //ClientPort = dgram.getPort();
              //System.out.println ("Datagram from: "+ Addr +":"+ ClientPort);
              
              // Get the client data
              ClientString = new String (PacketData, 0, dgram.getLength());
              System.out.println ("Message: "+ ClientString.trim());
              
              // Echo it back
              //dgram = new DatagramPacket(PacketData, DgramSize, Addr, ClientPort);
              
              /*try
              {
                  ListeningSocket.send (dgram);
              }
              catch(IOException ex)
              {
                  System.out.println(" Could not Send :"+ex.getMessage());
                  System.exit(0);
              }*/
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

    public static void main (String [] args)
    {
        if(args.length != 1)
            {
                System.out.println("Usage Error : java udps <port>");
                System.exit(0);
            }   
            int port = Integer.parseInt(args[0]);
       
        try
        {
            Thread t = new udps (port);
            t.start();
        }
        
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}