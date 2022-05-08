#include <bits/stdc++.h>
using namespace std;

int main() {
    int t;
    cin>>t;
    while(t--){
        int n;
        cin>>n;
        vector<int> a(n);
        for(int i=0;i<n;i++) cin>>a[i];
        
        sort(a.begin(),a.end());
        
        int diff=a[0];
        for(int i=1;i<n;i++){
            diff=__gcd(diff,a[i]);
        }
        cout<<diff<<"\n";
    }
	return 0;
}