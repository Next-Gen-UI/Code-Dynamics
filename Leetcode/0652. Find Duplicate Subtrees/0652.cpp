class Solution {
 public:
  vector<TreeNode*> findDuplicateSubtrees(TreeNode* root) {
    vector<TreeNode*> ans;
    unordered_map<string, int> count;
    encode(root, count, ans);
    return ans;
  }

 private:
  string encode(TreeNode* root, unordered_map<string, int>& count,
                vector<TreeNode*>& ans) {
    if (!root)
      return "";

    const string& left = encode(root->left, count, ans);
    const string& right = encode(root->right, count, ans);
    const string& encoding = to_string(root->val) + "#" + left + "#" + right;

    if (count.count(encoding) && count[encoding] == 1)
      ans.push_back(root);
    ++count[encoding];
    return encoding;
  }
};
