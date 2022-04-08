#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <string>
#include <map>
#include <algorithm>
#include <cassert>
using namespace std;
typedef long long ll;
typedef pair<int, int> ii;

int num;
vector<int> vis;
map<ll, int> want;
vector<string> ans;
string s = "abcdefghijklm";
void comp(string a){
    if(a.length()==s.length()){
        num++;
        //cout << num << " "<<want[num].second << " " << want[num].first << " "<<a << endl;
        if(want.find(num)!=want.end()){
            ans[want[num]] = a;
        }
        return;
    }
    for(int i=0;i<s.length();i++){
        if(!vis[i] ){
            vis[i] = true;
            comp(a+s[i]);
            vis[i] = false;
        }
    }
}
vector<int> f(15, 1);
string solve(int n, string a, int fact){
    if(a.length() == s.length()){
        return a;
    }
    int ind = 0;
    while(n > f[fact]){
        n -= f[fact];
        ind++;
    }
    for(int i=0;i<s.length();i++){
        if(!vis[i]) ind--;
        if(ind == -1){
            vis[i] = true;
            return solve(n, a+s[i], fact-1);
        }
    }
    assert(1==0);
    return "";
}
int main() {
    int t;scanf("%d", &t);int T = t;
    
    ans = vector<string>(T);
    for(int i=1;i<15;i++){
        f[i] = f[i-1]*i;
    }
    while(t--){
        ll n; scanf("%lld", &n);
        want[n] = T-t-1;
        vis = vector<int>(13, 0);
        printf("%s\n", solve(n, "", 12).c_str());
    }
    return 0;
}
