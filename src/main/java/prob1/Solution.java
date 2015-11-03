package prob1;

import java.util.Arrays;

class Solution {
    public int solution(int[] A) {
        // write your code in Java SE 8
        long[] sums = new long[A.length];
        Arrays.fill(sums, 0);
        int r = 0;
        for (int size = 0; size < sums.length; size++) {
            for (int i = 0; i + size < A.length; i++) {
                sums[i] += A[i + size];
                r += (sums[i] == 0 ? 1 : 0);
                if (r > 1_000_000_000) return -1;
            }
        }
        return r;
    }

}