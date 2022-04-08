#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;
typedef long long ll;

ll solve(ll n){
    ll b = n;
    for(ll i=2;;i++){
        if(i*i > b){
            return b;
        }
        if(n%i == 0){
            return max(i, solve(n/i));
        }
    }
}
int main() {
    int t;cin>>t;
    while(t--){
        ll n; cin >> n;
        ll m = solve(n);
        cout<<m<<endl;
    }
    return 0;
}