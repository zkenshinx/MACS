/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.AudioClip;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class HangmanExtension extends GraphicsProgram {
	
	// Instance variables
	private RandomGenerator rgen = new RandomGenerator().getInstance();
	private Object objClicked;
	private GImage start;
	private GImage hintButton;
	private GImage exitButton;
	private GImage continueButton;
	private String wordToBeGuessed;
	private String dashedWord;
	private String letterEntered;
	private String wordType;
	private int livesLeft;
	private int score;
	private boolean continueGame;
	private ArrayList<GObject> rects = new ArrayList<GObject>();
	private ArrayList<GLabel> labels = new ArrayList<GLabel>();
	private ArrayList<String> lettersAlreadyChosen = new ArrayList<String>();
	
	
	public void init() {
		addMouseListeners();
		addKeyListeners();
	}
	
	public void run() {
		while (true) {
			startMenu();
		}
	}
	
	private void startMenu() {
		addMenuBackground();
		addStartButton();
		waitForClickingButton();
	}

	private void addMenuBackground() {
		GImage menuBackground = new GImage("background.png");
		menuBackground.setSize(getWidth(), getHeight());
		add(menuBackground);
	}	
	
	// Adds starts button on canvas
	private void addStartButton() {
		GImage image = new GImage("start.png");
		start = image;
		image.setSize(75,75);
		int startX = 350;
		int startY = 250;
		add(image, startX, startY);
	}
	
	// Waits for the start button to be clicked
	private void waitForClickingButton() {
		while (true) {
			if (objClicked != null && objClicked.equals(start)) {
				chooseWordType();
				playGame();
				if (!continueGame) {
					removeAll();
					break;
				} 
			}
		}
	}
	
	// Adds three images on canvas
	// If you choose one of them, the corresponding word types will be your word list
	private void chooseWordType() {
		removeAll();
		objClicked = null;
		GImage carBrands = addWordImage("bmw.png","Car brands", 100, 175);
		GImage animals = addWordImage("animal.png", "Animals", 350, 175);
		GImage countries = addWordImage("country.png", "Countries", 600, 175);
		while (objClicked == null || !objClicked.getClass().getSimpleName().equals("GImage")) {
			pause(50);
		}
		if (objClicked == carBrands) wordType = "CarBrands.txt";
		if (objClicked == animals) wordType = "Animals.txt";
		if (objClicked == countries) wordType = "Countries.txt";
	}
	
	// Adss an image on canvas with label
	private GImage addWordImage(String filename, String text, int x, int y) {
		GImage image = new GImage(filename);
		GLabel imageL = new GLabel(text);
		image.setSize(75,75);
		imageL.setFont("Courier-25");
		add(image, x, y);
		add(imageL, x+(75-imageL.getWidth())/2, y+100);
		return image;
	}
		
	// Gameplay
	private void playGame() {
		getReady();
		while(true) {
			String input = getLetterFromPlayer();
		
			doActionWithTheInput(input);
			
			showScore();
			
			if (isGameEnded()) {
				gameEndState();
				break;
			}
		}
	}
	
	//Shows scores on top right
	private void showScore() {
		GLabel scoreLabel = new GLabel("Score: " + score);
		scoreLabel.setFont("Courier-20");
		GPoint labelP = new GPoint(600, (int)(1.5*scoreLabel.getAscent()));
		if (getElementAt(labelP) != null) remove(getElementAt(labelP));
		add(scoreLabel, labelP);
	}
	
    public void mousePressed(MouseEvent e) {
    	objClicked = getElementAt(e.getX(), e.getY());
    	checkWhatObjectIsIt();
    }
    
    // Checks what object was clicked, and does the corresponding action
    private void checkWhatObjectIsIt() {
    	if (objClicked == hintButton) {
    		hint();
    	}
    }
    
    // Recovers one letter and subtracts 200 from score (hint costs 200 points)
    private void hint() {
    	if (score >= 200) {
    		score -= 200;
    		recoverFirstLetter();
    		addDashedWordOnCanvas();
    	}
    }
    
    // Recovers the first non-recovered letter (used for hint)
    private void recoverFirstLetter() {
    	for (int i = 0; i < wordToBeGuessed.length();i++) {
    		if ( dashedWord.charAt(i) == '-') {
    			letterEntered = wordToBeGuessed.substring(i,i+1);
        		recoverLetter(letterEntered, false);
        		break;
    		} 
    	}
    }
    	
    // Checks if the game has ended
	private boolean isGameEnded() {
		return livesLeft == 0 || dashedWord.equals(wordToBeGuessed);
	}
	
	// If the player has won, the method doWhenWon() is being called
	// else the method doWhenLost() is called
	private void gameEndState() {
		if ( dashedWord.equals(wordToBeGuessed)) {
			doWhenWon();
		} else {
			doWhenLost();
		}
	}
	
	// Plays winning sound, prints winning message, adds a button and waits for the player to click it
	private void doWhenWon() {
		playSound("wonGame.au");
		printWinMessage();
		addContinueButton();
		removeHintButton();
		waitForClickingContinueButton();
		playGame();
	}
	
	// plays the file sound named filename
	private void playSound(String filename) {
		AudioClip correctSound = MediaTools.loadAudioClip(filename);
		correctSound.play();
	}
	
	// Adds a button for continiung game
	private void addContinueButton() {
		continueButton = new GImage("continueButton.png");
		continueButton.setSize(175, 45);
		add(continueButton,550, 325);
	}
	
	// Waits for the continue button to be clicked
	private void waitForClickingContinueButton() {
		while(objClicked != continueButton) {
			pause(50);
		}
		continueGame = true;
	}
	
	// plays game over sound, prints losing message, removes hint button and adds the exit button
	private void doWhenLost() {
		playSound("gameOverSound.au");
		printLoseMessage();
		removeHintButton();
		addExitToMenuButton();
		waitForClickingExitToMenuButton();
	}
	
	// Prints losing message
	private void printLoseMessage() {
		GLabel wordGuessedLabel = new GLabel("You are completely hung! The Word was: " + wordToBeGuessed);
		wordGuessedLabel.setFont("-20");
		add(wordGuessedLabel, getWidth()/2-wordGuessedLabel.getWidth()/2, 450);
	}
	
	private void removeHintButton() {
		remove(hintButton);
	}
	
	// Adds exit button on canvas
	private void addExitToMenuButton() {
		exitButton = new GImage("exitButton.png");
		exitButton.setSize(75,75);
		add(exitButton, 620, 350);
	}
	
	// Waits for the Exit button to be clicked
	private void waitForClickingExitToMenuButton() {
		while (objClicked != exitButton) {
			pause(50);
		}
		continueGame = false;
	}
	private void printWinMessage() {
		GLabel wordGuessedLabel = new GLabel("You guessed the word!");
		wordGuessedLabel.setFont("-20");
		add(wordGuessedLabel, getWidth()/2-wordGuessedLabel.getWidth()/2, 450);
	}
	
	/* Checks if the letter inputed by the user is already chosen
	* If it is not, then checks if the word we should guess contains that letter
	* if it contains, containsLetter() method is called
	* else doesNotContainLetter() method is called
	* Lastly it adds our inputed letter to a list */
	private void doActionWithTheInput(String input) {
		destroyLetterRect(input);
		if (!lettersAlreadyChosen.contains(input)) {
			if (wordToBeGuessed.contains(input)) {
				containsLetter(input);
	    	} else {
	    		doesNotContainLetter();
	    	}
		}
		lettersAlreadyChosen.add(input);
	}
	
	// Plays a sound and calls letterGuessed() method
	private void containsLetter(String input) {
		playSound("correct.au");
		letterGuessed(input);
	}
	
	// Subtracts one life, adds a body part, subtracts one heart from health and plays a sound
	private void doesNotContainLetter() {
		livesLeft--;
		addBodyPart();
   		showHealthBar();
		playSound("wrong.au");
	}
	
	// Shows health bar according to lifes left
	private void showHealthBar() {
		GImage healthBar;
		switch(livesLeft) {
		case 8: healthBar = new GImage("healthBar8.png"); break;
		case 7: healthBar = new GImage("healthBar7.png"); break;
		case 6: healthBar = new GImage("healthBar6.png"); break;
		case 5: healthBar = new GImage("healthBar5.png"); break;
		case 4: healthBar = new GImage("healthBar4.png"); break;
		case 3: healthBar = new GImage("healthBar3.png"); break;
		case 2: healthBar = new GImage("healthBar2.png"); break;
		case 1: healthBar = new GImage("healthBar1.png"); break;
		default: healthBar = new GImage("healthBar0.png"); break;
		}
		healthBar.scale(0.25);
		GPoint healthP = new GPoint (0,0);
		if (getElementAt(healthP) != null) remove(getElementAt(healthP));
		add(healthBar, healthP);
	}	
	
	// Add body part accoring to lifes left
	private void addBodyPart() {
		GImage bodyPart;
		switch(livesLeft) {
		case 8: bodyPart = new GImage("hang8.png"); break;
		case 7: bodyPart = new GImage("hang7.png"); break;
		case 6: bodyPart = new GImage("hang6.png"); break;
		case 5: bodyPart = new GImage("hang5.png"); break;
		case 4: bodyPart = new GImage("hang4.png"); break;
		case 3: bodyPart = new GImage("hang3.png"); break;
		case 2: bodyPart = new GImage("hang2.png"); break;
		case 1: bodyPart = new GImage("hang1.png"); break;
		default: bodyPart = new GImage("hang0.png"); break;
		}
		bodyPart.scale(1.25);
		GPoint bodyP = new GPoint (getWidth()/2-bodyPart.getWidth()/2,0);
		if (getElementAt(bodyP) != null) remove(getElementAt(bodyP));
		add(bodyPart, bodyP);
	}
	

	// Recover our inputed letter and adds the dashed version on canvas
    private void letterGuessed(String input) {
    	recoverLetter(input, true);
		addDashedWordOnCanvas();
    }
    
    /* Recovers a letter from a word
     * For example: Let's say our word is MOUNTAIN
     * And we have : --U-T--_
     * recoverLetter(N) will return --UN-T--N */
    private void recoverLetter(String input, boolean isScoreIncreased) {
    	for ( int i = 0; i < wordToBeGuessed.length(); i++ ) {
    		if ( wordToBeGuessed.substring(i, i+1).equals(input) ) {
    			addScore(input, isScoreIncreased);
    			dashedWord = dashedWord.substring(0,i) + input + dashedWord.substring(i+1);
    		}
    	}
    } 
    
    // isScoreIncreased indicates if the letter was guessed by the player and not by the hint
    // If true then we should add 50 for each letter
    // If not we subtract
    private void addScore(String input, boolean isScoreIncreased) {
		if (isScoreIncreased) score+=50;
		else score-=50;	
    }
    
    // Waits for the player to enter a letter from the keyboard
	private String getLetterFromPlayer() {
		while (true) {
			if(letterEntered != null) break;
			pause(50);
		}
		String temp = letterEntered;
		letterEntered = null;
		return temp;
	}
	
	public void keyPressed(KeyEvent e) {
		if (Character.isLetter(e.getKeyChar()) ) {
			letterEntered = Character.toUpperCase(e.getKeyChar())+"";
		}
	}
	
	// Gets ready for the game to start
	private void getReady() {
		getRandomWord();
		assignValuesToInstanceVariables();
		displayWorld();
	}
	
    private void assignValuesToInstanceVariables() {
		dashedWord = dashedWord(wordToBeGuessed);
		letterEntered = null;
		livesLeft = 8;
		if (!continueGame ) score = 0;
		clearInstanceLists();
    }
    
    private void clearInstanceLists() {
    	rects.clear();
    	labels.clear();
    	lettersAlreadyChosen.clear();
    }
 
    // Given a word str, returns dashes instead of letters
    // For example: "MOUNTAIN" --> "--------"
    private String dashedWord(String str) {
    	String underscored = "";
    	for (int i = 0; i < str.length(); i++) {
    		if (Character.isWhitespace(str.charAt(i))) {
    			underscored = underscored.concat(" ");
    		} else {
    			underscored = underscored.concat("-");
    		}
    	}
    	return underscored;
    }
        
   
	private void displayWorld() {
		removeAll();
		addLettersOnCanvas();
		addDashedWordOnCanvas();
		addBodyPart();
		addHintButton();
		showHealthBar();
		showScore();
	}
	
	private void addHintButton() {
		hintButton = new GImage("hint.png");
		hintButton.setSize(75,35);
		int startX = (int) (getWidth()-hintButton.getWidth()-50);
		int startY = getHeight()/2;
		add(hintButton, startX, startY);
	}
	
	// Adds letters with Rects around them on canvas 
	private void addLettersOnCanvas() {
		String[] topRowLetters = {"Q","W","E","R","T","Y","U","I","O","P"};
		String[] homeRowLetters = {"A","S","D","F","G","H","J","K","L"};
		String[] bottomRowLetters = {"Z","X","C","V","B","N","M"};
		addLettersRow(250, topRowLetters);
		addLettersRow(285, homeRowLetters);
		addLettersRow(320, bottomRowLetters);
	}
	
	// Adds a single row of given array of letter in order
	private void addLettersRow(int startY, String[] arr) {
		int Size = 30;
		int OffSet = 5;
		int arrL = arr.length;
		int startX = (getWidth() - arrL*Size - (arrL-1)*OffSet)/2;
		for (int i = 0; i < arrL; i++) {
			GRect rect = new GRect(Size, Size);
			GLabel letter = new GLabel(arr[i]);
			int rectX = startX + i*(Size + OffSet);
			int rectY = startY;
			letter.setFont("Courier-15");
			int labelX = (int) (rectX + (Size - letter.getWidth())/2);
			int labelY = (int) (rectY +(Size + letter.getAscent())/2);
			add(letter, labelX, labelY);
			add(rect, rectX, rectY);
			rects.add(rect);
			labels.add(letter);
		}
	}	
	
	// Deletes the rect containing a letter and the letter
	private void destroyLetterRect(String input) {
		for (int i = 0; i < rects.size(); i++) {
			if (labels.get(i).getLabel().equals(input)) {
				remove(rects.get(i));
				remove(labels.get(i));
			}
		}
	}
	
	private void addDashedWordOnCanvas() {
    	GPoint labelP = new GPoint(getWidth()/2, 9*getHeight()/10);
    	if ( getElementAt(labelP) != null) remove(getElementAt(labelP));
		GLabel label = new GLabel(dashedWord);
		label.setFont("Courier-35");
		add(label, getWidth()/2-label.getWidth()/2 , 9*getHeight()/10);
	}
	
    // Gets a random word from the lexicon
    private void getRandomWord() {
    	HangmanLexicon lexicon = new HangmanLexicon(wordType);
		int randomNum = rgen.nextInt(0, lexicon.getWordCount()-1);
		wordToBeGuessed = lexicon.getWord(randomNum);
    }	
}
