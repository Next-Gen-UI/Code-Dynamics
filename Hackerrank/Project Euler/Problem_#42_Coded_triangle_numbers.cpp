#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;
typedef long long ll;

int main() {
    int t; scanf("%d", &t);
    while(t--){
        ll a; scanf("%lld", &a);
        //a = 0.5*n*(n+1)
        //0 = n*n + n - 2a
        //n = (-1 + sqrt(1+8a)) / (2)
        ll s = (ll) sqrt(1.0*a*8 + 1);
        if(s*s == a*8+1 && s%2 == 1){
            printf("%lld\n", (s-1)/2);
        }else{
            printf("-1\n");
        }
        
    }
    return 0;
}
