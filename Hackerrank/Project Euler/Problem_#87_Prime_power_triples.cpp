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

const int N = 1e7 + 5;
int main(){
	int M = 10000;
	vi ps(M, 0);
	vi primes;
	for(int i=2;i<M;i++){
		if(!ps[i]){
			for(int j=i*i;j<M;j+= i){
				ps[j] = 1;
			}
			primes.push_back(i);
		}
	}
	map<int, int> memo;
	for(int i=0;i<primes.size();i++){
		ll a = primes[i];
		a = a*a*a*a;
		if(a > N) break;
		for(int j=0;j<primes.size();j++){
			ll b = primes[j];
			b = b*b*b;
			if(a +b > N) break;
			for(int k=0;k<primes.size();k++){
				ll c = primes[k];
				c = c*c;
				if(a +b + c > N) break;
				memo[a+b+c] = 1;
			}
		}
	}

	vii ans(memo.begin(), memo.end());
	for(int i=1;i<ans.size();i++){
		ans[i].second = ans[i-1].second+ans[i].second;
		//cout << ans[i].first <<" "<<ans[i].second <<endl;
	}

	int T; cin >> T;
	while(T--){
		int n; cin >> n;
		int a = 0; int b = ans.size()-1;
		int top = -1;
		while(a <= b){
			int mid = (a+b)/2;
			if(ans[mid].first <= n){
				top = mid;
				a = mid+1;
			}else{
				b = mid-1;
			}
		}
		cout << ans[top].second << endl;
	}

}

