//Ramnit here baby
#include<bits/stdc++.h>
using namespace std;

// typedef long long lolo;
#define fasterDaddy ios_base::sync_with_stdio(false);cin.tie(NULL);cout.tie(NULL);
// #define all(v) v.begin,v.end()
// #define puba push_back
// #define F .first
// #define S .second

int main() {
	fasterDaddy;
	int t;
    cin>>t;

    while(t--){
        int a,b,c,d,e;
        cin>>a>>b>>c>>d>>e;
        if(a<=e){
            if((c+b)<=d){
               cout<<"YES\n";
              continue;
            }
        }
        if(b<=e){
            if((a+c)<=d){
               cout<<"YES\n";
              continue;
            }
        }
        if(c<=e){
            if((a+b)<=d){
               cout<<"YES\n";
              continue;
            }
        }
        // else{
            cout<<"NO\n";
        // }
    }
	return 0;
}