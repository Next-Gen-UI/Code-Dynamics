#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <cassert>
#include <map>
#include <algorithm>
using namespace std;
typedef vector<int> vi;
typedef long long ll;

const ll M = 1e18;

vector<ll> p;
int dd(ll num, int pos){
    ll n = num;
    ll k= 0;
    while(n != 0){
        n/=10;
        k++;
    }
    k-=pos;
    ll z = 1;
    while(k > 0){
        z *= 10;
        k--;
    }
    return (num/z)%10;
}
map<ll, int> seen;
int dig(ll n){
    if(n < 10) return n;
    if(seen.find(n) != seen.end()){
        return seen[n];
    }
    
    ll k = 0;
    ll num = 1;
    while(p[k] <= n && k < p.size()){
        n-=p[k];
        k++;
        num *= 10;
    }
    k++;
    ll ith = n/k;
    n = n%k;
    if(n == 0){
        ith--;
        n = k;
    }
    //cout << ith << " "<<num <<" "<<n<<endl;
    return seen[n] = dd(ith+num, n);
}



int main() {
    ll z = 9;
    for(int i=0;;i++){
        ll b = z*(i+1);
        if(b >= 0 && b < M){
            p.push_back(b);
            z *= 10;
        } else{
            break;
        }
    }
    int t; scanf("%d", &t);
    while(t--){
        vector<ll> v(7);
        for(int i=0;i<7;i++){
            scanf("%lld", &v[i]);
        }
        int mul = 1;
        for(int j=0;j<7;j++){
            mul *= dig(v[j]);
        }
        printf("%d\n", mul);
    }
    return 0;
}