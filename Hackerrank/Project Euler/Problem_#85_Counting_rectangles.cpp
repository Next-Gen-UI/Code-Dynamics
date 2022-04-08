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

const int M = 2e3;
int main(){
	map<int, int> memo;
	for(int i=1;i<M;i++){
		for(int j=1;j<M;j++){
			ll t = i*(i+1);
			t *= j*(j+1);

			if(t > 1e8) continue;
			t /= 4;

			int x = t;
			memo[x] = max(memo[x], i*j);
		}
	}

	vii v(memo.begin(), memo.end());
	/*for(int i=0;i<10;i++){
		cout << v[i].first <<" "<<v[i].second << endl;
	}*/

	int t; cin >> t;
	while(t--){
		int n; cin >> n;
		int a = 0, b = v.size()-1;
		int res = -1;
		while(a <= b){
			int mid = (a+b)/2;
			if(v[mid].first <= n){
				a = mid+1;
			}else{
				res = mid;
				b = mid-1;
			}
		}
		int ans = -1;
		if(v[res].first == n){
			ans = v[res].second;
		}else{
			int bel = v[res-1].first;
			int ab = v[res].first;
			if(ab-n == n-bel){
				ans = max(v[res].second, v[res-1].second);
			}else if(ab-n < n-bel){
				ans = v[res].second;
			}else{
				ans = v[res-1].second;
			}
		}
		cout << ans << endl;
	}

}

