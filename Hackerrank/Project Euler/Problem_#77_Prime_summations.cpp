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

const int N = 1100;
vi primes;
map<ii, ll> memo;

ll solve(int num, int bound){
	if(num == 0) return 1;
	if(num <= 1) return 0;

	ii k(num, bound);
	if(memo.find(k) != memo.end()){
		return memo[k];
	}

	ll ans = 0;
	for(int i=0;primes[i] <= bound;i++){
		int p = primes[i];
		ans += solve(num-p, min(p, num-p));
	}

	return memo[k] = ans;
}

int main(){
	vi ps(N, 0);
	
	// Generate primes
	for(int i=2;i<N;i++){
		if(ps[i] == 0){
			for(int j=i*i;j<N;j+= i){
				ps[j] = 1;
			}
			primes.push_back(i);
		}
	}

	// Solve
	int T; cin >> T;
	while(T--){
		int n; cin >> n;
		ll ans = solve(n, n);
		cout << ans << endl;
	}

}
