/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas
	implements NameSurferConstants, ComponentListener {

	/**
	* Creates a new NameSurferGraph object that displays the data.
	*/
	public NameSurferGraph() {
		addComponentListener(this);
		
		entries = new ArrayList<NameSurferEntry>();
	}
	
	/**
	* Clears the list of name surfer entries stored inside this class.
	*/
	public void clear() {
		entries.clear();
	}
	
	/* Method: addEntry(entry) */
	/**
	* Adds a new NameSurferEntry to the list of entries on the display.
	* Note that this method does not actually draw the graph, but
	* simply stores the entry; the graph is drawn by calling update.
	*/
	public void addEntry(NameSurferEntry entry) {
		if (!entries.contains(entry)) {
			entries.add(entry);
		}
	}
	
	
	
	/**
	* Updates the display image by deleting all the graphical objects
	* from the canvas and then reassembling the display according to
	* the list of entries. Your application must call update after
	* calling either clear or addEntry; update is also called whenever
	* the size of the canvas changes.
	*/
	public void update() {
		drawLines();
		drawYearLabels();
		drawNames();
	}
	
	private void drawLines() {
		drawVerticalLines();
		drawUpperHorizontalLine();
		drawLowerHorizontalLine();
	}
		
	private void drawVerticalLines() {
		removeAll();
		for (int i = 0; i < NDECADES; i++) {
			GLine line = new GLine(i*getWidth()/(NDECADES), 0, i*getWidth()/(NDECADES), getHeight());
			add(line);
		}
	}

	private void drawUpperHorizontalLine() {
		GLine line = new GLine(0, GRAPH_MARGIN_SIZE, getWidth(), GRAPH_MARGIN_SIZE);
		add(line);
	}

	private void drawLowerHorizontalLine() {
		GLine line = new GLine(0, getHeight()-GRAPH_MARGIN_SIZE, getWidth(), getHeight()-GRAPH_MARGIN_SIZE);
		add(line);
	}

	private void drawYearLabels() {
		for (int i = 0; i < NDECADES; i++) {
			GLabel year = new GLabel(START_DECADE + i*10 + "");
			add(year, i*getWidth()/(NDECADES)+1 , getHeight() - GRAPH_MARGIN_SIZE + year.getAscent());
		}
	}
	
	private void drawNames() {
		for (int i = 0; i < entries.size(); i++) {
			NameSurferEntry currentEntry = entries.get(i);
			for (int j = 0; j < NDECADES - 1; j++) {
				drawLineBetweenDecades(j, currentEntry, getLineColor(i));
			}
		}
	}
	
	private void drawLineBetweenDecades(int j, NameSurferEntry currentEntry, Color color) {
		double startX = j*getWidth()/(NDECADES);
		double endX = (j+1)*getWidth()/(NDECADES);
		
		int currentDecade = START_DECADE + 10*j;
		int nextDecade = currentDecade + 10;

		double startY = getYCoordinate(currentDecade, currentEntry);
		double endY = getYCoordinate(nextDecade, currentEntry);
		
		// Draw Line Between Vertical Lines
		GLine line = new GLine(startX, startY, endX, endY);
		line.setColor(color);
		add(line);
		// Add name a little right to vertical line 
		GLabel name = new GLabel(currentEntry.getName() + " " + currentEntry.getRank(currentDecade));
		name.setColor(color);
		add(name, startX+2, startY);
		if (j == NDECADES - 2) {
			GLabel lastName = new GLabel(currentEntry.getName() + " " + currentEntry.getRank(nextDecade));
			lastName.setColor(color);
			add(lastName, endX+2, endY);
		}
	}
	
	private double getYCoordinate(int decade, NameSurferEntry currentEntry) {
		if (currentEntry.getRank(decade) != 0 ) {
			return GRAPH_MARGIN_SIZE+
					(currentEntry.getRank(decade))*(getHeight()-2*GRAPH_MARGIN_SIZE)/MAX_RANK;
		}  else {
			return getHeight() - GRAPH_MARGIN_SIZE;
		}
	}
	
	
	private Color getLineColor(int i) {
		if (i % 4 == 0) return Color.BLACK;
		if (i % 4 == 1) return Color.RED;
		if (i % 4 == 2) return Color.BLUE;
		else return Color.YELLOW;
	}


	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentResized(ComponentEvent e) { update(); }
	public void componentShown(ComponentEvent e) { }
	
	// private instance variable
	private ArrayList<NameSurferEntry> entries;

}
