#include <iostream>
#include <fstream>
#include "console.h"
#include "simpio.h"
#include "map.h"
#include "vector.h"
#include "random.h"
using namespace std;


int getLevel();
void openFile(ifstream & file);
string getInitialSeed(Map<string, Vector<char> > & map);
void writeFromFile(ifstream & file, Map<string, Vector<char> > & entries, int & len);
bool searchForNextCharacter(string & result, string & seed,Map<string, Vector<char> > & entries);

int main() {
	// Open file
    ifstream file;
	openFile(file);
	
	// Get Level
	int level = getLevel();
	
	Map<string, Vector<char> > entries;
	
	writeFromFile(file, entries, level);

	string seed = getInitialSeed(entries);
	
	string result = seed;
	while (result.size() < 2000) {
		if (!searchForNextCharacter(result, seed, entries)) {
			break;
		}
	}
	cout << result << endl;
	return 0;
}


// Asks the user for filename until he enters a valid filepath
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

// Asks the user for valid level until he enters a level between 1-10
int getLevel() {
	cout << "Enter the Markov order [1-10]: ";
	int level = getInteger();
	while (level < 1 || level > 10) {
		cout << "Please Enter the Markov order between 1-10: ";
		level = getInteger();
	}
	cout << "Processing..." << endl;
	return level;
}

// Writes from given ifstream into entries
void writeFromFile(ifstream & file, Map<string, Vector<char> > & entries, int & len) {
	char ch;
	string str = "";
	
	while (file.get(ch)) {
		if (str.size() >= len) {
			entries[str.substr(str.size() - len)].add(ch); // Adds the previous string with the next character in map
		} 
		str += ch;
	}
	if (str.size() >= len) {
		entries[str.substr(str.size() - len)]; // Add the last one
	} 
}

string getInitialSeed(Map<string, Vector<char> > & map) {
	int max = 0;
	string seed;
	
	foreach (string word in map) {
		int num = map[word].size();
		
		if (num > max) {
			seed = word;
			max = num;
		}
	}

	return seed;
}


// returns false if there is no next character;
bool searchForNextCharacter(string & result, string & seed, Map<string, Vector<char> > & entries) {
	int size = entries[seed].size();
	if (size == 0) return false;

	int randomIndex = randomInteger(0, size - 1);
	char nextCh = entries[seed][randomIndex];
	seed = seed.substr(1) + nextCh;
	result += nextCh;
	return true;
}