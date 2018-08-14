package testProject;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Player {

	private Stage stage;
	private int diff;
	
	private int maxHealth;
	private int health;
	private float healthWidth;
	
	public boolean alive = true;
	private boolean invincible = false;
	private int frameHit;
	
	private int lives;
	private int maxLives;
	
	private int frame = 0;
	//Number of frames till you can begin to move
	private static final int prepPeriod = 1000;
	
	private Rectangle playerRect;
	private Rectangle playerHitbox;
	private Image playerImg; //For when I skin the player
	
	private BufferedImage BHeart0;
	private Image Heart0;
	private BufferedImage BHeart1;
	private Image Heart1;
	
	private boolean collisionUp;
	private boolean collisionDown;
	private boolean collisionLeft;
	private boolean collisionRight;
	
	private int rightSpeed;
	private int leftSpeed;
	private int upSpeed;
	private int downSpeed;
	
	private double playerSizeMultiplier = 0.7;
	
	public int score = 0;
	
	public Player(Stage stage, int diff) {
		
		if(diff == 1) {
			maxLives = 5;
		}
		if(diff == 2) {
			maxLives = 3;
		}
		if(diff == 3) {
			maxLives = 1;
		}
		lives = maxLives;
		
		this.diff = diff;
		this.stage = stage;
		maxHealth = 100 * 1/diff;
		health = maxHealth;
		
		//Place player in the middle of the stage with a size of 0.75x a tile
		playerRect = new Rectangle	((stage.width / 2) - ((int)(stage.tileWidth * playerSizeMultiplier) / 2),
									(stage.height / 2) - ((int)(stage.tileHeight * playerSizeMultiplier) / 2),
									(int)(stage.tileHeight * playerSizeMultiplier),
									(int)(stage.tileWidth * playerSizeMultiplier));
		
		//Create a hit box that is one pixel bigger than the player
		playerHitbox = new Rectangle(playerRect.x - 1, playerRect.y - 1, playerRect.width + 2, playerRect.height + 2);
		
		try {
			
			BHeart0 = ImageIO.read(new File("./assets/images/h0.png"));
			BHeart1 = ImageIO.read(new File("./assets/images/h1.png"));
			
		}catch(Exception e) {System.out.println(e.getMessage());}
		
		Heart0 = (Image) ImageEditor.scale(BHeart0, 35, 35);
		Heart1 = (Image) ImageEditor.scale(BHeart1, 35, 35);
		
	}
	
	private void moveRight(int d) {
		rightSpeed = d;
	}
	private void moveLeft(int d) {
		leftSpeed = -d; 
	}
	private void moveUp(int d) {
		upSpeed = -d;
	}
	private void moveDown(int d) {
		downSpeed = d;
	}
	
	public void update() {
		
		frame++;
		
		if(frame > prepPeriod && alive) {

			checkCollision();
			move();
			updateHealth();
			
		}
		
	}
	
	private void updateHealth() {
		
		if(frame % 75 == 0 && health != 0) {
			health--;
		}
		
		if(health == 0 || lives == 0) {
			alive = false;
			stage.stopStage();
		}
		
		if(frame - frameHit == 400) {
			invincible = false;
		}
		
	}
	
	private void move() {
		
		if(!collisionUp) {
			playerRect.y += upSpeed;
		}
		if(!collisionDown) {
			playerRect.y += downSpeed;
		}
		if(!collisionRight) {
			playerRect.x += rightSpeed;
		}
		if(!collisionLeft) {
			playerRect.x += leftSpeed;
		}
		
		playerHitbox.x = playerRect.x - 1;
		playerHitbox.y = playerRect.y - 1;
		
	}
	
	private void checkCollision() {
		
		//Collision with items
		for(int i = 0; i < stage.items.length; i++) {
			if(stage.items[i] != null) {
				if(playerHitbox.intersects(stage.items[i].item)) {
					
					//Scoring System
					if(stage.items[i].type == 0) {
						score += 1;
						
						//Regaining health
						if(health + 8 >= maxHealth) {
							health = maxHealth;
						}else {
							health += 8;
						}
						
					}else if(stage.items[i].type == 1) {
						score += 2;
						
						if(health + 13 >= maxHealth) {
							health = maxHealth;
						}else {
							health += 13;
						}
						
					}else if(stage.items[i].type == 2) {
						score += 10;
						
						if(health + 35 >= maxHealth) {
							health = maxHealth;
						}else {
							health += 35;
						}
						
					}
					
					stage.items[i] = null;
				}
			}
		}
		
		//Collision with explosions
		if(!invincible) {
			for(int i = 0; i < stage.bombs.length; i++) {
				
				//If the bomb doesn't exist then your at the end of the bomb cycle and theres no need to keep checking them
				if(stage.bombs[i] == null) {
					break;
				}
				
				if(stage.bombs[i].damaging == true) {
					
					for(int j = 0; j < stage.bombs[i].explosion.length; j++) {
						
						if(stage.bombs[i].explosion[j] == null) {
							break;
						}
						
						//Check the explosion is touching the player
						if(playerHitbox.intersects(stage.bombs[i].explosion[j])) {
							lives--;
							invincible = true;
							frameHit = frame;
							break;
						}
						
					}
					
				}
				
			}
		}
		
		
		//Collision with Tiles
		//checkCollision() Index of Direction
		//                            0    =    Up
		//                            1    =    Right
		//                            2    =    Down
		//                            3    =    Left
		collisionUp = collisionRight = collisionLeft = collisionDown = false;
		
		for(int i = 0; i < stage.tileNum; i++) {
			if((stage.tileArray[i] != 4 &&
				stage.tileArray[i] != 15 &&
				stage.tileArray[i] != 18) //Blocks that are walkable through here
					
				&& playerHitbox.intersects(stage.tiles[i])) {
				int dir = findCollisionDirection(stage.tiles[i], playerRect);
				if(dir == 0) {
					collisionUp = true;
				}
				if(dir == 1) {
					collisionRight = true;
				}
				if(dir == 2) {
					collisionDown = true;
				}
				if(dir == 3) {
					collisionLeft = true;
				}
				
			}
		}
	}
	
	private int findCollisionDirection(Rectangle a, Rectangle b) {
		
		//a is tile, b is player
		
		if		(
				(b.x > a.x - b.width) && (b.x < a.x + a.width) &&
				(a.y < b.y) && (a.y + a.height == b.y)
				) {
			return 0;
		}
		else if(
				(b.y > a.y - b.height) && (b.y < a.y + a.height) &&
				(a.x > b.x) && (a.x == b.x + b.width)
				) {
			return 1;
		}
		else if(
				(b.x > a.x - b.width) && (b.x < a.x + a.width) &&
				(a.y > b.y) && (a.y == b.y + b.height)
				) {
			return 2;
		}
		else if(
				(b.y > a.y - b.height) && (b.y < a.y + a.height) &&
				(a.x < b.x) && (a.x + a.width == b.x)
				) {
			return 3;
		}else {
			//System.out.println("Collision Detection Error");
			return -1; //Some error has occurred if this happens
		}
		
	}
	
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == e.VK_W) {
			moveUp(1);
			//Any Animations When Implemented
		}
		
		if(e.getKeyCode() == e.VK_S) {
			moveDown(1);
			//Any Animations When Implemented
		}
		
		if(e.getKeyCode() == e.VK_A) {
			moveLeft(1);
			//Any Animations When Implemented
		}
		
		if(e.getKeyCode() == e.VK_D) {
			moveRight(1);
			//Any Animations When Implemented
		}
		
	}
	
	public void keyReleased(KeyEvent e) {
		
		if(e.getKeyCode() == e.VK_W) {
			moveUp(0);
		}
		if(e.getKeyCode() == e.VK_S) {
			moveDown(0);
		}
		if(e.getKeyCode() == e.VK_A) {
			moveLeft(0);
		}
		if(e.getKeyCode() == e.VK_D) {
			moveRight(0);
		}
		
	}
	
	public void display(Graphics g) {
		
		//Display the player
		if(!invincible) {
			g.setColor(Color.BLUE);
		}else {
			g.setColor(Color.RED);
		}
		g.fillRect(playerRect.x, playerRect.y, playerRect.width, playerRect.height);
		
		//Display the score
		g.setColor(Color.black);
		g.setFont(new Font("Consolas", Font.BOLD, 20));
		g.drawString("SCORE", (stage.width/2) - (int) ("SCORE".length() * 5.6), 52);
		g.drawString("" + score, (stage.width/2) - (int) (Integer.toString(score).length() * 5.6), 68);
		
		//Debugging
		g.setColor(Color.RED);
		//g.drawRect(playerHitbox.x, playerHitbox.y, playerHitbox.width, playerHitbox.height);
		
		g.setFont(new Font("Arial", Font.BOLD, 11));
		g.setColor(Color.ORANGE);
		
		//g.drawString("CollisionUp    : " + collisionUp, 10, 30);
		//g.drawString("CollisionDown  : " + collisionDown, 10, 45);
		//g.drawString("CollisionLeft  : " + collisionLeft, 10, 60);
		//g.drawString("CollisionRight : " + collisionRight, 10, 75);
		
		//Display countdown
		g.setColor(Color.black);
		g.setFont(new Font("Consolas", Font.BOLD, 50));
		
		if(frame > 0 && frame < prepPeriod/3){
			g.drawString("3", stage.width/2 - 13, stage.height/2 - 17);
		}
		if(frame >= prepPeriod/3 && frame < 2 * prepPeriod/3){
			g.drawString("2", stage.width/2 - 13, stage.height/2 - 17);
		}
		if(frame >= 2 * prepPeriod/3 && frame < prepPeriod){
			g.drawString("1", stage.width/2 - 13, stage.height/2 - 17);
		}
		if(frame >= prepPeriod && frame < 5 * prepPeriod/3){
			g.drawString("0", stage.width/2 - 13, stage.height/2 - 17);
		}
		
		//Display health
		//Draw max health bar
		g.setColor(Color.BLACK);
		g.fillRect(stage.width/2 - 53, 4, 106, 27);//Outline
		g.setColor(Color.red);
		g.fillRect(stage.width/2 - 50, 7, 100, 21);//Actual max health
		
		//Draw remaining health
		healthWidth = 100 * health/maxHealth;
		g.setColor(Color.green);
		g.fillRect(stage.width/2 - 50, 7, (int)healthWidth, 21);
		
		//Display lives remaining
		int hX = 420, hY = 0;
		
		for(int i = 0; i < maxLives; i++) {
			if(i <= lives) {
				g.drawImage(Heart0, hX, hY, null);
			}else {
				g.drawImage(Heart1, hX, hY, null);
			}
			hX += 35;
		}
		
		
	}
	
}
