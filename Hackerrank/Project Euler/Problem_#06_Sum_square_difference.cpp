#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;
typedef long long ll;

int main() {
    int t;cin>>t;
    while(t--){
        ll n;cin>>n;
        ll s2 = ((n*(n+1))/2);
        s2 = s2*s2;
        ll s1 = ((2*n+1)*(n+1)*n)/6;
        //cout << s2 << " "<<s1;
        cout << s2-s1 <<endl;
    }
    return 0;
}