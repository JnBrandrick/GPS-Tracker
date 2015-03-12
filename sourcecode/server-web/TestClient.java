
/*---------------------------------------------------------------------------------------
--	Source File:		 udpc.java - A simple Java UDP echo client
--
--	Classes:		udpc 		- public class
--				DatagramSocket 	- java.net
--				DatagramPacket	- java.net
--				InetAddress	- java.net
--				
--	Methods:
--				getData			(DatagramPacket Class)
--				getLocalPort		(DatagramSocket Class)
--				close			(DatagramSocket Class)
--				send			(DatagramSocket Class)
--				receive			(DatagramSocket Class)
--				getByName		(InetAddress Class)
--						
--
--	Date:			February 8, 2014
--
--	Revisions:		(Date and Description)
--					
--	Designer:		Aman Abdulla
--				
--	Programmer:		Aman Abdulla
--
--	Notes:
--	The program illustrates the use of the java.net package to implement a basic
-- 	echo client. 
--	
--	The application takes a user-supplied string from commnad line and sends it to 
--	a echo server, then waits for the echo and displays it. 
--
--	Generate the class file and run it as follows:
--			javac tcpc
--			java <server address> <destination port>
---------------------------------------------------------------------------------------*/

import java.net.*;
import java.io.*;

public class TestClient
{
    static final int DgramSize = 1024;
    
    
    public static void main (String [] args) throws UnknownHostException, SocketException
    {
	    if(args.length != 3)
	    {
	        System.out.println("Usage Error : Java EchoClient <Server> <port> <Message>");
	        System.exit(0);
	    }   
	    String ServerName = args[0];
	    int ServerPort = Integer.parseInt (args[1]);
	    String ClientString;
	    String ServerString;
	    byte[] PacketData;
	    InetAddress Addr;
	    DatagramSocket ClientSocket;
	    DatagramPacket dgram;
	    	
	    // Get the IP address of the Server
	    Addr = InetAddress.getByName(ServerName);
	    ClientSocket = new DatagramSocket();
	    PacketData = new byte[DgramSize];
	    ClientString = new String(args[2]);
	    System.arraycopy (ClientString.getBytes(), 0, PacketData, 0, ClientString.length());
	    
	    // Create the complete datagram
	    dgram = new DatagramPacket (PacketData, PacketData.length, Addr, ServerPort);
	    	
	    try
	    {
	        // Send the Datagram to the server
	        System.out.println ("Sending Datagram to: " + ServerName + " on port " + ServerPort);
	        ClientSocket.send (dgram);
	    }    
	    catch (IOException ie)
	    {
	        System.out.println("Send Failure: "+ie.getMessage());
	         System.exit(0);
	    }
    }
}