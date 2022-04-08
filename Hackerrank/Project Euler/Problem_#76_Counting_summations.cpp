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

const int N = 1e5;
const int M = 1e9 + 7;
int main(){
	vii pentagonal; // Wikipedia: partitions
	int m = -400;
	while(m < 400){ // x^2 < N
		m++;
		if(m == 0) continue;
		pentagonal.push_back(ii(abs((m*(3*m-1))/2), (abs(m)%2)*2 -1));
	}
	sort(pentagonal.begin(), pentagonal.end());

	vector<ll> v(N, 0);
	v[0] = 1;
	for(int i=1;i<N;i++){
		for(int j=0; pentagonal[j].first <= i; j++){
			v[i] += v[i-pentagonal[j].first]*pentagonal[j].second;
			while(v[i] < 0) v[i] += M;
			v[i] %= M;
		}
	}

	int t;cin >> t;
	while(t--){
		int n; cin >> n;
		cout << v[n]-1 <<endl;
	}


}
