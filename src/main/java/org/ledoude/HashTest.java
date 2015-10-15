package org.ledoude;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by edouard_pelosi on 10/15/15.
 */
public class HashTest {

    public static <T> TestPerformance<T> test(Hash<T> hasher, Collection<T> testData) {
        HashMap<Integer, HashSet<T>> collisions = new HashMap<>();
        Integer hash;
        HashSet<T> c;
        long perf = 0;
        long start;
        for (T next : testData) {
            start = System.currentTimeMillis();
            hash = hasher.hash(next);
            perf += System.currentTimeMillis() - start;
            c = collisions.getOrDefault(hash, new HashSet<>());
            c.add(next);
            collisions.put(hash, c);
        }

        HashMap<Integer, HashSet<T>> fin = new HashMap<>();
        collisions.entrySet().stream().filter(e -> e.getValue().size() > 1).forEach((e) -> fin.put(e.getKey(), e.getValue()));
        return new TestPerformance<T>(
                collisions.entrySet().stream().map((e) -> e.getValue().size() - 1).collect(Collectors.summingInt((o) -> o)),
                perf,
                fin,
                hasher);
    }

    public static class TestPerformance<T> {

        final int countCollisions;
        final long time;
        final HashMap<Integer, HashSet<T>> collisions;
        final Hash<T> hasher;

        public TestPerformance(int countCollisions, long time, HashMap<Integer, HashSet<T>> collisions, Hash<T> hasher) {
            this.countCollisions = countCollisions;
            this.time = time;
            this.collisions = collisions;
            this.hasher = hasher;
        }


        @Override
        public String toString() {
            return String.format("%s => collisions=%s time=%s ms", hasher, countCollisions, time);
        }

        public void printToFile() throws IOException {
            List<Integer> collided = collisions.entrySet().stream().filter((e) -> e.getValue().size() > 1).map(Map.Entry::getKey).collect(Collectors.<Integer>toList());
            HashSet<T> cs;
            File benchmarks = null;
            if (!collided.isEmpty()) {
                benchmarks = Paths.get("benchmarks", hasher.getClass().getSimpleName().toLowerCase(), hasher.hashCode() + ".txt").toFile();
                File dir = benchmarks.getParentFile();
                if ((dir.exists() ^ dir.mkdirs()) && ((benchmarks.exists() && benchmarks.delete()) || benchmarks.createNewFile())) {
                    try (PrintWriter writer = new PrintWriter(new FileWriter(benchmarks))) {
                        writer.println(hasher.toString());
                        for (Integer integer : collided) {
                            cs = collisions.getOrDefault(integer, new HashSet<>());
                            String[] strings = cs.stream().map(Object::toString).collect(Collectors.toList()).toArray(new String[cs.size()]);
                            writer.println(String.format("collision on [%s] >> | %s |", integer, Arrays.toString(strings)));
                        }
                    }
                }
            }
        }
    }

}
