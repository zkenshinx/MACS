/*
 * File: ProgramHierarchy.java
 * Name: Koba Karaputadze
 * Section Leader: Bakari Gamezardashvili
 * ---------------------------
 * This file is the starter file for the ProgramHierarchy problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class ProgramHierarchy extends GraphicsProgram {	
	
	/* Characteristics of frames */
	private static final int FRAME_WIDTH = 200;
	
	private static final int FRAME_HEIGHT = 80;
	
	/* Offset between the upper and bottom middle frame */
	private static final int OFFSET_PROGRAM = 60;
	
	/* Offset between the bottom frames */
	private static final int OFFSET_BETWEEN = 50;
	
	public void run() {
		drawBoxes();
		drawLines();
	}
	
	// Draws the 4 boxes with labels
	private void drawBoxes() {
		double centeredX = (getWidth() - FRAME_WIDTH) / 2;
		double centeredY = (getHeight() - FRAME_HEIGHT) / 2;
		drawBox("Program", centeredX, centeredY - (OFFSET_PROGRAM + FRAME_HEIGHT)/2);
		drawBox("ConsoleProgram", centeredX, centeredY + (OFFSET_PROGRAM + FRAME_HEIGHT)/2);
		drawBox("GraphicsProgram", centeredX - OFFSET_BETWEEN - FRAME_WIDTH,
				centeredY + (OFFSET_PROGRAM + FRAME_HEIGHT)/2);
		drawBox("DialogProgram", centeredX + OFFSET_BETWEEN + FRAME_WIDTH,
				centeredY + (OFFSET_PROGRAM + FRAME_HEIGHT)/2);
		drawLines();
	}
	
	// Draws one one with the corresponding label 
	private void drawBox(String text, double x, double y) {
		GRect rect = new GRect(x, y, FRAME_WIDTH, FRAME_HEIGHT);
		add(rect);
		GLabel label = new GLabel(text);
		label.setFont("-17");
		double x1 = x + (FRAME_WIDTH  - label.getWidth()) / 2;
		double y1 = y + (FRAME_HEIGHT + label.getAscent()) / 2;
		add(label, x1, y1);
	}
	
	// Draws the connecting lines
	private void drawLines() {
		GLine middleLine = new GLine(getWidth()/2, (getHeight()-OFFSET_PROGRAM)/2, 
				getWidth()/2, (getHeight() + OFFSET_PROGRAM)/2);
		add(middleLine);
		GLine leftLine = new GLine(getWidth()/2, (getHeight()-OFFSET_PROGRAM)/2, 
				getWidth()/2 - FRAME_WIDTH - OFFSET_BETWEEN, (getHeight() + OFFSET_PROGRAM)/2);
		add(leftLine);
		GLine rightLine = new GLine(getWidth()/2, (getHeight()-OFFSET_PROGRAM)/2, 
				getWidth()/2 + FRAME_WIDTH + OFFSET_BETWEEN, (getHeight() + OFFSET_PROGRAM)/2);
		add(rightLine);
	}
}

