/*
 * File: PythagoreanTheorem.java
 * Name: Koba Karaputadze
 * Section Leader: Bakari Gamezardashvili
 * -----------------------------
 * This file is the starter file for the PythagoreanTheorem problem.
 */

import acm.program.*;

public class PythagoreanTheorem extends ConsoleProgram {
	
	public void run() {
		println("Enter values to compute Pythagorean theorem.");
		int a = readInt("a = ");
		while( a < 1) {
			println("Please enter a positive number");
			a = readInt("a = ");
		}
		int b = readInt("b = ");
		while( b < 1) {
			println("Please enter a positive number");
			b = readInt("b = ");
		}
		println("c = " + Pythagora(a, b));
	}

	// Returns numeric value of hypotenuse given legs
	private double Pythagora(int x, int y) {
		return Math.sqrt(x*x + y*y);
	}
}
