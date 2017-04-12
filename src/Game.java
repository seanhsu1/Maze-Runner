import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

/* To stop level:
 * doRun = false
 * 
 * To start a level:
 * set doRun to true
 * call Run() <-- make sure only one instance is running
 * 
 * 
 */
public class Game {
	
	JPanel cards;
	CardLayout cardLayout;
	private ArrayList<Level> level;
	private int levelNum, numLevels;
	private String[] levelPlans = {
			"test.txt", //easy
			"amazingmaze.txt", //easy
			"test2.txt", //easy
			"levelBridge.txt", //easy
			"level3.txt", //medium
			"controltower.txt", //medium
			"doublerun.txt", //medium
			"arealock.txt", //medium
			"permute.txt", //medium
			"levelDescend.txt", //hard
			"pipe.txt", //hard
			"clockwork.txt", //very hard

	};
	
	private String[] levelDataFiles = {
			"testData1.txt",
			"amazingmazeData.txt",
			"testData2.txt",
			"levelBridgeData.txt",
			"levelData3.txt",
			"controltowerData.txt",
			"doublerunData.txt",
			"arealockData.txt",
			"permuteData.txt",
			"levelDescendData.txt",
			"pipeData.txt",
			"clockworkData.txt",

	};
	
	private LevelScreen select;
	private Menu menu;
	private JFrame window;
	private boolean doRun;
	
	public Game() throws IOException {
		
		//init the level
		
		levelNum = 0;
		numLevels = levelPlans.length;
		level = new ArrayList<Level>();
		for(int i = 0; i < numLevels; i++)
			try {
				level.add(LevelLoader.buildLevel(this, levelPlans[i], levelDataFiles[i]));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		
		
		
		//run
		doRun = true;	
		menu = new Menu(this);
		select = new LevelScreen(this);
		cards = new JPanel(new CardLayout());
		
		cards.add(menu, "menu");
		cards.add(select, "select");
		for (int i = 0; i < numLevels; i++)
		{
			cards.add(level.get(i), i + 1 + "");
		}
		
		//standard JFrames
		
		window = new JFrame("Synergy");
		window.setBounds(100, 100, 800, 600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		window.setResizable(true);
		window.setVisible(true);
		window.add(cards);
		window.addMouseListener(menu);
		window.addKeyListener(menu);
		cardLayout = (CardLayout) cards.getLayout();
		
	}
	
	public void run(){
		while(doRun){
			 	if(levelNum != 0)
				 {
			 		level.get(levelNum - 1).update();
				 }
			
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getLevelNum()
	{
		return levelNum;
	}
	
	
	public void setSelect() 
	{
		levelNum = 0;
		cardLayout.show(cards, "select");
		window.removeKeyListener(menu);
		window.removeMouseListener(menu);
		window.addKeyListener(select);
		
	}
	
	public void setMenu() 
	{
		if (levelNum != 0)
		{
			
				for (Level l: level)
				{
					 l.restartLevel();
					 
				}
				window.removeKeyListener(level.get(levelNum - 1));
		
		}
		cardLayout.show(cards, "menu");
		window.addKeyListener(menu);
		window.addMouseListener(menu);
		window.removeKeyListener(select);
		
		levelNum = 0;
	}
	


	
	public void setLevel(int n)
	{
		levelNum = n;
		cardLayout.show(cards, n + "");
		window.removeKeyListener(menu);
		window.removeMouseListener(menu);
		window.removeKeyListener(select);
		window.addKeyListener(level.get(n-1));
		
	
	}

	
	
	
	
	//if there are no more levels, win, otherwise go to next
	public void nextLevel() throws IOException {
		
		if (levelNum < level.size()) {
			levelNum++;
			window.removeKeyListener(level.get(levelNum-2));
			window.addKeyListener(level.get(levelNum-1));
			cardLayout.show(cards, levelNum + "");
		} else
			win();
	}
	
	public void win(){
		System.out.println("Congratulations! You have beat the game.");
		doRun = false;
		//setMenu();	
		cards.add(new JPanel() {
			
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.BLACK);
				g.setFont(new Font("Rockwell Bold", Font.BOLD, 30));
				g.drawString("YOU WIN", getWidth()/2 - 50, getHeight()/2);
			}
		}, "winner");
		cardLayout.show(cards, "winner");
	}
	
	//go!
	public static void main (String[] args){
		try {
			Game game = new Game();
			game.run();
		} catch (IOException e) {
			System.out.println("Something messed up.");
			e.printStackTrace();
		}
	}
}
