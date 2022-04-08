#include <cstdio>
#include <iostream>
#include <sstream>
#include <string>
#include <vector>
#include <set>
#include <map>
#include <cmath>
#include <algorithm>
using namespace std;
typedef long long ll;
typedef pair<int, int> ii;
typedef pair<double, double> dd;
typedef vector<ll> vll;
typedef vector<int> vi;
typedef vector<ii> vii;
typedef vector<vi> vvi;
typedef vector<vii> vvii;

//const int M = 5e6 +3;
const int M = 5e6 +5;

vector<int> chain(M, -1);
int comp(int at){
    int ans = 0;
    ll i = at;
    while(true){
        if(i <= at && chain[i] != -1){
            ans += chain[i];
            break;
        }
        if(i%2 == 0){
            i /= 2;
            ans++;
        }else{
            i = 3*i+1;
            ans++;
        }
    }
    return chain[at]=ans;
}

int main(){    
    chain[1] =0;
    for(int i =2;i<M;i++){
        comp(i);
    }
    
    vi maxs(M);
    maxs[1] = 1;
    for(int i=2;i<M;i++){
        if(chain[maxs[i-1]] <= chain[i]){
            maxs[i] = i;
        }else{
            maxs[i] = maxs[i-1];
        }
    }
    
    int T; scanf("%d", &T);
    while(T--){
        int n; scanf("%d", &n);
        printf("%d\n", maxs[n]);
    }
}