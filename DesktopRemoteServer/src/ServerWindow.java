// 	Copyright 2010 Justin Taylor
// 	This software can be distributed under the terms of the
// 	GNU General Public License. 

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class ServerWindow implements ActionListener{
	
	private RemoteDataServer server;
	
	private Thread sThread; //server thread
	
	private static final int WINDOW_HEIGHT = 200;
	private static final int WINDOW_WIDTH = 350;
	
	private String ipAddress;
	
	private JFrame window = new JFrame("Remote Desktop Server");
	
	private JLabel addressLabel = new JLabel("");
	private JLabel portLabel = new JLabel("PORT: ");
	private JTextArea[] buffers = new JTextArea[3];
	private JTextField portTxt = new JTextField(5);
	private JLabel serverMessages = new JLabel("Not Connected");
	
	private JButton connectButton = new JButton("Connect");
	private JButton disconnectButton = new JButton("Disconnect");
	
	public ServerWindow(){
		server = new RemoteDataServer();
		
		window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		connectButton.addActionListener(this);
		disconnectButton.addActionListener(this);
		
		Container c = window.getContentPane();
		c.setLayout(new FlowLayout());
		
		try{
			InetAddress ip = InetAddress.getLocalHost();
			ipAddress = ip.getHostAddress();
			addressLabel.setText("IP Address: "+ipAddress);
		}
		catch(Exception e){addressLabel.setText("IP Address Could Not be Resolved");}
		
		int x;
		for(x = 0; x < 3; x++){
			buffers[x] = new JTextArea("", 1, 30);
			buffers[x].setEditable(false);
			buffers[x].setBackground(window.getBackground());
		}
		
		c.add(addressLabel);
		c.add(buffers[0]);
		c.add(portLabel);
		portTxt.setText("5444");
		c.add(portTxt);
		c.add(buffers[1]);
		c.add(connectButton);
		c.add(disconnectButton);
		c.add(buffers[2]);
		c.add(serverMessages);
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		window.setResizable(false);
	}
	
	public void actionPerformed(ActionEvent e){
		Object src = e.getSource();
		
		if(src instanceof JButton){
			if((JButton)src == connectButton){
				int port = Integer.parseInt(portTxt.getText());
				runServer(port);
			}
				
			else if((JButton)src == disconnectButton){
				closeServer();
			}
		}
	}
	
	public void runServer(int port){
		if(port <= 9999){
			server.setPort(port);
			sThread = new Thread(server);
			sThread.start();
		}
		else{
			serverMessages.setText("The port Number must be less than 10000");
		}
	}
	
	public void closeServer(){
		serverMessages.setText("Disconnected");
		server.shutdown();
		connectButton.setEnabled(true);
	}
	
	public static void main(String[] args){
		new ServerWindow();
	}
	
	public class RemoteDataServer implements Runnable{
		int PORT;
		private DatagramSocket server;
		private byte[] buf;
		private DatagramPacket dgp;
		
		private String message;
		private AutoBot bot;
		
		public RemoteDataServer(int port){
			PORT = port;
			buf = new byte[1000];
			dgp = new DatagramPacket(buf, buf.length);
			bot = new AutoBot();
			serverMessages.setText("Not Connected");
		}
		
		public RemoteDataServer(){
			buf = new byte[1000];
			dgp = new DatagramPacket(buf, buf.length);
			bot = new AutoBot();
			serverMessages.setText("Not Connected");
		}
		
		public String getIpAddress(){
			String returnStr;
			try{
					InetAddress ip = InetAddress.getLocalHost();
					returnStr = ip.getCanonicalHostName();
			}
			catch(Exception e){ returnStr = new String("Could Not Resolve Ip Address");}
			return returnStr;
		}
		
		public void setPort(int port){
			PORT = port;
		}
		
		public void shutdown(){
			try{server.close();
				serverMessages.setText("Disconnected");}
			catch(Exception e){}
		}
		
		public void run(){
			boolean connected = false;
			try {InetAddress ip = InetAddress.getLocalHost(); 
				serverMessages.setText("Waiting for connection on " + ip.getCanonicalHostName());
				
				server = new DatagramSocket(PORT, ip);
				
				connected = true;
				connectButton.setEnabled(false);
			}
			catch(BindException e){ serverMessages.setText("Port "+PORT+" is already in use. Use a different Port"); }
			catch(Exception e){serverMessages.setText("Unable to connect");}
			
			while(connected){
				// get message from sender
				try{ server.receive(dgp);
				
					// translate and use the message to automate the desktop
					message = new String(dgp.getData(), 0, dgp.getLength());
					if (message.equals("Connectivity")){
						//send response to confirm connectivity
						serverMessages.setText("Trying to Connect");
						server.send(dgp); //echo the message back
					}else if(message.equals("Connected")){
						server.send(dgp); //echo the message back
					}else if(message.equals("Close")){
						serverMessages.setText("Controller has Disconnected. Trying to reconnect."); //echo the message back
					}else{
						serverMessages.setText("Connected to Controller");
						bot.handleMessage(message);
					}
				}catch(Exception e){
					serverMessages.setText("Disconnected");
					connected = false;}
			}
		}
	}
}
