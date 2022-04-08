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

int mi[] = {0, 0, -1, 1};
int mj[] = {1, -1, 0, 0};

int main(){
	int n; cin >> n;
	vvi v(n, vi(n, 1LL<<50));
	for(int i=0;i<n;i++){
		for(int j=0;j<n;j++){
			cin >> v[i][j];
		}
	}
	set<pair<ll, ii> > h;
	vvi dist(n, vi(n, 1LL<<50));
	h.insert(make_pair(v[0][0], ii(0, 0)));
	dist[0][0] = v[0][0];

	while(!h.empty()){
		ll d = h.begin()->first;
		ii at = h.begin()->second;
		h.erase(h.begin());
		if(d > dist[at.first][at.second]) continue;

		for(int i=0;i<4;i++){
			int ni = at.first + mi[i];
			int nj = at.second + mj[i];
			if(ni >= 0 && ni < n && nj >= 0 && nj < n){
				if(dist[ni][nj] > d+v[ni][nj]){
					dist[ni][nj] = d+v[ni][nj];
					h.insert(make_pair(dist[ni][nj], ii(ni, nj)));
				}
			}
		}
	}

	cout << dist[n-1][n-1] <<endl;

	/*for(int i=0;i<n;i++){
		for(int j=0;j<n;j++){
			cout << dist[i][j] <<" ";
		}cout << endl;
	}*/
}

