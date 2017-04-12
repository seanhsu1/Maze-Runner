import java.awt.*;
import javax.swing.ImageIcon;


public class Obstacle extends Entity {

	public Obstacle(Image image, int x, int y, int w, int h) {
		super(image, x, y, w, h);
		// TODO Auto-generated constructor stub
	}
	public Obstacle(String image, int x, int y, int w, int h) {
		this((new ImageIcon(image)).getImage(), x, y, w, h);
	}

	public Obstacle(String sides, Color c, int x, int y, int w, int h) {
		super(sides, c, x, y, w, h);
	}
	public Obstacle(Color c, int x, int y, int w, int h) {
		super("TBLR", c, x, y, w, h);
	}
}
