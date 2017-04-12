import java.awt.Color;
import java.io.*;
import java.util.*;

/* This class will open up file f1 (spacial map plan), read the contents
 * and construct a level with objects at specified x, y positions
 * Then it will open file f2 (bindings) and bind the buttons to their
 * corresponding gates in modes 1 and 2 properly
 */
public class LevelLoader {
	//To Bennett & Shangyu: Don't touch this class!
	
	private static ArrayList<Obstacle> obstacles;
	private static ArrayList<Gate> gates;
	private static ArrayList<Button> buttons;
	
	//IMPORTANT CONSTANTS! DO NOT CHANGE
	public static final char START = '@';
	public static final char FINISH = '$';
	public static final char PLATFORM = 'X';
	
	/* OTHER IMPORTANT DATA:
	 * 
	 * Uppercase = Vertical Gate (cap 25)
	 * Lowercase = Horizontal Gate (cap 26)
	 * Numbers = Buttons (cap 10)
	 * 
	 */

	//convert a coordinate from the 2D array to an x/y position in the game
	private static int scale(int gridPoint) {
		return Level.BLOCK * gridPoint;
	}
	
	private static boolean isHorizontalGate(char c) {
		return Character.isAlphabetic(c) && Character.isLowerCase(c);
	}
	
	private static boolean isVerticalGate(char c) {
		return Character.isAlphabetic(c) && Character.isUpperCase(c) && c != PLATFORM;
	}
	
	private static boolean isButton(char c) {
		return Character.isDigit(c);
	}
	
	//for pairing gate/button
	private static Gate getGateForCode(char code) {
		for (Gate g : gates) {
			if (g.getCode() == code)
				return g;
		}
		return null;
	}
	
	//for pairing gate/button
	private static Button getButtonForCode(char code) {
		for (Button b : buttons) {
			if (b.getCode() == code)
				return b;
		}
		return null;
	}

	//This method is what this class is really for, supreme parsing
	//***All Gates must be 1xN or Nx1 dimensions, 1 in l or w.
	public static Level buildLevel(Game parent, String filename1, String filename2) throws IOException {
		
		//init some shit
		int xStart = 0;
		int yStart = 0;
		
		int xFinish = 0;
		int yFinish = 0;
		
		obstacles = new ArrayList<>();
		gates = new ArrayList<>(); //<---easy to search through, merge w/ obstacles
		buttons = new ArrayList<>();
		
		//this will throw an exception on incorrect file names
	    BufferedReader f = new BufferedReader(new FileReader(filename1));
	    BufferedReader f2 = new BufferedReader(new FileReader(filename2));

	    //find dimensions for the char matrix
	    int rows = 0;
	    int maxcols = 0;
	    String line = f.readLine();
	    
	    while (line != null) {
	    	int len = line.length();
	    	if (len > maxcols)
	    		maxcols = len;
	    	rows++;
	    	line = f.readLine();
	    }
	    
	    //reinitialize, now actually make the matrix
	    f = new BufferedReader(new FileReader(filename1));

	    //the reason we make the matrix and not go line by line
	    //is the collision detection algorithm
	    char[][] matrix = new char[rows][maxcols];
	    	    
	    int r = 0;
	    line = f.readLine();	    
	    while (line != null) {
	    	char[] rowchars = line.toCharArray();
	    	for (int col = 0; col < rowchars.length; col++) {
	    		matrix[r][col] = rowchars[col];
	    	}
	    	
	    	//fill in the rest of the matrix with spaces
	    	for (int fills = rowchars.length; fills < matrix[r].length; fills++)
	    		matrix[r][fills] = ' ';
	 
	    	r++;
	    	line = f.readLine();
	    }
	    
	    //loop through entire reader
	    for (int i = 0; i < matrix.length; i++) {
	    	int y = scale(i);
	    	for (int j = 0; j < matrix[i].length; j++) {
	    		int x = scale(j); //scale to correct coordinates
	    		
	    		char c = matrix[i][j];
	    		//Now we can work with a single char and do whatever
	    		
	    		if (c == START) {
	    			xStart = x;
	    			yStart = y;
	    		} else if (c == FINISH) {
	    			xFinish = x;
	    			yFinish = y;
	    			
	    		} else if (c == PLATFORM) {
	    			//check the  sides, works bc java short circuits yeah!
	    			String colRects = "";
	    			if (i <= 0 || matrix[i-1][j] != PLATFORM) //no block above it
	    				colRects += "T";
	    			if (i >= matrix.length - 1 || matrix[i+1][j] != PLATFORM) //bottom of it
	    				colRects += "B";
	    			if (j <= 0 || matrix[i][j-1] != PLATFORM) //left of it
	    				colRects += "L";
	    			if (j >= matrix[i].length - 1 || matrix[i][j+1] != PLATFORM) //right
	    				colRects += "R";

	    			obstacles.add(new Obstacle(colRects, Color.WHITE, x, y, Level.BLOCK, Level.BLOCK));
	    			
	    		} else if (isHorizontalGate(c)) {	    			
	    			char left = matrix[i][j-1];
	    			
	    			if (isHorizontalGate(left)) { //extend the previous one, same one
	    				Gate sameGate = getGateForCode(left);
	    				sameGate.extendMaxW(Level.BLOCK);
	    				sameGate.extendRefresh();
	    			} else 
	    				gates.add(new Gate(c, Color.LIGHT_GRAY, x, y, Level.BLOCK, Level.BLOCK, false));
	    			
	    		} else if (isVerticalGate(c)) {	    			
	    			char top = matrix[i-1][j];
	    			
	    			if (isVerticalGate(top)) {
	    				Gate sameGate = getGateForCode(top);
	    				sameGate.extendMaxH(Level.BLOCK);
	    				sameGate.extendRefresh();
	    			} else 
	    				gates.add(new Gate(c, Color.LIGHT_GRAY, x, y, Level.BLOCK, Level.BLOCK, true));
	    			
	    		} else if (isButton(c)) //later add gates to the arraylist
	    			buttons.add(new Button(c, Level.PURPLE, Level.GOLD, x, y, Level.BLOCK, Level.BLOCK)); 		    	  			    		
	    	}
	    }
	    
	    /* Rules for f2:
	     * The even lines are the mode 1 gates (starts with 0)
	     * The odd lines are the mode 2 gates
	     * The first token is the button code
	     * all other tokens on the line are the gate codes
	     */
	    int i = 0;
	    line = f2.readLine();
	    StringTokenizer st;
	    
	    while (line != null) {
	    	st = new StringTokenizer(line);						
	    	Button thisButton = getButtonForCode(st.nextToken().charAt(0));
	    	Gate g;
	    	while (st.hasMoreTokens()) {
	    		g = getGateForCode(st.nextToken().charAt(0));
	    		if (i % 2 == 1)
	    			thisButton.addGateOne(g);
	    		else
	    			thisButton.addGateTwo(g);
	    	}
	    	
	    	i++;
		    line = f2.readLine();
	    }
	    
	    
	    //finally, for collisions
	    //but check gate collisions separately
	    for (Gate g: gates)
	    	obstacles.add(g);
	    for (Button b : buttons)
	    	obstacles.add(b);
	    
	    //initializing things in the right order, prevents extra checks & null pointers
	    //Like a multi-part constructor
		Level level = new Level(parent, xStart, yStart, xFinish, yFinish);
		level.addObstacles(obstacles);
		level.addButtons(buttons);
		level.startPlayers();
		return level;
	}
}
