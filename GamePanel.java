package testProject;

//Imports
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{

	//Double buffering
	private Image dbImage;
	private Graphics dbg;
	
	//JPanel variables
	final int GWIDTH = 700, GHEIGHT = 700;
	final Dimension gameDim = new Dimension(GWIDTH, GHEIGHT);
	
	//Game variables
	private Thread game;
	private volatile boolean running = false;
	private long period = 4 * 1000000; //milliseconds -> nanoseconds
	private static final int DELAYS_BEFORE_YIELD = 10;
	
	//Menu variables
	private int level = 0; //  Level 0       -- User is in menus
						   //  Level 1, 2, 3 -- User is in game
	private int difficulty;
	
	Menu menu = new Menu();
	Stage stage;
	Player p1;
	
	public GamePanel() {
		
		setPreferredSize(gameDim);
		setBackground(Color.WHITE);
		setFocusable(true);
		requestFocus();
		
		//Handle all key inputs from user
		addKeyListener(new KeyAdapter(){
			
			@Override
			public void keyPressed(KeyEvent e) {
				p1.keyPressed(e);
			}
			@Override
			public void keyReleased(KeyEvent e) {
				p1.keyReleased(e);
			}
			@Override
			public void keyTyped(KeyEvent e) {
	
			}
			
		});
		addMouseMotionListener(new MouseAdapter() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				
				if(level == 0) {
					menu.mouseMoved(e);
				}
				
			}
			
		});
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				int mx = e.getX();
				int my = e.getY();
				
				if(level == 0) {
					if(menu.buttonHovered != -1 && menu.diffHovered != -1) {
						stage = new Stage(menu.buttonHovered + 1, menu.buttonHovered + 1, GWIDTH, GHEIGHT);
						p1 = new Player(stage, menu.diffHovered + 1);
						level = menu.buttonHovered + 1;
						difficulty = menu.diffHovered + 1;
						System.out.println("Level " + level + " chosen at difficulty " + difficulty);
					}
				}
				
			}
			
		});
		
		
	}
	
	public void run() {
		long beforeTime, afterTime, diff, sleepTime, overSleepTime = 0;
		int delays = 0;
		while(running) {
			beforeTime = System.nanoTime();
			
			gameUpdate();
			gameRender();
			paintScreen();
			
			afterTime = System.nanoTime();
			diff = afterTime - beforeTime;
			sleepTime = (period - diff) - overSleepTime;
			
			// If the sleep time is between 0 and the period, we can sleep
			if(sleepTime < period && sleepTime > 0) {
				try {
					game.sleep(sleepTime / 1000000L);
					overSleepTime = 0;
				}catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// The diff was greater than the period
			else if(diff > period) {
				overSleepTime = diff - period;
			}
			// Accumulate the amount of delays, and eventually yield
			else if(++delays >= DELAYS_BEFORE_YIELD) {
				game.yield();
				delays = 0;
				overSleepTime = 0;
			}
			// The loop took less time than expected
			else {
				overSleepTime = 0;
			}
			
			//Print out Game Stats
			/*
			log(
					
					"beforeTime:      " + beforeTime + "\n" +
 					"afterTime:       " + afterTime + "\n" +
					"diff:            " + diff + "\n" +
					"sleepTime:       " + sleepTime / 1000000 + "\n" +
					"overSleepTime:   " + overSleepTime / 1000000 + "\n" +
					"delays:          " + delays + "\n"
					
			);
			*/
			
		}
	}
	
	private void gameUpdate() {
		if(running && game != null) {
			if(level != 0) {
				stage.update();
				p1.update();
			}
		}
	}
	
	private void gameRender() {
		if(dbImage == null) { //Create a buffer
			dbImage = createImage(GWIDTH, GHEIGHT);
			if(dbImage == null) {
				System.err.println("dbImage is still null");
				return;
			}else {
				dbg = dbImage.getGraphics();
			}
		}
		//Clear Screen
		dbg.setColor(Color.WHITE);
		dbg.fillRect(0, 0, GWIDTH, GHEIGHT);
		//Draw Game Elements
		draw(dbg);
	}
	
	/* Draw all game content in here */
	public void draw(Graphics g) {
		
		if(level == 0) {
			menu.display(g);
		}
		if(level == 1 || level == 2 || level == 3) {
			stage.display(g);
			p1.display(g);
		}
		
		//Debugging
		g.setFont(new Font("Arial", Font.BOLD, 11));
		g.setColor(Color.BLACK);
		
	}
	
	private void paintScreen() {
		Graphics g;
		try {
			g = this.getGraphics();
			if(dbImage != null && g != null) {
				g.drawImage(dbImage, 0, 0, null);
			}
		}catch(Exception e) {
			System.err.println(e);
		}
	}
	
	public void addNotify() {
		
		super.addNotify();
		startGame();
		
	}
	
	private void startGame() {
		
		if(game == null || !running) {
			game = new Thread(this);
			game.start();
			running = true;
		}
		
	}
	
	public void stopGame() {
		
		if(running) {
			running = false;
		}
		
	}
	
	public void log(String s) {
		System.out.println(s);
	}

}
