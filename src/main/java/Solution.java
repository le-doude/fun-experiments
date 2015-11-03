// you can also use imports, for example:
// import java.util.*;

// you can write to stdout for debugging purposes, e.g.
// System.out.println("this is a debug message");

class Solution {
    public int solution(int[] A) {
        System.out.println(((long)Integer.MAX_VALUE * 100000));

        if(A == null) return -1;

        long leftSum = 0;
        long rigthSum = total(A);
        for (int i = 0; i < A.length; i++) {
            rigthSum -= A[i];
            System.out.println(String.format("P = %s, %s <?> %s", i, leftSum, rigthSum));
            if(rigthSum == leftSum) {
                return i;
            }
            leftSum += A[i];
        }
        return -1;
    }

    long total(int[] A) {
        long sum = 0;
        for (int i = 0; i < A.length; i++) {
            sum = A[i];
        }
        return sum;
    }
}