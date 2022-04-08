#include <cstdio>
#include <iostream>
#include <sstream>
#include <string>
#include <cmath>
#include <cassert>
#include <algorithm>
#include <vector>
#include <set>
#include <unordered_map>
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

int main(){
	int n, k; cin >> n >> k;
	unordered_map<string, pair<int, ll> > m;
	for(int i=0;i<n;i++){
		ll cube = (ll(i)*i)*i;
		ll bckup = cube;
		vi digs(10, 0);
		while(cube != 0){
			digs[cube%10]++;
			cube /= 10;
		}
		string s = "";
		for(int i=0;i<10;i++){
			for(int j=0;j<digs[i];j++){
				s += ('0'+i);
			}
		}
		cube = bckup;
		if(m.find(s) == m.end()){
			m[s] = make_pair(1, cube);
		}else{
			m[s] = make_pair(m[s].first + 1, min(m[s].second, cube));
		}
	}

	vector<ll> ans;
	for(unordered_map<string, pair<int, ll> >::iterator it=m.begin(); it!=m.end();it++){
		int count = it->second.first;
		if(count == k){
			ans.push_back(it->second.second);
		}
	}

	sort(ans.begin(), ans.end());

	for(int i=0;i<ans.size();i++){
		cout << ans[i] << '\n';
	}

}
