/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.gui.IntField;
import acm.io.IODialog;
import acm.program.*;
import acm.util.RandomGenerator;

import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

public class NameSurfer extends Program implements NameSurferConstants {

/* Method: init() */
/**
 * This method has the responsibility for reading in the data base
 * and initializing the interactors at the bottom of the window.
 */
	public void init() {
		addSouthInteractors();
		addEastInteractors();
			
		addActionListeners();
		
		data = new NameSurferDataBase(NAMES_DATA_FILE);

		graph = new NameSurferGraph();
		add(graph);
		graph.update(getGraphType(), getThemeType());
		
	}
		
	private void addSouthInteractors() {
	    add(new JLabel("Name "), SOUTH);
	    addInputTextField();
	    add(new JButton("Graph"), SOUTH);
	    add(new JButton("Clear"), SOUTH);
	    add(new JButton("Delete"), SOUTH);
	}
	
	private void addEastInteractors() {
		addThemeInteractors();
		addGraphTypeInteractors();
		addColorInteractors();
	}
	
	// Adds interactors that change the theme of application
	private void addThemeInteractors() {
		add(new JLabel("Theme:"), EAST);
		ButtonGroup themeGroup = new ButtonGroup();
		
		light = new JRadioButton("Light", true);
		dark = new JRadioButton("Dark", false);
		
		themeGroup.add(light);
		themeGroup.add(dark);
		
		add(light, EAST);
		add(dark, EAST);
	}
	
	// Adds interactors that change the type of graph
	private void addGraphTypeInteractors() {
		add(new JLabel("Graph type:"), EAST);
		ButtonGroup graphTypeGroup = new ButtonGroup();

		lines = new JRadioButton("Lines", true);
		bars = new JRadioButton("Bars", false);
		
		graphTypeGroup.add(lines);
		graphTypeGroup.add(bars);
		
		add(lines, EAST);
		add(bars, EAST);
	}

	// Adds interactors that determines the color of NameSurferEntry object
	private void addColorInteractors() {
		add(new JLabel("Color: "), EAST);
		
		ButtonGroup colorGroup = new ButtonGroup();
		
		randomColor = new JRadioButton("Random", true);
		customColor = new JRadioButton("Custom", false);
		
		colorGroup.add(randomColor);
		colorGroup.add(customColor);
		
		add(randomColor, EAST);
		add(customColor, EAST);
		
		addRGBInputField();
	}
	
	// Add The Input Text Field where you write the name
	private void addInputTextField() {
		input = new JTextField(20);
		add(input, SOUTH);
		input.addActionListener(this);
	}
	
	// Adds IntFields where you write RGB color numbers
	private void addRGBInputField() {
	    RField = new IntField(0, 255);
	    GField = new IntField(0, 255);
	    BField = new IntField(0, 255);
	    add(new JLabel("R"), EAST);
		add(RField, EAST);
	    add(new JLabel("G"), EAST);
		add(GField, EAST);
	    add(new JLabel("B"), EAST);
		add(BField, EAST);
	}
	
	// Returns the value of custom color
	private Color getCustomColor() {
		int r = Integer.parseInt(RField.getText());
		int g = Integer.parseInt(GField.getText());
		int b = Integer.parseInt(BField.getText());
		return new Color(r, g, b);
	}
	
	public void run() {
		while (true) {
			graph.update(getGraphType(), getThemeType());
			pause(50);
		}
	}
	
/* Method: actionPerformed(e) */
/**
 * This class is responsible for detecting when the buttons are
 * clicked, so you will have to define a method to respond to
 * button actions.
 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Graph")) {
			graphName();
		} else if (e.getActionCommand().equals("Clear")) {
			graph.clear();
		} else if (e.getActionCommand().equals("Delete")) {
			removeName();
		}
		
	}
	
	// Assigns color to the name and adds the name in graph
	private void graphName() {
		String name = input.getText();
		NameSurferEntry entry = data.findEntry(name);
		if (entry != null ) {
			assignColor(entry);
			graph.addEntry(entry);
		}	
	}
	
	// Assigns color to the name
	private void assignColor(NameSurferEntry entry) {
		RandomGenerator rgen = new RandomGenerator();
		Color color = Color.BLACK;
		if (customColor.isSelected()) {
			checkCustomColorInput();
			color = getCustomColor();
		} else {
			color = rgen.nextColor();
		}
		graph.assignColor(entry, color);
	}
	
	// Checks if the RGB color inputs are correct
	private void checkCustomColorInput() {
	    IODialog dialog = new IODialog();
		if (!isNumber(RField.getText()) || 
				Integer.parseInt(RField.getText()) > 255 || Integer.parseInt(RField.getText()) < 0) {
		    int n = -1;
		    while (n > 255 || n < 0) {
		    	n = dialog.readInt("Enter an integer between 0 and 255 for R color: ");
		    }
		    RField.setText(n+"");
		}
		if (!isNumber(GField.getText()) || 
				Integer.parseInt(GField.getText()) > 255 || Integer.parseInt(GField.getText()) < 0) {
		    int n = -1;
		    while (n > 255 || n < 0) {
		    	n = dialog.readInt("Enter an integer between 0 and 255 for G color: ");
		    }
		    GField.setText(n+"");
		}
		if (!isNumber(BField.getText()) || 
				Integer.parseInt(BField.getText()) > 255 || Integer.parseInt(BField.getText()) < 0) {
		    int n = -1;
		    while (n > 255 || n < 0) {
		    	n = dialog.readInt("Enter an integer between 0 and 255 for B color: ");
		    }
		    BField.setText(n+"");
		}
	}
	
	// Checks if given string is a number
	private boolean isNumber(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	// Removes name from graph
	private void removeName() {
		String name = input.getText();
		NameSurferEntry entry = data.findEntry(name);
		if (entry != null ) {
			graph.removeEntry(entry);
		}	
	}
	
	// Gets the type of graph
	private String getGraphType() {
		if (lines.isSelected()) {
			return "Lines";
		} else {
			return "Bars";
		}
	}
	
	// Gets the type of theme
	private String getThemeType() {
		if (light.isSelected()) {
			return "Light";
		} else {
			return "Dark";
		}
	}
	
	// Instance variables
	private NameSurferDataBase data;
	private NameSurferGraph graph;
	private JTextField input;
	private JRadioButton lines;
	private JRadioButton bars;
	private JRadioButton randomColor;
	private JRadioButton customColor;
	private JRadioButton light;
	private JRadioButton dark;
	private IntField RField;
	private IntField GField;
	private IntField BField;
}
