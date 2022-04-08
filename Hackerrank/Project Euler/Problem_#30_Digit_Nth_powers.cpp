#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;

int power(int base, int n){
    if(base == 0){
        return 0;
    }
    int ans = 1;
    for(int i=0;i<n;i++) ans *= base;
    return ans;
}

int main() {
    int n; cin >> n;
    int ans = 0;
    for(int i=10;i<10000000;i++){
       int sum = 0;
        int k = i;
        while(k != 0){
            int d = k%10;
            k /= 10;
            sum += power(d, n);
        }
        if(sum == i){
            //cout << i <<endl;
            ans += i;
            //cout << ans<<endl;
        }
    }
    cout << ans <<endl;
    return 0;
}
