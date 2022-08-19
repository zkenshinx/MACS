/*
 * File: CheckerboardKarel.java
 */

import stanford.karel.*;

public class CheckerboardKarel extends SuperKarel {

	public void run() {
		fillTheWorldWithCheckerboardPattern();
	}
	
	// Precondition: Karel stands at 1x1, Facing East
	// Postcondition: Karel stands at the top of the first column, Facing East
	private void fillTheWorldWithCheckerboardPattern() {
		fillRowVersionOne(); // Because of the off-by-one error, fills the first row
		while(leftIsClear()) {
			goUp();
			fillRowVersionTwo();
			if (leftIsClear()) {
				goUp();
				fillRowVersionOne();
			}
		}
	}
	
	// Version one of filling row starts putting beepers from the first block of the row
	// Precondition: Karel Stands at the start of the row, Facing East
	// Postcondition: Karel Stands at the start of the row, Facing East
	private void fillRowVersionOne() {
		putBeeper();
		while(frontIsClear()) {
			move();
			if (frontIsClear()) {
				move();
				putBeeper();
			}
		}
		goBack();
	}
	
	// Version two of filling row starts putting beepers from the second block of the row
	// Precondition: Karel Stands at the start of the row, Facing East
	// Postcondition: Karel Stands at the start of the row, Facing East
	private void fillRowVersionTwo() {
		while(frontIsClear()) {
			move();
			putBeeper();
			if (frontIsClear()) {
				move();
			}
		}
		goBack();
	}
	
	// Goes back from the last block of the row to the first block of the row
	// Precondition: Karel stands at then end of the row, Facing East
	// Postcondition: Karel stands at the start of the row, Facing East
	private void goBack() {
		turnAround();
		while(frontIsClear()) {
			move();
		} 
		turnAround();
	}
	
	// Goes up by one block
	// Precondition: Karel stands at the start of the row, Facing East
	// Postcondition: Karel stands at the start of the upper row, Facing East
	private void goUp() {
		turnLeft();
		move();
		turnRight();
	}
}
