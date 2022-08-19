/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.*;

public class FacePamphlet extends Program 
					implements FacePamphletConstants {

	/**
	 * This method has the responsibility for initializing the 
	 * interactors in the application, and taking care of any other 
	 * initialization that needs to be performed.
	 */
	public void init() {
		addNorthInteractors();
		addWestInteractors();
		
		addActionListeners();
		
		data = new FacePamphletDatabase();
		currentActiveProfile = null;
		
		canvas = new FacePamphletCanvas();
		add(canvas);
		
		readFromFile();
		
	}
    
	private void addNorthInteractors() {
		add(new JLabel("Name "), NORTH);
		nameInput = new JTextField(TEXT_FIELD_SIZE);
		add(nameInput, NORTH);
		
		add(new JButton("Add"), NORTH);
		add(new JButton("Delete"), NORTH);
		add(new JButton("Lookup"), NORTH);
	}
  
	private void addWestInteractors() {
		statusInput = new JTextField(TEXT_FIELD_SIZE);
		add(statusInput, WEST);
		statusInput.addActionListener(this);
		add(new JButton("Change Status"), WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST); 
		
		pictureInput = new JTextField(TEXT_FIELD_SIZE);
		add(pictureInput, WEST);
		pictureInput.addActionListener(this);
		add(new JButton("Change Picture"), WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST); 
		
		friendInput = new JTextField(TEXT_FIELD_SIZE);
		add(friendInput, WEST);
		friendInput.addActionListener(this);
		add(new JButton("Add Friend"), WEST);
	}
	
	
    /**
     * This class is responsible for detecting when the buttons are
     * clicked or interactors are used, so you will have to add code
     * to respond to these actions.
     */
    public void actionPerformed(ActionEvent e) {
    	
    	String currentActionText = e.getActionCommand();
    	Object source = e.getSource();
		if (currentActionText.equals("Change Status") || source.equals(statusInput)) {
			changeStatus();
		} else if (currentActionText.equals("Change Picture") || source.equals(pictureInput)) {
			changePicture();
		} else if (currentActionText.equals("Add Friend") || source.equals(friendInput)) {
			addFriend();
		} else if (currentActionText.equals("Add")) {
			addButtonPressed();
		} else if (currentActionText.equals("Delete")) {
			deleteButtonPressed();
		} else if (currentActionText.equals("Lookup")) {
			lookupButtonPressed();
		} 
		
		saveOnFile();
	}

    private void changeStatus() {
    	if (currentActiveProfile != null) {
    		if (statusInput.getText().equals("")) {
        		canvas.displayProfile(currentActiveProfile);
        		canvas.showMessage("Please enter a valid status to update");
    		} else {
    			currentActiveProfile.setStatus(statusInput.getText());
        		canvas.displayProfile(currentActiveProfile);
        		canvas.showMessage("Status updated to " + statusInput.getText() + ".");
    		}
    	} else {
    		canvas.showMessage("Please select a profile to change status.");
    	}
    }
    
    private void changePicture() {
    	if (currentActiveProfile != null && !pictureInput.getText().equals("")) {
    		GImage image = null;
    		try {
    			image = new GImage(pictureInput.getText());
        		currentActiveProfile.setImage(image);
        		currentActiveProfile.setImageName(pictureInput.getText());
        		canvas.displayProfile(currentActiveProfile);
    			canvas.showMessage("Picture updated");
    		} catch (ErrorException ex) {
        		canvas.displayProfile(currentActiveProfile);
    			canvas.showMessage("Unable to open image file: " + pictureInput.getText());
    		}
    	} else if (currentActiveProfile == null) {
    		canvas.showMessage("Please select a profile to change picture.");
    	} else {
    		canvas.displayProfile(currentActiveProfile);
			canvas.showMessage("Please enter a valid picture name");
    	}
    }
    
    private void addFriend() {
    	if (currentActiveProfile != null) {
    		String newFriend = friendInput.getText();
    		
    		if (data.containsProfile(newFriend)) {
    			
    			if (currentActiveProfile.containsFriend(newFriend)) {
    				canvas.displayProfile(currentActiveProfile);
    				canvas.showMessage(currentActiveProfile.getName() + 
    						" already has " + newFriend + " as a friend.");
    			} else if (currentActiveProfile.getName().equals(newFriend)) {
    				canvas.displayProfile(currentActiveProfile);
    				canvas.showMessage("You can't add yourself as friend!");
    			} else {
    				currentActiveProfile.addFriend(newFriend);
    				data.getProfile(newFriend).addFriend(currentActiveProfile.getName());
    				canvas.displayProfile(currentActiveProfile);
    				canvas.showMessage(newFriend + " added as a friend.");
    			}
    			
    		} else if (newFriend.equals("")) {
				canvas.displayProfile(currentActiveProfile);
    			canvas.showMessage("Please enter a valid name.");
    		} else {
				canvas.displayProfile(currentActiveProfile);
    			canvas.showMessage("Such person doesn't exist.");
    		}
    		
    	} else {
    		canvas.showMessage("Please select a profile to add a friend.");
    	}
    }
    
    private void addButtonPressed() {
		String currentName = ignoredWhitespacesName(nameInput.getText());
		if (data.containsProfile(currentName)) {
			currentActiveProfile = data.getProfile(currentName);
			canvas.displayProfile(currentActiveProfile);
			canvas.showMessage("A profile with the name " + 
					currentName + " already exists.");
		} else if (!currentName.equals("")) {
			currentActiveProfile = new FacePamphletProfile(currentName);
			data.addProfile(currentActiveProfile);
			canvas.displayProfile(currentActiveProfile);
			canvas.showMessage("New profile created.");
		}
    }
    
    private String ignoredWhitespacesName(String name) {
    	StringTokenizer token = new StringTokenizer(name);
    	String newName = "";
    	while (token.hasMoreTokens()) {
    		
    		if (token.countTokens() == 1) {
    			newName += token.nextToken();
    		} else {
    			newName += token.nextToken() + " ";
    		}
    		
    	}
    	
    	return newName;
    }
    
    private void deleteButtonPressed() {
		String currentName = ignoredWhitespacesName(nameInput.getText());
		if (data.containsProfile(currentName)) {
			data.deleteProfile(currentName);
			
			currentActiveProfile = null;
			canvas.displayProfile(currentActiveProfile);
			canvas.showMessage("Profile of " + currentName
					+ " deleted.");
		} else if (!currentName.equals(""))  {
			canvas.displayProfile(currentActiveProfile);
			canvas.showMessage("A profile with the name " +
					currentName + " doesn't exist.");
		}	
    }
    
    private void lookupButtonPressed() {
		String currentName = ignoredWhitespacesName(nameInput.getText());
		if (data.containsProfile(currentName)) {
			currentActiveProfile = data.getProfile(currentName);
			canvas.displayProfile(currentActiveProfile);
			canvas.showMessage("Displaying " + currentName + ".");
		} else if (!currentName.equals("")) {
			currentActiveProfile = null;
			canvas.displayProfile(currentActiveProfile);
			canvas.showMessage("A profile with the name " + currentName 
					+ " doesn't exist.");
		} 
    }
    
    // Reads the data from file
    private void readFromFile() {
    	
    	try {
			BufferedReader dataFile = new BufferedReader(new FileReader(DATA_FILE_NAME));
			
			while (true) {
				String line = dataFile.readLine();
				if (line == null) break;
				createProfileFromDataFileLine(line);
			}
			
			dataFile.close();
		} catch (IOException e) {
			
		}

    	
    }
    
    private void createProfileFromDataFileLine(String line) {
    	int currentIndex = 1;
    	int startIndex = 1;
    	
 
    	FacePamphletProfile currentProfileCreated = null;
    	// Read name
    	while (true) {
    		if (line.charAt(currentIndex) == ']') {
    			String name = line.substring(startIndex, currentIndex);
    			currentProfileCreated = new FacePamphletProfile(name);
    			currentIndex += 2;    			
    			startIndex = currentIndex;    			
    			break;
    		}
    		currentIndex++;
    	}
    	
    	// Read status
    	while (true) {
    		if (line.charAt(currentIndex) == ']') {
    			String status = line.substring(startIndex, currentIndex);    			
    			currentProfileCreated.setStatus(status);   			
    			currentIndex += 2;    			
    			startIndex = currentIndex;   			
    			break;
    		}
    		currentIndex++;
    	}
    	
    	// Read Image
    	while (true) {
    		if (line.charAt(currentIndex) == ']') {
    			String imageName = line.substring(startIndex, currentIndex);
    			
    			if (imageName.length() != 0) {
    				try {
    					GImage image = new GImage(imageName);
            			
            			currentProfileCreated.setImage(image);
            			currentProfileCreated.setImageName(imageName);
    				} catch (ErrorException ex) {
    					
    	    		}
    			}
    			
    			currentIndex += 2;   			
    			startIndex = currentIndex;    			
    			break;
    		}
    		currentIndex++;
    	}
    	
    	data.addProfile(currentProfileCreated);
    	
    	// Read Friends
    	while (true) {
    		if (line.charAt(currentIndex) == ']') {
				String friendName = line.substring(startIndex, currentIndex);
				currentProfileCreated.addFriend(friendName);
    			break;
    		} else if (line.charAt(currentIndex) == ',') {
				String friendName = line.substring(startIndex, currentIndex);
				currentProfileCreated.addFriend(friendName);
				startIndex = currentIndex + 1;
    		}
    		currentIndex++;
    
    	}
    }
    
    // Saves the current data on file
    private void saveOnFile() {
    	ArrayList<FacePamphletProfile> profiles = data.getProfiles();
    	
    	int profilesSize = profiles.size();
    	    	
    	try {
			PrintWriter dataFile = new PrintWriter(new FileWriter("data.txt"));
			
	    	for (int profileIndex = 0; profileIndex < profilesSize; profileIndex++) {
	    		FacePamphletProfile currentProfile = profiles.get(profileIndex);

	    		dataFile.println(currentProfile.toString());
	    	}
						
			dataFile.close();
		} catch (IOException e) {
			
		}
    }
    
    /* Instance Variables */
    private JTextField nameInput;
    private JTextField statusInput;
    private JTextField pictureInput;
    private JTextField friendInput;
    private FacePamphletProfile currentActiveProfile;;
    private FacePamphletDatabase data;
    private FacePamphletCanvas canvas;
}
