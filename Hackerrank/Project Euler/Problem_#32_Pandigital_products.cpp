#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
#include <map>
using namespace std;

map<int, int> done;

int asProduct(vector<int> &v){
    int ans = 0;
    int n = v.size();
    //xxx|xx|xxx
    int first = 0;
    for(int i=0;i<n-2;i++){
        first = first*10 + v[i];
        //up to i
        
        int second = 0;
        for(int j=i+1;j<n-1;j++){
            second = second*10 + v[j];
            
            int third = 0;
            for(int k=j+1;k<n;k++){
                third = third*10 + v[k];
            }
            if(first*second == third && done.find(third) == done.end()){
                ans += third;
                done[third] = 1;
            }
        }
    }
    return ans;
}
int main() {
    int n; cin >> n;
    vector<int> v(n);
    for(int i=0;i<n;i++){
        v[i] = i+1;
    }
    int ans = 0;
    ans += asProduct(v);
    while(next_permutation(v.begin(), v.end())){
        ans += asProduct(v);
    }
    cout << ans << endl;
    return 0;
}
