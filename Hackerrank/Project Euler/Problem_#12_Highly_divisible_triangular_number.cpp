#include <cmath>
#include <cstdio>
#include <map>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;
typedef long long ll;

ll div(ll n, vector<int> &ps){
    map<int, int> m;
    int i=0;
    while(n>1 && i < ps.size()){
        if(n%ps[i] == 0){
            m[ps[i]]++;
            n /= ps[i];
        }else{
            i++;
        }
    }
    ll ans = 1;
    for(map<int, int>::iterator it=m.begin();it!=m.end();it++){
        ans *= (it->second+1);
    }
    return ans;
}
int main() {
    vector<int> prs(1000, 1);
    prs[0]=prs[1]=0;
    for(int i=2;i*i<=1000;i++){
        if(!prs[i])continue;
        for(int j=i*i;j<1000;j+=i){
            prs[j] = 0;
        }
    }
    vector<int> ps;
    for(int i=2;i<1000;i++){
        if(prs[i]) ps.push_back(i);
    }
    //for(int i=0;i<ps.size();i++)cout<<ps[i]<<" ";
    int t;cin>>t;
    while(t--){
        int n; cin >> n;
        int i = 1;
        ll num = 1;
        while(true){
            i++;
            num = (i*(i+1))/2;
            if(div(num, ps)>n){
                cout << num<<endl;
                break;
            }
        }
    }
    return 0;
}
