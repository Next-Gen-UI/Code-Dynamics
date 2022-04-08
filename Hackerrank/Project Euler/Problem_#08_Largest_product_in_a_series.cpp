#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
#include <string>
using namespace std;

int n, k;
int init(string &s, int from){
    int akt = 1;
    for(int i=from;i<from+k;i++){
        akt *= (s[i]-'0');
    }
    return akt;
}

int main() {
    int t;cin>>t;
    while(t--){
       cin>>n>>k;
        string s; cin>>s;
        int res = 0;
        for(int i=0;i<n-k;i++){
            res = max(res, init(s,i));
        }
        cout << res << endl;
    }
    return 0;
}
