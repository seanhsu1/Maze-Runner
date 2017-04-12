import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;


public class Button extends Obstacle {

	private boolean isPressed, wasPressed, mode1;
	private ArrayList<Gate> controlledGates;
	private ArrayList<Gate> controlledGates2;
	private Color pressed, unpressed;
	private char code; //<-- For level loading
	
	public Button(Color p, Color u, int x, int y, int w, int h, ArrayList<Gate> g, ArrayList<Gate> g2)
	{
		//to allow easy button creation in level loader
		super(u, x, y + h/2, w, h/2);
		pressed = p;
		unpressed = u;
		controlledGates = g;
		controlledGates2 = g2;
		mode1 = true;

		wasPressed = false;
	}
	
	//for file level
	public Button(char code, Color p, Color u, int x, int y, int w, int h)
	{
		this(p, u, x, y, w, h, new ArrayList<Gate>(), new ArrayList<Gate>());
		this.code = code;
	}
	
	//modes
	public void addGateOne(Gate g)
	{
		controlledGates.add(g);
	}
	
	public void addGateTwo(Gate g) 
	{
		controlledGates2.add(g);

	}

	
	public void collide(Player p, Player z)
	{
		if (getRect().intersects(p.getRect()) || getRect().intersects(z.getRect()) )
		{
			if (!wasPressed)
			{
				if (!isGateAction()) {
					toggleButton();
					wasPressed = true;
				}
			}
		}
		else
		{
			
				wasPressed = false;
		}
	}
	
	//public so resets work
	//otherwise do not call this method
	public void toggleButton()
	{
		mode1 = !mode1;
		
		if (getColor() == unpressed)
		{
			setColor(pressed);
		}
		else
		{
			setColor(unpressed);
		}
		for (Gate o: controlledGates)
		{
			if (mode1)
				o.open();
			else
				o.close();
		}
		for (Gate o: controlledGates2)
		{
			if (mode1)
				o.close();
			else
				o.open();
		}
	}
	
	public char getCode()
	{
		return code;
	}
	
	//so that you don't manipulate toggle while gates are open/closing
	public boolean isGateAction() {
		for (Gate g : controlledGates) {
			if (g.hasAction())
				return true;
		}
		for (Gate g2 : controlledGates2) {
			if (g2.hasAction())
				return true;
		}
		return false;
	}
	
	public boolean isModeOne() {
		return mode1;
	}
	
	public void restartToggle() {
		setColor(unpressed);
		mode1 = true;
	}
	
}
