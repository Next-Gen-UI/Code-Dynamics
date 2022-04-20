class Solution:
  def combine(self, n: int, k: int) -> List[List[int]]:
    ans = []

    def dfs(n: int, k: int, s: int, path: List[int]) -> None:
      if k == 0:
        ans.append(path.copy())
        return

      for i in range(s, n + 1):
        path.append(i)
        dfs(n, k - 1, i + 1, path)
        path.pop()

    dfs(n, k, 1, [])
    return ans
