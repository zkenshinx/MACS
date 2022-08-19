/******************************************************************************
 * File: Trailblazer.cpp
 *
 * Implementation of the graph algorithms that comprise the Trailblazer
 * assignment.
 */

#include "Trailblazer.h"
#include "TrailblazerGraphics.h"
#include "TrailblazerTypes.h"
#include "TrailblazerPQueue.h"
using namespace std;

/* Function: shortestPath
 * 
 * Finds the shortest path between the locations given by start and end in the
 * specified world.	 The cost of moving from one edge to the next is specified
 * by the given cost function.	The resulting path is then returned as a
 * Vector<Loc> containing the locations to visit in the order in which they
 * would be visited.	If no path is found, this function should report an
 * error.
 *
 * In Part Two of this assignment, you will need to add an additional parameter
 * to this function that represents the heuristic to use while performing the
 * search.  Make sure to update both this implementation prototype and the
 * function prototype in Trailblazer.h.
 */
Vector<Loc>
shortestPath(Loc start,
             Loc end,
             Grid<double>& world,
             double costFn(Loc from, Loc to, Grid<double>& world),
			 double heuristic(Loc start, Loc end, Grid<double>& world)) {
	
    Vector<Loc> result;
	
	Grid<Color> colors(world.nRows, world.nCols);
	colorAll(colors, GRAY); // Color all nodes gray
	
	// Color starting node yellow
	colors[start.row][start.col] = YELLOW; 
	colorCell(world, start, YELLOW);

	Grid<double> distances(world.nRows, world.nCols);

	TrailblazerPQueue<Loc> pq;
	pq.enqueue(start, heuristic(start, end, world));

	Map<Loc, Loc> parent;
	
	while (!pq.isEmpty()) {
		Loc curr = pq.dequeueMin();
		colors[curr.row][curr.col] = GREEN;
		colorCell(world, curr, GREEN);

		if (curr == end) {
			convertPathToVector(result, parent, end);
			return result;
		}

		for (int row = curr.row - 1; row <= curr.row + 1; row++) {
			for (int col = curr.col - 1; col <= curr.col + 1; col++) {
				if ((curr.row == row && curr.col == col) || !world.inBounds(row,col)) continue;
				Loc v = makeLoc(row, col);
				double distL = distances[curr.row][curr.col]+costFn(curr, v, world);
				if (colors[row][col] == GRAY) {
					colors[row][col] = YELLOW;
					colorCell(world, v, YELLOW);
					distances[row][col] = distL;
					parent[v] = curr;
					pq.enqueue(v, distL + heuristic(v, end, world));	
				} else if (colors[row][col] == YELLOW && distances[row][col] > distL) {
					distances[row][col] = distL;
					parent[v] = curr;
					pq.decreaseKey(v, distL + heuristic(v, end, world));
				}
			}
		}
	}

	return result;
}

// Given a map of parents, and an end location, it gives us a path from start to end
void convertPathToVector(Vector<Loc> &result, Map<Loc, Loc> &parent, Loc curr) {
	Stack<Loc> st;
	st.push(curr);

	while (parent.containsKey(curr)) {
		curr = parent[curr];
		st.push(curr);
	}
	while (!st.isEmpty()) {
		result.add(st.pop());
	}
}

// Colors the given grid with the given color
void colorAll(Grid<Color>& colors, Color cl) {
	for (int i = 0; i < colors.nRows; i++) {
		for (int j = 0; j < colors.nCols; j++) {
			colors[i][j] = cl;
		}
	}
}

Set<Edge> createMaze(int numRows, int numCols) {
	Set<Edge> result;
	Map<Edge, int> mapping; // Edge -> Weight;
	assignRandomWeights(numRows, numCols, mapping);
	
	// Make Clusters;
	Vector<int> clusters;
	for (int i = 0; i < numRows * numCols; i++) {
		clusters.push_back(i);
	}

	int clusterIndex = 0;
	TrailblazerPQueue<Edge> pq;
	// Place each node into its own cluster
	foreach (Edge edge in mapping) {
		// Insert in pq
		pq.enqueue(edge, mapping[edge]);
	}
	int count = clusters.size();
	// While there are two or more clusters remaining
	while (count > 1) {
		// Dequeue an edge e from the priority queue
		Edge currEdge = pq.dequeueMin();
		// If the endpoints of e are not in the same cluster:
		if (clusters[currEdge.start.row * numCols + currEdge.start.col] != clusters[currEdge.end.row * numCols + currEdge.end.col]) {
			// Merge the clusters containing the endpoints of e
			int startInd = clusters[currEdge.start.row * numCols + currEdge.start.col];
			int endInd = clusters[currEdge.end.row * numCols + currEdge.end.col];
			for (int i = 0; i < clusters.size(); i++) {
				if (clusters[i] == endInd) {
					clusters[i] = startInd;
				}
			}
			count--;
			// Add e to the resulting spanning tree
			result.add(currEdge);
		}
	}
    return result;
}

void assignRandomWeights(int numRows, int numCols, Map<Edge, int> &mapping) {
	for (int i = 0; i < numRows; i++) {
		for (int j = 0; j < numCols; j++) {
			if (i + 1 < numRows) {
				Edge curr1 = makeEdge(makeLoc(i, j), makeLoc(i + 1, j));
				mapping.put(curr1, randomInteger(0, 10));
			}
			if (j + 1 < numCols) {
				Edge curr2 = makeEdge(makeLoc(i, j), makeLoc(i, j + 1));
				mapping.put(curr2, randomInteger(0, 10));
			}
		}
	}
}