package testProject;

import java.awt.*;

public class Item {

	public int x, y, type;
	private double itemSizeMultiplier = 0.5;
	public Rectangle item;
	private Image itemImg;
	
	public Item(int x, int y, int width, int height, int type) {
		
		this.type = type;
		this.x = x;
		this.y = y;
		int iWidth = (int)(width * itemSizeMultiplier);
		int iHeight = (int)(height * itemSizeMultiplier);
		
		item = new Rectangle((x + (width / 2)) - (iWidth/2) , (y + (height / 2)) - (iHeight/2), iWidth, iHeight); //Centre the item in the middle of the Tile
		
	}
	
	public void display(Graphics g) {
		
		switch(type) {
		
			default:
				g.setColor(Color.MAGENTA);
				g.fillRect(item.x, item.y, item.width, item.height);
				break;
			case 0:
				//Type 1 of item -- Replace with different image when skinning
				g.setColor(Color.green);
				g.fillRect(item.x, item.y, item.width, item.height);
				break;
			case 1:
				g.setColor(Color.cyan);
				g.fillRect(item.x, item.y, item.width, item.height);
				break;
			case 2:
				g.setColor(Color.yellow);
				g.fillRect(item.x, item.y, item.width, item.height);
				break;
				
		}
		
	}
	
}
