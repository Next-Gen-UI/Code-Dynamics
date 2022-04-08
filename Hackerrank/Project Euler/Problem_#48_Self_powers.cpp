#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;
typedef long long ll;

const ll M = 1e10;
const ll m = 1e8;

//10^10 * 10^10 = 10^20 > long long
struct num{
    ll a;
    ll b;
    num(ll c, ll d){
        a = c;
        b = d;
    }
};
num power(ll base, int p){
    if(p == 0) return num(0, 1);
    if(p == 1) return num(0, base);
    if(p%2 == 0){
        num x = power(base, p/2);
        //x^2 = a*a*m*m + 2*a*m*b + b*b
        ll z = 2*(x.a*m)*x.b + x.b*x.b;
        x.b = z%m;
        x.a = z/m;
        x.a %= 100;
        return x;
    }else{
        num x = power(base, p-1);
        ll z = (x.b+m*x.a)*base;
        x.a = z/m;
        x.b = z%m;
        x.a %= 100;
        
        return x;
    }
}
int main() {
    int n; scanf("%d", &n);
    ll ans = 0;
    for(int i=1;i<=n;i++){
        num x = power(i,i);
        ll res = x.b + (m)*x.a;
        ans = (ans + res)%M;
        //printf("%lld\n", ans);
    }
    printf("%lld\n", ans);
    return 0;
}
