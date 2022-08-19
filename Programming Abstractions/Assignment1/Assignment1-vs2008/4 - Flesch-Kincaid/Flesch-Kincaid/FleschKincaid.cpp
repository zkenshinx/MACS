/*
 * File: FleschKincaid.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Flesch-Kincaid problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include <fstream>
#include <ctype.h>
#include "simpio.h"
#include "console.h"
#include "tokenscanner.h";

using namespace std;

// Constant coefficients of Flesch–Kincaid test
const double C0 = -15.59;
const double C1 = 0.39;
const double C2 = 11.8;

// Functions prototypes
void countEverything(TokenScanner & scanner, int & numWords, int & numSentences, int & numSyllables);
void readFromFile(ifstream & file, TokenScanner & scanner);
void openFile(ifstream & file);
bool isEndOfSentenceCharacter(string s);
bool isVowel(char ch);
int countSyllables(string str);
double getGrade(int numWords, int numSentences, int numSyllables);

int main() {
	
	// Open file
	ifstream file;
	openFile(file);
	
	// Read From File into TokenScanner
	TokenScanner scanner("");
	readFromFile(file, scanner);
	
	// count words, sentences and syllables
	int numWords = 0;
	int numSentences = 0;
	int numSyllables = 0;
	countEverything(scanner, numWords, numSentences, numSyllables);
	
	cout << "Words: " << numWords << endl;
	cout << "Sentences: " << numSentences << endl;
	cout << "Syllables: " << numSyllables << endl;
	cout << "Grade: " << getGrade(numWords, numSentences, numSyllables) << endl;

	file.close();
    return 0;
}

void openFile(ifstream & file) {
	cout << "Please enter a filename: ";
	string filename = getLine();
	file.open(filename.c_str());
	while (file.fail()) {
		file.clear();
		cout << "Please enter a valid filename: ";
		filename = getLine();
		file.open(filename.c_str());
	}
}

void readFromFile(ifstream & file, TokenScanner & scanner) {
	scanner.setInput(file);
 	scanner.ignoreWhitespace();
	scanner.addWordCharacters("\'");
}

void countEverything(TokenScanner & scanner, int & numWords, int & numSentences, int & numSyllables) {
	while (scanner.hasMoreTokens()) {
		string currentToken = scanner.nextToken();
		
		if (isalpha(currentToken[0])) { // Checks if the token is a valid word
			numWords++;
			numSyllables += countSyllables(currentToken);
		} else if (isEndOfSentenceCharacter(currentToken)) { 
			numSentences++;
		}
	}	
}

// Returns true if the given string is a character that represents the ending of sentence.
bool isEndOfSentenceCharacter(string s) {
	return s.length() == 1 && (s == "." || s == "?"
		|| s == "!");
}

bool isVowel(char ch) {
	ch = (char) tolower(ch);
	return (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u' || ch == 'y');
}

int countSyllables(string str) {
	int count = 0;
	int size = str.size();

	bool lastCharacterWasVowel = false;

	for (int i = 0; i < size - 1; i++) {

		if ( isVowel(str[i]) ) {
			if (!lastCharacterWasVowel) count++;
			lastCharacterWasVowel = true;
		} else {
			lastCharacterWasVowel = false;
		}

	}

	if (isVowel(str[size-1]) && !lastCharacterWasVowel && 
		str[size - 1] != 'e' && str[size - 1] != 'E') count++; 
	return count == 0 ? 1 : count; 
}

double getGrade(int numWords, int numSentences, int numSyllables) {
	if (numWords == 0) numWords = 1;
	if (numSentences == 0) numSentences = 1;
	return C0 + (C1 * numWords) / numSentences + (C2 * numSyllables) / numWords;
}