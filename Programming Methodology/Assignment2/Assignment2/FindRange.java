/*
 * File: FindRange.java
 * Name: Koba Karaputadze
 * Section Leader: Bakari Gamezardashvili
 * --------------------
 * This file is the starter file for the FindRange problem.
 */

import acm.program.*;

public class FindRange extends ConsoleProgram {
	
	// Stops the findMinMax proccess when it is inputted
	private static final int BREAK_SYMBOL = 0;
	
	public void run() {
		println("This program finds the largest and smallest numbers.");
		findMinMax();
	}
	
	private void findMinMax() {
		// The first input is both the max and min number at first
		int max = readInt("Enter a number: ");
		while (max == BREAK_SYMBOL) {
			println("You entered the break symbol first, please enter a different number:");
			max = readInt("Enter a number: ");
		}
		int min = max;
		
		while(true) {
			int n = readInt("Enter another number: ");
			if (n == BREAK_SYMBOL) {
				break;
			} 
			if (n > max) {
				max = n;
			} else if (n < min) {
				min = n;
			}
 		}
		
		println("Smallest = " + min);
		println("Largest = " + max);
	}
}

