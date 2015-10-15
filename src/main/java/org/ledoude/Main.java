package org.ledoude;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by edouard_pelosi on 10/14/15.
 */
public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException {
        URL url = Main.class.getClassLoader().getResource("words.txt");
        List<String> lines = Collections.unmodifiableList(Files.readAllLines(Paths.get(url.toURI())));

        System.out.println(String.format("Found %s words.", lines.size()));

        Stream<Long> stream = StreamSupport.stream(new LongRange(3000L, 4000L), true);

        Optional<HashTest.TestPerformance> collect = stream.parallel()
                .map((seed) -> HashTest.test(ByteHash.Defaults.withSeed(seed), lines))
                .filter((p) -> p != null)
                .collect(Collectors.minBy((l, r) -> Integer.compare(l.countCollisions, r.countCollisions)));

        if (collect.isPresent()) {
            System.out.printf(collect.get().toString());
            collect.get().printToFile();
        } else {
            System.out.println("somethign is fucky");
        }

    }

}

