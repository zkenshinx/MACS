/* https://acm.timus.ru/problem.aspx?space=1&num=1183 */
// Compiler: G++ 9.2 x64
#include <string>
#include <iostream>
#include <cstddef>

using namespace std;

bool isMatch(char ch1, char ch2) {
    return (ch1 == '(' && ch2 == ')') || (ch1 == '[' && ch2 == ']');
}

char getMatch(char ch) {
    if (ch == '(') return ')';
    if (ch == ')') return '(';
    if (ch == '[') return ']';
    return '[';
}

string longest_valid_subsequence(string &str) {
    string subs[str.size()][str.size()];
    for (int i = 0; i < str.size() - 1; i++) {
        if (isMatch(str[i], str[i + 1])) {
            subs[i][i + 1] = subs[i][i + 1] + str[i] + str[i + 1];
        }
    }

    for (int len = 3; len <= str.size(); ++len) {
        for (int i = 0; i < str.size() - len + 1; ++i) {
            int j = i + len - 1;
            if (isMatch(str[i], str[j])) 
                subs[i][j] = str[i] + subs[i + 1][j - 1] + str[j];
            for (int k = i; k < j; ++k) {
               if (subs[i][j].size() < subs[i][k].size() + subs[k + 1][j].size()) {
                   subs[i][j] = subs[i][k] + subs[k + 1][j];
               }
            }
        }
    } 

    return subs[0][str.size() - 1];
}

void solve(string &str) {
    if (str.size() == 0) {
        cout << "" << endl;
        return;
    }
    string longest = longest_valid_subsequence(str);
    string result(2 * str.size() - longest.size(), ' ');
    int longestInd = 0, strInd = 0, resultInd = 0;
    while (resultInd < result.size()) {
        if (longest[longestInd] == str[strInd]) {
            result[resultInd] = str[strInd];
            ++longestInd; ++strInd; ++resultInd;
        } else {
            if (str[strInd] == '(' || str[strInd] == '[') {
                result[resultInd] = str[strInd];
                result[resultInd + 1] = getMatch(str[strInd]);
            } else {
                result[resultInd + 1] = str[strInd];
                result[resultInd] = getMatch(str[strInd]);
            }
            strInd++;
            resultInd += 2;
        }   
    }
    cout << result << endl;
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);
    string str;
    cin >> str;
    solve(str);
    return 0;
}
