
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;


	public class LevelScreen extends JPanel implements KeyListener{
		// TODO Your Instance Variables Here
		private Game parent;
				
		public LevelScreen(Game g) {			
			parent = g;		
			setBackground(Level.LIGHT_BLUE);						
		}
		
		public void paintComponent(Graphics g) {
			
			super.paintComponent(g);
			
			Font a = new Font("Rockwell Bold", Font.BOLD, 30);
			Color purple = new Color(125, 0 ,125);
			
			int width = getWidth();
			int height = getHeight();
			double ratioX = width/640.0;
			double ratioY = height/480.0;
			
			Graphics2D g2 = (Graphics2D)g;
			AffineTransform at = g2.getTransform();
			g2.scale(ratioX,ratioY);
			

			g.setColor(Level.PURPLE);
			g.setFont(a);
			g.drawString("Press the corresponding number key", 20, 40);
			
			g.setColor(Color.WHITE);
			g.drawString("  1. Introduction", 40, 80);
			g.drawString("  2. The Amazing Maze (Easy)", 40, 110);
			g.drawString("  3. The Fort (Easy)", 40, 140);
			g.drawString("  4. The Bridge (Easy)", 40, 170);
			g.drawString("  5. A Very Nas Level (Easy)", 40, 210);
			g.drawString("  6. Control Tower (Medium)", 40, 240);
			g.drawString("  7. Double Run (Medium)", 40, 270);
			g.drawString("  8. Area Lock (Medium)", 40, 300);		
			g.drawString("  9. Permute (Medium)", 40, 330);	
			g.drawString("  0. The Building (Hard)", 40, 360);	
			g.drawString("  X. Return Pipe (Hard)", 40, 390);	
			g.drawString("  Y. Clockwork (Hard)", 40, 420);	
			
	
			}
			
		

		@Override
		public void keyPressed(KeyEvent e) {
			int code = e.getKeyCode();
			
			if (code == KeyEvent.VK_1) {
				parent.setLevel(1);
			}
			else if (code == KeyEvent.VK_2)
			{
				parent.setLevel(2);
			}
			else if (code == KeyEvent.VK_3)
			{
				parent.setLevel(3);
			}
			else if (code == KeyEvent.VK_4)
			{
				parent.setLevel(4);
			}
			else if (code == KeyEvent.VK_5)
			{
				parent.setLevel(5);
			}
			else if (code == KeyEvent.VK_6)
			{
				parent.setLevel(6);
			}
			else if (code == KeyEvent.VK_7)
			{
				parent.setLevel(7);
			}		
			else if (code == KeyEvent.VK_8)
			{
				parent.setLevel(8);
			}	
			else if (code == KeyEvent.VK_9)
			{
				parent.setLevel(9);
			}	
			else if (code == KeyEvent.VK_0)
			{
				parent.setLevel(10);
			}	
			else if (code == KeyEvent.VK_X)
			{
				parent.setLevel(11);
			}	
			else if (code == KeyEvent.VK_Y)
			{
				parent.setLevel(12);
			}	
			else if (code == KeyEvent.VK_ESCAPE)
			{
				parent.setMenu();
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {		
		}

		@Override
		public void keyTyped(KeyEvent arg0) {		
		}

		// As your program grows, you may want to...
		// 1) Move this main method into its own 'main' class
		// 2) Customize the JFrame by writing a class that extends it, then creating
		// that type of object in your main method instead
		
	}



