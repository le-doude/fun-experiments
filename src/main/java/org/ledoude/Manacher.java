package org.ledoude;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by edouard_pelosi on 10/13/15.
 */
public class Manacher {

//    public static void main(String[] args) {
//
//        List<String> tests = Arrays.asList(
//                "Stuff I really do not like are palindromes and those look like this $e12345678987654321e¥ or $eBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBe¥ and $eaaaaaaaaaaaaae¥, but I really need the longest one.",
//                "odd length palyndrome abcba",
//                "even lenght palyndrom aahhaajjaahhaa",
//                "long pyramid asdfghjklkjhgfdsa",
//                "long samy aaaaaaaaaaaaaaaaaaaaa",
//                "symetric random pal " + randompal(14),
//                "no palindrome bitch!");
//        for (String test : tests) {
//            System.out.printf(String.format("The longest palindrome in \n %s \n is \n %s \n", test, manacher(test)));
//            System.out.println();
//        }
//    }

    public static String randompal(int l) {
        String base = RandomStringUtils.randomAlphabetic(l / 2);
        return new StringBuilder(base).reverse().append(base).toString();
    }

    private static String manacher(final String test) {
        StringBuilder builder = new StringBuilder();
        builder.append("#");
        for (char c : test.toCharArray()) {
            builder.append(c).append('#');
        }
        String string = builder.toString();
        int[] tracking = new int[string.length()];
        Arrays.fill(tracking, 0);

        int left, right;

        for (int i = 0; i < string.length(); ) {
            left = i - tracking[i];
            right = i + tracking[i];
            if (left >= 0 && right < string.length() && string.charAt(left) == string.charAt(right)) {
                tracking[right] = tracking[left];
                tracking[i]++;
            } else {
                i++;
            }
        }
        int maxLength = 1;
        int indexOfMax = -1;
        for (int i = 0; i < tracking.length; i++) {
            if (maxLength < tracking[i]) {
                indexOfMax = i;
                maxLength = tracking[i];
            }
        }
        int l = indexOfMax - maxLength + 1;
        int r = indexOfMax + maxLength;
        return string.substring(l, r).replaceAll("#", "");
    }


}

/**
 * Functional class that counts strings by lengths. Usefull for some NLP processing.
 */
class CountHolder {

    private final int[] counts;
    private final int min;
    private final int max;
    private final boolean isDetailed;
    private final int lastIndex;

    private static int[] countByLength(List<String> strings, int m, int M) {
        int max = Math.max(m, M);
        int min = Math.min(m, M);
        int last = max - min;
        if (last > 0) {
            int[] dico = new int[last + 1];
            Arrays.fill(dico, 0);
            for (String string : strings) {
                int l = string.length();
                if (l > max) {
                    dico[last]++;
                } else {
                    dico[l - min]++;
                }
            }
            return dico;
        } else {
            throw new IllegalArgumentException(String.format("Count by size would be equivalent of collection.size() for params m=%s and M=%s", min, max));
        }
    }

    public CountHolder(String... strings) {
        List<String> list = Arrays.asList(strings);
        this.min = 0;
        this.max = list.stream().map(String::length).max(Integer::compare).orElse(0) + 1;
        this.counts = countByLength(list, this.min, this.max);
        this.isDetailed = true;
        this.lastIndex = counts.length - 1;
    }

    public CountHolder(int min, int max, String... strings) {
        this.max = Math.max(min, max);
        this.min = Math.min(min, max);
        this.counts = countByLength(Arrays.asList(strings), this.min, this.max);
        this.isDetailed = false;
        this.lastIndex = counts.length - 1;
    }

    public int minOrBelow() {
        return counts[0];
    }

    public int maxOrAbove() {
        return counts[lastIndex];
    }

    public int maxLenght() {
        return max;
    }

    public int minLenght() {
        return min;
    }

    public int forSize(int s) throws UnsupportedOperationException {
        if ((s > min && s < max) || (isDetailed && s >= min && s <= max)) {
            return counts[s - min];
        } else {
            throw new UnsupportedOperationException(String.format("Specific counter for size [%s] not available please use minOrBelow or maxOrAbove methods for this size.", s));
        }
    }


}
