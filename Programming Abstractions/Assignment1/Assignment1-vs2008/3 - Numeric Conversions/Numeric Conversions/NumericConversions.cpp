/*
 * File: NumericConversions.cpp
 * ---------------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Numeric Conversions problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include <string>
#include "console.h"
using namespace std;

/* Function prototypes */

string intToString(int n);
int stringToInt(string str);

/* Main program */

int main() {

    return 0;
}

string intToString(int n) {

	if (n < 0) {
		return "-" + intToString(-n);
	}

	if (n < 10)
		return string() + char(n+'0');
	
	return intToString( n / 10 ) + char( (n % 10) + '0' );
}

int stringToInt(string str) {

	if (str.length() == 0)
		return 0;
		
	if ( str[0] == '-' )
		return -1 * stringToInt( str.substr(1) );
	
	return stringToInt( str.substr(0, str.length() - 1) ) * 10 + (str[ str.length() - 1 ] - '0');
}
