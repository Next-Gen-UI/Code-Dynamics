#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;
typedef long long ll;

int main() {
    int n, k; cin >>n >>k;
    vector<ll> penta;
    for(int i=1;i<n;i++){
        penta.push_back((((ll)(3*i-1))*i)/2);
        //cout << penta[i-1]<<endl;
    }
    for(int i=k;i<penta.size();i++){
        ll pa = penta[i];
        ll pb = penta[i-k];
        if(binary_search(penta.begin(), penta.end(), pa+pb) || binary_search(penta.begin(), penta.end(), pa-pb)){
            printf("%lld\n", pa);
        }
    }
    return 0;
}
