#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;

typedef long long ll;

int prime(int x){
    for(int i=2;i*i<=x;i++){
        if(x%i == 0){
            return false;
        }
    }
    return true;
}
int main() {
    vector<int> anss;
    for(int k=9;k>=2;k--){
        vector<int> ds(k);
        for(int i=0;i<k;i++){
            ds[i] = k-i;
        }
        ll num = 0;
        for(int i=0;i<k;i++){
            num = num*10+ds[i];
        }
        if(prime(num)){
            anss.push_back(num);
        }
        //cout << num <<endl;
        while(prev_permutation(ds.begin(), ds.end())){
            num = 0;
            for(int i=0;i<k;i++){
                num = num*10+ds[i];
            }
            if(prime(num)){
                anss.push_back(num);
            }
        }
    }
    
   // reverse(anss.begin(), anss.end());
    int t; cin >> t;
    while(t--){
        ll n; cin >> n;
        
        int found = false;
        for(int i=0;i<anss.size();i++){
            if(anss[i] <= n){
                found = true;
                cout << anss[i]<<endl;
                break;
            }
        }
        if(!found){
            cout << -1 <<endl;
        }
    }
    return 0;
}
