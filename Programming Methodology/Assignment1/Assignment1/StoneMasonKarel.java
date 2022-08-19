/*
 * File: StoneMasonKarel.java
 */

import stanford.karel.*;

public class StoneMasonKarel extends SuperKarel {

	public void run() {
		while(frontIsClear()) {
			fixArch();
			goDown();
			travelFourBlocksEast();
		}
		fixArch();
		goDown();
	}
	
	// Karel fixes the arch
	// Precondition: Karel stands at the bottom of the arch, Facing East
	// Postcondition: Karel stands at the top of the arch, Facing East
	private void fixArch() {
		turnLeft();
		if (noBeepersPresent()) {
			putBeeper();
		}
		while(frontIsClear()) {
			move();
			if (noBeepersPresent()) {
				putBeeper();
			}
		}
	}
	
	// Karel goes to the the bottom of the arch
	// Precondition: Karel stands at the top of the arch, Facing North
	// Postcondition: Karel stands at the bottom of the arch, Facing East
	private void goDown() {
		turnAround();
		while(frontIsClear()) {
			move();
		}
		turnLeft();
	}
	
	// Karel moves four blocks east
	// Precondition: Karel stands at m*1 block, Facing East
	// Postcondition: Karel stands at (m+4)*1 block, Facing East
	private void travelFourBlocksEast() {
		for(int i = 0; i < 4; i++) {
			move();
		}
	}
}
