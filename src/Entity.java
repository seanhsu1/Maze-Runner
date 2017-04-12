import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;

public class Entity {

	private int xPos, yPos, width, height, xFacing;
	private boolean isVisible;
	private Image image;
	private Rectangle rect;
	private Color c;
	private String openSides;
	// collision rectangle codes to simplify, these are the array indices
	public static final int TOP = 0, BOTTOM = 1, LEFT = 2, RIGHT = 3;

	public Entity(String image, int x, int y, int w, int h) {
		this((new ImageIcon(image)).getImage(), x, y, w, h);
	}

	public Entity(Image image, int x, int y, int w, int h) {
		this.image = image;
		xFacing = 1;
		moveTo(x, y);
		imageSize(w, h);
		isVisible = true;
		rect = new Rectangle(x, y, w, h);
		openSides="TBLR";
	}

	// for the constructor with custom colors
	public Entity(String collisionSides, Color c, int x, int y, int w, int h) {
		this.c = c;
		xFacing = 1;
		moveTo(x, y);
		imageSize(w, h);
		isVisible = true;
		rect = new Rectangle(x, y, w, h);
		openSides = collisionSides;
	}
	
	public Entity(Color c, int x, int y, int w, int h) {
		this("TBLR", c, x, y, w, h);
	}

	public void draw(Graphics g, int xScreen, int yScreen, ImageObserver i) {
		if (isVisible) {
			if (image != null) {
				if (xFacing == 1)
					g.drawImage(image, xPos - xScreen, yPos - yScreen, width,
							height, i);
				else if (xFacing == 2)
					g.drawImage(image, xPos - xScreen, yPos - yScreen, -width,
							height, i);
			} else {
				g.setColor(c);
				g.fillRect(xPos - xScreen, yPos - yScreen, width, height);
			}
		}
	}

	public void moveTo(int x, int y) {
		xPos = x;
		yPos = y;
	}

	public void moveBy(int x, int y) {
		xPos += x;
		yPos += y;
	}

	public void imageSize(int w, int h) {
		width = w;
		height = h;
	}
	
	public void setWidth(int w){
		width = w;
	}
	
	public void setHeight(int h){
		height = h;
	}

	public void setVisibile(boolean b) {
		isVisible = b;
	}

	public void toggleXFacing() {
		xFacing %= 2;
		xFacing++;
	}
	
	public void setSides(String s){
		openSides = s;
	}

	public boolean isPointInEntity(int mouseX, int mouseY) {
		return mouseX >= xPos && mouseY >= yPos && mouseX <= xPos + width
				&& mouseY <= yPos + height;
	}

	public int getX() {
		return xPos;
	}

	public int getY() {
		return yPos;
	}
	
	public Color getColor() {
		return c;
	}
	
	public void setColor(Color a) {
		c = a;
	}


	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Rectangle getRect() {
		rect.setBounds(xPos, yPos, width, height);
		return rect;
	}

	public Rectangle[] getAllRects() {
		Rectangle t = new Rectangle(xPos, yPos, width, height / 8), 
				b = new Rectangle(xPos, yPos + 7 * height / 8, width, height / 8), 
				l = new Rectangle(xPos, yPos, width / 8, height), 
				r = new Rectangle(xPos + 7 * width / 8, yPos, width / 8, height);
		if (!openSides.contains("T") && !openSides.contains("t"))
			t = null;
		if (!openSides.contains("B") && !openSides.contains("b"))
			b = null;
		if (!openSides.contains("L") && !openSides.contains("l"))
			l = null;
		if (!openSides.contains("R") && !openSides.contains("r"))
			r = null;
		rect.setBounds(xPos, yPos, width, height);
		return new Rectangle[] { t, b, l, r }; //<--Change shifts??
	}
	
}
