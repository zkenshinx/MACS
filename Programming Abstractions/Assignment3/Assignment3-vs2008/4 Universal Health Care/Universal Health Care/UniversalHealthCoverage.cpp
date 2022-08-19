/*
 * File: UniversalHealthCoverage.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the UniversalHealthCoverage problem
 * on Assignment #3.
 * [TODO: extend the documentation]
 */
#include <iostream>
#include <string>
#include "set.h"
#include "vector.h"
#include "console.h"
using namespace std;

/* Function: canOfferUniversalCoverage(Set<string>& cities,
 *                                     Vector< Set<string> >& locations,
 *                                     int numHospitals,
 *                                     Vector< Set<string> >& result);
 * Usage: if (canOfferUniversalCoverage(cities, locations, 4, result)
 * ==================================================================
 * Given a set of cities, a list of what cities various hospitals can
 * cover, and a number of hospitals, returns whether or not it's
 * possible to provide coverage to all cities with the given number of
 * hospitals.  If so, one specific way to do this is handed back in the
 * result parameter.
 */
bool canOfferUniversalCoverage(Set<string>& cities,
                               Vector< Set<string> >& locations,
                               int numHospitals,
                               Vector< Set<string> >& result);
bool wrapper(Set<string>& cities, Vector< Set<string> >& locations,
				int numHospitals, Vector< Set<string> >& result,
				Set<string>& citiesLeft, int index);

int main() {

    return 0;
}



bool canOfferUniversalCoverage(Set<string>& cities,
                               Vector< Set<string> >& locations,
                               int numHospitals,
							   Vector< Set<string> >& result) {
	Set<string> citiesLeft = cities;
	int index = 0;
	return wrapper(cities, locations, numHospitals, result, citiesLeft, index);
}

void removeCities(Set<string>& toBeRemoved, Set<string>& citiesLeft) {
	citiesLeft -= toBeRemoved;
}

void addCities(Set<string>& toBeAdded, Set<string>& citiesLeft) {
	citiesLeft += toBeAdded;
}



bool wrapper(Set<string>& cities, Vector< Set<string> >& locations,
int numHospitals, Vector< Set<string> >& result, Set<string>& citiesLeft, int index) {
	if (citiesLeft.isEmpty()) {
		return true;
	} else if (numHospitals == 0 || locations.size() <= index) {
		return false;
	}
	Set<string> currentLocations = locations[index];
	Set<string> sameCities = (currentLocations * citiesLeft);
	// Include current hospital
	removeCities(sameCities, citiesLeft);
	result.add(currentLocations);
	if (wrapper(cities, locations, numHospitals - 1, result, citiesLeft, index + 1)) {
		return true;
	}
	// Don't Include current hospital
	result.remove(result.size() - 1);
	addCities(sameCities, citiesLeft);
	if (wrapper(cities, locations, numHospitals, result, citiesLeft, index + 1)) {
		return true;
	}

	return false;
}