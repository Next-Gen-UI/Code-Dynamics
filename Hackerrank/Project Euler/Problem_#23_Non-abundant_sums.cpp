#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;

const int M = 1e5 +5;
vector<int> v(M, -1);
int divs(int m){
    int sum = 1;
    int i;
    for(i=2;i*i<m;i++){
        if(m%i == 0){
            sum += i;
            sum += (m/i);
        }
    }
    if(i*i == m){
        sum += i;
    }
    return sum;
}
int main() {
    vector<int> ab;
    for(int i=1;i<M;i++){
        v[i] = divs(i);
        if(v[i] > i){
            ab.push_back(i);
        }else{
            v[i] = -1;
        }
        //cout << v[i] << " ";
    }
     
    int t; scanf("%d",&t);
    while(t--){
        int n; scanf("%d", &n);
        int ok = false;
        for(int i=0;i<ab.size();i++){
            int first = ab[i];
            if(n-first >= 0 && v[n-first] != -1){
                //cout << first << " "<<(n-first)<< " "<<v[n-first] << endl;
                ok = true;
                break;
            }
        }
        printf("%s\n", ok?"YES":"NO");
    }
    return 0;
}
