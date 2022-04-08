#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;
typedef long long ll;

int main() {
    int a, b; ll n;
    scanf("%lld %d %d", &n, &a, &b);
    vector<ll> tri, penta, hexa;
    for(ll i=1;;i++){
        ll t = (i*(i+1))/2;
        ll p = (i*(3*i-1))/2;
        ll h = i*(2*i-1);
        if(t >=n && p >= n && h >= n) break;
        if(a == 3){
            if(p < n){
              penta.push_back(p);
            }
            if(binary_search(penta.begin(), penta.end(), t)){
                printf("%lld\n", t);
            }
        }else{
            if(h < n){
                hexa.push_back(h);
            }
            if(binary_search(hexa.begin(), hexa.end(), p)){
                printf("%lld\n", p);
            }
        }
       
    }
    return 0;
}
