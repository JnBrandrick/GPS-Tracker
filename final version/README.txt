=================================================
|	Data Communications (COMP 4985)		|
|	Assisgnment #3 - GPS			|
|		Rhea Lauzon			|
|		Julian Brandrick		|
|		Jeff Bayntun			|
|		Michael Chimick			|
=================================================

This readme is to document all the pieces of this project and how to
run them.

====================
      WEBSITE
====================
To run the website, place it in the directory of choice for your Apache Web Server.
Make sure Apache is running and virtual hosting is set up correctly.

When all the other pieces of the project are running, open the website
and you will see the data update accordingly for the connected clients


Using our web server:
------------------
- Point your browser at: chimera.datacom.me
- You will be prompted for a username and password:
		username: teamchimera
		password: datacomm


====================
	SEVER
=====================
To run the java server, place it in the same directory as the website's index.

You can either compile and run the java file: java Server [optional-port]

Or run the jar file: java -jar Server.jar [optional-port]

Make sure port forwarding is correct. The server will auto run until you kill it 
and add data to the data.xml file automatically.


============================
	ANDROID CLIENT
============================
Install the .apk file on your device. 
Run the application and fill in the IP and port of the server.
Press the submit button.
Once in tracking central, press the start tracking button to begin sending data.