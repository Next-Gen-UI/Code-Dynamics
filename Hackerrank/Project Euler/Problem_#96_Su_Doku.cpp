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
typedef vector<int> vi;
typedef vector<ii> vii;
typedef vector<vi> vvi;
typedef vector<vii> vvii;

/* input format (0 = empty): 

123456780
456780123
780123456
234567801
567801234
801234567
345678012
678012345
012345678

*/

vector<string> g;
const int n = 9;

ii next_cell(int mi, int mj){
	if(mj != n-1){
		return ii(mi, mj+1);
	}else if(mi != n-1){
		return ii(mi+1, 0);
	}else{
		return ii(-1, -1);
	}
}

bool can_place(int mi, int mj, int num){
	for(int i=0;i<n;i++){
		if(g[mi][i] == num+'0') return false;
		if(g[i][mj] == num+'0') return false;
	}
	// check 3x3 area
	int ni = mi/3;
	int nj = mj/3;
	ni *= 3;
	nj *= 3;
	for(int i=ni;i<ni+3;i++){
		for(int j=nj;j<nj+3;j++){
			if(g[i][j] == num+'0') return false;
		}
	}
	return true;
}

bool solve(int mi, int mj){
	if(mi == -1 && mj == -1) return true; // past last cell

	ii nxt = next_cell(mi, mj);

	if(g[mi][mj] != '0'){
		return solve(nxt.first, nxt.second);
	}else{
		for(int i=1;i<=9;i++){
			if(can_place(mi, mj, i)){
				g[mi][mj] = '0'+i;
				int solved = solve(nxt.first, nxt.second);
				if(solved){
					return true;
				}
				g[mi][mj] = '0';
			}
		}
	}
	return false;
}

int main(){
	g = vector<string>(n);
	for(int i=0;i<n;i++){
		cin >> g[i];
	}

	solve(0, 0);

	for(int i=0;i<n;i++){
		cout << g[i] << endl;
	}
}

