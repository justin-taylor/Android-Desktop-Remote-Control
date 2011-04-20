// 	Copyright 2010 Justin Taylor
// 	This software can be distributed under the terms of the
// 	GNU General Public License. 

package org.example.touch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.SeekBar;

import android.util.Log;

public class Touch extends Activity{
	private EditText ipField;
	private EditText portField;
	private AlertDialog alert;
	private AlertDialog network_alert;
	private SeekBar sensitivity;
	
	private boolean firstRun = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		ipField = (EditText) findViewById(R.id.EditText01);
		portField = (EditText) findViewById(R.id.EditText02);
		sensitivity = (SeekBar) findViewById(R.id.SeekBar01);
		
		ipField.setText("192.168.1.2");	
		portField.setText("5444");
		
		alert = new AlertDialog.Builder(this).create();
	    alert.setTitle("Server Connection Unavailable");
	    alert.setMessage("Please make sure the server is running on your computer");
	    alert.setButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
	    
	    network_alert = new AlertDialog.Builder(this).create();
	    network_alert.setTitle("Network Unreachable");
	    network_alert.setMessage("Your device is not connected to a network.");
	    network_alert.setButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
	}
	
	@Override
	public void onResume(){
		Log.d("RESUME", "RESUMED");
		super.onResume();
		AppDelegate appDel = ((AppDelegate)getApplicationContext());
		
		if(!appDel.connected && !firstRun){
			alert.show();
		}
		
		appDel.stopServer();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		firstRun = false;
	}
	
	public void clickHandler(View view) {
		AppDelegate appDel = ((AppDelegate)getApplicationContext());
		int s = sensitivity.getProgress();
		appDel.mouse_sensitivity = Math.round(s/20) + 1;
		
		if(!appDel.connected){
			String serverIp;
			int serverPort;
			
			serverIp = ipField.getText().toString();
			serverPort = Integer.parseInt(portField.getText().toString());
			
			appDel.createClientThread(serverIp, serverPort);
		}
		
		int x;
		for(x=0;x<4;x++){// every quarter second for one second check if the server is reachable
			if(appDel.connected){
				startActivity(new Intent(view.getContext(), Controller.class));
				x = 6;
			}
			try{Thread.sleep(250);}
			catch(Exception e){}
		}
		
		if(!appDel.connected)
			if(!appDel.network_reachable)
				network_alert.show();
			else
				alert.show();
	}
}
