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

int getNum(int p, int n){
	switch(p){
		case 3: return (n*(n+1))/2;
		case 4: return n*n;
		case 5: return (n*(3*n-1))/2;
		case 6: return n*(2*n-1);
		case 7: return (n*(5*n-3))/2;
		case 8: return n*(3*n-2);
	}
	return -1;
}

string toString(int n){
	stringstream ss;
	ss << n;
	return ss.str();
}
int fromString(string s){
	stringstream ss(s);
	int x; ss >> x;
	return x;
}

bool concat(string a, string b){
	return a[2] == b[0] && a[3] == b[1];
}


vector<pair<int, string> > g;
vector<int> answer;
vector<pair<int, string> > path;
int targetLen;
int start;

bool checkEndCondition(){
	vector<pair<int, string> > tmp = path;
	sort(tmp.begin(), tmp.end());
	vector<string> ns(tmp.size());
	for(int i=0;i<tmp.size();i++){
		ns[i] = tmp[i].second;
	}
	for(int i=1;i<tmp.size();i++){
		if(tmp[i-1].first == tmp[i].first){
			return false;
		}
	}
	sort(ns.begin(), ns.end());
	for(int i=1;i<ns.size();i++){
		if(ns[i-1] == ns[i]){
			return false;
		}
	}
	return true;
}

void findCycles(int at, int len, vi &vis, vvi &edges){
	if(len >= targetLen) return;

	vis[at] = 1;
	path[len] = g[at];
	//cout << at <<" "<<start<< " "<<len << " "<<g[at].first <<" "<<g[at].second<<endl;
	for(int i=0;i<edges[at].size();i++){
		int to = edges[at][i];
		if(to == start && len == targetLen-1){
			if(checkEndCondition()){
				int sum = 0;
				for(int i=0;i<path.size();i++){
					//cout << path[i].first <<","<<path[i].second <<" ";
					sum += fromString(path[i].second);
				}//cout << endl;
				answer.push_back(sum);
			}
		}else if(!vis[to]){
			findCycles(to, len+1, vis, edges);
		}
	}
	
	vis[at] = 0;
}

int main(){
	int n; cin >> n;
	vi v(n);
	// vector<vector<string> > nums(n);

	for(int i=0;i<n;i++){
		cin >> v[i];
	}
	sort(v.begin(), v.end());

	for(int i=0;i<n;i++){
		int p = v[i];
		int k = 1;
		int z = getNum(p, k);
		while(z < 1000){
			k++;
			z = getNum(p, k);
		}
		while(z < 10000){
			// nums[i].push_back(toString(z));
			g.push_back(make_pair(p, toString(z)));
			k++;
			z = getNum(p, k);
		}
	}

	/*for(int i=0;i<n;i++){
		cout << nums[i].size() <<endl;
		for(int j=0;j<nums[i].size();j++){
			cout << nums[i][j] <<" ";
		}cout << endl;
	}*/

	vvi edges(g.size());
	for(int i=0;i<g.size();i++){
		for(int j=0;j<g.size();j++){
			if(i == j) continue;
			if(g[i].first != g[j].first && concat(g[i].second, g[j].second)){
				edges[i].push_back(j);
			}
		}
	}

	targetLen = n;
	for(int i=0;i<g.size();i++){
		if(g[i].first == v[0]){
			vi vis(g.size(), 0);
			path = vector<pair<int, string> >(n);
			start = i;
			findCycles(i, 0, vis, edges);
		}
	}

	// print answer
	sort(answer.begin(), answer.end());

	vi ans;
	map<int, int> m;
	for(int i=0;i<answer.size();i++){
		m[answer[i]] += 1;
	}

	for(map<int, int>::iterator it=m.begin();it!=m.end();it++){
		cout << it->first << endl;
	}

}
