class Solution:
  def findDuplicateSubtrees(self, root: Optional[TreeNode]) -> List[Optional[TreeNode]]:
    ans = []
    count = Counter()

    def encode(root: Optional[TreeNode]) -> str:
      if not root:
        return ''

      left = encode(root.left)
      right = encode(root.right)
      encoding = str(root.val) + '#' + left + '#' + right

      if count[encoding] == 1:
        ans.append(root)
      count[encoding] += 1
      return encoding

    encode(root)
    return ans
