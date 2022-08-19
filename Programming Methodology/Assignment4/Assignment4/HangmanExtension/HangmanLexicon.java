/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will reimplement for Part III of the assignment.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import acm.util.*;

public class HangmanLexicon {

	private ArrayList<String> wordList = new ArrayList<String>();

/** HangmanLexicon constructor */
	public HangmanLexicon(String filename) {
		addWordsToList(filename);
	}
	
	
	private void addWordsToList(String filename) {
		BufferedReader file = openFile(filename);
		addWordsFromFile(file);
	}
	
	// Opens HangmanLexicon.txt
	private BufferedReader openFile(String filename) {
		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new FileReader(filename));
		} catch (IOException e) {}
		return rd;
	}
	
	// Adds words from HangmanLexicon to wordList
	private void addWordsFromFile(BufferedReader file) {
		try {
			while(true) {
				String line = file.readLine();
				if (line == null) break;
				wordList.add(line.toUpperCase());
			} file.close();
		} catch (IOException ex) {
			throw new ErrorException(ex);
		}
	}
	
/** Returns the number of words in the lexicon. */
	public int getWordCount() {
		return wordList.size();
	}

/** Returns the word at the specified index. */
	public String getWord(int index) {
		return wordList.get(index);
	}
}
