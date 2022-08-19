/* Link: https://acm.timus.ru/problem.aspx?space=1&num=1752 */
// Compiler: G++ 9.2 x64
#include <bits/stdc++.h>
 
using namespace std;

int n, q;

int bfsFarthestVertice(vector<vector<int>>& adj, int start) {
    vector<bool> visited(adj.size());
    queue<int> q;
    q.push(start);
    visited[start] = true;
    int curr;
    while (!q.empty()) {
        curr = q.front();
        q.pop();
        for (auto u : adj[curr]) {
            if (!visited[u]) {
                visited[u] = true;
                q.push(u);
            }
        }
    }
    return curr;
}

void makeParentsHelper(vector<vector<int>>& adj, vector<int>& parent, vector<bool>& visited,
    vector<int>& order, int par, int node) {
        if (visited[node]) return;
        visited[node] = true;
        parent[node] = par;
        order.push_back(node);
        for (auto u : adj[node]) {
            makeParentsHelper(adj, parent, visited, order, node, u);
        }
}

void makeParents(vector<vector<int>>& adj, vector<int>& parent, vector<int>& order, int root) {
    vector<bool> visited(adj.size());
    makeParentsHelper(adj, parent, visited, order, 0, root);
}

vector<vector<int>> preprocessBinaryLifting(vector<int>& parent, vector<int>& order) {
    // 2^15 > 20000
    vector<vector<int>> result(parent.size(), vector<int>(16, 0)); 
    for (int i = 0; i < order.size(); ++i) {
        result[order[i]][0] = parent[order[i]]; 
    }
    for (int pow = 1; pow < 16; ++pow) {
        for (int i = 0; i < order.size(); ++i) {
            if (result[order[i]][pow - 1] != 0) {
                result[order[i]][pow] = result[result[order[i]][pow - 1]][pow - 1];
            }
        }
    }
    return result;
}

int getAncestor(vector<vector<int>>& blift, int v, int d)  {
    for (int i = 0; i < 16; ++i) {
        if ((d >> i) & 1) {
            v = blift[v][i];
            if (v == 0) return 0;
        }
    }
    return v;
}

int main() {    
    cin >> n >> q;
    vector<vector<int>> adj(n + 1);
    for (int i = 0; i < n - 1; ++i) {
        int a, b; cin >> a >> b;
        adj[a].push_back(b);
        adj[b].push_back(a);
    }    
    int root1 = bfsFarthestVertice(adj, 1);
    int root2 = bfsFarthestVertice(adj, root1);
    vector<int> parent1(n + 1);
    vector<int> order1;
    makeParents(adj, parent1, order1, root1);
    vector<vector<int>> blift1 = preprocessBinaryLifting(parent1, order1);
    vector<int> parent2(n + 1);
    vector<int> order2;
    makeParents(adj, parent2, order2, root2);
    vector<vector<int>> blift2 = preprocessBinaryLifting(parent2, order2);
    cout << endl;
    for (int i = 0; i < q; ++i) {
        int v, d; cin >> v >> d;
        int ans1 = getAncestor(blift1, v, d);
        if (ans1 != 0) {
            cout << ans1 << endl;
            continue;
        }
        cout << getAncestor(blift2, v, d) << endl;
    }
    return 0;
}
