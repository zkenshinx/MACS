/*
 * File: Hailstone.java
 * Name: Koba Karaputadze
 * Section Leader: Bakari Gamezardashvili
 * --------------------
 * This file is the starter file for the Hailstone problem.
 */

import acm.program.*;

public class Hailstone extends ConsoleProgram {
	
	public void run() {
		
		print("Enter a number: ");
		int n = readInt();
		while (n < 1) {
			n = readInt("Wrong input! Try again: ");
		} 
		
		doCollatzFunction(n); // The problem is known as the Collatz Conjecture

	}
	
	// Takes number n
	// If n is even, then it divides it by 2
	// If n is odd, it multiplies it by 3 and adds 1
	// This proccess repeats until we get number 1
	private void doCollatzFunction(int n) {
		int numOfProc = 0; // Counts Numbers of proccesses the function took
		while( n != 1) {
			if (n % 2 == 0) {
				println(n + " is even, so I take half: " + n / 2);
				n = n / 2;
			} else {
				println(n + " is odd, so I take 3n + 1: " + (3 * n + 1) );
				n = 3 * n + 1;
			}
			numOfProc++; // adds one on each proccess
		}
		println("The process took " + numOfProc + " to reach 1");
	}
}

