#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
#include <cassert>
using namespace std;
typedef unsigned long long ll;

int pali(ll x){
    string s = "";
    while(x != 0){
        s += ('0'+x%10);
        x/=10;
    }
    for(int i=0;i<s.length()/2;i++){
        if(s[i] != s[s.length()-1-i]) return false;
    }
    return true;
}
int main() {
    int n, k; scanf("%d %d", &n, &k);
    ll sum = 0;
    for(int i=1;i<n;i++){
        if(pali(i)){
            ll other = 0;
            int t = i;
            ll mul = 1;
            while(t != 0){
                other = other + (t%k)*mul;
                t /= k;
                mul *= 10;
            }
            //cout << other <<endl;
            if(other <= 0){
                cout << i <<" " <<other<<endl;
            }
            assert(other > 0);
            if(pali(other)){
                //cout << i <<endl;
                sum += i;
            } 
        }
    }
    printf("%lld\n", sum);
    
    return 0;
}
