#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <string>
#include <algorithm>
using namespace std;


int main() {
    int t; scanf("%d", &t);
    int n = t;
    vector<string> v;
    
    while(t--){
        char s[20];
        scanf("%s", s);
        v.push_back(s);
    }
    scanf("%d", &t);
    sort(v.begin(), v.end());
    
    while(t--){
        char s[20];
        scanf("%s", s);
        string h = s;
        long long sum = 0;
        for(int i=0;i<v.size();i++){
            if(h == v[i]){
                int c = 0;
                for(int j=0;j<v[i].length();j++){
                    c += v[i][j]-'A'+1;
                }
                sum += c*(i+1);
            }
        }
        printf("%lld\n", sum);
        
        
    }
    
    return 0;
}
