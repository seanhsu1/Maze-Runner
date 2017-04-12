import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;

import javax.swing.Timer;

/* A resizable obstacle
 * If a player touches a closing gate, they die
 * Animates a change in width or height
 * Controlled by buttons, it does not know its button controllers
 * that logic is in the Button object
 */
public class Gate extends Obstacle {
	
	//max width and height defined so it knows how to resize
	private int maxH;
	private int maxW;
	private boolean isOpen, isVertical;
	
	//for animating open/close
	private Timer timerClose, timerOpen;
	private ActionListener openListener, closeListener;
	
	//its unique text file char code so it can be referenced by buttons
	private char code;
	
	public Gate(Color c, int x, int y, int maxWidth, int maxHeight, boolean vertical) {
		
		super(c, x, y, maxWidth, maxHeight);
		maxH = maxHeight;
		maxW = maxWidth;
		isOpen = true;
		isVertical = vertical;
		
		//gates are a little thinner than obstacles
		//move accordingly
		if (isVertical) {
			moveBy(maxW/4, 0);
			maxW = maxW/2;
		} else {
			moveBy(0, maxH/4);
			maxH = maxH/2;
		}
		setWidth(maxW);
		setHeight(maxH);
		
		//initialize the animations
		openListener  = new ActionListener() {		
			public void actionPerformed(ActionEvent event) {
				if (!isVertical) {
					if (getWidth() < maxW)
						setWidth(getWidth() + 2);
					else
						timerClose.stop();
				} else {
					if (getHeight() < maxH)
						setHeight(getHeight() + 2);
					else
						timerClose.stop();
				}
			}
		};
		timerClose = new Timer(20, openListener);
		
		closeListener  = new ActionListener() {		
			public void actionPerformed(ActionEvent event) {
				if (!isVertical) {
					if (getWidth() > 0)
						setWidth(getWidth() - 2);
					else
						timerOpen.stop();
				} else {
					if (getHeight() > 0)
						setHeight(getHeight() - 2);
					else
						timerOpen.stop();
				}
			}
		};
		timerOpen = new Timer(20, closeListener);

		
	}
	
	public Gate(char code, Color c, int x, int y, int maxWidth, int maxHeight, boolean vertical) {
		this(c, x, y, maxWidth, maxHeight, vertical);
		this.code = code;
	}
	

	//self explanatory
	public void toggle() {
		//prevents weird fluctuations
		if (timerClose.isRunning() || timerOpen.isRunning())
			return;
		
		//toggle
		isOpen = !isOpen;
		if (isOpen) {
			timerClose.start();
		} else {
			timerOpen.start();
		}
		
	}
	
	//do not start opens or closes while it is animating
	public void open() {
		if (timerClose.isRunning() || timerOpen.isRunning())
			return;
		isOpen = false;
		timerClose.start();
	}
	
	public void close() {
		if (timerClose.isRunning() || timerOpen.isRunning())
			return;
		isOpen = true;
		timerOpen.start();
	}
	
	//File level loading stuff
	public void extendMaxW(int moreW) {
		maxW += moreW;
	}
	
	public void extendMaxH(int moreH) {
		maxH += moreH;
	}
	
	public void extendRefresh() {
		setWidth(maxW);
		setHeight(maxH);
	}
	
	public char getCode() {
		return code;
	}
	
	public boolean hasAction() {
		return timerClose.isRunning() || timerOpen.isRunning();
	}
	
	public boolean isOpen() {
		return isOpen;
	}
	
	//to know when to kill
	public boolean isVertical() {
		return isVertical;
	}
	
	//to know when to kill
	public boolean isClosing() {
		return timerClose.isRunning();
	}
		
}
