/* https://onlinejudge.org/index.php?option=onlinejudge&Itemid=8&page=show_problem&problem=1099 */
// Compiler:
// C++ 5.3.0 - GNU C++ Compiler with options: -lm -lcrypt -O2 -pipe -DONLINE_JUDGE
#include <bits/stdc++.h>

using namespace std;

int n, a, x, y;
vector<int> friendParent, enemyParent, groupSize;

int findParent(int x) {
    if (x == friendParent[x])
        return x;
    friendParent[x] = findParent(friendParent[x]);
    return friendParent[x];
}

void unitePeople(int x, int y) {
    x = findParent(x);
    y = findParent(y);
    if (x != y) {
        if (groupSize[x] < groupSize[y]) swap(x, y);
        friendParent[y] = x;
        groupSize[x] += groupSize[y];
    }
}

int areFriends(int x, int y) {
    return findParent(x) == findParent(y) ? 1 : 0; 
}

int areEnemies(int x, int y) {
    int yParent = findParent(y);
    int xParent = findParent(x);
    if (enemyParent[xParent] == -1 || enemyParent[yParent] == -1) return 0;
    return xParent == findParent(enemyParent[yParent]) ? 1 : 0;
}

void setFriends(int x, int y) {
    if (areEnemies(x, y) == 1) {
        cout << -1 << '\n';
        return;
    }

    int xParent = findParent(x);
    int yParent = findParent(y);
    int someEnemy = enemyParent[xParent] != -1 ? enemyParent[xParent] : enemyParent[yParent];
    if (enemyParent[xParent] != -1 && enemyParent[yParent] != -1) 
        unitePeople(enemyParent[xParent], enemyParent[yParent]);
    unitePeople(x, y);
    enemyParent[findParent(x)] = someEnemy;
}

void setEnemies(int x, int y) {
    if (areFriends(x, y) == 1) {
        cout << -1 << '\n'; 
        return;
    }
    int yParent = findParent(y);
    int xParent = findParent(x);
    if (enemyParent[yParent] == -1) enemyParent[yParent] = xParent;
    if (enemyParent[xParent] == -1) enemyParent[xParent] = yParent;
    unitePeople(x, enemyParent[yParent]);
    unitePeople(y, enemyParent[xParent]);
}


void solve() {
    if (a == 1) {
        setFriends(x, y);
    } else if (a == 2) {
        setEnemies(x, y);
    } else if (a == 3) {
        cout << areFriends(x, y) << '\n';
    } else if (a == 4) {
        cout << areEnemies(x, y) << '\n';
    }
}

int main() {    
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);
    cin >> n;
    friendParent.resize(n);
    enemyParent.resize(n, -1);
    groupSize.resize(n, 1);
    for (int i = 0; i < n; i++) friendParent[i] = i;
    while (true) {
        cin >> a >> x >> y;
        if (a == 0) break;
        solve();
    }
    return 0;
}
