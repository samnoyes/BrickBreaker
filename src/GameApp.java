
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;


public class GameApp extends BasicGame implements MouseListener {
	int ROWS = 3;
	int COLUMNS  = 10;
	int SCREEN_HEIGHT = 600;
	int SCREEN_WIDTH = 1200;
	int STARTING_PADDLE_X = 338;
	int locationX = STARTING_PADDLE_X;
	int locationY = (SCREEN_HEIGHT/8)*7;
	boolean rightPressed = false;
	boolean leftPressed = false;
	boolean gameOver = false;
	boolean won = false;
	boolean paused = false;
	int lives = 3;
	int BRICK_WIDTH = SCREEN_WIDTH/COLUMNS-10;
	int BRICK_HEIGHT = SCREEN_HEIGHT/15-10;
	int PADDLE_WIDTH = 150;
	int PADDLE_HEIGHT = 10;
	int BALL_LOCATION_X = locationX+PADDLE_WIDTH/2;
	int BALL_LOCATION_Y = locationY-5;
	double BVX = .5;
	double BVY = .5;
	Rectangle r = new Rectangle(SCREEN_WIDTH/2,SCREEN_HEIGHT/2, 100,10);
	Ball ball = new Ball(BVX,BVY, BALL_LOCATION_X, BALL_LOCATION_Y);
	ColoredRectangle [][] bricks = new ColoredRectangle[COLUMNS][ROWS];
	AppGameContainer ac;
	Rectangle paddle = new Rectangle(locationX,locationY,PADDLE_WIDTH,PADDLE_HEIGHT);
	
	public GameApp(String title) {
		super(title);
		start();
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws SlickException {
		GameApp app = new GameApp("Brick Breaker v2.1");
		app.ac = new AppGameContainer(app);
		app.ac.setDisplayMode(app.SCREEN_WIDTH,app.SCREEN_HEIGHT,false);
		app.ac.setTargetFrameRate(60);
		app.ac.setShowFPS(false);
		app.ac.start();
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		// TODO Auto-generated method stub
		}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		if (!paused){
		// TODO Auto-generated method stub
		if (rightPressed && locationX<=SCREEN_WIDTH-PADDLE_WIDTH) {//if  right key is pressed but paddle still on screen
			locationX += delta;
			paddle.setX(locationX);
		}
		if (leftPressed && locationX>=0) {//if left key is pressed but still on screen
			locationX -= delta;
			paddle.setX(locationX);	
		}
		if (ball.getRect().intersects(paddle)) {//ball bouncing off of paddle
			ball.multiplyVelocityY(-1);
			int centered = rightOrLeftCentered((int)paddle.getX(), (int)ball.getRect().getCenterX(), PADDLE_WIDTH);//return which side ball is on paddle
			if (centered != 0) {
				ball.setVelocityX(centered*BVX);
			}
			if (ball.getVelocityY()<0) {//ball is hitting top of paddle
				ball.setLocationY((int)(paddle.getY()-ball.getRect().getHeight()));//move ball back on top of paddle
			}
			else if (ball.getVelocityY()>0) {//ball is hitting bottom of paddle
				ball.setLocationY((int)(paddle.getY()+paddle.getHeight()));
			}
		}
		if (ball.getRect().getX()<=0 || ball.getRect().getX()>=SCREEN_WIDTH-ball.getRect().getWidth()) {//ball is off side of screen so move back and bounce
			ball.multiplyVelocityX(-1);
			if (ball.getRect().getX()<=0)
				ball.setLocationX(0);
			else
				ball.setLocationX(SCREEN_WIDTH-(int)ball.getRect().getWidth());
		}
		if (ball.getRect().getY()<=0 || ball.getRect().getY()>=SCREEN_HEIGHT-ball.getRect().getHeight()) {//ball is off top or bottom of screen, so move back and bounce and lose life if bounced on bottom
			ball.multiplyVelocityY(-1);
			if (ball.getRect().getY()<=0)
				ball.setLocationY(0);
				
			else {//ball has bounced on the bottom of the screen
				ball.setLocationY(locationY-(int)ball.getRect().getHeight());//move ball back onto the screen
				ball.setLocationX(locationX+PADDLE_WIDTH/2);
				ball.makeVelocityXPosOrNeg(1);
				loseLife();
				
				if (lives == 0)
					gameOver = true;
			}
		}
		boolean changed = false;
		for (int i = 0; i<COLUMNS; i++) {
			for (int j = 0; j<ROWS; j++) {
				if (bricks[i][j] != null) {//Make sure that brick isn't already destroyed
					if (bricks[i][j].intersects(ball.getRect())) {//Iterate through each brick and see if ball intersects with it
						if ((ball.getCY()>=bricks[i][j].getY()+bricks[i][j].getHeight() ||ball.getCY()<=bricks[i][j].getY())&&ball.getCX()>bricks[i][j].getX() && ball.getCX()<bricks[i][j].getX()+bricks[i][j].getWidth()) {//on top or bottom side of brick
							ball.multiplyVelocityY(-1);
							bricks[i][j] = null;
							changed = true;
							break;
						}
						else if ((ball.getCX()>=bricks[i][j].getX()+bricks[i][j].getWidth() ||ball.getCX()<=bricks[i][j].getX())&&ball.getCY()>bricks[i][j].getY() && ball.getCY()<bricks[i][j].getY()+bricks[i][j].getHeight()) {//on right or left side of brick
							ball.multiplyVelocityX(-1);
							bricks[i][j] = null;
							changed = true;
							break;
						}
						else {//on corner
							boolean left = false;
							boolean up = false;
							if (ball.getCX()-bricks[i][j].getCenterX()<0) {//on left side of brick
								left = true;
							}
							if (ball.getCY()-bricks[i][j].getCenterY()<0) {//on top of brick
								up = true;
							}
							
							//The following makes sure that the ball bounces away from the center of the brick if on the corner
							if (left) {
								ball.makeVelocityXPosOrNeg(-1);
							}
							else {
								ball.makeVelocityXPosOrNeg(1);
							}
							if (up) {
								ball.makeVelocityYPosOrNeg(-1);
							}
							else {
								ball.makeVelocityYPosOrNeg(1);
							}
							bricks[i][j] = null;
							changed = true;
						}
					}	
				}
			}
			if (changed) {
				break;
			}
		}
		ball.setLocationX(ball.getLocationX()+(int)(ball.getVelocityX()*delta));//move ball relative to delta
		ball.setLocationY(ball.getLocationY()+(int)(ball.getVelocityY()*delta));
	}
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		g.setColor(Color.white);
		for (int i = 0; i<COLUMNS; i++) {
			for (int j = 0; j<ROWS; j++) {
				if (bricks[i][j] != null) {//draw only bricks which haven't been hit
					g.setColor(bricks[i][j].getColor());
					g.fill(bricks[i][j]);
				}
			}
		}
		g.setColor(Color.white);
		g.fill(paddle);
		g.fillOval(ball.getRect().getX(), ball.getRect().getY(), ball.getRect().getWidth(), ball.getRect().getHeight());
		g.drawString("Lives: " + lives, 0 ,SCREEN_HEIGHT - 50);
		
		int c = 0;
		for (int k = 0; k<ROWS; k++) {
			for (int d = 0; d<COLUMNS;d++) {
				if (bricks[d][k] == null) {//iterate through all the bricks, and if they are all null it means they have all been hit, so player has won.d
					c++;
				}
			}
		}
		if (c == ROWS*COLUMNS) {
			won = true;
		}
		if (gameOver) {//if game is over, means the player lost, so show the game over button in the die function
			die(g);
		}
		if (won) {//if game is won, show you won button
			win(g);
		}
	}


	@Override
	public void keyPressed(int key, char c) {//when key is pressed, start moving
		// TODO Auto-generated method stub
		if (!paused) {
			if (key == Input.KEY_RIGHT)
				rightPressed = true;
			else if (key == Input.KEY_LEFT)
				leftPressed = true;
		}
		if (key == Input.KEY_P) {
			paused = !paused;
		}
	}

	@Override
	public void keyReleased(int key, char c) {//once key is released, stop moving
		// TODO Auto-generated method stub
		if (key == Input.KEY_RIGHT)
			rightPressed = false;
		else if (key == Input.KEY_LEFT)
			leftPressed = false;
	}
	
	
	public void start() {//resets the game
		gameOver = false;
		won = false;
		locationX = STARTING_PADDLE_X;
		ball = new Ball(BVX,BVY, BALL_LOCATION_X, BALL_LOCATION_Y);
		bricks = new ColoredRectangle[COLUMNS][ROWS];
		paddle = new Rectangle(locationX,locationY,PADDLE_WIDTH,PADDLE_HEIGHT);
		lives = 3;
		for (int i = 0; i<COLUMNS; i++) {
			for (int j = 0; j<ROWS; j++) {
				int r = (int)(Math.random()*255);
				int g = (int)(Math.random()*255);
				int b = (int)(Math.random()*255);
			Color color = new Color((r<=100 ? r + 155: r), (g<=100 ? g + 155: g),(b<=100 ? b + 155: b));
				bricks[i][j] = new ColoredRectangle(i*BRICK_WIDTH+i*10+5,j*BRICK_HEIGHT+j*10+5 + 50,BRICK_WIDTH,BRICK_HEIGHT, color);
			}
		}
	}
	
	public void win(Graphics g) {
		Font f = g.getFont();
		int width = f.getWidth("You won! Click to restart");//get width and height of string to draw rect around it
		int height = f.getHeight("You won! Click to restart");
		g.drawString("You won! Click to restart", SCREEN_WIDTH/2-width/2,SCREEN_HEIGHT/2-height/2);
		ball.multiplyVelocityX(0);
		ball.multiplyVelocityY(0);
		r.setSize(width, height);
		r.setCenterX(SCREEN_WIDTH/2);
		r.setCenterY(SCREEN_HEIGHT/2);
		g.draw(r);//show restart button
	}
	
	public void die(Graphics g) {
		Font f = g.getFont();
		int width = f.getWidth("You lost! Click to restart");
		int height = f.getHeight("You lost! Click to restart");
		g.drawString("You lost! Click to restart", SCREEN_WIDTH/2-width/2,SCREEN_HEIGHT/2-height/2);
		ball.multiplyVelocityX(0);//Sets velocity to 0
		ball.multiplyVelocityY(0);
		r.setSize(width, height);
		r.setCenterX(SCREEN_WIDTH/2);
		r.setCenterY(SCREEN_HEIGHT/2);
		g.draw(r);//show restart button
	}
	
	public void mousePressed(int button,
            int x,
            int y) {
		if (r.contains(Mouse.getX(), Mouse.getY()) && (gameOver || won)) {//if game is over, restart button must be shown, so if the click is in said button, restart the game
			start();
		}
	}

	public void loseLife() {//only take away lives if lives is greater than 0
		if (lives>0) {
			lives--;
		}
	}
	public int rightOrLeftCentered(int paddleC, int ballC, int paddleWidth) {//finds out if 
		if (paddleC-ballC>=-paddleWidth/3) {
			return -1;
		}
		else if (paddleC-ballC>=-((paddleWidth/3)*2)) {
			return 0;
		}
		else  {
			return 1;
		}
	}
	
}
