#include <cstdio>
#include <iostream>
#include <sstream>
#include <string>
#include <cmath>
#include <cassert>
#include <algorithm>
#include <vector>
#include <set>
#include <map>
#include <deque>
using namespace std;
typedef long long ll;
typedef pair<double, double> dd;
typedef pair<int, int> ii;
typedef pair<int, ii> iii;
typedef vector<ll> vi;
typedef vector<ii> vii;
typedef vector<vi> vvi;
typedef vector<vii> vvii;

int main(){
	int n; cin >> n;
	vvi v(n+1, vi(n+1, 1LL<<50));
	for(int i=1;i<=n;i++){
		for(int j=1;j<=n;j++){
			cin >> v[i][j];
			if(i == 1 && j == 1) continue;

			v[i][j] += min(v[i-1][j], v[i][j-1]);
		}
	}
	cout << v[n][n] <<endl;

}

