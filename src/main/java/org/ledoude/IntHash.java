package org.ledoude;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by edouard_pelosi on 10/14/15.
 */
public class IntHash<T> implements Hash<T> {

    public static class Defaults {
        public static final IntHash<String> STRING_FUNNY_HASH = new IntHash<>((s) -> {
            char[] chars = s.toCharArray();
            int[] ints = new int[chars.length];
            for (int i = 0; i < chars.length; i++) {
                ints[i] = chars[i];
            }
            return ints;
        }, 1234567890L);
    }

    private static final int[] basePrimes() {
        return new int[]{
                2, 3, 5, 7, 11, 13, 17, 19,
                23, 29, 31, 37, 41, 43, 47, 53,
                59, 61, 67, 71, 73, 79, 83, 89,
                97, 101, 103, 107, 109, 113, 127, 131,
                137, 139, 149, 151, 157, 163, 167, 173,
                179, 181, 191, 193, 197, 199, 211, 223,
                227, 229, 233, 239, 241, 251, 257, 263,
                269, 271, 277, 281, 283, 293, 307, 311
        };
    }

    private static final int hashBase = 31;
    private static final int oneOnTop = 0b1 << 31;


    private final Inter<T> byter;
    private final int[] primes;
    private final long seed;

    public IntHash(Inter<T> byter, long shuffleSeed) {
        this.byter = byter;
        this.seed = shuffleSeed;
        this.primes = basePrimes();
        Random r = new Random(this.seed);
        for (int i = primes.length - 1; i >= 0; i--) {
            int j = r.nextInt(i + 1);
            int swap = primes[i];
            primes[i] = primes[j];
            primes[j] = swap;
        }
    }

    public int hash(T t) {
        if (t != null) {
            int[] nums = byter.ints(t);
            long accumulator = t.hashCode();
            for (int i = 0; i < nums.length; i++) {
                int num = nums[i];
                for (int j = 0; j < primes.length && num != 1; j++) {
                    int prime = primes[j];
                    while (num % prime == 0 && num != 1) {
                        num /= prime;
                        accumulator += oneOnTop >>> j;
                    }
                }
                accumulator *= num;
            }
            return (int) accumulator;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return new StringBuilder().append("IntHash{").append("seed=").append(seed).append('}').toString();
    }
}
