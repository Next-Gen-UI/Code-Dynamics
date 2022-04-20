class Solutionclass Solution {
  public List<TreeNode> findDuplicateSubtrees(TreeNode root) {
    List<TreeNode> ans = new ArrayList<>();
    Map<String, Integer> count = new HashMap<>();
    encode(root, count, ans);
    return ans;
  }

  private String encode(TreeNode root, Map<String, Integer> count, List<TreeNode> ans) {
    if (root == null)
      return "";

    final String left = encode(root.left, count, ans);
    final String right = encode(root.right, count, ans);
    final String encoding = root.val + "#" + left + "#" + right;

    if (count.getOrDefault(encoding, 0) == 1)
      ans.add(root);
    count.merge(encoding, 1, Integer::sum);
    return encoding;
  }
}
