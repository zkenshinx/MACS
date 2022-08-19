/*
 * File: MidpointFindingKarel.java
 */

import stanford.karel.*;

public class MidpointFindingKarel extends SuperKarel {
	
	public void run() {
		putBeeperInTheMiddle();
		removeTheRemainingBeepers();
	}
	
	// Karel puts beeper in the middle of the first row
	// Precondition: Karel stands at 1x1 block, Facing east
	// Postcondition: Karel stands at the middle of the first row, Facing West
	private void putBeeperInTheMiddle() {
		putBeepersOnDiagonal();
		goDown();
		searchForMiddleDiagonalBeeper();
		putBeeperDown();
	}
	
	// Karel puts beepers on diagonal
	// Precondition: Karel stands at 1x1 block, Facing East
	// Postcondition: Karel stands at the top right corner, Facing east
	private void putBeepersOnDiagonal() {
		while(leftIsClear()) {
			putBeeper();
			move();
			turnLeft();
			move();
			turnRight();
		}
		putBeeper();
	}	
	
	// Karel goes at the bottom of the column
	// Precondition: Karel stands somewhere at the column, Facing East
	// Postcondition: Karel stands at the bottom of the column, Facing West
	private void goDown() {
		turnRight();
		while(frontIsClear()) {
			move();
		}
		turnRight();
	}
	
	// Karel searches for the diagonal's middle beeper 
	// (or one of the middle beeper in case of even by even world)
	// Precondition: Karel stands at the end of the first row, Facing West
	// Postcondition: Karel stands at the middle of the diagonal, Facing West
	private void searchForMiddleDiagonalBeeper() {
		while (noBeepersPresent()) {
			move();
			if (noBeepersPresent()) {
				turnRight();
				move();
				turnLeft();
			}
		}
	}
	
	// Karel goes at the bottom of the column and puts beeper
	// Precondition: Karel stands at the middle of the diagonal, Facing West
	// Postcondition: Karel stands at the middle of the first row, Facing West
	private void putBeeperDown() {
		turnAround();
		goDown();
		putBeeper();
	}
	
	// Karel removes all beepers except the one in the middle of the first row
	// Precondition: Karel stands at the middle of the first row, Facing West
	// Postcondition: Karel stands at the top right corner, Facing East
	private void removeTheRemainingBeepers() {
		goBack();
		removeBeepersFromDiagonal();
	}
	
	// Karel goes back to the first block of the row
	// Precondition: Karel stands at the middle of the first row, Facing West
	// Postcondition: Karel stands at the first block of the first row, Facing East
	private void goBack() {
		while(frontIsClear()) {
			move();
		}
		turnAround();
	}
	
	// Karel removes all beepers on the diagonal
	// Precondition: Karel stands at 1x1 block, Facing East
	// Postcondition: Karel stands at the top right corner, Facing east
	private void removeBeepersFromDiagonal() {
		while(leftIsClear()) {
			pickBeeper();
			move();
			turnLeft();
			move();
			turnRight();
		}
		pickBeeper();
	}
}
