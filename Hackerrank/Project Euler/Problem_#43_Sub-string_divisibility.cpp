#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;
typedef long long ll;

ll toNumber(vector<int> &v){
    ll k = 0;
    for(int i=0;i<v.size();i++){
        k = k*10 + v[i];
    }
    return k;
}
int p[] = {2, 3, 5, 7, 11, 13, 17};

int main() {
    int n; cin >> n;
    vector<int> v(n+1);
    for(int i=0;i<=n;i++){
        v[i] = i;
    }
    ll sum = 0;
    do{
        int sat = true;
        int num = 10*v[1]+v[2];
        for(int ind=3;ind<=n;ind++){
            num = num%100;
            num = num*10 + v[ind];
            if(num%p[ind-3] != 0){
                sat = false;
                break;
            }
        }
        if(sat){
            sum += toNumber(v);
        }
    }while(next_permutation(v.begin(), v.end()));
    
    printf("%lld\n", sum);
    return 0;
}
