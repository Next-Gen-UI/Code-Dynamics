#include <cstdio>
#include <iostream>
#include <sstream>
#include <string>
#include <cmath>
#include <cassert>
#include <algorithm>
#include <vector>
#include <set>
#include <map>
using namespace std;
typedef long long ll;
typedef pair<int, int> ii;
typedef pair<double, double> dd;
typedef vector<int> vi;
typedef vector<ii> vii;
typedef vector<vi> vvi;
typedef vector<vii> vvii;
const int M = 5*(1e6) +5;

int gcd(int a, int b){
    if(b ==0)return a;
    return gcd(b, a%b);
}
int main(){
    vi ans(M), act(M, 0);
    ans[0] = 0;
    act[0] = 0;
    for(int i=2;;i++){
        if(2*i+i*i > M) break;
        for(int j=1;j<i;j++){
            if((i-j)%2 == 1 && gcd(i, j) == 1){ //primitive pythagorean triple
                
                for(int k=1;;k++){ //all pythagorean triples (extending every primitive by multiplication)
                    int a = k*(i*i-j*j);
                    int b = k*(2*i*j);
                    int c = k*(i*i+j*j);
                    if(a <= 0) break;
                    if(a+b+c >= M) break;
                    int p = a+b+c;
                    act[p]++;
                }
            }
        }
    }
    int mx = 0;
    for(int i=1;i<M;i++){
        if(act[i] > mx){
            mx = act[i];
            ans[i] = i;
        }else{
            ans[i] = ans[i-1];
        }
        //cout << act[i] <<" " <<ans[i]<<endl;
    }
	int T; scanf("%d", &T);
	while(T--){
		int n; scanf("%d", &n);
        printf("%d\n", ans[n]);
	}

}