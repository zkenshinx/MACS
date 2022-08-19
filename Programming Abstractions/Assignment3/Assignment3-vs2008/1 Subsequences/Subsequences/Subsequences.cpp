#include <iostream>
#include <string>
#include "console.h"
#include "simpio.h"
using namespace std;

/* Given two strings, returns whether the second string is a
 * subsequence of the first string.
 */
bool isSubsequence(string text, string subsequence);

int main() {
    
    return 0;
}

bool isSubsequence(string text, string subsequence) {
	// Base case
	if (subsequence.length() == 0) {
		return true;
	} else if (text.length() == 0) {
		return false;
	}

	if (text[0] == subsequence[0]) {
		return isSubsequence(text.substr(1), subsequence.substr(1));
	} else {
		return isSubsequence(text.substr(1), subsequence);
	}
}
