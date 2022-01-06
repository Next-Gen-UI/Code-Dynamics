#include <iostream>
using namespace std;

void update(int *a,int *b)
{
    int temp = *a;
    *a = *a + *b;
    *b = temp - *b;

    if(*b < 0)
 {
        *b = -(*b);
    }
}

int main() 
{
    int a, b;
    int *pa = &a, *pb = &b;

    cin>>a>>b;
    update(pa, pb);
    cout<<a<<"\n"<<b;

    return 0;
}

// #include <iostream>
// using namespace std;

// void update(int *a, int *b){
//     int sum = *a + *b;
//     int sub = *a - *b;
//     cout<<sum<<endl<<sub<<endl;
// }
// int main(){

//     int a, b;
//     int *pa = &a, *pb = &b;

//     cin>>a>>b;

//     update(pa, pb);

//     return 0;
// }