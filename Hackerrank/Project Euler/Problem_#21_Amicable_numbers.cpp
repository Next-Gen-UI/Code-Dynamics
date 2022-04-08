#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <cassert>
#include <algorithm>
using namespace std;

const int M = 5e5 +5;
vector<int> v(M, -1);
int divs(int m){
    int sum = 1;
    int i;
    for(i=2;i*i<m;i++){
        if(m%i == 0){
            sum += i;
            sum += (m/i);
        }
    }
    if(i*i == m){
        sum += i;
    }
    return sum;
}
int main() {
    for(int i=1;i<M;i++){
        v[i] = divs(i);
        assert(v[i] > 0);
        //cout << v[i] << " ";
    }
    int t; scanf("%d", &t);
    while(t--){
        int n; scanf("%d", &n);
        int sum = 0;
        for(int i=1;i<n;i++){
            if(i == v[v[i]] && i != v[i]){
                sum += i;
            }
        }
        printf("%d\n", sum);
    }
    return 0;
}
