import java.awt.*;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Player extends Entity {

	private static final double WALK_SPEED = 3, GRAVITY = .15, JUMP_SPEED = -6;
	private double xSpeed, ySpeed;
	private boolean isOnGround, touchedGate = false, isJumping = false;
	private int jumpMode = 1;

	public Player(Image image, int x, int y, int w, int h) {
		super(image, x, y, w, h);
	}

	public Player(String image, int x, int y, int w, int h) {
		this((new ImageIcon(image)).getImage(), x, y, w, h);
	}

	public Player(Color c, int x, int y, int w, int h) {
		super("TBLR", c, x, y, w, h);
	}

	public void update(ArrayList<Obstacle> obstacles) {
		ySpeed += GRAVITY;
		if(ySpeed > 8)
			ySpeed=8;

		if (jumpMode == 1 && isJumping && isOnGround)
			ySpeed = JUMP_SPEED;

		isOnGround = false;

		for (Obstacle o : obstacles) {
			collide(o);
		}
		moveBy((int) xSpeed, (int) ySpeed);

	}

	public void walk(int dir) {
		xSpeed = dir * WALK_SPEED;
	}

	public void stopWalk(int dir) {
		if (xSpeed == dir * WALK_SPEED) {
			xSpeed = 0;
		}
	}

	public void jump() {
		if (jumpMode == 1) {
			isJumping = true;
		} else {
			if (isOnGround) 
				ySpeed = JUMP_SPEED;
			
		}
	}

	public void stopJump() {
		if (jumpMode == 1)
			isJumping = false;
	}

	public void setSpeed(double x, double y) {
		xSpeed = x;
		ySpeed = y;
	}

	private void collide(Obstacle o) {
		
		if (getRect().intersects(o.getRect())) { // if there is a collision
			Rectangle[] pRects = getAllRects(); // of the player
			Rectangle[] oRects = o.getAllRects(); // of the obstacle
			if (oRects[Entity.BOTTOM] != null) {
				if (pRects[Entity.TOP].intersects(oRects[Entity.BOTTOM])) { // collide top
					
					boolean skipGate = o instanceof Gate && ((Gate)o).isVertical();
					//gate kills, use boolean to notify level
					if (skipGate)
						if(((Gate)o).isClosing())
							touchedGate = true;
					
					if (!skipGate) {
						moveTo(getX(), o.getY() + o.getHeight());
						if (ySpeed < 0)
							ySpeed = 0;
					}
				}
			}
			if (oRects[Entity.TOP] != null) {							//under: watch this 
				if ((getMotionYRect().intersects(o.getRect())) && ySpeed >= 0 && getY() < o.getY()) { 
					// collide bottom is on ground
					
					boolean skipGate = o instanceof Gate && ((Gate)o).isVertical();
					if (o instanceof Gate && !skipGate && getY() >= o.getY()) {
							skipGate = true;
					}
					//gate kills, use boolean to notify level
					if (skipGate)
						if(((Gate)o).isClosing())
							touchedGate = true;
					
					if (!skipGate) {
						moveTo(getX(), o.getY() - getHeight() + 1); // so it doesnt
																	// fall through
						if (ySpeed > 0) {
							ySpeed = 0;
							isOnGround = true; //able to jump again
						}
					}

					
				}
			}
			if (oRects[Entity.RIGHT] != null) {
				if (pRects[Entity.LEFT].intersects(oRects[Entity.RIGHT]) && xSpeed <= 0
						&& !pRects[Entity.RIGHT]
								.intersects(oRects[Entity.RIGHT])) { // collide
																		// left
					//gate kills, use boolean to notify level
					boolean skipGate = o instanceof Gate && !((Gate)o).isVertical();
					if (skipGate && ((Gate)o).isClosing())
						touchedGate = true;
					
					if (!skipGate) {
						xSpeed = 0;
						moveTo(o.getX() + o.getWidth(), getY());
					}
				}
			}
			if (oRects[Entity.LEFT] != null) {
				if (pRects[Entity.RIGHT].intersects(oRects[Entity.LEFT]) && xSpeed >= 0
						&& !pRects[Entity.LEFT].intersects(oRects[Entity.LEFT])) { // collide
																					// right
					//gate kills, use boolean to notify level
					boolean skipGate = o instanceof Gate && !((Gate)o).isVertical();
					if (skipGate && ((Gate)o).isClosing())
						touchedGate = true;
					
					if (!skipGate) {
						xSpeed = 0;
						moveTo(o.getX() - getWidth(), getY());
					}
				}
			}
		}
	}

	public boolean isOnGround() {
		return isOnGround;
	}
	
	//returns the motion rectangle
	//which includes both rectangles
	public Rectangle getRect() {
		//based off where it is, where it will be, find a rect encompassing both
		Rectangle thisRect = super.getRect();
		Rectangle nextRect = new Rectangle(getX() + (int)xSpeed, getY() + (int)ySpeed, getWidth(), getHeight());

		//math.min() get smallest x,y in order to increase width and height
		//until the new rect fits both of them, by getting dimen + delta (x or y)
		return new Rectangle(
				Math.min(thisRect.x, nextRect.x),
				Math.min(thisRect.y, nextRect.y),
				thisRect.width + Math.abs(thisRect.x - nextRect.x),
				thisRect.height + Math.abs(thisRect.y - nextRect.y));
	}
	
	public Rectangle getMotionYRect() {
		Rectangle thisRect = super.getRect();
		Rectangle nextRect = new Rectangle(getX(), getY() + (int)ySpeed, getWidth(), getHeight());

		//math.min() get smallest x,y in order to increase width and height
		//until the new rect fits both of them, by getting dimen + delta (x or y)
		return new Rectangle(
				thisRect.x,
				Math.min(thisRect.y, nextRect.y),
				thisRect.width,
				thisRect.height + Math.abs(thisRect.y - nextRect.y));
	}
	
	//Override Entity's for motion Rect
	//Check collisions on the Motion of the rectangle
	public Rectangle[] getAllRects() {
	
		Rectangle motionRect = getRect();
		int xPos = motionRect.x;
		int yPos = motionRect.y;
		int width = motionRect.width;
		int height = motionRect.height;
		
		Rectangle t = new Rectangle(xPos, yPos, width, height / 8), 
				b = new Rectangle(xPos, yPos + 7 * height / 8, width, height / 8), 
				l = new Rectangle(xPos, yPos, width / 8, height), 
				r = new Rectangle(xPos + 7 * width / 8, yPos, width / 8, height);
		return new Rectangle[] {t, b, l, r};
	}
	
	public boolean hasTouchedGate() {
		return touchedGate;
	}

}
