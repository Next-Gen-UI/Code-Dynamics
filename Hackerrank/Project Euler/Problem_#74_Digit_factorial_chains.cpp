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

const int N = 1e6 + 7;
vi fact(10, 1);
vi chain(N, -1);

int next(int num){
	if(num == 0) return 1;

	int ans = 0;
	while(num != 0){
		int d = num%10;
		num /= 10;
		ans += fact[d];
	}
	return ans;
}

int solve(int num){
	if(num >= N) return 1+solve(next(num));

	if(chain[num] != -1) return chain[num];

	chain[num] = 0;
	int nxt = next(num); 
	//cout << "at "<<num <<" nxt "<<nxt<<endl;
	return chain[num] = solve(nxt)+1;
}

int main(){
	for(int i=2;i<=9;i++){
		fact[i] = i*fact[i-1];
	}

	// From problem statement
	chain[169] = chain[363601] = chain[1454] = 3;
	chain[871] = chain[45361] = 2;
	chain[872] = chain[45362] = 2;

	for(int i=0;i<N;i++){
		if(chain[i] == -1){
			solve(i);
		}
	}

	int T; cin >> T;
	while(T--){
		int n, len; cin >> n >> len;
		bool something = false;
		for(int i=0;i<=n;i++){
			if(chain[i] == len){
				something = true;
				cout << i <<" ";
			}
		}
		if(!something) cout << -1;
		cout << endl;
	}

}
