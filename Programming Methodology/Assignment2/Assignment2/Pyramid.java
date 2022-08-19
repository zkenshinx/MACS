/*
 * File: Pyramid.java
 * Name: Koba Karaputadze
 * Section Leader: Bakari Gamezardashvili
 * ------------------
 * This file is the starter file for the Pyramid problem.
 * It includes definitions of the constants that match the
 * sample run in the assignment, but you should make sure
 * that changing these values causes the generated display
 * to change accordingly.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.RandomGenerator;

import java.awt.*;

public class Pyramid extends GraphicsProgram {

/** Width of each brick in pixels */
	private static final int BRICK_WIDTH = 30;

/** Width of each brick in pixels */
	private static final int BRICK_HEIGHT = 12;

/** Number of bricks in the base of the pyramid */
	private static final int BRICKS_IN_BASE = 14;
	
	public void run() {
		drawPyramid();
	}
	
	// Draws a pyramid with the corresponding characteristics
	private void drawPyramid() {
		for (int j = BRICKS_IN_BASE; j > 0; j--) {
			drawRow(j);
		}
	}
	
	// Draws a centered row with j bricks
	private void drawRow(int j) {
		for (int i = 0; i < j; i++) {
			int startX = ( getWidth() - j * BRICK_WIDTH) / 2 + i * BRICK_WIDTH;
			int startY = getHeight() - (BRICKS_IN_BASE - j + 1) * BRICK_HEIGHT;
			GRect rect = new GRect(startX, startY, BRICK_WIDTH, BRICK_HEIGHT);
			add(rect);
		}
	}	
}

