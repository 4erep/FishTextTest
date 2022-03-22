package ru.fishtext.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class CountHelper {

    public static int countNumbersOfEntries(String initialString, String substring) {

        return (int) IntStream.iterate(
                        initialString.indexOf(substring),
                        i -> i != -1,
                        i -> initialString.indexOf(substring, i + 1))
                .count();
    }

    public static int countSentences(String initialString) {
        Matcher matcher = Pattern.compile("([.!?])([\\s\\n])([A-Z]*)").matcher(initialString);
        int count = 1;
        while (matcher.find()) count++;
        return count;
    }
}
