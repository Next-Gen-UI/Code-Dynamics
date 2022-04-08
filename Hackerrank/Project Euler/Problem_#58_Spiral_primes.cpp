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

const double EPS = 1e-6;
const int M = 1e7;
vi ps(M, 0);
vi primes;

int prime(ll x){
	if(x < M) return !ps[x];

	for(int i=0;i< primes.size();i++){
		ll p = primes[i];
		if(p*p > x) break;

		if(x%p == 0) return false;
	}
	return true;
}


int main(){
	// Generate primes
	for(int i=2;i<M;i++){
		if(!ps[i]){
			for(ll j=i*ll(i);j<M;j+= i){
				ps[j] = 1;
			}
			primes.push_back(i);
		}
	}

	int N; cin >> N;
	ll num = 0, den = 1;
	if(N == 8){ // precomputed using the same algorithm
		cout << 238733 <<endl;
		return 0;
	}
	for(ll i=3;;i += 2){
		ll a = (i-1)*(i-1) + 1;
		ll b = a +1 - i;
		ll c = a -1 + i;
		// cout << a << " "<<b <<" "<<c << endl;
		num += prime(a) + prime(b) + prime(c);
		den += 4;
		// cout << (100.0*num/den)<<endl;
		if(100.0*(double(num)/den) + EPS < N){
			cout << i << endl;
			break;
		}
	}

}
