/* Link: https://www.spoj.com/problems/KSMALL */
#include <bits/stdc++.h>

using namespace std;

unsigned arr[5000000];

pair<int, int> partition(unsigned * arr, int low, int high) {
    unsigned pivot = arr[high];
    int left = low;
    int mid = low;
    int right = high;
    int countMiddle = 0;
    while (mid <= right) {
        if (arr[mid] < pivot) {
            swap(arr[left], arr[mid]);
            left++;
            mid++;
        } else if (arr[mid] == pivot) {
            mid++;
            countMiddle++;
        } else {
            swap(arr[mid], arr[right]);
            right--;
        }
    }
    return {right - countMiddle + 1, right};
}

unsigned quickFind(unsigned * arr, int low, int high, unsigned k) {
    while (low <= high) {
        pair<int, int> p = partition(arr, low, high);
        if (p.first - low + 1 <= k && k <= p.second - low + 1) {
            return arr[p.first];
        } else if (p.first - low + 1 < k) {
            k -= p.second - low + 1;
            low = p.second + 1;
        } else {
            high = p.first - 1;
        }
    }   
    return -1;
}

void randomize(unsigned a,unsigned b,unsigned mod) {   
    for( int i = 0 ; i < 5000000 ; i++ ) {
        a = 31014 * (a & 65535) + (a >> 16);
        b = 17508 * (b & 65535) + (b >> 16);
        arr[i] = ((a << 16) + b) % mod;
    }
}

int main() {
    srand(time(NULL));
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);
    unsigned a, b, mod, k;
    cin >> a >> b >> mod >> k;
    randomize(a, b, mod);
    cout << quickFind(arr, 0, 4999999, k) << endl;
    return 0;
}
