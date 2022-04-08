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

map<string, int> m;

int main(){
	m["I"] = 1;
	m["V"] = 5;
	m["X"] = 10;
	m["L"] = 50;
	m["C"] = 100;
	m["D"] = 500;
	m["M"] = 1000;
	int T; scanf("%d", &T);
	while(T--){
		string s; cin >> s;

		int sum = 0;
		for(int i=0;i<s.size();i++){
			string z = "";
			z += s[i];
			sum += m[z];
		}
		//cout << sum <<endl;
		string result = "";
		int sub[] =  {1000, 900, 500, 400, 100, 90,   50, 40,   10,   9,  5,   4, 1};
		string add[]={"M",  "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
		
		int p = 0;
		while(sum != 0){
			while(sum >= sub[p]){
				sum -= sub[p];
				result += add[p];
			}
			p++;
		}

		cout << result << endl;
	}

}

