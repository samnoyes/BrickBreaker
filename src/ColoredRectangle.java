import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;


public class ColoredRectangle extends Rectangle {
	private Color color;
	public ColoredRectangle(float x, float y, float width, float height, Color color) {
		super(x, y, width, height);
		this.color = color;
		// TODO Auto-generated constructor stub
	}
	public Color getColor() {
		return color;
	}
}
