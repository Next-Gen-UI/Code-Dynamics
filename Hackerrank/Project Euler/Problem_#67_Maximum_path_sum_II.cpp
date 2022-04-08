#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;


int main() {
    int t; cin >> t; 
    while(t--){
        int n; cin >> n;
        vector<vector<int> > v(n, vector<int>(n, 0)), dp(n, vector<int>(n, -1));
        for(int i=0;i<n;i++){
            for(int j=0;j<=i;j++){
                cin >> v[i][j];
            }
        }
        for(int i=n-1;i>=0;i--){
            for(int j=0;j<=i;j++){
                if(i == n-1){ dp[i][j] = v[i][j];}
                else{
                    dp[i][j] = v[i][j]+max(dp[i+1][j], dp[i+1][j+1]);
                }
            }
        }
        cout << dp[0][0] << endl;
    }
    return 0;
}
