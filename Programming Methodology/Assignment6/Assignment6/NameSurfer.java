/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class NameSurfer extends Program implements NameSurferConstants {

/* Method: init() */
/**
 * This method has the responsibility for reading in the data base
 * and initializing the interactors at the bottom of the window.
 */
	public void init() {
	    add(new JLabel("Name "), SOUTH);
	    addInputTextField();
	    add(new JButton("Graph"), SOUTH);
	    add(new JButton("Clear"), SOUTH);
	    
		addActionListeners();
		
		data = new NameSurferDataBase(NAMES_DATA_FILE);
		
		graph = new NameSurferGraph();
		add(graph);
	}

	private void addInputTextField() {
		input = new JTextField(20);
		add(input, SOUTH);
		input.addActionListener(this);
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
			graph.update();
		}
	}
	
	// Updates the graph with the new name
	private void graphName() {
		String name = input.getText();
		NameSurferEntry entry = data.findEntry(name);
		if (entry != null ) {
			graph.addEntry(entry);
			graph.update();
		}	
	}
	
	// Instance variables
	private NameSurferDataBase data;
	private NameSurferGraph graph;
	private JTextField input;
}
