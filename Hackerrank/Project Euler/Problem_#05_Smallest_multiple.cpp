#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;

int main() {
    int t;cin>>t;
    while(t--){
        int n;cin>>n;
        for(int i=n;;i++){
            int j;
            for(j=2;j<=n;j++){
                if(i%j != 0)
                    break;
            }
            if(j == n+1){
                cout << i <<endl;
                break;
            }
        }
    }
    return 0;
}
