#include <iostream>
#include "simpio.h"
#include "console.h"
using namespace std;

int C(int n, int k) {
	if (n == k || k == 0) {
		return 1;
	}

	return C(n-1, k-1) + C(n-1, k);
}

int main() {
	cout << "Enter n: ";
	int n = getInteger();
	cout << "Entern k: ";
	int k = getInteger();
	cout << "C(n,k) = " << C(n,k) << endl;
    return 0;
}
