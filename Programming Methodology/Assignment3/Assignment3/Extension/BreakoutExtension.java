/*
 * File: Breakout.java
 * -------------------
 * Name: Koba Karaputadze
 * Section Leader: Bakari Gamezardashvili
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class BreakoutExtension extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
	
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
	private GRect paddle;
	
	
	// The object which is collider (The ball collided to)
	private GObject collider;
	
	// Ball characteristics
	private GImage ball;
	private double ballX, ballY; 	// X and Y Coordinates of 
	private double vx, vy;	// Ball Speed
	
	// Lives left
	private int lifes = NTURNS;
	
	// State of game (Is it ended or not)
	private boolean gameEnded = false;
	private boolean gameStart = false;
	
	// How many bricks are left
	private int bricksLeft = NBRICKS_PER_ROW * NBRICK_ROWS;
	
	private GLabel message;
	
	// Background image
	private GImage background;
	
	// Side of a button
	private double buttonSize = 75;
	
	// Game starter button
	private GRect startGame;
	
	// Background changer button
	private GRect changeBack;
	
	// Checks if any of the menu buttons were clicked or not
	private boolean clickedWrong = true;
	
	// Checks if the background has been changed
	private boolean changeBackground = false;
	
	// Checks if we have opened the background menu
	private boolean inBackgroundMenu = false;
	
	// Health bar
	private GImage healthBar;
	
	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		addMouseListeners();
		menu();
		while (true) {
			if (inBackgroundMenu == false && gameEnded == false) {
				menu();
			}
		}
	}
	
	// Opens menu, where you can choose starting game or changing background
	private void menu() {
		button("Start Game", getWidth()/2 - 3*buttonSize/2, (getHeight() - buttonSize)/2, 1);
		button("Background", getWidth()/2 + buttonSize/2, (getHeight() - buttonSize)/2, 2);
		waitForClick();
		while (clickedWrong == true){
			waitForClick();
		} 
		if (gameStart == true) {
			gameStart();
		} else if (changeBackground == true) {
			changeBackground();
		}
	}
	
	// Creates buttons for Starting game and Changing background
	private void button(String str, double x, double y, int num) {
		GRect rect = new GRect(x, y, buttonSize, buttonSize);
		rect.setFilled(true);
		rect.setColor(Color.CYAN);
		add(rect);
		if (num == 1) startGame = rect;
		else changeBack = rect;
		GLabel label = new GLabel(str);
		label.setLocation(x+(buttonSize-label.getWidth())/2,y+(buttonSize+label.getAscent())/2);
		add(label);
	}
	
	// Starts the game, Adds Background, Draws Bricks, paddle and health bar
	private void gameStart() {
		changeBack = null;
		startGame = null;
		addBackground();
		drawBricks();
		addHealthBar();
		drawPaddle();
		game();
	}
	
	
	private void game() {
		giveSpeed();
		drawBall();
		startMessage();
		while(gameEnded == false) {
			updateWorld();
		} 
		gameEndMessage();
	}
	
	// adds images in the Background menu to choose from
	private void changeBackground() {
		double xSize = getWidth()/3, ySize = getHeight()/3;
		addImageOnBoard("background1.png", xSize, ySize, 1);
		addImageOnBoard("background2.png", xSize, ySize, 2);
		addImageOnBoard("background3.png", xSize, ySize, 3);
		addImageOnBoard("background4.png", xSize, ySize, 4);
	}
	
	// adds an image at the corresponding x, y coordinates
	private void addImageOnBoard(String str,double xSize, double ySize, int i) {
		GImage back = new GImage(str);
		back.setSize(xSize, ySize);
		double xCoordinate = 0, yCoordinate = 0;
		if (i == 1 || i == 3) {
			xCoordinate = getWidth()/2 - 4*back.getWidth()/3;
		} else {
			xCoordinate = getWidth()/2 + back.getWidth()/3;
		} if (i == 1 || i == 2) {
			yCoordinate = getHeight()/2 - 4*back.getHeight()/3;
		} else {
			yCoordinate = getHeight()/2 + back.getHeight()/3;
		}
		add(back, xCoordinate, yCoordinate);
	}
	
	// Draws Bricks at the corresponding coordinates and with the corresponding width and height
	private void drawBricks() {
		for (int i = 0; i < NBRICK_ROWS; i++) {
			for (int j = 0; j < NBRICKS_PER_ROW; j++) {
				GRect rect = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
				rect.setFilled(true);
				rect.setFillColor(ColoriseBrick(i));
				double startX = (getWidth() - (NBRICKS_PER_ROW * BRICK_WIDTH +  
							( NBRICKS_PER_ROW - 1) * BRICK_SEP) ) /2 + j * (BRICK_WIDTH + BRICK_SEP );
				double startY = BRICK_Y_OFFSET + i * (BRICK_SEP + BRICK_HEIGHT);
				add(rect, startX, startY);
				pause(10);
			}	
		}
	}
		
	// Colors the bricks in the following order: Red, Orange, Yellow, Green, Cyan
	private Color ColoriseBrick(int i) {
		if ( i % 10 <= 1) return Color.RED;
		else if ( i % 10 <= 3) return Color.ORANGE;
		else if ( i % 10 <= 5) return Color.YELLOW;
		else if ( i % 10 <= 7) return Color.GREEN;
		else return Color.CYAN;	
	}
		
	// Add health bar at the top right corner
	private void addHealthBar() {
		if (lifes == 3) {
			addBar("HealthBar3.png");
		} else if (lifes == 2) {
			addBar("HealthBar2.png");
		} else if (lifes == 1){
			addBar("HealthBar1.png");
		} else {
			remove(healthBar);
		}
	}
	
	
	private void addBar(String str) {
		if (lifes != 3) remove(healthBar);
		healthBar = new GImage(str);
		healthBar.setSize(90,30);
		add(healthBar, getWidth() - healthBar.getWidth(), 0);
	}
	
	
	public void mousePressed(MouseEvent e) {
		if (startGame.contains(e.getX(), e.getY())) { // Starts the game if startGame button is clicked
			gameStart = true;
			clickedWrong = false;
			removeAll();
		} else if (changeBack.contains(e.getX(), e.getY()))  { // Opens the background changing menu if changeBack button is clicked
			clickedWrong = false;
			removeAll();
			inBackgroundMenu = true;
			changeBackground();
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		 if (inBackgroundMenu == true) { // Adds background the image we clicked
			if (getElementAt(e.getX(), e.getY()) != null) {
				background = (GImage) getElementAt(e.getX(), e.getY());
				removeAll();
				addBackground();
				inBackgroundMenu = false;
			}
		}
	}
	
	// adds background
	private void addBackground() {
		if (background != null) {
			background.setSize(getWidth(), getHeight());
			add(background, 0, 0);
		}
	}

	// Draws a ball with the corresponding width and height
	private void drawPaddle() {
		paddle = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle, ( getWidth() - PADDLE_WIDTH) / 2 , getHeight() - ( PADDLE_Y_OFFSET + PADDLE_HEIGHT ));
	}
	
	// Draws a ball with the corresponding radius in the center of window
	private void drawBall() {
//		ball = new GOval(2*BALL_RADIUS, 2*BALL_RADIUS);
//		ball.setFilled(true);
		ball = new GImage("ballRight.png");
		ball.setSize(2*BALL_RADIUS, 2*BALL_RADIUS);
		int startX = (getWidth() - 2 * BALL_RADIUS)/2;
		int startY = (getHeight() - 2 * BALL_RADIUS) /2 ;
		add(ball, startX, startY);
		ball.sendToFront();
	}
	


	// Message when starting a new game, or after dying and still having a life
	private void startMessage() {
		if (lifes == 3) {
			messageOnScreen("Click to start");
		} else {
			messageOnScreen("Click to start again");
		}
		waitForClick();
		remove(message);
	}
	
	// Gives ball initial speed
	private void giveSpeed() {
		vx = rgen.nextDouble(1.0, 3.0);
		if(rgen.nextBoolean(0.5)) vx = -vx;
		vy = 3.0;
	}
	
	// Updates world every 7 milliseconds
	private void updateWorld() {
		ball.move(vx, vy);
		ballX = ball.getX(); 
		ballY = ball.getY();
		collider = getCollidingObject();
		if (collider != null) {
			collision();
		} 
		changeBall();
		checkBricksLeft();
		checkCollisionWithWall();
		pause(6);
	}
	
	// Ending Message
	private void gameEndMessage() {
		if (lifes != 0) {
			messageOnScreen("Congratulations, You Won!");
		} else {
			messageOnScreen("Game over");
		}
	}
	
	// Writes a message a little below the windows center
	private void messageOnScreen(String str) {
		message = new GLabel(str);
		message.setFont("-30");
		message.setColor(Color.DARK_GRAY);
		add(message, WIDTH / 2 - message.getWidth()/2, HEIGHT / 2 + message.getAscent());
	}
	
	
	// Moves the paddle
	public void mouseMoved(MouseEvent e) {
		while ( e.getX() > paddle.getX() + PADDLE_WIDTH / 2 && paddle.getX() + PADDLE_WIDTH + 1 < getWidth()) {
			paddle.move(1, 0);
		} while ( e.getX() < paddle.getX() + PADDLE_WIDTH / 2 && paddle.getX() > 0) {
			paddle.move(-1, 0);
		}
	}
	
	// Checks Collision With Walls
	private void checkCollisionWithWall() {
		if ( ballX + 2*BALL_RADIUS >= WIDTH || ballX < 0) {
			vx = -vx;
			ball.move(4.0 * (Math.abs(vx)/vx), vy); // Bounces
		} 
		else if ( ballY + 2*BALL_RADIUS > HEIGHT) {
			lifes--;
			remove(ball);
			addHealthBar();
			if (lifes == 0) {
				gameEnded = true;
			} else {
				game();
			}
		} else if ( ballY < 0) {
			vy = -vy;
		}
	}
	
	// Checks Collision
	// If the collider is not a paddle, ball changes direction and removes the brick
	// If the collider is a paddle, ball only changes direction
	private void collision() {
		if (collider == paddle) {
			if ( ball.getX() + BALL_RADIUS - collider.getX() < collider.getWidth()/2 ) {
				vx = -(collider.getWidth()/2 - (ball.getX() + BALL_RADIUS - collider.getX())) / (collider.getWidth()/2) * vy;
			} else {
				vx = (ball.getX() + BALL_RADIUS - collider.getX() - collider.getWidth()/2) / (collider.getWidth()/2) * vy;
			}
		}
		if ( vx > 0) {
			if (ballX + 2*BALL_RADIUS - vx < collider.getX() ) {
				vx = -vx;
			} else {
				vy = -vy;
			}
		} else {
			if (ballX - vx  > collider.getX() + collider.getWidth() - 1) {
				vx = -vx;
			} else {
				vy = -vy;
			}
		}
		if ( collider != paddle) {
			remove(collider);
			bricksLeft--;
		} else {
			if (ballY - vy > paddle.getY()) ball.move(vx, 10.0);
			else ball.move(vx, -5.0);
		}
	}
	
	// Checks if any bricks are left
	private void checkBricksLeft() {
		if (bricksLeft == 0) {
			gameEnded = true;
		}
	}
	
	// Returns collided object, if there is one
	// else returns null
	private GObject getCollidingObject() {
		if ( objChecker(ballX, ballY)) {
			return getElementAt(ballX, ballY);
		} else if (objChecker(ballX + 2*BALL_RADIUS, ballY)) {
			return getElementAt(ballX + 2*BALL_RADIUS, ballY);
		} else if (objChecker(ballX, ballY + 2*BALL_RADIUS)) {
			return getElementAt(ballX, ballY + 2*BALL_RADIUS);
		} else if (objChecker(ballX + 2*BALL_RADIUS, ballY + 2*BALL_RADIUS)) {
			return getElementAt(ballX + 2*BALL_RADIUS, ballY + 2*BALL_RADIUS);
		}
		return null;
	}
	
	// Checks if the object is paddle or a brick
	private boolean objChecker(double x, double y) {
		GObject obj = getElementAt(x,y);
		if (obj != null) {
			if (obj == paddle ) {
				return true;
			} else if ( obj == background || obj == ball || obj == healthBar) {
				return false;
			} else {
				return true;
			}
		} return false;
	}
	
	// Change balls icon according to the direction the ball goes to
	private void changeBall() {
		if (vx > 0) {
			ball.setImage("ballRight.png");
			ball.setSize(2*BALL_RADIUS, 2*BALL_RADIUS);
		} else {
			ball.setImage("ballLeft.png");
			ball.setSize(2*BALL_RADIUS, 2*BALL_RADIUS);
		}
	}
}
	