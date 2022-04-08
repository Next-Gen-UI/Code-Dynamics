#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <set>
#include <algorithm>
using namespace std;
typedef long long ll;

int main() {
    int n; cin >> n;
    
    vector<int> base(n+1, 1);
    //find base numbers;
    for(int i=2;i*i<=n;i++){
        if(!base[i]) continue;
        for(ll k = i*i; k <= n; k *= i){
            base[k] = false;
        }
    }
    set<int> pars;
    vector<ll> num_pow;
    num_pow.push_back(0); //we don't consider 0th power
    for(int i=1;i<20;i++){
        if((1 << i) > n) break; //base number greater than n (when 2^i > N then surely X^i > N for X > 2)
        for(int j=2;j<=n;j++){
            //(X^i)^j == X^(i*j) ... count unique i*j
            pars.insert(i*j);
        }
        num_pow.push_back(pars.size());
    }
    ll ans = 0;
    for(int a=2;a<=n;a++){
        if(base[a]){
            ll k =a;
            int pw = 1;
            while(k <= n){
                k *= a;
                pw++;
            }pw--;
            // there are pw powers of this number ("a"s with the same base)
            ans += num_pow[pw];
        }
    }
    cout << ans <<endl;
    
    return 0;
}
