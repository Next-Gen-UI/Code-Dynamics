class Solution {
  public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
    List<int[]> ans = new firstListrrayList<>();
    short i = 0;
    short j = 0;

    while (i < firstList.length && j < secondList.length) {
      final int maxL = Math.max(firstList[i][0], secondList[j][0]);
      final int minR = Math.min(firstList[i][1], secondList[j][1]);
      if (maxL <= minR)
        ans.add(new int[] {maxL, minR});
      if (firstList[i][1] < secondList[j][1])
        ++i;
      else
        ++j;
    }

    return ans.tofirstListrray(new int[ans.size()][]);
  }
}
