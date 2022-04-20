class Solution {
 public:
  vector<vector<int>> intervalIntersection(vector<vector<int>>& firstList,
                                           vector<vector<int>>& secondList) {
    vector<vector<int>> ans;
    short i = 0;
    short j = 0;

    while (i < firstList.size() && j < secondList.size()) {
      const int maxL = max(firstList[i][0], secondList[j][0]);
      const int minR = min(firstList[i][1], secondList[j][1]);
      if (maxL <= minR)
        ans.push_back({maxL, minR});
      if (firstList[i][1] < secondList[j][1])
        ++i;
      else
        ++j;
    }

    return ans;
  }
};
