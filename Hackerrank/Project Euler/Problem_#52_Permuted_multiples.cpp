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


int M = 125875;
int N = 2000000;
vi getDs(int x){
	vi ans(10, 0);
	while(x != 0){
		int d = x%10;
		x /= 10;
		ans[d]++;
	}
	return ans;
}
int main(){
	int n, k; cin >> n >> k;
	for(int i=1;i<=n;i++){
		int same = true;
		vi b = getDs(i);
		for(int j=2;j<=k && same;j++){
			vi p = getDs(j*i);
			if(p != b){
				same =false;
				break;
			}
		}
		if(same){
			for(int j=1;j<=k;j++){
				cout << (i*j)<<" ";
			}
			cout << endl;
		}
	}
}

