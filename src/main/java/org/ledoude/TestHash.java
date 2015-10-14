package org.ledoude;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by edouard_pelosi on 10/14/15.
 */
public class TestHash {

    public static void main(String[] args) throws URISyntaxException, IOException {
        URL url = TestHash.class.getClassLoader().getResource("words.txt");
        List<String> lines = Files.readAllLines(Paths.get(url.toURI()));

        System.out.println(String.format("Found %s words.", lines.size()));

        HashMap<Integer, ArrayList<String>> collisions = new HashMap<>();

        Integer hash;
        ArrayList<String> c;
        for (String word : lines) {
            if(StringUtils.isNotBlank(word)){
                hash = FunnyHash.Defaults.STRING_FUNNY_HASH.hash(word);
                c = collisions.getOrDefault(hash, new ArrayList<>());
                c.add(word);
                collisions.put(hash, c);
            }
        }

        List<Integer> collided = collisions.entrySet().stream().filter((e) -> e.getValue().size() > 1).map(Map.Entry::getKey).collect(Collectors.<Integer>toList());
        System.out.println(String.format("Found %s collisions", collided.size()));
        ArrayList<String> cs;
        for (Integer integer : collided) {
            cs = collisions.getOrDefault(integer, new ArrayList<>());
            System.out.println(String.format("collision on [%s] >> | %s |", integer, String.join(" | ", cs.toArray(new CharSequence[cs.size()]))));
        }
    }

}
