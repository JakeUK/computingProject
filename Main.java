package testProject;

import java.io.*;

//Imports
import javax.swing.JFrame;

public class Main extends JFrame{

	GamePanel gp;
	
	public Main() {
		
		gp = new GamePanel();
		setSize(gp.GWIDTH, gp.GHEIGHT); // <-- This isn't needed but it means if for some reason the JPanel fails, the window will still open, just blank then the process can be shut down accordingly
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Computing Game");
		setResizable(false);
		add(gp);
		pack();
		
	}
	
	
	public static void main(String[] args)throws Exception {
		System.out.println("Program Running...");
		Main m = new Main();
		
	}

}
