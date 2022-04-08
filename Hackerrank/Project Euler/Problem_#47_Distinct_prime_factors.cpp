#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;

int distinct(int h){
    int d = 0;
    for(int i=2;;){
        if(i*i > h){
            return d+1;
        }
        if(h%i == 0){
            d++;
            h /= i;
            while(h%i == 0) h /= i;
            if(h == 1){
                return d;
            }
        }
        i++;
    }
    
}
int main() {
    int n, k; cin >> n >> k;
    vector<int> v(n+k+1);
    for(int i=2;i<=n+k-1;i++){
        v[i] = distinct(i);
    }
    int counter = 0;
    for(int i=2;i<=n+k-1;i++){
        if(v[i] == k){
            counter++;
        }else{
            counter = 0;
        }
        if(counter >= k){
            printf("%d\n", i-k+1);
        }
    }
    return 0;
}
