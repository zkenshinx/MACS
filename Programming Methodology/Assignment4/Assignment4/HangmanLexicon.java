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
	public HangmanLexicon() {
		addWordsToList();
	}
	
	private void addWordsToList() {
		BufferedReader file = openFile();
		addWordsFromFile(file);
	}
	
	// Opens HangmanLexicon.txt
	private BufferedReader openFile() {
		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new FileReader("HangmanLexicon.txt"));
		} catch (IOException e) {}
		return rd;
	}
	
	// Adds words from HangmanLexicon to wordList
	private void addWordsFromFile(BufferedReader file) {
		try {
			while(true) {
				String line = file.readLine();
				if (line == null) break;
				wordList.add(line);
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
