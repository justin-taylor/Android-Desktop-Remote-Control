// 	Copyright 2010 Justin Taylor
// 	This software can be distributed under the terms of the
// 	GNU General Public License. 

import java.awt.Robot;
import java.awt.MouseInfo;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class AutoBot {
	private Robot robot;
	private int mouse_sensitivity = 1;
	int lastXpos = 0;
	int lastYpos = 0;
	
	int lastScrollY;
	
	boolean special_key = true;
	
	public BufferedImage img;
	
	public AutoBot(){
		try{ robot = new Robot(); }
		catch(Exception e){ System.out.println("Couldn't Create robot");}
	}
	
	public void handleMessage(String event){

		try{
			String[] tokens = event.split("!!");
			
			if(tokens[0].equals("CLICK")){
				clickMouse(tokens[1]);
			}
			
			else if(tokens[0].equals("Mouse Sensitivity")){
				mouse_sensitivity = Integer.parseInt(tokens[1]);
			}
			
			else if(tokens[0].equals("MOVE")){
				int currXpos = Integer.parseInt(tokens[1]);
				int currYpos = Integer.parseInt(tokens[2]);
				
				int deltaX = (lastXpos - currXpos) * -1;
				int deltaY = (lastYpos - currYpos) * -1;
				
				moveMouse(deltaX, deltaY);
				
				lastXpos = currXpos;
				lastYpos = currYpos;
				//screenCap();
			}
			else if(tokens[0].equals("DOWN")){
				lastXpos = Integer.parseInt(tokens[1]);
				lastYpos = Integer.parseInt(tokens[2]);
			}
			else if(tokens[0].equals("UP")){
				int deltaTime = Integer.parseInt(tokens[2]) - Integer.parseInt(tokens[1]);
				if(deltaTime < 250) clickMouse("LEFT");
			}
			
			else if(tokens[0].equals("SCROLL")){
				if(tokens[1].equals("DOWN"))
					lastScrollY = lastYpos;
				
				else if(tokens[1].equals("MOVE")){
					int currYpos = Integer.parseInt(tokens[3]);
					
					int deltaY = currYpos - lastScrollY;
					int tolerance = 10;
					
					if	(deltaY > tolerance){
						robot.mouseWheel(1);
						lastScrollY = currYpos;
					}
					
					if (deltaY < -1 * tolerance){
						robot.mouseWheel(-1);
						lastScrollY = currYpos;
					}					
					//screenCap();
				}
			}
			
			else if(tokens[0].equals("KEY")){
				type(tokens[1].charAt(0));
			}
			
			else if(tokens[0].equals("S_KEY")){
				
				if(special_key){
					int key;
					int key_code = Integer.parseInt(tokens[1]);
					
					switch(key_code){
						case 66: key = KeyEvent.VK_ENTER; break;
						case 67: key = KeyEvent.VK_BACK_SPACE; break;
						default: key = -1; break;
					}
					keyBoardPress(key);
					special_key = false;
				}
				else special_key = true;
				
				
			}
			
			else
				System.out.println("else message" + tokens[0] +" "+ tokens[1]);
		}catch(Exception e){}
	}
	
	public void screenCap(){
		Rectangle captureSize = new Rectangle(lastXpos, lastYpos, 500, 500);
		img = robot.createScreenCapture(captureSize);
	}
	
	public BufferedImage getScreenCap(){
		return img;
	}
	
	public void moveMouse(int x, int y){
		//get the current position of the mouse cursor
		int current_x_local = MouseInfo.getPointerInfo().getLocation().x;
		int current_y_local = MouseInfo.getPointerInfo().getLocation().y;
		
		//move the mouse relative to the current position
		robot.mouseMove(current_x_local + x*mouse_sensitivity,
						current_y_local + y*mouse_sensitivity);
	}
	
	public void clickMouse(String which){
		int button;
		if(which.equals("LEFT"))
			button = InputEvent.BUTTON1_MASK;
		
		else button = InputEvent.BUTTON3_MASK;
		
		robot.mousePress(button);
		robot.mouseRelease(button);
	}
	
	public void scrollMouseWheel(int notches){
		robot.mouseWheel(notches);
	}
	
	public void type(char character) {
        switch (character) {
        case 'a': keyBoardPress(KeyEvent.VK_A); break;
        case 'b': keyBoardPress(KeyEvent.VK_B); break;
        case 'c': keyBoardPress(KeyEvent.VK_C); break;
        case 'd': keyBoardPress(KeyEvent.VK_D); break;
        case 'e': keyBoardPress(KeyEvent.VK_E); break;
        case 'f': keyBoardPress(KeyEvent.VK_F); break;
        case 'g': keyBoardPress(KeyEvent.VK_G); break;
        case 'h': keyBoardPress(KeyEvent.VK_H); break;
        case 'i': keyBoardPress(KeyEvent.VK_I); break;
        case 'j': keyBoardPress(KeyEvent.VK_J); break;
        case 'k': keyBoardPress(KeyEvent.VK_K); break;
        case 'l': keyBoardPress(KeyEvent.VK_L); break;
        case 'm': keyBoardPress(KeyEvent.VK_M); break;
        case 'n': keyBoardPress(KeyEvent.VK_N); break;
        case 'o': keyBoardPress(KeyEvent.VK_O); break;
        case 'p': keyBoardPress(KeyEvent.VK_P); break;
        case 'q': keyBoardPress(KeyEvent.VK_Q); break;
        case 'r': keyBoardPress(KeyEvent.VK_R); break;
        case 's': keyBoardPress(KeyEvent.VK_S); break;
        case 't': keyBoardPress(KeyEvent.VK_T); break;
        case 'u': keyBoardPress(KeyEvent.VK_U); break;
        case 'v': keyBoardPress(KeyEvent.VK_V); break;
        case 'w': keyBoardPress(KeyEvent.VK_W); break;
        case 'x': keyBoardPress(KeyEvent.VK_X); break;
        case 'y': keyBoardPress(KeyEvent.VK_Y); break;
        case 'z': keyBoardPress(KeyEvent.VK_Z); break;
        case 'A': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_A); break;
        case 'B': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_B); break;
        case 'C': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_C); break;
        case 'D': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_D); break;
        case 'E': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_E); break;
        case 'F': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_F); break;
        case 'G': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_G); break;
        case 'H': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_H); break;
        case 'I': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_I); break;
        case 'J': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_J); break;
        case 'K': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_K); break;
        case 'L': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_L); break;
        case 'M': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_M); break;
        case 'N': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_N); break;
        case 'O': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_O); break;
        case 'P': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_P); break;
        case 'Q': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_Q); break;
        case 'R': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_R); break;
        case 'S': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_S); break;
        case 'T': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_T); break;
        case 'U': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_U); break;
        case 'V': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_V); break;
        case 'W': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_W); break;
        case 'X': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_X); break;
        case 'Y': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_Y); break;
        case 'Z': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_Z); break;
        case '`': keyBoardPress(KeyEvent.VK_BACK_QUOTE); break;
        case '0': keyBoardPress(KeyEvent.VK_0); break;
        case '1': keyBoardPress(KeyEvent.VK_1); break;
        case '2': keyBoardPress(KeyEvent.VK_2); break;
        case '3': keyBoardPress(KeyEvent.VK_3); break;
        case '4': keyBoardPress(KeyEvent.VK_4); break;
        case '5': keyBoardPress(KeyEvent.VK_5); break;
        case '6': keyBoardPress(KeyEvent.VK_6); break;
        case '7': keyBoardPress(KeyEvent.VK_7); break;
        case '8': keyBoardPress(KeyEvent.VK_8); break;
        case '9': keyBoardPress(KeyEvent.VK_9); break;
        case '-': keyBoardPress(KeyEvent.VK_MINUS); break;
        case '=': keyBoardPress(KeyEvent.VK_EQUALS); break;
        case '~': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_QUOTE); break;
        case '!': keyBoardPress(KeyEvent.VK_EXCLAMATION_MARK); break;
        case '@': keyBoardPress(KeyEvent.VK_AT); break;
        case '#': keyBoardPress(KeyEvent.VK_NUMBER_SIGN); break;
        case '$': keyBoardPress(KeyEvent.VK_DOLLAR); break;
        case '%': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_5); break;
        case '^': keyBoardPress(KeyEvent.VK_CIRCUMFLEX); break;
        case '&': keyBoardPress(KeyEvent.VK_AMPERSAND); break;
        case '*': keyBoardPress(KeyEvent.VK_ASTERISK); break;
        case '(': keyBoardPress(KeyEvent.VK_LEFT_PARENTHESIS); break;
        case ')': keyBoardPress(KeyEvent.VK_RIGHT_PARENTHESIS); break;
        case '_': keyBoardPress(KeyEvent.VK_UNDERSCORE); break;
        case '+': keyBoardPress(KeyEvent.VK_PLUS); break;
        case '\t': keyBoardPress(KeyEvent.VK_TAB); break;
        case '\n': keyBoardPress(KeyEvent.VK_ENTER); break;
        case '[': keyBoardPress(KeyEvent.VK_OPEN_BRACKET); break;
        case ']': keyBoardPress(KeyEvent.VK_CLOSE_BRACKET); break;
        case '\\': keyBoardPress(KeyEvent.VK_BACK_SLASH); break;
        case '{': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_OPEN_BRACKET); break;
        case '}': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_CLOSE_BRACKET); break;
        case '|': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_SLASH); break;
        case ';': keyBoardPress(KeyEvent.VK_SEMICOLON); break;
        case ':': keyBoardPress(KeyEvent.VK_COLON); break;
        case '\'': keyBoardPress(KeyEvent.VK_QUOTE); break;
        case '"': keyBoardPress(KeyEvent.VK_QUOTEDBL); break;
        case ',': keyBoardPress(KeyEvent.VK_COMMA); break;
        case '<': keyBoardPress(KeyEvent.VK_LESS); break;
        case '.': keyBoardPress(KeyEvent.VK_PERIOD); break;
        case '>': keyBoardPress(KeyEvent.VK_GREATER); break;
        case '/': keyBoardPress(KeyEvent.VK_SLASH); break;
        case '?': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH); break;
        case ' ': keyBoardPress(KeyEvent.VK_SPACE); break;
        default: System.out.println("Character " + character); break;
        }
    }
	
	public void keyBoardPress(int key){
		try{
			robot.keyPress(key);
			robot.keyRelease(key);
		}catch(Exception e){}
	}
	
	public void keyBoardPress(int key, int key2){
		try{
			robot.keyPress(key);
			robot.keyPress(key2);
			robot.keyRelease(key2);
			robot.keyRelease(key);
		}catch(Exception e){}
	}
}
