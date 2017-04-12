
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import javax.swing.*;

public class Menu extends JPanel implements MouseListener, KeyListener{
	// TODO Your Instance Variables Here
	private Game parent;
	private Image startButton;
	private Rectangle startRect;
			
	public Menu(Game g) {
		
		parent = g;
		
		setBackground(Level.LIGHT_BLUE);
		
		startButton = (new ImageIcon("start button.png")).getImage();
		startRect = new Rectangle(220, 190, 200, 100);
		
	}
	
	public Rectangle getRect()
	{
		return startRect;
	}

	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Font a = new Font("Rockwell Bold", Font.BOLD, 32);
		Color purple = new Color(125, 0 ,125);
		
		int width = getWidth();
		int height = getHeight();
		double ratioX = width/640.0;
		double ratioY = height/480.0;
		
		Graphics2D g2 = (Graphics2D)g;
		AffineTransform at = g2.getTransform();
		g2.scale(ratioX,ratioY);
		

		g.drawLine(0, 360, 640, 360);
		g.setColor(Color.WHITE);
		g.setFont(a);
		g.drawString("SYNERGY", getWidth()/2 - 150, 140);
		g.drawString("PRESS SPACE FOR LEVEL SELECT", 50, 400);
		
		g.drawImage(startButton, 320 - 100, 240 - 50,200,100, this);

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
		
		int width = getWidth();
		int height = getHeight();
		double ratioX = width/640.0;
		double ratioY = height/480.0;
		
		int code = e.getButton();
		int x = e.getX();
		int y = e.getY();
		int unscaledX = (int)(x/ratioX);
		int unscaledY = (int)(y/ratioY);
		
		if ( getRect().contains(unscaledX, unscaledY))
		{
			parent.setLevel(1);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int code = e.getKeyCode();
		
		if (code == KeyEvent.VK_SPACE)
		{
			parent.setSelect();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	// As your program grows, you may want to...
	// 1) Move this main method into its own 'main' class
	// 2) Customize the JFrame by writing a class that extends it, then creating
	// that type of object in your main method instead
	
}

