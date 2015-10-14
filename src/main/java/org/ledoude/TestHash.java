package org.ledoude;

/**
 * Created by edouard_pelosi on 10/14/15.
 */
public class TestHash {

    public static void main(String[] args) {
        String input = String.join(" ", args);
        System.out.println(FunnyHash.Defaults.STRING_FUNNY_HASH.hash(input));
    }

}
