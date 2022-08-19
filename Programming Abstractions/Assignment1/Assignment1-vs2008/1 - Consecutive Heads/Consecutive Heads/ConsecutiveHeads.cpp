/*
 * File: ConsecutiveHeads.cpp
 * --------------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Consecutive Heads problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
#include "random.h"
using namespace std;

const double chanceOfHead = 0.5;
const int consecutiveHeads = 3;


int simulateFlipping() {
	int countHeadsInRow = 0;
	int countFlips = 0;

	while ( countHeadsInRow != consecutiveHeads ) {
		if ( randomChance(chanceOfHead) ) {
			countHeadsInRow++;
			cout << "heads" << endl;
		} else {
			countHeadsInRow = 0;
			cout << "tails" << endl;
		}

		countFlips++;
	}

	return countFlips;
}

int main() {
    
	int countFlips = simulateFlipping();

	cout << "It took " << countFlips << " to get " << consecutiveHeads << " consecutive heads" << endl;
    return 0;
}
