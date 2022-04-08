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

int pan(int n, int k){
    int digs[10] = {0};
    int filled = 0;
    for(int i=1;;i++){
        int num = n*i;
        while(num != 0){
            int d = num%10;
            if(d == 0 || (d== 9 && k == 8)) return false;
            num /= 10;
            if(digs[d] == 1){
                return false;
            }else{
                digs[d] = 1;
                filled++;
            }
        }
        if(filled == k){
            return true;
        }
    }
}
int main(){
	int n, k; scanf("%d %d", &n, &k);
    for(int i=2;i<n;i++){
        if(pan(i, k)){
            printf("%d\n", i);
        }
    }

}