#include <iostream>
#include "strlib.h"
#include "console.h"
#include "simpio.h"
#include "lexicon.h"
#include "queue.h"
#include "set.h"

using namespace std;

void difByOne(Set<string> & dif, string lastWord, Lexicon & lex, Set<string> & usedWords);
void findWordLadder(string fromWord, string toWord, Lexicon & lex);
bool isValidInput(string str1, string str2, Lexicon & lex);
void printLadder(Vector<string> ladder);


int main() {
    
	Lexicon lexicon("EnglishWords.dat");
	
	while (true) {
		string fromWord = toLowerCase(getLine("Enter the first word: "));
		if (fromWord.length() == 0) {
			break;
		}
		string toWord = toLowerCase(getLine("Enter the second word: "));
		findWordLadder(fromWord, toWord, lexicon);
	}

    return 0;
}


// Finds the word ladder and prints it in the console
void findWordLadder(string fromWord, string toWord, Lexicon & lex) {
	if (!isValidInput(fromWord, toWord, lex)) {
		cout << "Ladder couldn't be found" << endl << endl;
		return;
	}

	Set<string> usedWords; // Keep track of words we already used 
	
	Queue<Vector<string> > ladders; // Creating Empty Queue

	Vector<string> ladder;
	ladder.add(fromWord); // Adding the first word to the first ladder
	usedWords.add(fromWord);

	ladders.enqueue(ladder);

	while ( !ladders.isEmpty() ) {
		Vector<string> currentLadder = ladders.dequeue();
		string currentWord = currentLadder[currentLadder.size() - 1];
		if (currentWord == toWord) {
			printLadder(currentLadder);
			return;
		}
		
		// Words that are different by one letter from given word and has not been used
		Set<string> wordsDifByOne; 
		difByOne(wordsDifByOne, currentWord, lex, usedWords);

		foreach (string word in wordsDifByOne) {
			Vector<string> newLadder = currentLadder;
			newLadder.add(word);
			ladders.enqueue(newLadder);
		}
	}
	// Ladder is empty
	cout << "Ladder couldn't be found" << endl << endl;
}

bool isValidInput(string str1, string str2, Lexicon & lex) {
	return (str1.length() == str2.length()) &&
			lex.contains(str1) && lex.contains(str2);
}

// Adds all words that are different from given word by one letter
// , are legal english words and hasn't been used in Set<string> dif
void difByOne(Set<string> & dif, string word, Lexicon & lex, Set<string> & usedWords) {
	for (int i = 0; i < word.size(); i++) {
		string tempWord = word;
		for (char ch = 'a'; ch <= 'z'; ch++) {
			tempWord[i] = ch;
			if (lex.contains(tempWord) && !usedWords.contains(tempWord)) {
				dif.add(tempWord);
				usedWords.add(tempWord);
			}
		}
	}
}


void printLadder(Vector<string> ladder) {
	int size = ladder.size();
	cout << "Ladder found: ";
	for (int i = 0; i < size - 1; i++) {
		cout << ladder[i] << " -> ";
	}
	cout << ladder[size - 1] << endl << endl;
}