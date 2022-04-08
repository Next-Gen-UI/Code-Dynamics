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

int high_card(vii &a, vii &b, int k){
	assert(k >= 0); // guaranteed by problem statement
	if(a[k].first > b[k].first) return 1;
	else if(a[k].first < b[k].first) return 2;
	else return high_card(a, b, k-1);
}

ii one_pair(vii &v){
	map<int, int> m;
	for(int i=0;i<5;i++){
		m[v[i].first] += 1;
	}
	for(map<int, int>::iterator it=m.begin();it!=m.end();it++){
		if(it->second == 2) return ii(true, it->first);
	}
	return ii(false, false);
}

ii two_pairs(vii &v){
	map<int, int> m;
	for(int i=0;i<5;i++){
		m[v[i].first] += 1;
	}
	bool onep = false;
	int lastp = -1;
	for(map<int, int>::iterator it=m.begin();it!=m.end();it++){
		if(it->second == 2){
			if(onep) return ii(true, max(it->first, lastp));
			else{
				onep = true;
				lastp = it->first;
			}
		}
	}
	return ii(false, false);
}

ii three_kind(vii &v){
	map<int, int> m;
	for(int i=0;i<5;i++){
		m[v[i].first] += 1;
	}

	for(map<int, int>::iterator it=m.begin();it!=m.end();it++){
		if(it->second == 3) return ii(true, it->first);
	}
	return ii(false, false);
}

ii straight(vii &v){
	if(v[4].first == 14){ // Ace can be the lowest card
		bool steel_wheel = v[0].first == 2;
		for(int i=1;i<4 && steel_wheel;i++){
			if(v[i].first != v[i-1].first +1) steel_wheel = false;
		}
		if(steel_wheel) return ii(true, v[3].first); 
	}

	for(int i=1;i<5;i++){ // straight
		if(v[i].first != v[i-1].first + 1) return ii(false, false);
	}
	return ii(true, v[4].first);
}

ii same_suit(vii &v){
	int same = v[0].second;
	for(int i=0;i<5;i++){
		if(v[i].second != same) return ii(false, false);
	}
	return ii(true, v[4].first);
}

ii full_house(vii &v){
	map<int, int> m;
	for(int i=0;i<5;i++){
		m[v[i].first] += 1;
	}
	bool three = false, two = false;
	int power = -1;
	for(map<int, int>::iterator it=m.begin();it!=m.end();it++){
		if(it->second == 3){
			three = true;
			power = it->first;
		} else if(it->second == 2) two = true;
	}
	if(three && two){
		return ii(true, power);
	}else{
		return ii(false, false);
	}
}

ii four_kind(vii &v){
	map<int, int> m;
	for(int i=0;i<5;i++){
		m[v[i].first] += 1;
	}
	for(map<int, int>::iterator it=m.begin();it!=m.end();it++){
		if(it->second >= 4) return ii(true, it->first);
	}
	return ii(false, false);
}

ii straight_flush(vii &v){
	if(!(same_suit(v).first)) return ii(false, false);

	return straight(v);
}

ii royal_flush(vii &v){ 
	if(!(same_suit(v).first)) return ii(false, false);
	if(v[0].first != 10) return ii(false, false);
	for(int i=1;i<5;i++){ // straight starting from 10
		if(v[i].first != v[i-1].first + 1) return ii(false, false);
	}
	return ii(true, v[4].first);
}

ii get_rank(vii &v){
	sort(v.begin(), v.end());

	int r = -1;
	ii ret(-1, -1);

	if((ret = royal_flush(v)).first) r = 10;
	else if((ret = straight_flush(v)).first) r = 9;
	else if((ret = four_kind(v)).first) r = 8;
	else if((ret = full_house(v)).first) r = 7;
	else if((ret = same_suit(v)).first) r = 6;
	else if((ret = straight(v)).first) r = 5;
	else if((ret = three_kind(v)).first) r = 4;
	else if((ret = two_pairs(v)).first) r = 3;
	else if((ret = one_pair(v)).first) r = 2;
	else r = 1;

	ret.first = r;
	return ret;
}

int main(){
	int T; cin >> T;
	//int nn = 0;
	while(T--){
		vector<ii> v[2];
		for(int i=0;i<10;i++){
			string s; cin >> s;
			int val = s[0]-'0';
			if(s[0] == 'T') val = 10;
			if(s[0] == 'J') val = 11;
			if(s[0] == 'Q') val = 12;
			if(s[0] == 'K') val = 13;
			if(s[0] == 'A') val = 14; // or 1???
			v[i/5].push_back(ii(val, s[1]));
		}

		ii A = get_rank(v[0]);
		ii B = get_rank(v[1]);
		//cout << A.first <<" "<< A.second <<" "<<B.first <<" "<<B.second <<endl;
		int ans = 1;
		if(A.first < B.first) ans = 2;
		else if(A.first == B.first){
			if(A.second < B.second) ans = 2;
			else if(A.second == B.second){
				ans = high_card(v[0], v[1], 4);
			}
		}
		//if(ans == 1) nn++;
		cout << "Player "<<ans <<'\n';

	}
	//cout << nn <<endl;

}
