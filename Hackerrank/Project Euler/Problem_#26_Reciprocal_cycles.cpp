#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;
const int M = 10005;

int cycle(int n){
    int num = 1;
    int den = n;
    vector<int> rems(n+1, -1);
    rems[1] = 0;
    int ind = 0;
    while(true){
        while(num < den){
            ind++;
            num*= 10;
        }
        int rem = num%den;
        num = rem;
        if(rem == 0)return 0;
        if(rems[rem] != -1){
            return ind-rems[rem];
        }else{
            rems[rem] = ind;
        }
    }
}
    
int main() {
    int t; cin >> t;
    vector<int> anss(M);
    int ans = 0;
    int mx = 0;
    for(int i=2;i<M;i++){
        int k = cycle(i);
        if(mx < k){
            mx = k;
            ans = i;
        }
        anss[i] = ans;
        //cout << i<<" " <<anss[i] << " "<<k<<" - ";
    }
    while(t--){
        int n; cin >> n;
        printf("%d\n", anss[n-1]);
    }
    return 0;
}
