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

const int M = 200;
vvi g(M);
vi deg(M, -1);

int main(){
	int n; cin >> n;
	int letters = 0;
	for(int i=0;i<n;i++){
		string s; cin >> s;
		int a = s[0];
		int b = s[1];
		int c = s[2];

		if(deg[a] == -1){
			deg[a] = 0;
			letters++;
		}
		if(deg[b] == -1){
			deg[b] = 1;
			letters++;
		}else{
			deg[b]++;
		}
		if(deg[c] == -1){
			deg[c] = 1;
			letters++;
		}else{
			deg[c]++;
		}

		g[a].push_back(b);
		g[b].push_back(c);
	}

	// Topo sort + picking min. letter
	set<int> q;
	for(int i=0;i<M;i++){
		if(deg[i] == 0){
			q.insert(i);
		}
	}
	string ans = "";
	while(!q.empty()){
		char c = *q.begin();
		q.erase(q.begin());
		ans += c;
		
		for(int i=0;i<g[c].size();i++){
			int to = g[c][i];
			deg[to]--;
			if(deg[to] == 0){
				q.insert(to);
			}
		}
	}
	if(ans.size() != letters){
		cout << "SMTH WRONG" <<endl;
	}else{
		cout << ans <<endl;
	}

}
