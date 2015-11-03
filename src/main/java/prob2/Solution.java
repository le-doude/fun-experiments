package prob2;

import java.util.Arrays;


/**
 * Find the 2 breaking points so that the cost (sum of the ints) is the lowest giver that
 * for index P and Q we must have |P - Q| > 1.
 * We have to "break the chain in 3 smaller chains of at least 1 link each".
 */
class Solution {
    public static int solution(int[] A) {
        A[0] = Integer.MAX_VALUE;
        A[A.length - 1] = Integer.MAX_VALUE;


        int[] minLeft = initArrayForMins(A.length);
        for (int i = 1; i < A.length; i++) {
            minLeft[i] = Math.min(A[i - 1], minLeft[i - 1]);
        }

        System.out.println(Arrays.toString(minLeft));

        int[] minRight = initArrayForMins(A.length);
        for (int i = A.length - 2; i >= 0; i--) {
            minRight[i] = Math.min(A[i + 1], minRight[i + 1]);
        }

        System.out.println(Arrays.toString(minRight));

        long[] sums = new long[A.length];
        for (int i = 0; i < minRight.length; i++) {
            sums[i] = (long) minLeft[i] + (long) minRight[i];
        }

        System.out.println(Arrays.toString(sums));

        long min = Integer.MAX_VALUE;
        long sum;
        for (int i = 0; i < minRight.length; i++) {
            sum = (long) minLeft[i] + (long) minRight[i];
            min = Math.min(min, sum);
        }
        return (int) min;
    }

    static int[] initArrayForMins(int l) {
        int[] minRight = new int[l];
        minRight[0] = Integer.MAX_VALUE;
        minRight[minRight.length - 1] = Integer.MAX_VALUE;
        return minRight;
    }

    public static void main(String[] args) {

        solutionFor(1, 0, 5, 66, 58, 12, 1, 0, 5, 8, 6);
        solutionFor(55, 55, 55, 5, 2, 2, 8, 55, 55, 55, 1000);

    }

    static void solutionFor(int... ints) {
        System.out.println(String.format("Lowest cost for %s is %s", Arrays.toString(ints), solution(ints)));
    }
}