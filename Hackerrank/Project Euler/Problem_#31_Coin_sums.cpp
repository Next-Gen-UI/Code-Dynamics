#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;
const int mod = 1e9 + 7;
typedef long long ll;

int curr[8] = {1, 2, 5, 10, 20, 50, 100, 200};
int main() {
    vector<int> v(1e5+10, 0);
    v[0] = 1;
    for(int k=0;k<8;k++){
        for(int i=curr[k];i<=1e5;i++){
            v[i] = (v[i] + v[i-curr[k]])%mod;
        }
    }
    int t; cin >> t;
    while(t--){
        int n; cin >> n;
        
        cout << v[n] << endl;
    }
    return 0;
}
