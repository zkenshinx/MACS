/* Link: https://acm.timus.ru/problem.aspx?space=1&num=1890 */
// Compiler:
// G++ 9.2 x64
#include <bits/stdc++.h>

using namespace std;

struct employee {
    int treeIndex;
    int num; // Number of employees in his department
    long long salary; // His salary
    vector<int> subordinates; // Indices of subordinates

    employee(long long salary) {
        this->salary = salary;
    }
};

int n, q, p, s, x;
long long y, z;
string str;
vector<employee> company;
vector<long long> tree, lazy, arr;

int recNum(employee &e) { 
    int num = 0;
    for (auto sub : e.subordinates) num += recNum(company[sub]);
    e.num = 1 + num;
    return e.num;
}

void makeTree(employee &e, int &index) {
    arr[index] = e.salary;
    e.treeIndex = index;
    for (auto sub : e.subordinates) makeTree(company[sub], ++index);
}

void build(int node, int start, int end) {
    if (start == end) {
        tree[node] = arr[start];
    } else {
        int mid = (start + end) / 2;
        build(2 * node + 1, start, mid);
        build(2 * node + 2, mid + 1, end);
        tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
    }
}

long long query(int node, int start, int end, int l, int r) {
    if (end < l || r < start) {
        return 0;
    }

    if (lazy[node] != 0) {
        tree[node] += lazy[node] * (end - start + 1) ;
        if (start != end) {
            lazy[2 * node + 1] += lazy[node];
            lazy[2 * node + 2] += lazy[node];
        }
        lazy[node] = 0;
    }

    if (l <= start && end <= r) {
        return tree[node];
    } else {
        int mid = (start + end) / 2;
        long long s1 = query(2 * node + 1, start, mid, l, r);
        long long s2 = query(2 * node + 2, mid + 1, end, l , r);
        return s1 + s2;
    }
}

void update(int node, int start, int end, int l, int r, long long val) {
    if (lazy[node] != 0) {
        tree[node] += lazy[node] * (end - start + 1);
        if (start != end) {
            lazy[2 * node + 1] += lazy[node];
            lazy[2 * node + 2] += lazy[node];
        }
        lazy[node] = 0;
    }
    if (end < l || r < start) {
        return;
    } else if (l <= start && end <= r) {
        tree[node] += val * (end - start + 1);
        if (start != end) {
            lazy[2 * node + 1] += val;
            lazy[2 * node + 2] += val;
        }
    } else {
        int mid = (start + end) / 2;
        update(2 * node + 1, start, mid, l, r, val);
        update(2 * node + 2, mid + 1, end, l ,r, val);
        tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
    }
}

void solve() {
    if (str == "employee" && query(0, 0, n - 1, company[x].treeIndex, company[x].treeIndex) < y) {
        update(0, 0, n - 1, company[x].treeIndex, company[x].treeIndex, z);
    } else if (str == "department" && 
        (double) query(0, 0, n - 1, company[x].treeIndex, company[x].treeIndex + company[x].num - 1) / company[x].num < (double) y) {
        update(0, 0, n - 1, company[x].treeIndex, company[x].treeIndex + company[x].num - 1, z);
    }
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL); cout.tie(NULL);

    cin >> n >> q >> s;
    tree.resize(4 * n);
    lazy.resize(4 * n);
    arr.resize(n);
    company.push_back(employee(s));
    for (int i = 1; i < n; ++i) {
        cin >> p >> s;
        company[p].subordinates.push_back(i);
        company.push_back(employee(s));
    }

    recNum(company[0]);
    int index = 0;
    makeTree(company[0], index); 
    build(0, 0, n - 1);
    for (int i = 0; i < q; ++i) {
        cin >> str >> x >> y >> z;
        solve();
    }

    for (int i = 0; i < company.size(); ++i) cout << query(0, 0, n - 1, company[i].treeIndex, company[i].treeIndex) << endl;
    return 0;
}
