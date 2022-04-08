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


int main(){
	int k; cin >> k;
	int n = k+3;
	vector<map<int, int> > v(n);
	vi p(n, 0);
	for(int i=2;i<n;i++){
		if(!p[i]){
			for(int j=i;j<n;j+=i){
				p[j] = 1;
				v[j][i] = 1;
			}
		}
	}
	vector<ll> s(n);
	s[0] = s[1] = 1<<30;
	for(int i=2;i<=k;i++){
		ll sum = 1;
		int x = i;
		for(map<int,int>::iterator it=v[i].begin();it!=v[i].end();it++){
			int a = it->first;
			ll r = a;
			while(x%a == 0){
				x /= a;
				r *= a;
			}
			r -= 1;
			r /= (a-1);
			sum *= r;
		}
		sum -= i;
		s[i] = sum;
		//cout << i <<" "<< sum <<endl;
	}

	vi vis(n, 0);
	vi stack;
	vi res(n, 0);
	vi dis(n, 0);
	int counter = 0;
	int mx = -1;
	int mxi = -1;
	for(int i=6;i<=k;i++){
		if(!vis[i]){
			int at = i;
			
			while(true){
				stack.push_back(at);
				vis[at] = 1;
				dis[at] = ++counter;
				int to = s[at];

				if(to > k || to < 6) break;

				if(vis[to] == 0){
					at = to;
					continue;
				}else if(vis[to] == 1){
					int len = dis[at]-dis[to]+1;
					while(stack.back() != to){
						int z = stack.back(); stack.pop_back();
						vis[z] = 2;
						res[z] = len;
					}
					int z = stack.back(); stack.pop_back();
					vis[z] = 2;
					res[z] = len;
					break;
				}else{
					break;
				}
			}
			while(!stack.empty()){
				int z = stack.back(); stack.pop_back();
				vis[z] = 2;
			}
		}
		//if(res[i] > 0) cout << i << " "<<res[i] <<endl;
		if(res[i] > mx){
			mx = res[i];
			mxi = i;
		}
	}
	//cout << mx <<endl;
	cout << mxi << endl;
}

