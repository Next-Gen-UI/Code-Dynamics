#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;
typedef long long ll;

int main() {
    vector<int> p(2e7, 1); 
    p[0]=p[1]=0;
    for(int i=2;i*i<2e7;i++){
        if(!p[i])continue;
        for(int j=i*i;j<2e7;j+=i){
            p[j]=0;
        }
    }
    vector<int> ps;
    for(int i=2;i<2e7;i++){
        if(p[i]) ps.push_back(i);
    }
    vector<int> pref(ps.size()+1);
    pref[0]=0;
    for(int i=0;i<ps.size();i++){
        pref[i+1]=pref[i]+ps[i];
    }
    int t; cin >> t;
    while(t--){
        int n; cin >> n;
        ll s = 0;
        for(int i=0;ps[i] <=n;i++){
            s += ps[i];
        }
        cout << s << endl;
        
    }
    return 0;
}