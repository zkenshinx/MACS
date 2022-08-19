import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

import acm.util.ErrorException;

/*
 * File: NameSurferDataBase.java
 * -----------------------------
 * This class keeps track of the complete database of names.
 * The constructor reads in the database from a file, and
 * the only public method makes it possible to look up a
 * name and get back the corresponding NameSurferEntry.
 * Names are matched independent of case, so that "Eric"
 * and "ERIC" are the same names.
 */

public class NameSurferDataBase implements NameSurferConstants {
	
/* Constructor: NameSurferDataBase(filename) */
/**
 * Creates a new NameSurferDataBase and initializes it using the
 * data in the specified file.  The constructor throws an error
 * exception if the requested file does not exist or if an error
 * occurs as the file is being read.
 */
	public NameSurferDataBase(String filename) {
		try {
			BufferedReader file = new BufferedReader(new FileReader(filename));
			while(true) {
				String line = file.readLine();
				
				if (line == null) {
					break;
				}
				
				nameInformation.put(getName(line), getNameSurferEntry(line));
			}
			file.close();
		} catch (IOException ex) {
			throw new ErrorException(ex);
		}
	}
	
	// Returns the name of given our text file line
	private String getName(String line) {
		StringTokenizer token = new StringTokenizer(line);
		return token.nextToken();
	}
	
	// creates a NameSurferEntry object which stores data of given line
	private NameSurferEntry getNameSurferEntry(String line) {
		StringTokenizer tokens = new StringTokenizer(line);
		
		String name = tokens.nextToken(); // Gets the first token which is the name
		
		int[] popularity = new int[NDECADES];
		for (int index = 0; index < NDECADES; index++) {
			popularity[index] = Integer.parseInt(tokens.nextToken());
		}
		return new NameSurferEntry(name, popularity);
	}
	
/* Method: findEntry(name) */
/**
 * Returns the NameSurferEntry associated with this name, if one
 * exists.  If the name does not appear in the database, this
 * method returns null.
 */
	public NameSurferEntry findEntry(String name) {
		if (nameInformation.containsKey(name)) {
			return nameInformation.get(name);
		}
		return null;
	}
	
	// Instance variables
	HashMap<String, NameSurferEntry> nameInformation = new HashMap<String, NameSurferEntry>();
}

