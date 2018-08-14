package testProject;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.*;

public class Menu {

	public int buttonHovered = -1; //-1 is a placeholder for no button being hovered
	public int diffHovered = -1;
	public int levelCount = 3;
	
	private int buttonWidth = 120;
	private int buttonHeight = 40;
	
	//Position of Level Buttons
	Rectangle[] buttons = new Rectangle[] {                                         //x, y, width, height
						  new Rectangle(510, 200, buttonWidth, buttonHeight),       //Level 1 Button
						  new Rectangle(141, 52, buttonWidth, buttonHeight),        //Level 2 Button
						  new Rectangle(127, 421, buttonWidth, buttonHeight)};      //Level 3 Button
	
	Rectangle easy =      new Rectangle(-500, -500, buttonWidth, buttonHeight);     //-500, -500 are coordinates just to place the button offscreen so it 
	Rectangle medium =    new Rectangle(-500, -500, buttonWidth, buttonHeight);;
	Rectangle hard =      new Rectangle(-500, -500, buttonWidth, buttonHeight);;
	
	
	public void display(Graphics g) {
		
		//Displaying level buttons
		for(int i=0; i < buttons.length; i++) {
			if(i == buttonHovered) {
				drawButton(buttons[i], Color.LIGHT_GRAY, "Stage "+ (i+1), g);
			}else {
				drawButton(buttons[i], Color.GRAY, "Stage "+ (i+1), g);
			}
		}
		
		//Creating drop down
		if(buttonHovered != -1){
			easy.x = buttons[buttonHovered].x;
			easy.y = buttons[buttonHovered].y + (buttonHeight * 1);
			medium.x = buttons[buttonHovered].x;
			medium.y = buttons[buttonHovered].y + (buttonHeight * 2);
			hard.x = buttons[buttonHovered].x;
			hard.y = buttons[buttonHovered].y + (buttonHeight * 3);
			if(diffHovered == 0) {
				drawButton(easy, Color.LIGHT_GRAY, "Easy", g);
			}else {
				drawButton(easy, Color.GRAY, "Easy", g);
			}
			
			if(diffHovered == 1) {
				drawButton(medium, Color.LIGHT_GRAY, "Medium", g);
			}else {
				drawButton(medium, Color.gray, "Medium", g);
			}
			
			if(diffHovered == 2) {
				drawButton(hard, Color.LIGHT_GRAY, "Hard", g);
			}else {
				drawButton(hard, Color.gray, "Hard", g);
			}
		}else {
			easy.x = easy.y = -500;
			medium.x = medium.y = -500;
			hard.x = hard.y = -500;
		}
		
		
		//Debugging
		g.setFont(new Font("Arial", Font.BOLD, 11));
		g.setColor(Color.BLACK);
		
		g.drawString("Button Hovered     : " + (buttonHovered + 1), 10, 15);
		g.drawString("Difficulty Hovered : " + (diffHovered + 1), 10, 30);
		
	}
	
	private void drawButton(Rectangle r, Color c, String input, Graphics g) {
		
		g.setColor(Color.BLACK);
		g.fillRect(r.x, r.y, r.width, r.height);
		g.setColor(c);
		g.fillRect(r.x+2, r.y+2, r.width-4, r.height-4); //Creates a 2 pixel black border to the buttons.
		g.setColor(Color.BLACK);
		g.setFont(new Font("Consolas", Font.BOLD, 20)); //Mono spaced font to make centering easier
		g.drawString(input, r.x + (r.width/2) - (int) (input.length() * 5.6), r.y + (r.height/2) + 6); //Centres the text
		
	}
	
	public void mouseMoved(MouseEvent e) {
		
		int mx = e.getX();
		int my = e.getY();
		
		//Main Button Hovering
		if(buttonHovered == -1) {
			for(int i=0; i < buttons.length; i++) {
				if(mx >= buttons[i].x && mx <= buttons[i].x + buttons[i].width && my >= buttons[i].y && my <= buttons[i].y + buttons[i].height) {
					buttonHovered = i;
				}
			}
		}else{
			if(!(mx >= buttons[buttonHovered].x && mx <= buttons[buttonHovered].x + buttons[buttonHovered].width && my >= buttons[buttonHovered].y && my <= buttons[buttonHovered].y + (buttons[buttonHovered].height*4))) {
				buttonHovered = -1;
			}
		}
		
		//Difficulty Button Hovering
		if(mx >= easy.x && mx <= easy.x + easy.width && my >= easy.y && my <= easy.y + easy.height) {
			diffHovered = 0;
		}else if(mx >= medium.x && mx <= medium.x + medium.width && my >= medium.y && my <= medium.y + medium.height) {
			diffHovered = 1;
		}else if(mx >= hard.x && mx <= hard.x + hard.width && my >= hard.y && my <= hard.y + hard.height) {
			diffHovered = 2;
		}else {
			diffHovered = -1;
		}
	}
	
	private void log(String s) {
		System.out.println(s);
	}

}
