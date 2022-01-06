#include <bits/stdc++.h>
#include <iostream>
using namespace std;

int main(){
    string num[9] = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
    int n,m,i;
    cin>>n>>m;
    for(i=n;i<=m;i++){
        if(i<=9){
            cout<<num[i-1]<<endl;
        }
        else{
            if(i%2==0){
                cout<<"even"<<endl;
            }
            else{
                cout<<"odd"<<endl;
            }
        }
    }
        
    return 0;
}