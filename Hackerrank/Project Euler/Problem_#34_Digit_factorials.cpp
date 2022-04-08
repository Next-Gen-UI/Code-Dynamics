#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;

int fact(int n){
    if(n == 0) return 1;
    int f = 1;
    for(int i=1;i<=n;i++) f*=i;
    return f;
}
int main() {
    int n; cin >> n;
    int ans = 0;
    for(int i=10;i<n;i++){
        int k = i;
        int sum = 0;
        while(k != 0){
            int d = k%10;
            k /= 10;
            sum += fact(d);
        }
        if(sum%i == 0){
            ans += i;
            //cout << i <<endl;
        }
    }
    cout << ans << endl;
    return 0;
}
