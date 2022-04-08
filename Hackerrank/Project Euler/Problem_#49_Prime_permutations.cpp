#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
#include <cassert>
using namespace std;

const int m = 1000000;

int sameDigs(int k, vector<int> &p){
    int c[10] = {0};
    for(int i=0;i<p.size();i++){
        c[p[i]]++;
    }
    while(k != 0){
        c[k%10]--;
        k/=10;
    }
    for(int i=0;i<10;i++){
        if(c[i] != 0){
            return false;
        }
    }
    return true;
}
int main() {
    //Compute primes first
    vector<int> prime(m, 1);
    prime[0]=prime[1]=0;
    for(int i=2;i*i<=m;i++){
        if(!prime[i]) continue;
        for(int j=i*i;j<m;j+=i){
            prime[j] = 0;
        }
    }
    
    int n, k; cin >> n >> k;
    for(int i=10;i<n;i++){
        if(!prime[i]) continue;
        vector<int> p;
        int ds = 1;
        int z = i; while(z != 0){ z/=10; ds*=10;}
        ds/=10;
        while(ds != 0){
            p.push_back((i/ds)%10);
            ds /= 10;
        }
        while(next_permutation(p.begin(), p.end())){
            int h = 0;
            for(int j=0;j<p.size();j++){
                h = h*10 + p[j];
            }
            int diff = h-i;
            if(diff > 0 && prime[h]){
                int third = h+diff;
                if(third < m && prime[third] && sameDigs(third, p)){
                    assert(i < h && h < third);
                    if(k == 3){
                        cout << i << h << third<<endl;
                    }else{
                        int fourth = third+diff;
                        if(fourth < m && prime[fourth] && sameDigs(fourth, p)){
                            cout <<i<<h<<third<<fourth<<endl;
                        }
                    }
                }
            }
        }
    }
    return 0;
}
