#include <iostream>
using namespace std;
int main(){
    int n,targ;
    cout<<"enter size of array: ";
    cin>>n;
    int arr[n];
    
    for(int i=0; i<n; i++){
        cin>>arr[i];
    }
    
    cout<<"enter the target number:";
    cin>> targ;
    for(int i=0; i<n; i++){
        for(int j=i; j<n; j++){
            int sum = arr[i]+arr[j];
            if(sum==targ){
                cout<<arr[i]<<" "<<arr[j]<<" ";
            }
        }
    }
    return 0;
}

//TODO: submited this one only
// class Solution {
// public:
//     vector<int> twoSum(vector<int>& nums, int target) {
//         vector<int> ans;
//         for(int i=0;i<nums.size();i++){
//             for(int j=i+1;j<nums.size();j++){
//                 if(nums[i]+nums[j]==target){
//                     ans.push_back(i);
//                     ans.push_back(j);
//                     break;
//                 }
//             }
//         }
//         return ans;
//     }
// };