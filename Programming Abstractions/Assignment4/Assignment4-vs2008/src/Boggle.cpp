/*
 * File: Boggle.cpp
 * ----------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the main starter file for Assignment #4, Boggle.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include <cctype>
#include "gboggle.h"
#include "grid.h"
#include "strlib.h"
#include "gwindow.h"
#include "lexicon.h"
#include "random.h"
#include "simpio.h"
#include "set.h"
#include "random.h"

using namespace std;

/* Constants */

const int BOGGLE_WINDOW_WIDTH = 900;
const int BOGGLE_WINDOW_HEIGHT = 600;
const int BOARD_SIZE = 4;

const string STANDARD_CUBES[16]  = {
    "AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS",
    "AOOTTW", "CIMOTU", "DEILRX", "DELRVY",
    "DISTTY", "EEGHNW", "EEINSU", "EHRTVW",
    "EIOSST", "ELRTTY", "HIMNQU", "HLNNRZ"
};
 
const string BIG_BOGGLE_CUBES[25]  = {
    "AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM",
    "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW",
    "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DDHNOT",
    "DHHLOR", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU",
    "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"
};

/* Function prototypes */

void welcome();
void giveInstructions();
void configureBoard(Grid<char> &board);
void labelBoard(Grid<char> &board);
Vector<string> arrayToVector(const string cubes[]);
void shuffle(Vector<string> &vec);
void labelBoardWithVector(Vector<string> &vec, Grid<char> &board);
Vector<string> askUserForConfiguration();
void humansTurn(Lexicon &lex, Grid<char> &board, Set<string> &foundWords);
bool isValidWord(string word,Set<string> &foundWords,Lexicon &lex, Player player);
bool canBeFoundInBoard(string word, Grid<char>& board, Player player);
bool searchForPath(string word, Grid<char>& board,int row, int col,Grid<bool> &alreadyUsed, int index);
void highlight(Grid<bool>& grid, bool flag);
void computersTurn(Lexicon &lex, Grid<char> &board, Set<string> &foundWords);
void searchForWord(Grid<char> &board, Lexicon &lex, Grid<bool> &alreadyUsed, Set<string> &foundWords, int row, int col, string currentWord);
/* Main program */

int main() {
    GWindow gw(BOGGLE_WINDOW_WIDTH, BOGGLE_WINDOW_HEIGHT);

    welcome();
    giveInstructions();
	
	Lexicon lex("EnglishWords.dat");

	while (true) {
		initGBoggle(gw);

		drawBoard(BOARD_SIZE, BOARD_SIZE);	

		Grid<char> board(BOARD_SIZE, BOARD_SIZE); // Board

		configureBoard(board);
	
		Set<string> foundWords;
	
		humansTurn(lex, board, foundWords);

		computersTurn(lex, board, foundWords);

		string playAgain = getLine("If you want to play again type yes, otherwise type anything ");

		if (toUpperCase(playAgain) != "YES") {
			break;
		}
	}

    return 0;
}

/*
 * Function: welcome
 * Usage: welcome();
 * -----------------
 * Print out a cheery welcome message.
 */

void welcome() {
    cout << "Welcome!  You're about to play an intense game ";
    cout << "of mind-numbing Boggle.  The good news is that ";
    cout << "you might improve your vocabulary a bit.  The ";
    cout << "bad news is that you're probably going to lose ";
    cout << "miserably to this little dictionary-toting hunk ";
    cout << "of silicon.  If only YOU had a gig of RAM..." << endl << endl;
}

/*
 * Function: giveInstructions
 * Usage: giveInstructions();
 * --------------------------
 * Print out the instructions for the user.
 */

void giveInstructions() {
    cout << endl;
    cout << "The boggle board is a grid onto which I ";
    cout << "I will randomly distribute cubes. These ";
    cout << "6-sided cubes have letters rather than ";
    cout << "numbers on the faces, creating a grid of ";
    cout << "letters on which you try to form words. ";
    cout << "You go first, entering all the words you can ";
    cout << "find that are formed by tracing adjoining ";
    cout << "letters. Two letters adjoin if they are next ";
    cout << "to each other horizontally, vertically, or ";
    cout << "diagonally. A letter can only be used once ";
    cout << "in each word. Words must be at least four ";
    cout << "letters long and can be counted only once. ";
    cout << "You score points based on word length: a ";
    cout << "4-letter word is worth 1 point, 5-letters ";
    cout << "earn 2 points, and so on. After your puny ";
    cout << "brain is exhausted, I, the supercomputer, ";
    cout << "will find all the remaining words and double ";
    cout << "or triple your paltry score." << endl << endl;
    cout << "Hit return when you're ready... ";
    getLine();
}

/*
 * Function: configureBoard
 * Usage: configureBoard(Grid<char> &board);
 * --------------------------
 * Asks the user if he wants to manually configure the board
 * If not, randomly configures the board using the standard cubes;
 */

void configureBoard(Grid<char> &board) {
	string answer = getLine("Would you like to force the board configuration? ");
	while (toUpperCase(answer) != "NO" && toUpperCase(answer) != "YES") {
		answer = getLine("yes or no: ");	
	}
	Vector<string> shuffled;
	if (toUpperCase(answer) == "NO") {
		shuffled = arrayToVector(STANDARD_CUBES);
	} else {
		shuffled = askUserForConfiguration();
	}
	shuffle(shuffled);
	labelBoardWithVector(shuffled, board);
	labelBoard(board);
}

/*
 * Function: arrayToVector
 * Vector<string> arrayToVector(const string cubes[]);
 * --------------------------
 * converts array to vector
 */
Vector<string> arrayToVector(const string cubes[]) {
	Vector<string> vec;
	for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
		vec.add(cubes[i]);
	}
	return vec;
}


/*
 * Function: labelBoard
 * Usage: labelBoard(Grid<char> &board);
 * --------------------------
 * labels the board with the given grid
 */
void labelBoard(Grid<char> &board) {
	for (int row = 0; row < board.numRows(); row++) {
		for (int col = 0; col < board.numCols(); col++) {
			labelCube(row, col, board[row][col]);
		}
	}
}

/*
 * Function: shuffle
 * Usage: shuffle();
 * --------------------------
 * shuffles the given vector
 */

void shuffle(Vector<string> &vec) {
	for (int i = 0; i < vec.size(); i++) {
		int randIndex = randomInteger(i, vec.size() - 1);
		swap(vec[i], vec[randIndex]);
	}	
}


/*
 * Function: labelBoardWithVector
 * Usage: labelBoardWithVector(Vector<string> &vec, Grid<char> &board);
 * --------------------------
 * labels the board with the given vector
 */
void labelBoardWithVector(Vector<string> &vec, Grid<char> &board) {
	for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
		int rand = randomInteger(0, 5);
		board[i / BOARD_SIZE][i % BOARD_SIZE] = vec[i][rand];
	}
}

/*
 * Function: askUserForConfiguration
 * Usage: askUserForConfiguration();
 * --------------------------
 * asks the user for configuration 
 */
Vector<string> askUserForConfiguration() {
	cout << "Enter " << BOARD_SIZE << " times 6 length cubes: " << endl;
	Vector<string> vec;
	for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
		string currentCube = toUpperCase(getLine("Enter a cube: "));
		vec.add(currentCube);
	}
	return vec;
}

/*
 * Function: humansTurn
 * Usage: humansTurn(Lexicon &lex,  Grid<char> &board);
 * --------------------------
 * labels the board with the given string
 */
void humansTurn(Lexicon &lex,  Grid<char> &board, Set<string> &foundWords) {
	while (true) {
		string word = toUpperCase(getLine("Enter a word: "));
		if (word == "") {
			break;
		}
		if (isValidWord(word, foundWords, lex, HUMAN)) {
			if (canBeFoundInBoard(word, board, HUMAN)) {
				foundWords.add(word);
				recordWordForPlayer(word, HUMAN);
			} else {
				cout << "The word couldn't be found in the board" << endl;
			}
		}
	}
}

/*
 * Function: isValidWord
 * Usage: isValidWord(string word,  Set<string> &foundWords,  Lexicon &lex)
 * --------------------------
 * Checks if the given word is valid according to 4 rules:
 * 1) The word length must be >= 4
 * 2) It must be a proper English word
 * 3) It shouldn't be already found by the player
 */
bool isValidWord(string word,  Set<string> &foundWords,  Lexicon &lex, Player player) {
	if (word.size() < 4) {
		if (player == HUMAN) cout << "The word is too short!" << endl;
		return false;
	} else if (!lex.contains(word)) {
		if (player == HUMAN) cout << "It's not a proper word!" << endl;
		return false;
	} else if (foundWords.contains(word)) {
		if (player == HUMAN) cout << "You've already found this word!" << endl;
		return false;
	} 

	return true;
}


/*
 * Function: canBeFoundInBoard
 * Usage: canBeFoundInBoard(string word, Grid<char>& board)
 * --------------------------
 * checks if the given word can be found in the board
 * If it is found and its players turn, it temporarily highlights the cubes combinations
 */
bool canBeFoundInBoard(string word, Grid<char>& board, Player player) {
	
	for (int row = 0; row < board.numRows(); row++) {
		for (int col = 0; col < board.numCols(); col++) {
			Grid<bool> alreadyUsed(board.numRows(), board.numCols()); // Keep track of used letters
			if (searchForPath(word, board, row, col, alreadyUsed, 0)) {
				if (player == HUMAN) {
					highlight(alreadyUsed, true);
					pause(500);
					highlight(alreadyUsed, false);
				}
				return true;
			}
		}
	}
	return false;
}

/*
 * Function: searchForPath
 * Usage: searchForPath(string word, Grid<char>& board,int row, int col,Grid<bool> &alreadyUsed, int index)
 * --------------------------
 * Searches for a path from the given row and col for the given word if it exists
 */
bool searchForPath(string word, Grid<char>& board,int row, int col,Grid<bool> &alreadyUsed, int index) {
	if (index >= word.size()) 
		return true;
	else if (!board.inBounds(row, col) || board[row][col] != word[index]
			 || alreadyUsed[row][col]) {
		return false;
	}
	alreadyUsed[row][col] = true;
	for (int dRow = -1; dRow <= 1; dRow++) {
		for (int dCol = -1; dCol <= 1; dCol++) {
			if ( searchForPath(word, board, row+dRow, col+dCol, alreadyUsed, index+1) ) {
				return true;
			}
		}
	}
	alreadyUsed[row][col] = false;
}

/*
 * Function: highlight
 * Usage: highlight(Grid<bool>& grid, bool flag)
 * --------------------------
 * highlights with the given flag all true values of given grid
 */
void highlight(Grid<bool>& grid, bool flag) {
	for (int row = 0; row < grid.numRows(); row++) {
		for (int col = 0; col < grid.numCols(); col++) {
			if (grid[row][col]) {
				highlightCube(row, col, flag);
			}
		}
	}
}

/*
 * Function: computersTurn
 * Usage: computersTurn(Lexicon &lex, Grid<char> &board, Set<string> &foundWords));
 * --------------------------
 * Searches for all possible valid words in board that haven't been found by problem
 */

void computersTurn(Lexicon &lex, Grid<char> &board, Set<string> &foundWords) {
	for (int row = 0; row < board.numRows(); row++) {
		for (int col = 0; col < board.numCols(); col++) {
			Grid<bool> alreadyUsed(board.numRows(), board.numCols());
			searchForWord(board, lex, alreadyUsed, foundWords, row, col, string()+board[row][col]);
		}
	}
}

/*
 * Function: searchForWord
 * Usage: searchForWord(Grid<char> &board, Lexicon &lex, Grid<bool> &alreadyUsed, Set<string> &foundWords, int row, int col, string currentWord);
 * --------------------------
 * Searches for word that starts with "currentWord" or is currentWord
 */

void searchForWord(Grid<char> &board, Lexicon &lex, Grid<bool> &alreadyUsed, Set<string> &foundWords, int row, int col, string currentWord) {
	if (!lex.containsPrefix(currentWord) || alreadyUsed[row][col]) {
		return;
	}
	if (isValidWord(currentWord, foundWords, lex, COMPUTER)) {
		recordWordForPlayer(currentWord, COMPUTER);
		foundWords.add(currentWord);
	}
	alreadyUsed[row][col] = true;
	for (int dRow = -1; dRow <= 1; dRow++) {
		for (int dCol = -1; dCol <= 1; dCol++) {
			if (board.inBounds(row+dRow, col+dCol)) {
				searchForWord(board, lex, alreadyUsed, foundWords, row+dRow, col+dCol, currentWord+board[row+dRow][col+dCol]);
			}
		}
	}
	alreadyUsed[row][col] = false;
}