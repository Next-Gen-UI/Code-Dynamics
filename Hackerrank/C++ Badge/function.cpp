#include <iostream>
#include <cstdio>
using namespace std;

int a,b,c,d,large;
int max_of_four(){
    if(a>b && a>c && a>d){
        cout<<a;
        return(a);
    }
    else if(b>a && b>c && b>d){
        cout<<b;
        return(b);
    }
    else if(c>a && c>b && c>d){
        cout<<c;
        return(c);
    }
    else{
        cout<<d;
        return(d);
    }
}

int main() {
   cin>>a>>b>>c>>d;
   max_of_four();
}