#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <cassert>
#include <algorithm>
using namespace std;


int main() {
    vector<int> pr(1e6, 0);
    pr[0]=pr[1] =1;
    for(int i=2;i<1e3;i++){
        if(pr[i]==1)continue;
        for(int j=i*i;j<1e6;j+=i){
            pr[j]=1;
        }
    }
    int t;cin>>t;
    while(t--){
        int n;cin>>n;
        int c =0;
        for(int i=2;;i++){
            if(pr[i]==0){
                c++;
            }
            if(c==n){
                cout << i <<endl;
                break;
            }
        }
        
    }
    return 0;
}
