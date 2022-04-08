#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;
const int M = 5*(1e5);


int main() {
    vector<int> primes(M, 1);
    primes[0]=primes[1] = 0;
    for(int i=2;i*i<=M;i++){
        if(!primes[i]) continue;
        for(int j = i*i;j<M;j+=i){
            primes[j] = 0;
        }
    }
    vector<int> ways(M);
    for(int j=9;j<M;j++){
        for(int k=1;j-2*k*k >= 2;k++){
            if(primes[j-2*k*k]){
                ways[j]++;
            }
        }
    }
    
    int t; cin >> t;
    while(t--){
        int n; cin >> n;
        cout << ways[n]<<endl;
    }
    return 0;
}
