/*
 * File: Target.java
 * Name: Koba Karaputadze
 * Section Leader: Bakari Gamezardashvili
 * -----------------
 * This file is the starter file for the Target problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Target extends GraphicsProgram {	
	
	/** Characteristics of the big red oval */
	private static final double BIG_RADIUS = 72	;
	private static final Color BIG_COLOR = Color.RED;
	
	/** Characteristics of the medium white oval */
	private static final double MEDIUM_RADIUS = 46.7; // 72/2.54 = x/1.65 --> x ~ 46.7
	private static final Color MEDIUM_COLOR = Color.WHITE;
	
	/** Characteristics of the small red oval */
	private static final double SMALL_RADIUS = 21.5;  // 72/2.54 = x/0.76 --> x ~ 21.5
	private static final Color SMALL_COLOR = Color.RED;

	public void run() {
		drawCircle(BIG_RADIUS, BIG_COLOR);
		drawCircle(MEDIUM_RADIUS, MEDIUM_COLOR);
		drawCircle(SMALL_RADIUS, SMALL_COLOR);
	}
	
	// Draws a centered circle with the corresponding radius and color
	private void drawCircle(double radius, Color color) {
		double startX = (getWidth() - radius) / 2;
		double startY = (getHeight() - radius) / 2;
		
		GOval oval = new GOval(startX, startY, radius, radius);
		oval.setFilled(true);
		oval.setColor(color);
		add(oval);
	}
}
