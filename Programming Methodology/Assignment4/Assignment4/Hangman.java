/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;

public class Hangman extends ConsoleProgram {
	
	// instance variables
	private HangmanCanvas canvas;
	private String word;
	private String dashedWord;
	private boolean lastEnteredWrongSymbol;
	private int guessesLeft;
	
	public void init() {
		canvas = new HangmanCanvas();
		add(canvas);
	}
	
    public void run() {			
		println("Welcome to Hangman!");
		
		playGame();
	}
    
    private void playGame() { 	
    	while(true) {
    		getReady();
    		game();
    		if (!getInputForContiniungGame()) 
    			break;
    	}
    }

    // Getting ready for the game to start
    private void getReady() {
		canvas.reset();
		getRandomWord();
		assignValuesToInstanceVariables();
		canvas.displayWord(dashedWord);
    }
    
    // Gets a random word from the lexicon
    private void getRandomWord() {
		RandomGenerator rgen = RandomGenerator.getInstance();
    	HangmanLexicon lexicon = new HangmanLexicon();
		int randomNum = rgen.nextInt(0, lexicon.getWordCount()-1);
    	word = lexicon.getWord(randomNum);
    }
    
    
    private void assignValuesToInstanceVariables() {
    	guessesLeft = 8;
    	dashedWord = dashedWord(word);
    }
    
    private void game() {
    	while( true ) {
			startMessage();
			lastEnteredWrongSymbol = false;
			String input = readLine().toUpperCase();
			doActionWithTheInput(input);
			if (checkGameEndedState()) break;
		}
    }
    
    private void doActionWithTheInput(String input) {
		if ( isInputValid(input) ) {
			isLetterRightOrWrong(input);
		} else {
			lastEnteredWrongSymbol = true;
		}
    }
    
    private void isLetterRightOrWrong(String input) {
    	if (wordContainsLetter(input)) {
    		letterGuessed(input);
    	} else {
    		letterNotGuessed(input);
    	}
    }

    // Checks if Guessable word contains our input letter
    private boolean wordContainsLetter(String input) {
    	return word.contains(input);
    }
    
    private void letterGuessed(String input) {
    	recoverLetter(input);
    	canvas.displayWord(dashedWord);
    }
    
    /* Recovers a letter from a word
     * For example: Let's say our word is MOUNTAIN
     * And we have : --U-T--_
     * recoverLetter(N) will return --UN-T--N */
    private void recoverLetter(String input) {
    	for ( int i = 0; i < word.length(); i++ ) {
    		if ( word.substring(i, i+1).equals(input) ) {
    			dashedWord = dashedWord.substring(0,i) + input + dashedWord.substring(i+1);
    		}
    	}
    }    
    
    private void letterNotGuessed(String input) {
    	println("There are no " + input + "'s in the word");
    	guessesLeft--;
    	canvas.noteIncorrectGuess(input.charAt(0), guessesLeft);
    }
    
    // Checks if the input is valid (The only valid input is one letter character)
    private boolean isInputValid(String input) {
    	return input.length() == 1 && Character.isLetter(input.charAt(0));
    }
    
    // Asks player if he wants to continue the game
    // If the player types different thing frmo "yes", the game ends.
    private boolean getInputForContiniungGame() {
    	String input = readLine("Would you like to play again? Type yes: ");
    	return input.toLowerCase().equals("yes");
    }
    
    // Given a word str, returns dashes instead of letters
    // For example: "MOUNTAIN" --> "--------"
    private String dashedWord(String str) {
    	String underscored = "";
    	for (int i = 0; i < str.length(); i++) {
    		underscored = underscored.concat("-");
    	}
    	return underscored;
    }
    
    
    /* Returns true if the game has ended, else returns false 
    * Writes the appropriate messages for losing/winning */
    private boolean checkGameEndedState() {
    	if ( guessesLeft == 0 ) {
	    	canvas.displayWord(word);
			printGameOverMessage();
			return true;
		} else if ( word.equals(dashedWord) ) {
			printWinMessage();
			return true;
		} return false;
    }
    
   
    private void startMessage() {
    	if ( lastEnteredWrongSymbol ) {
    		printWrongSymbolMessage();
    	} else { 	
        	printAfterInputedLetterMessage();
    	}
    }
    
    // Prints a message when the user inputs a non-letter character or a word
    private void printWrongSymbolMessage() {
    	println("You have entered a wrong symbol!");
    	print("Try again: ");
    }
    
    private void printAfterInputedLetterMessage() {
    	println("The word now looks like this: " + dashedWord);
		println("You have " + guessesLeft + " guesses left. ");
		print("Your guess: ");
    }
    
    // Prints a message when we lost and shows what word we couldn't guess
    private void printGameOverMessage() {
    	println("You are completely hung");
    	println("The word was: " + word);
    	println("You lose!");
    }
    
    // Prints winning message and shows the guessed word
    private void printWinMessage() {
    	println("You guessed the word: " + word);
    	println("You win!");
    }
   
}
