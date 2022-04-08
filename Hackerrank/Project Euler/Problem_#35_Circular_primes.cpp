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

int prime(int x){
    for(int i=2;i*i<=x;i++){
        if(x%i == 0) return false;
    }
    return true;
}
int p(int x){
    int d = 1;
    int digs = 0;
    while(d <= x){
        d *= 10;
        digs++;
    }
    d/=10;
    for(int k=0;k<digs;k++){
        x = (x%10)*d+(x/10);
        if(!prime(x)){
            return false;
        }
    }
    return true;
}
int main(){
	int n; scanf("%d", &n);
    int sum = 0;
    for(int i=2;i<n;i++){
        if(p(i)){
            sum += i;
            //cout << i <<endl;
        }
    }
    printf("%d\n", sum);
}