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

const double EPS = 1e-7;
int n; 
vi v;
map<ii, vector<double> > memo;

vector<double> solve(int from, int to){
	if(from == to){
		return vector<double>(1, v[from]);
	}

	ii p(from, to);
	if(memo.find(p) != memo.end()){
		return memo[p];
	}

	set<double> cs;
	for(int i=from; i<to;i++){
		vector<double> as = solve(from, i);
		vector<double> bs = solve(i+1, to);
		
		for(int i=0;i<as.size();i++){
			for(int j=0;j<bs.size();j++){
				double a = as[i];
				double b = bs[j];
				cs.insert(a+b);
				cs.insert(a*b);
				cs.insert(a-b);
				if(abs(b) > EPS) cs.insert(a/b);
			}
		}
	}
	return memo[p] = vector<double>(cs.begin(), cs.end());
}

int main(){
	cin >> n;
	v = vi(n);
	for(int i=0;i<n;i++){
		cin >> v[i];
		assert(v[i] < 10);
		assert(v[i] >= 0);
	}
	sort(v.begin(), v.end());

	set<double> ans_temp;
	do{ //can use digits in any order - try all
		memo.clear();
		vector<double> res = solve(0, n-1);
		// append solutions
		ans_temp.insert(res.begin(), res.end()); 
	} while(next_permutation(v.begin(), v.end())); 

	vector<double> ans(ans_temp.begin(), ans_temp.end());
	int mx = 0;
	for(int i=0;i<ans.size();i++){
		//cout << ans[i] <<" ";
		if(abs(ans[i]-mx-1) < EPS){
			mx++;
		}
	}
	cout << mx <<endl;
}

