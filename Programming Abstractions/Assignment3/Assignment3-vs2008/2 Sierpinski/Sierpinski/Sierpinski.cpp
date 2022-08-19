#include <iostream>
#include "gwindow.h"
#include "simpio.h"
#include <cmath>
using namespace std;

const int W_WIDTH = 1280;
const int W_HEIGHT = 720;

void drawTriangle(GWindow & window, GPoint center, double len, int order) {
	if (order == 0) {
		double leftX = center.getX() - len / 2;
		double leftY = center.getY() + len / (2 * sqrt(3.0) );
		GPoint pt(leftX, leftY); // Left lower angle
		
		pt = window.drawPolarLine(pt, len, 60);
		pt = window.drawPolarLine(pt, len, -60);
		window.drawPolarLine(pt, len, 180);
	} else {
		// Left lower triangle center
		GPoint pt1(center.getX() - len / 4, center.getY() + (len * sqrt(3.0)) / 8);
		// Right lower triangle center
		GPoint pt2(center.getX() + len / 4, center.getY() + (len * sqrt(3.0)) / 8);
		// Upper Triangle center
		GPoint pt3(center.getX(), center.getY() - (len * sqrt(3.0)) / 8);
		
		drawTriangle(window, pt1, len / 2, order - 1);
		drawTriangle(window, pt2, len / 2, order - 1);
		drawTriangle(window, pt3, len / 2, order - 1);
	}
}

int main() {
	
	GWindow window(W_WIDTH, W_HEIGHT);
	
	int centerX = window.getWidth()/2;
	int centerY = window.getHeight()/2;
	GPoint center(centerX, centerY);
	
	int len = getInteger("Enter triangles length: ");
	int order = getInteger("Enter fractal order: ");

	drawTriangle(window, center, len, order);

    return 0;
}
