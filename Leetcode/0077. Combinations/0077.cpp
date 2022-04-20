class Solution {
 public:
  vector<vector<int>> combine(int n, int k) {
    vector<vector<int>> ans;
    dfs(n, k, 1, {}, ans);
    return ans;
  }

 private:
  void dfs(int n, int k, int s, vector<int>&& path, vector<vector<int>>& ans) {
    if (k == 0) {
      ans.push_back(path);
      return;
    }

    for (int i = s; i <= n; ++i) {
      path.push_back(i);
      dfs(n, k - 1, i + 1, move(path), ans);
      path.pop_back();
    }
  }
};
