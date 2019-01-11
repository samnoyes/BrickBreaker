import org.newdawn.slick.geom.Rectangle;

public class Ball {
	private double vx = 100;
	private double vy = 100;
	private int lx = 100;
	private int ly = 250;
	private int cx;
	private int cy;
	private Rectangle rect;
	
	public Ball(double x, double y, int lx, int ly) {
		vx = x;
		vy = y;
		this.lx = lx;
		this.ly = ly;
		rect = new Rectangle(lx,ly,20,20);
		cx = (int)rect.getCenterX();
		cy = (int)rect.getCenterY();
	}
	public void setVelocityX(double x) {
		vx = x;
		updateRect();
	}
	public void setVelocityY(double y) {
		vy = y;
		updateRect();
	}
	public void setLocationX(int x) {
		lx = x;
		updateRect();
	}
	public void setLocationY(int y) {
		ly = y;
		updateRect();
	}
	public int getLocationX() {
		return lx;
	}
	public int getLocationY() {
		return ly;
	}
	public double getVelocityX() {
		return vx;
	}
	public double getVelocityY() {
		return vy;
	}
	public double getCX() {
		return cx;
	}
	public double getCY() {
		return cy;
	}
	public void multiplyVelocityY(int multiplier) {
		vy *= multiplier;
		updateRect();
	}
	public void multiplyVelocityX(int multiplier) {
		vx *= multiplier;
		updateRect();
	}
	public void makeVelocityYPosOrNeg(int multiplier) {
		if (vy * -1<0) {
			if (multiplier < 0) {
				vy*=-1;
			}
		}
		else if (vy * -1>0) {
			if (multiplier > 0) {
				vy*=-1;
			}
		}
		updateRect();
	}
	public void makeVelocityXPosOrNeg(int multiplier) {
		if (vx * -1<0) {
			if (multiplier < 0) {
				vx*=-1;
			}
		}
		else if (vx * -1>0) {
			if (multiplier > 0) {
				vx*=-1;
			}
		}
		updateRect();
	}
	public void updateRect() {
		rect.setX(lx);
		rect.setY(ly);
		cx = (int)rect.getCenterX();
		cy = (int)rect.getCenterY();
	}
	public Rectangle getRect() {
		return rect;
	}
}
