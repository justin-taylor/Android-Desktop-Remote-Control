Remote Desktop Controller v1.0

Copyright 2010 Justin Taylor
This software can be distributed under the terms of the
GNU General Public License. 

This project was created for Android devices running 2.1 and higher.

The Remote Desktop controler can control the mouse and keyboard of a dekstop
computer from an Android device. There are two pieces of software needed for
this to run properly. The DesktopRemoteServer which runs on the users desktop
machine, and the Touchapp which runs on the user's Android device

1. DekstopRemoteServer ----------------------------------------------------------------

A simple UDP server written in java that runs on the users desktop machine.
The server has a UI that allows the user the specify a port to listen on,
then waits for a connection from the Andoird device. There are two classes
to the server.

	1.A) ServerWindow
		This class contains both the GUI and the server. Messages from
		the Android device is received on the server thread then passed
		to and AutoBot object (See section 1.b below).

	1.b) AutoBot
		Receives messages from the server thread and translates them to
		I/O interactions (Move Mouse, Keyboard key stroke).


2. Touch -------------------------------------------------------------------------------

The Android app that send messages over wifi to the receiving server. The app is
divided into three classes.
	
	2.A) Touch
		This view allows the user to adjust the settings of he app. The
		first setting is the port that the messages are sent over. This 
		must match the port set from the server UI on the DekstopRemoteServer.
		There is also a setting to control mouse sensitivity.
	
	2.B) Controller
		This view is shown after the settings in touch are accepted. Listners
		receive user interactions, such as taps, movement and keyboard interactions,
		and are then translated into messages to be sent over the UDP socket 
		established in the AppDelegate (See section 2.C).

	2.C) AppDelegate
		The AppDelegate bridges the gap between the Touch view (2.A) and the
		Controller view (2.B). The settings from the Touch view are used to
		create a UDP socket that will send messages from the Controller view
		to the receiveing DesktopRemoteServer (1). If there is a connection
		issue this class will close the Controller view and present the touch
		view displaying and message about the issue.

Known Bugs:

	1) The messaging System:  Could be a bit more elegant. A separate class 
		should be created and added to both the DesktopRemoteServer (section 1) 
		and the Touch source for encoding and decoding. Currently, messages are
		strings (which are easily sent over sockets) using a delimeter ("!!", 
		used now) and decoded using string.split.

	2) Key Board Support: 	Not all Keys on the Android Keyboard are supported.
				Not entirely sure why. Some keys return the same
				key code in the onKey method Controller.java

	3) Sever Connection Test: There should be a way to ensure server connectivity
				  before switching to the Controller view. Currently 
				  a test message is sent to the server and listens for
				  a message back (similar to a ping request), however
				  the connection takes a couple of tries before connecting.
