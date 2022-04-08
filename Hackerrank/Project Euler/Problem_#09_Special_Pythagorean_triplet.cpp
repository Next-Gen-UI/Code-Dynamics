#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;

typedef long long ll;

int main() { 
    int t;scanf("%d", &t);
    while(t--){
        int n;scanf("%d", &n);
        ll ok = -1;
        for(ll i=1;i<n;i++){
            ll j = (n*n-2*n*i)/(2*n-2*i);
            ll k = n-i-j;
            if(i*i+j*j == k*k && j>0 && k>0){
                ok = max(ok, i*j*k);
            }
        }
        printf("%lld\n", ok);
    }
    return 0;
}