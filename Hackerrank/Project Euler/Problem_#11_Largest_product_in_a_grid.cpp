#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;

int g[20][20];
int comp(int x, int y){
    int m = 0;
    if(y+3<20){
        m = max(m, g[x][y]*g[x][y+1]*g[x][y+2]*g[x][y+3]);
    }
    if(x+3<20){
        m = max(m, g[x][y]*g[x+1][y]*g[x+2][y]*g[x+3][y]);
    }
    if(x+3 < 20 && y+3 <20){
        m = max(m, g[x][y]*g[x+1][y+1]*g[x+2][y+2]*g[x+3][y+3]);
    }
    if(x+3 < 20 && y-3 >=0){
        m = max(m, g[x][y]*g[x+1][y-1]*g[x+2][y-2]*g[x+3][y-3]);
    }
    return m;
}
int main() {
    
    for(int i=0;i<20;i++){
        for(int j=0;j<20;j++){
           cin >>  g[i][j];
        }
    }
    int m = 0;
    for(int i=0;i<20;i++){
        for(int j=0;j<20;j++){
            m = max(m, comp(i, j));
        }
    }
    cout << m;
    return 0;
}
