/*
 * File: CollectNewspaperKarel.java
 */

import stanford.karel.*;

public class CollectNewspaperKarel extends SuperKarel {

	public void run() {
		goToNewspaper();
		pickBeeper();
		returnHome();
	}
	
	// Precondition : Position: 3x4, Direction: East
	// Postcondition : Position: 6x3, Direction: East
	
	private void goToNewspaper() {
		move(); 		
		move(); 
		turnRight();
		move(); 
		turnLeft(); 
		move(); 
	}
	
	// Precondition : Position: 6x3, Direction: East
	// Postcondition : Position: 3x4, Direction: East
	
	private void returnHome() {
		turnAround();
		move(); 
		turnRight();
		move(); 
		turnLeft();
		move(); 
		move(); 
		turnAround();
	}

}
