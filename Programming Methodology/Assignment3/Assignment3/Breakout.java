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

public class Breakout extends GraphicsProgram {

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
	private GOval ball; 
	private double ballX, ballY; // X and Y Coordinates of 
	private double vx, vy; 	// Ball Speed

	// Lifes left
	private int lifes = NTURNS;
	
	// State of game (Is it ended or not)
	private boolean gameEnded = false;
	
	// How many bricks are left
	private int bricksLeft = NBRICKS_PER_ROW * NBRICK_ROWS;
	
	private GLabel message;
	
	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		drawBricks();
		drawPaddle();
		addMouseListeners();
		game();
	}

	// Draws Bricks at the corresponding coordinates and with the corresponding width and height
	private void drawBricks() {
		for (int i = 0; i < NBRICK_ROWS; i++) {
			for (int j = 0; j < NBRICKS_PER_ROW; j++) {
				GRect rect_red = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
				rect_red.setFilled(true);
				rect_red.setColor(ColoriseBrick(i));
				double startX = (getWidth() - (NBRICKS_PER_ROW * BRICK_WIDTH +  
							( NBRICKS_PER_ROW - 1) * BRICK_SEP) ) /2 + j * (BRICK_WIDTH + BRICK_SEP );
				double startY = BRICK_Y_OFFSET + i * (BRICK_SEP + BRICK_HEIGHT);
				add(rect_red, startX, startY);
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

	// Draws a paddle with the corresponding width and height
	private void drawPaddle() {
		paddle = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle, ( getWidth() - PADDLE_WIDTH) / 2 , getHeight() - ( PADDLE_Y_OFFSET + PADDLE_HEIGHT ));
	}
	
	// Draws a ball with the corresponding radius in the center of window
	private void drawBall() {
		ball = new GOval(2*BALL_RADIUS, 2*BALL_RADIUS);
		ball.setFilled(true);
		int startX = (getWidth() - 2 * BALL_RADIUS)/2;
		int startY = (getHeight() - 2 * BALL_RADIUS) /2 ;
		add(ball, startX, startY);
		ball.sendToFront();
	}
	
	private void game() {
		drawBall();
		startMessage();
		giveSpeed();
		while(gameEnded == false) {
			updateWorld();
		}
		gameEndMessage();
	}

	// Message when starting a new game, or after dying and still having a life left
	private void startMessage() {
		if (lifes == 3) {
			messageOnScreen("Click to start game");
		} else {
			messageOnScreen("Click to start again, Life left: " + lifes);
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
		checkBricksLeft();
		checkCollisionWithWall();
		pause(7);
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
		if ( ballX + 2*BALL_RADIUS >= WIDTH) {
			vx = -vx;
			ball.move(4.0 * (Math.abs(vx)/vx), vy); // Bounces
		} else if ( ballX < 0) {
			vx = -vx;
			ball.move(4.0 * (Math.abs(vx)/vx), vy); // Bounces
		} else if ( ballY + 2*BALL_RADIUS > HEIGHT) {
			lifes--;
			remove(ball);
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
		if ( collider != paddle ) {
			remove(collider);
			bricksLeft--;
		} else {
			ball.move(vx, -5.0);
		}
	}
	

	// Returns collided object, if there is one
	// else returns null
	private GObject getCollidingObject() {
		if (getElementAt(ballX, ballY) != null) {
			return getElementAt(ballX, ballY);
		} else if (getElementAt(ballX + 2*BALL_RADIUS, ballY) != null) {
			return getElementAt(ballX + 2*BALL_RADIUS, ballY);
		} else if (getElementAt(ballX, ballY + 2*BALL_RADIUS) != null) {
			return getElementAt(ballX, ballY + 2*BALL_RADIUS);
		} else if (getElementAt(ballX + 2*BALL_RADIUS, ballY + 2*BALL_RADIUS) != null) {
			return getElementAt(ballX + 2*BALL_RADIUS, ballY + 2*BALL_RADIUS);
		}
		return null;
	}
	
	// Checks if any bricks are left, if not the game ends
	private void checkBricksLeft() {
		if (bricksLeft == 0) {
			gameEnded = true;
		}
	}
	
	// Ending Message
	private void gameEndMessage() {
		if (lifes != 0) {
			messageOnScreen("Congratulations, You Won!");
		} else {
			messageOnScreen("Game over");
		}
	}
}
	