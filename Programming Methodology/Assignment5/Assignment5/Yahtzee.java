/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import java.util.ArrayList;
import java.util.Arrays;

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	
	public void run() {
		IODialog dialog = getDialog();

		getNumberOfPlayers(dialog);
		
		initialiseInstanceVariables();

		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		
		playGame();
	}
	
	/* Gets the number of players
	If the input is not between 1 and 4 (inclusive)
	then we ask the user again to input a valid number */ 
	private void getNumberOfPlayers(IODialog dialog) {
		nPlayers = dialog.readInt("Enter number of players");
		while (nPlayers < 1 || nPlayers > 4) {
			nPlayers = dialog.readInt("Enter a valid number of players");
		}
	}
	
	// Initializes the instance variables we use for the game
	private void initialiseInstanceVariables() {
		playerNames = new String[nPlayers];
		dice = new int[N_DICE];
		sumOfUpperScores = new int[nPlayers];
		sumOfLowerScores = new int [nPlayers];
		sumOfAllScores = new int[nPlayers];
		isCategoryChecked = new boolean[nPlayers][N_CATEGORIES];
	}
	
	/* Game Play: 
	 * For Each Round, we get the player number, the round number
	 * we roll dice, we select category and update the Score
	 * repeat if there are more rounds left
	 * Finally We Determine the winner and print the appropriate message */
	private void playGame() {
		for (int i = 0; i < N_SCORING_CATEGORIES*nPlayers; i++) {
			int player = i % nPlayers + 1;	
			int round = i / nPlayers + 1;

			rollDice(player);

			int category = selectCategory(player);
			
			updateScore(category, player, round);
		}
		
		winnerMessage();
	}

	// Rolling the dice
	private void rollDice(int player) {
		firstRoll(player);
		nextRolls(player);
	}
	
	/* We wait for player to click the roll button,
	 *  then randomize dice and display them */
	private void firstRoll(int player) {
		display.printMessage(playerNames[player - 1] + "'s turn! Click \"Roll Dice\" button to roll the dice");
		display.waitForPlayerToClickRoll(player);
		randomizeDice(false);
		display.displayDice(dice);
	}
	
	/* We do the same thing twice:
	 * We wait for player to select which dice to be rolled again
	 * We randomize the selected dice and display them */
	private void nextRolls(int player) {
		for (int i = 0; i < 2; i++) {
			display.printMessage("Select the Dice you wish to re-roll and click \"Roll Again\" ");
			display.waitForPlayerToSelectDice();
			randomizeDice(true);
			display.displayDice(dice);
		}
	}
	
	/* Dice Randomizer:
	 * if selectionTime is false, meaning that it's first roll and the player
	 * can't choose any dice, all dice will be randomized.
	 * else if selectionTime is True, meaning that it's not the first roll,
	 * and the player can choose which dice to be selected, only the
	 * selected dice will be randomized */
	private void randomizeDice(boolean selectionTime) {
		for (int i = 0; i < N_DICE; i++) {
			if (!selectionTime) {
				dice[i] = rgen.nextInt(1,6);
			} else if ( display.isDieSelected(i)) {
				dice[i] = rgen.nextInt(1,6);
			}
		}
	}
	
	private int selectCategory(int player) {
		display.printMessage("Select a category for this roll.");
		
		int	category = display.waitForPlayerToSelectCategory();
		
		while ( isCategoryChecked[player - 1][category - 1] == true) {
			display.printMessage("You can't choose the same category twice! Select another one.");
			category = display.waitForPlayerToSelectCategory();
		}
		isCategoryChecked[player - 1][category - 1] = true;
		
		return category;
	}
	
	private void updateScore(int category, int player, int round) {
		boolean p = checkCategory(dice, category);
		
		int score = getScore(category, p);
		
		addScoreToLowerOrUpper(category, player, score);
		
		if (round == N_SCORING_CATEGORIES) {
			updateAllScores(player, round);
		}
		
		display.updateScorecard(category, player, score);
	}
	
	/* Checks if the selected category is valid 
	 * Returns true if is it, else returns false */
	private boolean checkCategory(int[] dice, int category) {
		sortArray(dice);
		if (category == THREE_OF_A_KIND) {
			return checkThreeOfAKind(dice);
		} else if (category == FOUR_OF_A_KIND) {
			return checkFourOfAKind(dice);
		} else if (category == FULL_HOUSE) {
			return checkFullHouse(dice);
		} else if (category == SMALL_STRAIGHT) {
			return checkSmallStraight(dice);
		} else if (category == LARGE_STRAIGHT) {
			return checkLargeStraight(dice);
		} else if (category == YAHTZEE) {
			return checkYahtzee(dice);
		}
		// We return true here because all other categories are valid
		return true; 
	}
	
	//Sorts an array with selection sort
	private void sortArray(int[] dice) {
		for (int i = 0; i < N_DICE - 1; i++) {
			int minIndex = i;
			for (int q = i + 1; q < N_DICE; q++) {
				if ( dice[minIndex] > dice[q]) {
					minIndex = q;
				}
			}
			
			int temp = dice[i];
			dice[i] = dice[minIndex];
			dice[minIndex] = temp;
		}
	}
	
	// Checks if the dice configuration is three of a kind
	private boolean checkThreeOfAKind(int[] dice) {
		if (dice[0] == dice[1] && dice[1] == dice[2]) return true;
		else if (dice[1] == dice[2] && dice[2] == dice[3]) return true;
		else if (dice[2] == dice[3] && dice[3] == dice[4]) return true;
		return false;
	}
	
	// Checks if the dice configuration is four of a kind
	private boolean checkFourOfAKind(int[] dice) {
		if (dice[0] == dice[1] && dice[1] == dice[2] && dice[2] == dice[3]) return true;
		else if (dice[1] == dice[2] && dice[2] == dice[3] && dice[3] == dice[4]) return true;
		return false;
	}
	
	// Checks if the dice configuration is full house
	private boolean checkFullHouse(int[] dice) {
		if (dice[0] == dice[1] && dice[1] != dice[2] && dice[2] == dice[3] && dice[3] == dice[4]) 
			return true;
		else if (dice[0] == dice[1] && dice[1] == dice[2] && dice[2] != dice[3] && dice[3] == dice[4])
			return true;
		return false;
	}
	
	// Checks if the dice configuration is small straight
	private boolean checkSmallStraight(int[] dice) {
		boolean one = false, two = false, three = false, four = false, five = false, six = false;
		for (int i = 0; i < N_DICE; i++) {
			if (dice[i] == 1) one = true;
			else if (dice[i] == 2) two = true;
			else if (dice[i] == 3) three = true;
			else if (dice[i] == 4) four = true;
			else if (dice[i] == 5) five = true;
			else six = true;
		}
		if (one && two && three && four) 
			return true;
		else if(two && three && four && five)
			return true;
		else if(three && four && five && six)
			return true;
		return false;
	}
	
	// Checks if the dice configuration is large straight
	private boolean checkLargeStraight(int[] dice) {
		if (dice[0] + 1 == dice[1] && dice[1] + 1 == dice[2] && dice[2] + 1 == dice[3] && dice[3] + 1 == dice[4]) 
			return true;
		return false;
	}
	
	// Checks if the dice configuration is yahtzee
	private boolean checkYahtzee(int[] dice) {
		for (int i = 0; i < N_DICE - 1; i++) {
			if (dice[i] != dice[i+1]) return false;
		}
		return true;
	}
	
	// Adds score to Sum of Lower Scores Or to Sum Of Upper Scores
	private void addScoreToLowerOrUpper(int category, int player, int score) {
		if (category <= SIXES) {
			sumOfUpperScores[player - 1] += score;
		} else {
			sumOfLowerScores[player - 1] += score;
		}
	}
	
	// Updates Every score (Called when the last round is over)
	private void updateAllScores(int player, int round) {
			int bonus = 0;
			// Updates Upper Score score
			display.updateScorecard(UPPER_SCORE, player, sumOfUpperScores[player - 1]);
			// Updates Upper Bonus score
			if (sumOfUpperScores[player - 1] >= 63) {
				display.updateScorecard(UPPER_BONUS, player, 35);
				bonus = 35;
			} else {
				display.updateScorecard(UPPER_BONUS, player, 0);
			}
			
			display.updateScorecard(LOWER_SCORE, player, sumOfLowerScores[player - 1]);
			
			sumOfAllScores[player - 1] = sumOfUpperScores[player - 1] + sumOfLowerScores[player - 1] + bonus;
			
			display.updateScorecard(TOTAL, player, sumOfAllScores[player - 1]);
		
	}
	
	/* Gets score for the chosen if category
	* If the selected category is not valid, it returns 0
	* else it returns the appropriate score for the chosen category */
	private int getScore(int category, boolean selectedCategoryIsValid) {
		if (!selectedCategoryIsValid) {
			return 0;
		} else if (category <= SIXES) {
			return getSum(category);
		} else if (category == THREE_OF_A_KIND || category == FOUR_OF_A_KIND || category == CHANCE) {
			return getSum();
		} else if (category == FULL_HOUSE) {
			return 25;
		} else if (category == SMALL_STRAIGHT) {
			return 30;
			
		} else if (category == LARGE_STRAIGHT) {
			return 40;
		} else  {
			return 50;
		}
	}
	
	// Gets the sum of all dice
	private int getSum() {
		int sum = 0;
		for (int i = 0; i < N_DICE; i++) {
			sum += dice[i];
		}
		return sum;
	}
	
	/* Gets the sum of all dice that are equal to num
	* Example: if dice = {1,2,2,2,4}:
	* get(2) returns 6, get(1) returns 1, get(5) returns 0*/
	private int getSum(int num) {
		int sum = 0;
		for (int i = 0; i < N_DICE; i++) {
			if (dice[i] == num) sum += dice[i];
		}
		return sum;
	}
	
	// Prints the winner message
	private void winnerMessage() {
		int winnerScore = getWinnerScore();
		ArrayList<String> winnersArray = new ArrayList<String>();
		getAllWinners(winnerScore, winnersArray);
		if (winnersArray.size() == 1) {
			display.printMessage("Congratulations,"+winnersArray.get(0)+", you're the winner with a total score of "+winnerScore+"!");
		} else {
			String winners = getWinnersString(winnersArray);
			display.printMessage(winners + " you guys tied with a total score of " + winnerScore+"!");
		}
	}
	
	// Gets the highest score
	private int getWinnerScore() {
		int winnerScore = sumOfAllScores[0];
		for (int i = 0; i < nPlayers - 1; i++) {
			if ( sumOfAllScores[i+1] > sumOfAllScores[i]) {
				winnerScore = sumOfAllScores[i+1];
			}
		}
		return winnerScore;
	}
	
	// Adds all winners (there might be more than one who got the highest score) in array
	private void getAllWinners(int winnerScore, ArrayList<String> winnersArray) {
		for (int i = 0; i < nPlayers; i++) {
			if (sumOfAllScores[i] == winnerScore) {
				winnersArray.add(playerNames[i]);
			}
		}
	}
	
	// If There is more than two winners, This method returns their names by comma-separated
	private String getWinnersString(ArrayList<String> winnersArray) {
		String winners = "";
		for (int i = 0; i < winnersArray.size(); i++) {
			winners += winnersArray.get(i) + ", ";
		}
		return winners;
	}
	
/* Private instance variables */
	private int nPlayers;
	private boolean[][] isCategoryChecked;
	private int[] dice; 
	private int[] sumOfUpperScores;
	private int[] sumOfLowerScores;
	private int[] sumOfAllScores;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();

}
