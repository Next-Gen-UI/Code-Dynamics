#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
#include <string>
using namespace std;
typedef long long ll;

string units[] = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen", "Twenty"};
string decim[] = {"", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
string hun = "Hundred";
string thous = "Thousand";
string mill = "Million";
string bill = "Billion";

vector<string> ans;
void solve(ll n){
    if(n <= 20){
        ans.push_back(units[n]);
    }else if(n < 100){ // > 20
        ans.push_back(decim[n/10]);
        solve(n%10);
    }else if(n < 1000){// >= 100
        solve(n/100);
        ans.push_back(hun);
        solve(n%100);
    }else if(n < 1e6){ // >= 1000
        solve(n/1000);
        ans.push_back(thous);
        solve(n%1000);
    }else if(n < 1e9){
        ll h = 1e6;
        solve(n/h);
        ans.push_back(mill);
        solve(n%h);
    }else{
        ll h = 1e9;
        solve(n/h);
        ans.push_back(bill);
        solve(n%h);
    }
        
}
int main() {   
    int t; scanf("%d", &t);
    while(t--){
        ll n; scanf("%lld", &n);
        if(n == 0){
            cout << "Zero\n";
        }
        else{
            ans.clear();
            solve(n);
            cout << ans[0];
            for(int i=1;i<ans.size();i++){
                if(ans[i] == "")continue;
                cout << " "<<ans[i];
            }
            cout << "\n";
        }
    }
    return 0;
}
