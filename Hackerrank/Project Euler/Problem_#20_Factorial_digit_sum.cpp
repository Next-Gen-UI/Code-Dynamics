#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;


void mul(vector<int> &v, int x){
    int carry = 0;
    for(int i=0;i<v.size();i++){
        carry += v[i]*x;
        v[i] = carry%10;
        carry /= 10;
    }
    while(carry != 0){
        v.push_back(carry%10);
        carry /= 10;
    }
}
int main() { 
    int t; scanf("%d", &t);
    while(t--){
        int n; scanf("%d", &n);
        vector<int> v (3000);
        v.push_back(1);
        int sc = 1;
        while(sc <= n){
            mul(v, sc);
            sc++;
        }
        int sum = 0;
        for(int i=0;i<v.size();i++){
            sum += v[i];
        }
        printf("%d\n", sum);
    }
    return 0;
}
