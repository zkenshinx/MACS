/* Link: https://acm.timus.ru/problem.aspx?space=1&num=1643 */
// Compiler: G++ 9.2 x64
#include <bits/stdc++.h>

using namespace std;

int n, m;

void print(vector<vector<int>>& vc) {
    for (int i = 0; i < vc.size(); i++) {
        for (int j = 0 ; j < vc[0].size(); j++) {
            cout << vc[i][j] << " ";
        }
         cout << endl;
    }
    cout << endl;
}

void bfs(vector<string>& vc, vector<vector<int>>& res, vector<vector<pair<int, int>>> teleports, int startI, int startJ) {
    queue<pair<int, int>> q;
    q.push({startI, startJ});
    int count = 0;
    int depth_size = 0;
    while (!q.empty()) {
        int i = q.front().first;
        int j = q.front().second;
        if (depth_size == 0) {
            depth_size = q.size();
            count++;
        }
        q.pop();
        depth_size--;
        for (int to_i = i - 1; to_i <= i + 1; ++to_i) {
            if (to_i < 0 || to_i >= n) continue;
            for (int to_j = j - 1; to_j <= j + 1; ++to_j) {
                if (to_j < 0 || to_j >= m) continue;
                if (res[to_i][to_j] != 0) continue;
                if (vc[to_i][to_j] == '*') continue;
                if (vc[to_i][to_j] == '#') continue;
                if (to_i == startI && to_j == startJ) continue;

                if (vc[to_i][to_j] >= 'A' && vc[to_i][to_j] <= 'Z') {
                    for (auto a : teleports[vc[to_i][to_j] - 'A']) {
                        q.push({a.first, a.second});
                        res[a.first][a.second] = count;
                    }
                } else {
                    q.push({to_i, to_j});
                    res[to_i][to_j] = count;
                }

            }
        }
    }
}

void solve(vector<string>& vc) {
    vector<vector<pair<int, int>>> teleports(26);
    // init teleports
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < m; ++j) {
            if (vc[i][j] >= 'A' && vc[i][j] <= 'Z') {
                teleports[vc[i][j] - 'A'].push_back({i ,j});
            }
        }
    }
    // find catherine, gelu;
    int ci, cj, gi, gj, fi, fj;
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < m; ++j) {
            if (vc[i][j] == '!') {
                gi = i;
                gj = j;
            } else if (vc[i][j] == '$') {
                ci = i;
                cj = j;
            } else if (vc[i][j] == '*') {
                fi = i;
                fj = j;
            }
        }
    }
    
    vector<vector<int>> res1(n, vector<int>(m));
    bfs(vc, res1, teleports, ci, cj);
    vector<vector<int>> res2(n, vector<int>(m));
    bfs(vc, res2, teleports, gi, gj);
    int min = INT32_MAX;
    for (int i = fi - 1; i <= fi + 1; ++i) {
        if (i < 0 || i >= n) continue;
        for (int j = fj - 1; j <= fj + 1; ++j) {
            if (j < 0 || j >= m) continue;
            if (res1[i][j] == 0 && !(ci == i && cj == j)) continue;
            if (res2[i][j] == 0 && !(gi == i && gj == j)) continue;
            min = fmin(min, fmax(res1[i][j], res2[i][j]));
        }
    }
    cout << (min == INT32_MAX ? "Impossible" : to_string(min + 1)) << endl;
}

int main() {
    cin >> n >> m;
    vector<string> vc;
    for (int i = 0; i < n; i++) {
        string str; cin >> str;
        vc.push_back(str);
    }
    solve(vc);
    return 0;
}

