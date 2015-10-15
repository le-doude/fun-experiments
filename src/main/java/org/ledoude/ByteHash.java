package org.ledoude;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * Created by edouard_pelosi on 10/15/15.
 */
public class ByteHash<T> implements Hash<T> {


    public static final class Defaults {

        public static final Hash<String> STRING_HASH = new ByteHash<>((s) -> s.getBytes(StandardCharsets.UTF_8), 1234567890L);

        public static Hash<String> withSeed(long seed) {
            return new ByteHash<>((s) -> s.getBytes(StandardCharsets.UTF_8), seed);
        }

    }

    private static final short[] basePrimes() {
        return new short[]{
                2, 3, 5, 7, 11, 13, 17, 19,
                23, 29, 31, 37, 41, 43, 47, 53,
                59, 61, 67, 71, 73, 79, 83, 89,
                97, 101, 103, 107, 109, 113, 127, 131,
                137, 139, 149, 151, 157, 163, 167, 173,
                179, 181, 191, 193, 197, 199, 211, 223,
                227, 229, 233, 239, 241, 251};
    }


    private static final int hashBase = 31;
    private static final int oneOnTop = 0b1 << 31;


    private final Byter<T> byter;
    private final short[] primes;
    private final long seed;

    public ByteHash(Byter<T> byter, long shuffleSeed) {
        this.byter = byter;
        this.seed = shuffleSeed;
        this.primes = basePrimes();

        short swap;
        Random r = new Random(this.seed);
        for (int i = primes.length - 1; i >= 0; i--) {
            int j = r.nextInt(i + 1);
            swap = primes[i];
            primes[i] = primes[j];
            primes[j] = swap;
        }
    }

    @Override
    public int hash(T t) {
        if (t != null) {
            byte[] bytes = byter.bytes(t);
            long accumulator = t.hashCode();
            short num;
            short prime;
            for (int i = 0; i < bytes.length; i++) {
                num = (short) (bytes[i] & 0xff);
                for (int j = 0; j < primes.length && num != 1; j++) {
                    prime = primes[j];
                    while (num % prime == 0 && num != 1) {
                        num /= prime;
                        accumulator += oneOnTop >>> j;
                    }
                }
            }
            return (int) accumulator;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return new StringBuilder().append("ByteHash{").append("seed=").append(seed).append('}').toString();
    }
}
