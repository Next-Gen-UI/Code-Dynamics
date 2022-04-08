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


int euler(int x){
	int ans = x;

	for(int i=2;i*i<=x;i++){
		if(x%i == 0){
			while(x%i == 0) x/=i;
			ans = ans - ans/i;
		}
	}
	if(x > 1){
		ans = ans - ans/x;
	}
	return ans;

}

// https://en.wikipedia.org/wiki/Farey_sequence
int main(){
	int N = 1e6 + 10;
	vector<ll> v(N, 0);
	v[0] = 2;
	ll ans = 1;
	for(int i=1;i<N;i++){
		ans += euler(i);
		v[i] = ans;
	}

	int t; cin >> t;
	while(t--){
		int d; cin >> d;
		cout << (v[d]-2) << endl;
	}
}
