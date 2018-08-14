package testProject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class Stage {

	public int width, height;
	
	private final int stageWidth    = 20;     // Choose the width and height of the stage, make sure level file is correct size to do this
	private final int stageHeight   = 20;
	public final int tileWidth;
	public final int tileHeight;
	
	private long frame;
	private int counter = 0;
	
	public final int tileNum       = stageWidth * stageHeight;
	int[] tileArray;
	
	private boolean alive = true;
	
	private int x, y;
	
	//Drawing the stage
	public Rectangle[] tiles;
	public Rectangle[] spawnableTiles;
	
	//Tiles
	private BufferedImage BBRICK;
	private Image BRICK;
	private BufferedImage BCRATE;
	private Image CRATE;
	private BufferedImage BGRASS;
	private Image GRASS;
	private BufferedImage BWATER;
	private Image WATER;
	private BufferedImage BFENCE1;
	private Image FENCE1;
	private BufferedImage BFENCE2;
	private Image FENCE2;
	private BufferedImage BFENCE3;
	private Image FENCE3;
	private BufferedImage BFENCE4;
	private Image FENCE4;
	private BufferedImage BFENCE5;
	private Image FENCE5;
	private BufferedImage BFENCE6;
	private Image FENCE6;
	private BufferedImage BFENCE7;
	private Image FENCE7;
	private BufferedImage BFENCE8;
	private Image FENCE8;
	private BufferedImage BWATERTOP;
	private Image WATERTOP;
	private BufferedImage BCONCRETE;
	private Image CONCRETE;
	private BufferedImage BCAUTION;
	private Image CAUTION;
	private BufferedImage BDARKBRICK;
	private Image DARKBRICK;
	private BufferedImage BWRENCH;
	private Image WRENCH;
	private BufferedImage BCONCRETE2;
	private Image CONCRETE2;
	private BufferedImage BBOX;
	private Image BOX;
	
	private Image[] tileImg; //For when I skin the blocks
	
	private int itemLimit = 500;
	Item[] items;
	int itemCounter = 0;
	
	private int bombLimit = 2000;
	Obstacle[] bombs;
	int bombCounter = 0;
	
	int diff;
	
	public Stage(int level, int diff, int width, int height) {
		
		this.width = width;
		this.height = height;
		
		this.diff = diff;
		
		tileWidth = this.width/stageWidth;
		tileHeight = this.height/stageHeight;
		
		tileArray = loadLevelArray(level);
		System.out.println(Arrays.toString(tileArray));
		
		tiles = new Rectangle[tileNum];
		tileImg = new Image[tileNum];
		items = new Item[itemLimit];
		bombs = new Obstacle[bombLimit];
		
		//Initialise Tile Sprites
		try {
			
			BBRICK = ImageIO.read(new File("./assets/images/2.png"));
			BCRATE = ImageIO.read(new File("./assets/images/3.png"));
			BGRASS = ImageIO.read(new File("./assets/images/4.png"));
			BWATER = ImageIO.read(new File("./assets/images/5.png"));
			BFENCE1 = ImageIO.read(new File("./assets/images/6.png"));
			BFENCE2 = ImageIO.read(new File("./assets/images/7.png"));
			BFENCE3 = ImageIO.read(new File("./assets/images/8.png"));
			BFENCE4 = ImageIO.read(new File("./assets/images/9.png"));
			BFENCE5 = ImageIO.read(new File("./assets/images/10.png"));
			BFENCE6 = ImageIO.read(new File("./assets/images/11.png"));
			BFENCE7= ImageIO.read(new File("./assets/images/12.png"));
			BFENCE8 = ImageIO.read(new File("./assets/images/13.png"));
			BWATERTOP = ImageIO.read(new File("./assets/images/14.png"));
			BCONCRETE = ImageIO.read(new File("./assets/images/15.png"));
			BCAUTION = ImageIO.read(new File("./assets/images/16.png"));
			BDARKBRICK = ImageIO.read(new File("./assets/images/17.png"));
			BWRENCH = ImageIO.read(new File("./assets/images/18.png"));
			BCONCRETE2 = ImageIO.read(new File("./assets/images/19.png"));
			BBOX = ImageIO.read(new File("./assets/images/20.png"));
			
			
		} catch (IOException e) {e.printStackTrace();}
		
		//Resize Sprites to fit tile size
		BRICK = (Image) ImageEditor.scale(BBRICK, tileWidth, tileHeight);
		CRATE = (Image) ImageEditor.scale(BCRATE, tileWidth, tileHeight);
		GRASS = (Image) ImageEditor.scale(BGRASS, tileWidth, tileHeight);
		WATER = (Image) ImageEditor.scale(BWATER, tileWidth, tileHeight);
		FENCE1 = (Image) ImageEditor.scale(BFENCE1, tileWidth, tileHeight);
		FENCE2 = (Image) ImageEditor.scale(BFENCE2, tileWidth, tileHeight);
		FENCE3 = (Image) ImageEditor.scale(BFENCE3, tileWidth, tileHeight);
		FENCE4 = (Image) ImageEditor.scale(BFENCE4, tileWidth, tileHeight);
		FENCE5 = (Image) ImageEditor.scale(BFENCE5, tileWidth, tileHeight);
		FENCE6 = (Image) ImageEditor.scale(BFENCE6, tileWidth, tileHeight);
		FENCE7 = (Image) ImageEditor.scale(BFENCE7, tileWidth, tileHeight);
		FENCE8 = (Image) ImageEditor.scale(BFENCE8, tileWidth, tileHeight);
		WATERTOP = (Image) ImageEditor.scale(BWATERTOP, tileWidth, tileHeight);
		CONCRETE = (Image) ImageEditor.scale(BCONCRETE, tileWidth, tileHeight);
		CAUTION = (Image) ImageEditor.scale(BCAUTION, tileWidth, tileHeight);
		DARKBRICK = (Image) ImageEditor.scale(BDARKBRICK, tileWidth, tileHeight);
		WRENCH = (Image) ImageEditor.scale(BWRENCH, tileWidth, tileHeight);
		CONCRETE2 = (Image) ImageEditor.scale(BCONCRETE2, tileWidth, tileHeight);
		BOX = (Image) ImageEditor.scale(BBOX, tileWidth, tileHeight);
		
		
		
		loadArrays();
		
	}
	
	private void loadArrays() {
		
		for(int i = 0; i < tileNum; i++) {
			
			if(x >= width) {
				x = 0;
				y += tileHeight;
			}
			tiles[i] = new Rectangle(x, y, tileWidth, tileHeight);
			
			//Set 0 to any tiles that I want to be able to have obstacles/items spawn on
			//This is also setting the tile sprites
			if(tileArray[i] == 0 || tileArray[i] == 4 || tileArray[i] == 15 || tileArray[i] == 18) {
				spawnableTiles[counter] = tiles[i];
				counter++;
			}
			if(tileArray[i] == 2) {
				tileImg[i] = BRICK;
			}
			if(tileArray[i] == 3) {
				tileImg[i] = CRATE;
			}
			if(tileArray[i] == 4) {
				tileImg[i] = GRASS;
			}
			if(tileArray[i] == 5) {
				tileImg[i] = WATER;
			}
			if(tileArray[i] == 6) {
				tileImg[i] = FENCE1;
			}
			if(tileArray[i] == 7) {
				tileImg[i] = FENCE2;
			}
			if(tileArray[i] == 8) {
				tileImg[i] = FENCE3;
			}
			if(tileArray[i] == 9) {
				tileImg[i] = FENCE4;
			}
			if(tileArray[i] == 10) {
				tileImg[i] = FENCE5;
			}
			if(tileArray[i] == 11) {
				tileImg[i] = FENCE6;
			}
			if(tileArray[i] == 12) {
				tileImg[i] = FENCE7;
			}
			if(tileArray[i] == 13) {
				tileImg[i] = FENCE8;
			}
			if(tileArray[i] == 14) {
				tileImg[i] = WATERTOP;
			}
			if(tileArray[i] == 15) {
				tileImg[i] = CONCRETE;
			}
			if(tileArray[i] == 16) {
				tileImg[i] = CAUTION;
			}
			if(tileArray[i] == 17) {
				tileImg[i] = DARKBRICK;
			}
			if(tileArray[i] == 18) {
				tileImg[i] = WRENCH;
			}
			if(tileArray[i] == 19) {
				tileImg[i] = CONCRETE2;
			}
			if(tileArray[i] == 20) {
				tileImg[i] = BOX;
			}
			
			x += tileWidth;
		}
		
	}
	
	public void display(Graphics g) {
		
		for(int i = 0; i < tileNum; i++) {
			
			if(tileArray[i] == 0) {
				g.setColor(Color.WHITE);
			}else if(tileArray[i] == 1){
				g.setColor(Color.WHITE);
			}else if(tileArray[i] == 2) {
				g.setColor(Color.GRAY);
			}else {
				g.setColor(Color.MAGENTA); //Shown if image doesn't load properly
			}
			g.fillRect(tiles[i].x, tiles[i].y, tiles[i].width, tiles[i].height);
			if(tileImg[i] != null) {
				g.drawImage(tileImg[i], tiles[i].x, tiles[i].y, null);
			}
			
		}
		
		//Items and Enemies
		for(int i = 0; i < itemLimit; i++) {
			if(items[i] != null) {
				items[i].display(g);
			}
		}
		
		for(int i = 0; i < bombLimit; i++) {
			if(bombs[i] != null) {
				bombs[i].display(g);
			}
		}
		
		if(!alive) {
			g.setColor(Color.black);
			g.setFont(new Font("Consolas", Font.BOLD, 20));
			g.drawString("Game Over!", 300, height/2 - 31);
			g.drawString("Restart game to try again.", 219, height/2 - 13);
		}
		
		//Debugging
		g.setFont(new Font("Arial", Font.BOLD, 11));
		g.setColor(Color.BLACK);
		
		g.drawString("Frame Counter : " + frame, 10, 15);
		
	}
	
	public void update() {

		//Add a frame every time this is called
		frame++;
		
		//Game play features
		if(frame % 500 == 0 && itemCounter < itemLimit && alive) {
			
			Random rand = new Random();
			int n = rand.nextInt(spawnableTiles.length);
			int n2 = rand.nextInt(101);
			
			//System.out.println(n2);
			
			//Different Spawn Rates for different difficulties
				if(n2 <= 5) {
					items[itemCounter] = new Item(spawnableTiles[n].x, spawnableTiles[n].y, tileWidth, tileHeight, 2);
				}else if(n2 <= 25){
					items[itemCounter] = new Item(spawnableTiles[n].x, spawnableTiles[n].y, tileWidth, tileHeight, 1);
				}else {
					items[itemCounter] = new Item(spawnableTiles[n].x, spawnableTiles[n].y, tileWidth, tileHeight, 0);
				}
			
			itemCounter++;
			
		}
		
		if(frame % 250 == 0 && bombCounter < bombLimit && alive) {
			
			Random rand = new Random();
			int n = rand.nextInt(spawnableTiles.length);
			
			//System.out.println(n2);
			bombs[bombCounter] = new Obstacle(spawnableTiles[n].x, spawnableTiles[n].y, this);
			bombCounter++;
			
		}
		
		
		
		for(int i = 0; i < bombLimit; i++) {
			if(bombs[i] != null) {
				bombs[i].update();
			}
		}
		
	}
	
	private int[] loadLevelArray(int level) {
		
		int[] stage = new int[tileNum];
		
		try {
			
			
			String contents = new String(Files.readAllBytes(Paths.get("./assets/files/level" + level + ".txt"))); //Found this online
			
			int count;
			String contentBuffer;
			contentBuffer = contents.replace("04", "").replace("15", "").replace("18", ""); //Spawn-able blocks
			count = ((contents.length() - contentBuffer.length()) / 2);
			System.out.println(count + " Spawn-able Tiles");
			
			spawnableTiles = new Rectangle[count];
			
			String[] contentsArray = contents.split("(?<=\\G..)");
			
			for(int i = 0; i <= tileNum - 1; i++) {
				stage[i] = Integer.valueOf(contentsArray[i]);
			}
			System.out.println("Level successfully loaded into TileArray");
		
		}catch(Exception e) {System.out.println(e);}
		
		return stage;
	}
	
	public void stopStage() {
		alive = false;
	}
	
	
}
