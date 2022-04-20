class Solution {
  public int minMeetingRooms(int[][] intervals) {
    if (intervals.length == 0)
      return 0;

    // store end times of each room
    Queue<Integer> minHeap = new PriorityQueue<>((a, b) -> a - b);

    Arrays.sort(intervals, (a, b) -> (a[0] - b[0]));

    for (int[] interval : intervals) {
      if (!minHeap.isEmpty() && interval[0] >= minHeap.peek())
        minHeap.poll(); // no overlap, we can reuse the same room
      minHeap.offer(interval[1]);
    }

    return minHeap.size();
  }
}
