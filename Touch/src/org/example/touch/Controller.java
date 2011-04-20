// 	Copyright 2010 Justin Taylor
// 	This software can be distributed under the terms of the
// 	GNU General Public License. 

package org.example.touch;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import android.view.Display;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.View.OnKeyListener;
import android.view.*;

import android.widget.Button;
import android.widget.EditText;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.inputmethod.InputMethodManager;
import android.content.Context;


public class Controller extends Activity implements OnTouchListener, OnKeyListener{
	
	int lastXpos = 0;
	int lastYpos = 0;
	boolean keyboard = false;
	Thread checking;

	String delim = new String("!!");
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.control);
		
	 	Display display = getWindowManager().getDefaultDisplay(); 
	 	int width = display.getWidth();
	 	
	 	Button left = (Button) findViewById(R.id.LeftClickButton);
	 	Button right =  (Button) findViewById(R.id.RightClickButton);
	 	
	 	left.setWidth(width/2);
	 	right.setWidth(width/2);
	 	
	    View touchView = (View) findViewById(R.id.TouchPad);
	    touchView.setOnTouchListener(this);
	    
	    EditText editText = (EditText) findViewById(R.id.KeyBoard);
	    editText.setOnKeyListener(this);
	    editText.addTextChangedListener(new TextWatcher(){
		    public void  afterTextChanged (Editable s){
		    	Log.d("seachScreen", ""+s);
		    	s.clear();
	        } 
	        public void  beforeTextChanged  (CharSequence s, int start, int count, int after){ 
	                Log.d("seachScreen", "beforeTextChanged"); 
	        } 
	        public void  onTextChanged  (CharSequence s, int start, int before, int count) {
	        	AppDelegate appDel = ((AppDelegate)getApplicationContext());
	        	
	        	try{
	        		char c = s.charAt(start);
	        		appDel.sendMessage("KEY"+delim+c);
	        	}
	        	catch(Exception e){}
	        }
	    });
	}
	
	public void onStart(){
		super.onStart();

		AppDelegate appDel = ((AppDelegate)getApplicationContext());
		sendToAppDel(new String("Mouse Sensitivity!!"+appDel.mouse_sensitivity));
		
			new Thread(new Runnable(){
				AppDelegate appDel = ((AppDelegate)getApplicationContext());
				public void run(){
					while(appDel.connected){
						try{Thread.sleep(1000);}
						catch(Exception e){};
						if(!appDel.connected){
							finish();
						}
					}
				}
			}).start();
	}
	
	// detect touch events
	// and pass them to mousePadHandler method
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		mousePadHandler(event);
	 	return true;
	}
	
	// detect keyboard event
	// and send to delegate
	@Override
	public boolean onKey(View v, int c, KeyEvent event){
		Log.d("ello", ""+event.getKeyCode());
		AppDelegate appDel = ((AppDelegate)getApplicationContext());
		
	 	appDel.sendMessage("S_KEY"+delim+event.getKeyCode());
		return false;
	}

	// send message to AppDelegate class
	// to be sent to server on client desktop
	private void sendToAppDel(String message){
		AppDelegate appDel = ((AppDelegate)getApplicationContext());
		if(appDel.connected){
			appDel.sendMessage(message);
		}
		else{
			finish();
		}
	}
	// send a mouse message
    private void mousePadHandler(MotionEvent event) {
 	   StringBuilder sb = new StringBuilder();
 	   
 	   int action = event.getAction();
 	   int touchCount = event.getPointerCount();
 	   
	   // if a single touch
	   // send movement based on action
 	   if(touchCount == 1){
			switch(action){
				case 0: sb.append("DOWN"+delim);
						sb.append((int)event.getX()+delim);
						sb.append((int)event.getY()+delim);
						break;
				
				case 1: sb.append("UP"+delim);
						sb.append(event.getDownTime()+delim);
						sb.append(event.getEventTime());
						break;
				
				case 2: sb.append("MOVE"+delim);
						sb.append((int)event.getX()+delim);
						sb.append((int)event.getY());
						break;
						
				default: break;
			}
 	   }

	   // if two touches
	   // send scroll message
	   // based off MAC osx multi touch
	   // scrolls up and down
 	   else if(touchCount == 2){
 		   sb.append("SCROLL"+delim);
 		   if(action == 2){
 			  sb.append("MOVE"+delim);
 			  sb.append((int)event.getX()+delim);
			  sb.append((int)event.getY());
 		   }
 		   else
 			   sb.append("DOWN");
 	   }
 	   
 	  sendToAppDel(sb.toString());
 	}
    
    public void LeftButtonClickHandler(View v){
    	Log.d("eloo", "CLICKED");
    	sendToAppDel("CLICK"+delim+"LEFT");
    }
    
    public void RightButtonClickHandler(View v){
    	sendToAppDel("CLICK"+delim+"RIGHT");
    }
    
	// Show and hide Keyboard by setting the
	// focus on a hidden text field
    public void keyClickHandler(View v){
    	EditText editText = (EditText) findViewById(R.id.KeyBoard);
    	InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    	
    	if(keyboard){
    		mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    		keyboard = false;
    	}
    	else{
    		mgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    		keyboard = true;
    	}
    		
    	Log.d("SET", "Foucs");
    }
}
