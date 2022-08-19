/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;
import acm.util.ErrorException;

public class HangmanCanvas extends GCanvas {
	
	private double scaffoldY1;
	
	public HangmanCanvas() {

	}
	
/** Resets the display so that only the scaffold appears */
	public void reset() {
		removeAll();
		addScaffold();
	}

/**
 * Updates the word on the screen to correspond to the current
 * state of the game.  The argument string shows what letters have
 * been guessed so far; unguessed letters are indicated by hyphens.
 */
	public void displayWord(String word) {
		if ( getElementAt(getWidth()/4 , 9*getHeight()/10) != null) remove(getElementAt(getWidth()/4 , 9*getHeight()/10));
		GLabel label = new GLabel(word);
		label.setFont("-20");
		add(label, getWidth()/4 , 9*getHeight()/10);
	}

/**
 * Updates the display to correspond to an incorrect guess by the
 * user.  Calling this method causes the next body part to appear
 * on the scaffold and adds the letter to the list of incorrect
 * guesses that appears at the bottom of the window.
 */
	public void noteIncorrectGuess(char letter, int guessesLeft) {
		displayCharacter(letter+"", guessesLeft);	
		addBodyPart(guessesLeft);
	}
	
	private void addScaffold() {
		scaffoldY1 = getHeight()/2 - SCAFFOLD_HEIGHT/2 - 30;
		GLine scaffold = new GLine(getWidth()/2 - BEAM_LENGTH, scaffoldY1, getWidth()/2 - BEAM_LENGTH, scaffoldY1+SCAFFOLD_HEIGHT);
		add(scaffold);
		GLine beam = new GLine(getWidth()/2 - BEAM_LENGTH, scaffoldY1, getWidth()/2, scaffoldY1);
		add(beam);
		GLine rope = new GLine(getWidth()/2, scaffoldY1, getWidth()/2 , scaffoldY1+ROPE_LENGTH);
		add(rope);
	}
	
	private void drawHead() {
		GOval head = new GOval(getWidth()/2-HEAD_RADIUS, scaffoldY1+ROPE_LENGTH, 2*HEAD_RADIUS, 2*HEAD_RADIUS);
		add(head);
	}
	
	private void drawBody() {
		double startY = scaffoldY1+ROPE_LENGTH+2*HEAD_RADIUS+1;
		double endY = scaffoldY1+ROPE_LENGTH+2*HEAD_RADIUS+BODY_LENGTH;
		GLine body = new GLine(getWidth()/2, startY, getWidth()/2, endY);
		add(body);
	}
	
	private void drawLeftHand() {
		double startY = scaffoldY1+ROPE_LENGTH+2*HEAD_RADIUS + ARM_OFFSET_FROM_HEAD;
		GLine upperArm = new GLine(getWidth()/2, startY, getWidth()/2 - UPPER_ARM_LENGTH, startY);
		add(upperArm);
		GLine lowerArm = new GLine(getWidth()/2 - UPPER_ARM_LENGTH, startY, getWidth()/2 - UPPER_ARM_LENGTH, startY+LOWER_ARM_LENGTH);
		add(lowerArm);
	}
	
	private void drawRightHand() {
		double startY = scaffoldY1+ROPE_LENGTH+2*HEAD_RADIUS + ARM_OFFSET_FROM_HEAD;
		GLine upperArm = new GLine(getWidth()/2, startY, getWidth()/2 + UPPER_ARM_LENGTH, startY);
		add(upperArm);
		GLine lowerArm = new GLine(getWidth()/2 + UPPER_ARM_LENGTH, startY, getWidth()/2 + UPPER_ARM_LENGTH, startY+LOWER_ARM_LENGTH);
		add(lowerArm);
	}
	
	private void drawLeftLeg() {
		double startY = scaffoldY1+ROPE_LENGTH+2*HEAD_RADIUS+BODY_LENGTH;
		double endY = scaffoldY1+ROPE_LENGTH+2*HEAD_RADIUS+BODY_LENGTH+LEG_LENGTH;
		GLine hip = new GLine(getWidth()/2, startY, getWidth()/2 - HIP_WIDTH, startY);
		add(hip);
		GLine leg = new GLine(getWidth()/2 - HIP_WIDTH, startY, getWidth()/2-HIP_WIDTH, endY);
		add(leg);
	}
	
	private void drawRightLeg() {
		double startY = scaffoldY1+ROPE_LENGTH+2*HEAD_RADIUS+BODY_LENGTH;
		double endY = scaffoldY1+ROPE_LENGTH+2*HEAD_RADIUS+BODY_LENGTH+LEG_LENGTH;
		GLine hip = new GLine(getWidth()/2, startY, getWidth()/2 + HIP_WIDTH, startY);
		add(hip);
		GLine leg = new GLine(getWidth()/2 + HIP_WIDTH, startY, getWidth()/2+HIP_WIDTH, endY);
		add(leg);
	}
	
	private void drawLeftFoot() {
		double startY = scaffoldY1+ROPE_LENGTH+2*HEAD_RADIUS+BODY_LENGTH+LEG_LENGTH;
		double startX = getWidth()/2 - HIP_WIDTH;
		GLine foot = new GLine(startX, startY, startX - FOOT_LENGTH, startY);
		add(foot);
	}
	
	private void drawRightFoot() {
		double startY = scaffoldY1+ROPE_LENGTH+2*HEAD_RADIUS+BODY_LENGTH+LEG_LENGTH;
		double startX = getWidth()/2 + HIP_WIDTH;
		GLine foot = new GLine(startX, startY, startX + FOOT_LENGTH, startY);
		add(foot);
		
	}
	
	private void displayCharacter(String letter, int guessesLeft) {
		GLabel incorrectLetters = null;
		if (guessesLeft == 7) {
			incorrectLetters = new GLabel(letter, getWidth()/4, 19*getHeight()/20);
			incorrectLetters.setFont("-15");
			add(incorrectLetters);
		} else {
			String letters = ((GLabel) getElementAt(getWidth()/4, 19*getHeight()/20)).getLabel();
			if (!letters.contains(letter)) {
				remove(getElementAt(getWidth()/4, 19*getHeight()/20));
				incorrectLetters = new GLabel(letters+letter, getWidth()/4, 19*getHeight()/20);
				incorrectLetters.setFont("-15");
				add(incorrectLetters);
			}
		}
	}
	
	private void addBodyPart(int guessesLeft) {
		switch (guessesLeft) {
			case 7: drawHead(); break;
			case 6: drawBody(); break;
			case 5: drawLeftHand(); break;
			case 4: drawRightHand(); break;
			case 3: drawLeftLeg(); break;
			case 2: drawRightLeg(); break;
			case 1: drawLeftFoot(); break;
			case 0: drawRightFoot(); break;
			default: break;
	}
		
	}
/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;
	
}
