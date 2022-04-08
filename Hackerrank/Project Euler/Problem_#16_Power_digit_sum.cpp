#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;
vector<int> v;

int main() {
    vector<int> two(1, 1);
    int power = 0;
    v.push_back(1);
    for(;power<=1e4;){
        int digSum = 0;
        
        int carry = 0;
        for(int i=0;i<two.size();i++){
            carry += two[i]*2;
            two[i] = carry%10;
            digSum += two[i];
            carry /= 10;
        }
        while(carry != 0){
            two.push_back(carry%10);
            digSum += carry%10;
            carry /= 10;
        }
        v.push_back(digSum);
        power++;
    }
    /*for(int i=0;i<10;i++){
        cout << v[i] << " ";
    }*/
    int t; scanf("%d", &t);
    while(t--){
        int n; scanf("%d", &n);
        printf("%d\n", v[n]);
    }
    return 0;
}
