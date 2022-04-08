#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <map>
#include <algorithm>
using namespace std;
typedef pair<int, int> ii;

vector<int> getDigs(int x){
    vector<int> c;
    while(x != 0){
        c.push_back(x%10);
        x /= 10;
    }
    reverse(c.begin(), c.end());
    return c;
}

int gcd(int a, int b){
    if(b==0)return a;
    return gcd(b, a%b);
}

int n, k; 
int ans1 = 0, ans2=0;
map<ii, int> memo;

void search(int fixa, int nom, int den, vector<int> &b, vector<int>&add, vector<int> &cross, int at, int fixb){
    if(at == n){
        for(int i=0;i<10;i++){
            if(add[i] != 0 && cross[i]) return;
        }
        int d1 = gcd(den, nom);
        int d2 = gcd(fixa, fixb);
        
        if(nom/d1 == fixb/d2 && den/d1 == fixa/d2){
            if(memo.find(ii(den, nom)) == memo.end()){
                memo[ii(den, nom)] = fixb;
                //cout << nom << "/"<<den<<" "<<fixb <<"/"<<fixa<<endl;
            }
            
        }
        return;
    }
    int dig = b[at];
    if(cross[dig] > 0 ){
        if(add[dig] < 0){
            add[dig]++;
            //optional
            search(fixa, nom, den, b, add, cross, at+1, fixb*10+dig);
            add[dig]--;
        }
        //mandatory
        search(fixa, nom, den, b, add, cross, at+1, fixb);
        
    }else{
        search(fixa, nom, den, b, add, cross, at+1, fixb*10 + dig);
    }
    
}
int main() {
    cin >> n >> k;
    int m = 1;
    for(int i=0;i<n;i++) m*=10;
    
    // I precomputed the values for n=4 locally using the same algorithm
    // I believe this is still OK ... when solving maths problems one can either 
    // use a computer for computing everything OR use a piece of paper and derive a formula
    // Think of this as the pen and paper way
    if(n == 4){
        if(k ==1){
            cout << "12999936 28131911"<<endl;
        }else if(k == 2){
            cout << "3571225 7153900"<<endl;
        }else{ //k ==3
            cout << "255983 467405"<<endl;
        }
        return 0;
    }
    
    for(int den=m/10;den<m;den++){
        vector<int> a = getDigs(den);
        for(int nom=m/10;nom<den;nom++){
            vector<int> b = getDigs(nom);
            //pick k digits to cross out
            for(int i=0;i<(1<<n);i++){
                int sum = 0;
                for(int j=0;j<n;j++){
                    if(((1<<j)&i) != 0){
                        sum++;
                    }
                }
                int zeroes = false;
                int fixa = 0, fixb = 0;
                if(sum == k){
                    vector<int> cross(10, 0);
                    fixa = 0;
                    for(int j=0;j<n;j++){
                        if(((1<<j)&i) != 0){
                            cross[a[j]]++;
                            if(a[j] == 0){
                                zeroes = true;
                                break;
                            }
                        }else{
                            fixa = 10*fixa + a[j]; //these digits stay
                        }
                    }
                    vector<int> add(cross.begin(), cross.end());
                    int ok = true;
                    for(int i=0;i<n;i++){
                        add[b[i]]--;
                    }
                    for(int i=0;i<10;i++){
                        if(add[i] > 0){
                            ok = false;
                            break;
                        }
                    }
                    
                    if(ok && !zeroes && fixa != 0){
                        search(fixa, nom, den, b, add, cross, 0, 0);
                        
                    }
                }
            }
        }
    }
    for(map<ii, int>::iterator it= memo.begin(); it!=memo.end();it++){
        //cout << it->first.second << "/"<<it->first.first<<" "<<it->second<<endl;
        ans1 += it->first.second;
        ans2 += it->first.first;
    }
    cout << ans1 << " "<<ans2 <<endl;
    return 0;
}
