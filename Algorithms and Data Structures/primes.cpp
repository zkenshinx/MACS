/* Link: https://www.spoj.com/problems/PRIME1/ */
#include <bits/stdc++.h>
 
using namespace std;
 
void sieve(vector<int>& primes, int upper) {
    vector<int> arr(upper + 1);
    for (int i = 2; i <= upper; i++) {
        if (arr[i] == 0) {
            arr[i] = i;
            primes.push_back(i);
        } 
        for (int j = 0; j < primes.size() && primes[j] <= arr[i] && i * primes[j] <= upper; j++) {
            arr[i * primes[j]] = primes[j];
        }
    }
}
 
bool isPrime(int num, vector<int>& primes) {
    if (num == 1) return false;
    for (int i = 0; i < primes.size(); i++) {
        if (num == primes[i]) return true;
        if (num % primes[i] == 0) return false;
    }
    return true;
}
 
int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);
    
    int t, m, n;
    cin >> t;
    vector<pair<int, int>> testCases;
    int max = 1;
    for (int i = 0; i < t; i++) {
        cin >> m >> n;
        max = fmax(max, n);
        testCases.push_back({m,n});
    }
 
    vector<int> primes;
    sieve(primes, (int) sqrt(max));
    for (int i = 0; i < t; i++) {  
        for (int j = testCases[i].first; j <= testCases[i].second; j++) {
            if (isPrime(j, primes)) {
               cout << j << '\n';
            }   
        }
        if (i < t - 1) cout << '\n';
    }
    return 0;
} 
