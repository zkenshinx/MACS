/* https://www.spoj.com/problems/DSUBSEQ/ */
// Compiler: C++ (g++ 4.3.2)
#include <bits/stdc++.h>

using namespace std;

const int mod = 1000000007;

long long distinctSubsequences(string &str) {
    long long dp[str.size()];
    int letters[26];
    for (int i = 0; i < 26; ++i) letters[i] = -1;    
    dp[0] = 1;
    for (int i = 0; i < str.size(); i++) {
        dp[i + 1] = (2 * dp[i]) % mod;
            if (letters[str[i]-'A'] > 0) {
                dp[i + 1] = (mod + dp[i + 1] - dp[letters[str[i]-'A']]) % mod;
            } else if (letters[str[i] - 'A'] == 0) {
                dp[i + 1] = (mod + dp[i + 1] - 1) % mod;
            }
        letters[str[i]-'A'] = i;
    }
    return dp[str.size()];
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);
    int t; cin >> t;
    while (t--) {
        string str; cin >> str;
        cout << distinctSubsequences(str) << endl;
    }
    return 0;
} 
