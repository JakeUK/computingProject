package testProject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Obstacle {

	public int x, y;
	private double obstSizeMultiplier = 0.9;
	public Rectangle bomb;
	private int frame;
	private int upTime = 2500;
	private int explosionDuration = 500;
	
	public boolean damaging;
	
	private BufferedImage bomb1;
	private BufferedImage bomb2;
	private Image frame1;
	private Image frame2;
	
	private BufferedImage ex0;
	private Image eFrame0;
	private BufferedImage ex1;
	private Image eFrame1;
	
	public Rectangle[] explosion = new Rectangle[9];
	
	int currentFrame = 1;
	
	Stage stage;
	
	public Obstacle(int x, int y, Stage stage) {
		
		this.x = x;
		this.y = y;
		
		this.stage = stage;
		
		damaging = false;
		
		int iWidth = (int)(35 * obstSizeMultiplier);
		int iHeight = (int)(35 * obstSizeMultiplier);
		
		bomb = new Rectangle(x + (35/2) - (iWidth/2), y + (35/2) - (iHeight/2), iWidth, iHeight);
		
		try {
			
			bomb1 = ImageIO.read(new File("./assets/images/b1.png"));
			bomb2 = ImageIO.read(new File("./assets/images/b2.png"));
			
			ex0 = ImageIO.read(new File("./assets/images/e0.png"));
			ex1 = ImageIO.read(new File("./assets/images/e1.png"));
			
		}catch(Exception e) {System.out.println(e.getMessage());}
		
		frame1 = (Image) ImageEditor.scale(bomb1, iWidth, iHeight);
		frame2 = (Image) ImageEditor.scale(bomb2, iWidth, iHeight);
		
		eFrame0 = (Image) ImageEditor.scale(ex0, 105, 105);
		eFrame1 = (Image) ImageEditor.scale(ex1, 35, 35);
		
	}
	
	public void update() {
		
		if(frame == upTime) {
			damaging = true;
			int exCounter = 0;
			bomb = null;
			for(int i = 0; i < stage.spawnableTiles.length; i++) {
				
				//Places an explosion in places where there aren't any tiles
				if(stage.spawnableTiles[i].x <= x+35 && stage.spawnableTiles[i].x >= x-35 &&
				   stage.spawnableTiles[i].y <= y+35 && stage.spawnableTiles[i].y >= y-35) {
					
					explosion[exCounter] = new Rectangle(stage.spawnableTiles[i].x, stage.spawnableTiles[i].y, 35, 35);
					exCounter++;
					
				}
				
			}
			
		}
		//When the explosion is finished, it removes all of the explosion times so they cannot be collided with
		if(frame == upTime + explosionDuration) {
			for(int i = 0; i < explosion.length; i++) {
				explosion[i] = null;
			}
		}
		
		frame++;
		
	}
	
	public void display(Graphics g) {
		
		//Bomb animation
		if(frame <= 3*upTime/5) {
			currentFrame = 1;
		}
		else if(frame > 3*upTime/5 && frame <= upTime) {
			
			if(currentFrame == 1) {
				currentFrame = 2;
			}else {
				currentFrame = 1;
			}
			
		}
		else if(frame > upTime && frame < upTime + explosionDuration){
			currentFrame = 3;
		}else {
			currentFrame = 0;
		}
		
		if(currentFrame == 1) {
			g.drawImage(frame1, bomb.x, bomb.y, null);
		}else if(currentFrame == 2) {
			g.drawImage(frame2, bomb.x, bomb.y, null);
		}
		
		for(int i = 0; i < explosion.length; i++) {
			if(explosion[i] != null) {
				g.drawImage(eFrame1, explosion[i].x, explosion[i].y, null);
			}
		}
		
	}
	
}
