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

int prime(int n){
    if(n < 2) return false;
    for(int i=2;i*i<=n;i++){
        if(n%i == 0){
            return false;
        }
    }
    return true;
}
int main(){
	int n; scanf("%d", &n);
    int ans = 0;
    int A, B;
    for(int i=-n;i<=n;i++){
        for(int j=-n;j<=n;j++){
            //n^2 + i*n + j
            int primes = 0;
            for(int k=0;;k++){
                if(prime(k*k+i*k+j)){
                    primes++;
                }else{
                    break;
                }
            }
            if(primes > ans){
                //cout << primes<<endl;
                ans = primes;
                A = i;
                B = j;
            }
        }
    }
    cout << A<<" "<<B <<endl;
}