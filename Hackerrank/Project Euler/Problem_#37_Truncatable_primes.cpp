#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;

vector<int> v(1e6+5, 0);
int trun(int  x){
    int k =x;
    int c = 1;
    while(k != 0){
        if(v[k]){
            return false;
        }
        k /= 10;
        c*=10;
    }
    k = x;
    while(c != 1){
        if(v[k%c]) return false;
        c /=10;
    }
    return true;
}
int main() {
    
    v[0]=v[1] = 1;
    for(int i=2;i*i<=v.size();i++){
        if(v[i] == 1) continue;
        for(int j=i*i;j<v.size();j+=i){
            v[j] = 1;
        }
    }
    vector<int> primes;
    for(int i=2;i<=1e6;i++){
        if(v[i] == 0) primes.push_back(i);
    }
    int n; cin >> n;
    int sum = 0;
    for(int i=0;i<primes.size();i++){
        if(primes[i] < 10) continue;
        if(primes[i] >= n) break;
        if(trun(primes[i])){
            //cout << primes[i] <<endl;
            sum += primes[i];
        }
    }
    cout << sum <<endl;
    return 0;
}
