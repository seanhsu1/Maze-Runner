import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/* How to create this class:
 * Constructor
 * addObstacles, buttons
 * Start Players
 * 
 * This is where all the logic happens in a level
 * characters live inside level
 * Level is loaded from level loader, do not construct a Level object
 */
public class Level extends JPanel implements ImageObserver, KeyListener {
	
	private Player player1, player2;
	//xS and yS control screen scrolling
	private int xS, yS, frameWidth, frameHeight;
	
	private ArrayList<Obstacle> obstacles;
	private ArrayList<Button> buttons;
	
	private int xStart, yStart, xFinish, yFinish;
	private Rectangle winRect;
	private boolean hasWon;
	private Game parent;
	private Image finishFlag;
	
	private boolean[] p1movement, p2movement; //0 = left, 1 = right, 2 = top
	private int setLevel;
	
	//Constants
	public static final int BLOCK = 32; //fileIO will init into blocks
	private static final int SQUARE_SIZE = BLOCK - 4; //preferably divisible by 8
	
	public static final Color LIGHT_BLUE = new Color(25, 180, 250);
	public static final Color RED_SHADE = new Color(240, 105, 105);
	public static final Color EMERALD = new Color(40, 200, 110);
	public static final Color GOLD = new Color(220, 165, 30);
	public static final Color PURPLE = new Color(155, 90, 180);

	
							//starting positions
	public Level(Game game, int xStart, int yStart, int xEnd, int yEnd) {		
		
		super();
		setBackground(LIGHT_BLUE);
		
		parent = game;
		this.xStart = xStart;
		this.yStart = yStart;
		xFinish = xEnd;
		yFinish = yEnd;
		hasWon = false;
		
		finishFlag = (new ImageIcon("flag.png")).getImage();
		winRect = new Rectangle(xFinish, yFinish, BLOCK, BLOCK); //<--Same as dimensions as the flag
		
		xS = 0;
		yS = 0;
		setLevel = 0;
		
		/* Prevents bugs while moving in multiple directions
		 * Set booleans while pressing/releasing keys
		 * update movement in update() accordingly
		 */
		p1movement = new boolean[3];
		p2movement = new boolean[3];
		for (int i = 0; i < p1movement.length; i++) {
			p1movement[i] = false;
			p2movement[i] = false;
		}
		//init yS after init players
	}
	
	//this includes the gates
	public void addObstacles(ArrayList<Obstacle> adds) {
		obstacles = new ArrayList<>(adds);
	}
	
	public void addButtons(ArrayList<Button> adds) {
		buttons = new ArrayList<>(adds);
	}
	
	
	public void startPlayers() {
		player1 = new Player(Color.DARK_GRAY, xStart, yStart, SQUARE_SIZE, SQUARE_SIZE);
		player2 = new Player(RED_SHADE, xStart + 2*SQUARE_SIZE, yStart, SQUARE_SIZE, SQUARE_SIZE);	
		
		yS= player1.getY() - frameHeight/2; //center of the screen
	}
	
	/* left unscaled on purpose
	 * Players can resize to see more / less
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // Call JPanel's paintComponent method
		// to paint the background
		frameWidth = getWidth();
		frameHeight = getHeight();
		//g2.scale(width / 25.0, height / 25.0);
		
		player1.draw(g, xS,yS, this);
		player2.draw(g, xS,yS, this);
		
		//Draw all buttons as well
		for (Button b : buttons)
			b.draw(g, xS, yS, this);
		
		for (Obstacle o: obstacles)
			o.draw(g, xS, yS, this);
		
		//draw the finish flag!				  //width, height
		g.drawImage(finishFlag, xFinish - xS, yFinish - yS - SQUARE_SIZE, 2*SQUARE_SIZE, 2*SQUARE_SIZE, this);
		
		//g2.setTransform(t);	
	}
	
	/* The main controller of the level
	 * updates all entities in the level
	 * notifies parent (Game) when level is completed
	 * repaints Panel;
	 */
	public void update(){
		
  		if (hasWon()) { //go to next level, or finish game.		
  			try {
				parent.nextLevel();  //in case there is no text file handle IOException
			} catch (IOException e) {
				e.printStackTrace();
			}
  		}

  		player1.update(obstacles);
  		player2.update(obstacles);
  		
  		for (Button b : buttons)
  			b.collide(player1, player2);

		xS = getAvg(true) - frameWidth/2;
		yS = getAvg(false) - frameHeight/2; //center of the screen
		checkBlocks(); // <-- calls isDead()
		applyMovements();
		
  		repaint();
	  	
	}
	
	//true = x
	//false = y
	//weighted average scrolling
	public int getAvg(boolean x) {
		if (x)
			return 2*player1.getX()/3 + player2.getX()/3;
		return 2*player1.getY()/3 + player2.getY()/3;
	}
	
	public boolean hasWon() {
		return player1.getRect().intersects(winRect) || player2.getRect().intersects(winRect);
	}
	
	public boolean isDead(Player p) {
		//touching gate kills player if the gate closing
		if (p.hasTouchedGate())
			return true;
		
		//check if out of bounds, by a certain buffer
		int buffer = 4*SQUARE_SIZE;
		int yLow = 0; //the lowest y a player can get to before they are considered dead
		for (Obstacle o: obstacles) {
			if (o.getY() + o.getHeight() >= yLow)
				yLow = o.getY() + o.getHeight();
		} //+ 40 pixel threshold
		yLow += buffer;
		
		return p.getY() >= yLow;
	}
	
	//reset if dead
	public void checkBlocks() {
		if (isDead(player1) || isDead(player2)) {
			restartLevel();
		}
	}
	
	//resets level and all gates to how it was in the beginning
	public void restartLevel() {
		player1 = new Player(Color.DARK_GRAY, xStart, yStart, SQUARE_SIZE, SQUARE_SIZE);
		player2 = new Player(RED_SHADE, xStart + 2*SQUARE_SIZE, yStart, SQUARE_SIZE, SQUARE_SIZE);	
		
		for (Button b : buttons)
			b.restartToggle();
		
		//set all gates closed like in beginning
		//set all buttons to mode 1
		//TODO: open() and close() are switched, change that
		for (Obstacle o : obstacles) {
			if (o instanceof Gate)
				((Gate)o).open();
		}

	}
	
	//modifies boolean array for smoother movement
	//see below
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			// WALK LEFT
			p1movement[0] = true;
	  	} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
	  		// WALK RIGHT
			p1movement[1] = true;
	  	} else if (e.getKeyCode() == KeyEvent.VK_UP) {
	  		// JUMP
			p1movement[2] = true;
	  	} else if (e.getKeyCode() == KeyEvent.VK_A) {
			// WALK LEFT
			p2movement[0] = true;
	  	} else if (e.getKeyCode() == KeyEvent.VK_D) {
	  		// WALK RIGHT
			p2movement[1] = true;
	  	} else if (e.getKeyCode() == KeyEvent.VK_W) {
	  		// JUMP
			p2movement[2] = true;
	  	} else if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
	  		parent.setMenu();
	  	} else if (e.getKeyCode() == KeyEvent.VK_R) {
	  		restartLevel();
	  	}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			// WALK LEFT
			p1movement[0] = false;
	  	} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
	  		// WALK RIGHT
			p1movement[1] = false;
	  	} else if (e.getKeyCode() == KeyEvent.VK_UP) {
	  		// JUMP
			p1movement[2] = false;
	  	}  else if (e.getKeyCode() == KeyEvent.VK_A) {
			// WALK LEFT
			p2movement[0] = false;
	  	} else if (e.getKeyCode() == KeyEvent.VK_D) {
	  		// WALK RIGHT
			p2movement[1] = false;
	  	} else if (e.getKeyCode() == KeyEvent.VK_W) {
			p2movement[2] = false;
	  	} 
		
	}
	
	/* Boolean array is used for motion
	 * Key presses modifies array, update calls this
	 * to check for motion
	 * This way, when a key is pressed, but you hit a wall
	 * vx/y = 0, when you clear the wall, keyPressed() will not
	 * be called but you should still move -> this method
	 * handles moving after collisions well
	 */	
	//0 = left, 1 = right, 2 = jump
	//TODO: Make constants for 0, 1, 2 array indices
	public void applyMovements() {
		if (p1movement[0])
			player1.walk(-1);
		else
			player1.stopWalk(-1);
		
		if (p1movement[1])
			player1.walk(1);
		else
			player1.stopWalk(1);
		
		if (p1movement[2])
			player1.jump();
		else
			player1.stopJump();
		
		
		if (p2movement[0])
			player2.walk(-1);
		else
			player2.stopWalk(-1);
		
		if (p2movement[1])
			player2.walk(1);
		else
			player2.stopWalk(1);
		
		if (p2movement[2])
			player2.jump();
		else
			player2.stopJump();
		
		
	}
	
	@Override
	public void keyTyped(KeyEvent arg0) {}

}
