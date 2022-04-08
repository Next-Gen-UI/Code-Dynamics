#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;
typedef long long ll;
const int mod = 1e9 + 7;
int main() {
    int t; cin >> t;
    while(t--){
        int n, m; scanf("%d %d", &n, &m);
        n++;m++;
        vector<vector<ll> > v(n, vector<ll>(m, 1));
        for(int i=1;i<n;i++){
            for(int j=1;j<m;j++){
                v[i][j] = (v[i-1][j]+v[i][j-1])%mod;
            }
        }
        printf("%lld\n", v[n-1][m-1]);
    }
    return 0;
}
