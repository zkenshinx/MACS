

#include <iostream>
#include <string>
#include <fstream>
#include "set.h"
#include "map.h"
#include "simpio.h"
#include "console.h"
using namespace std;

/* Function: listAllRNAStrandsFor(string protein,
 *                                Map<char, Set<string> >& codons);
 * Usage: listAllRNAStrandsFor("PARTY", codons);
 * ==================================================================
 * Given a protein and a map from amino acid codes to the codons for
 * that code, lists all possible RNA strands that could generate
 * that protein
 */
void listAllRNAStrandsFor(string protein, Map<char, Set<string> >& codons);

/* Function: loadCodonMap();
 * Usage: Map<char, Lexicon> codonMap = loadCodonMap();
 * ==================================================================
 * Loads the codon mapping table from a file.
 */
Map<char, Set<string> > loadCodonMap();

int main() {
    /* Load the codon map. */
    Map<char, Set<string> > codons = loadCodonMap();

    /* [TODO: Implement this!] */
	while (true) {
		string protein = getLine("Enter protein: ");
		listAllRNAStrandsFor(protein, codons);
	}
    return 0;
}

/* You do not need to change this function. */
Map<char, Set<string> > loadCodonMap() {
    ifstream input("codons.txt");
    Map<char, Set<string> > result;

    /* The current codon / protein combination. */
    string codon;
    char protein;

    /* Continuously pull data from the file until all data has been
     * read.
     */
    while (input >> codon >> protein) {
        result[protein] += codon;
    }

    return result;
}

void wrapper(string protein, Map<char, Set<string> >& codons, string prefix) {
	if (protein.length() == 0) {
		// Base case
		cout << prefix << endl;
	} else {
		
		char currentChar = protein[0]; 
		string nextProtein = protein.substr(1);
		foreach (string codon in codons.get(currentChar)) {
			wrapper( nextProtein, codons, prefix + codon);
		}
	
	}
}

void listAllRNAStrandsFor(string protein, Map<char, Set<string> >& codons) {
	wrapper(protein, codons, "");
}