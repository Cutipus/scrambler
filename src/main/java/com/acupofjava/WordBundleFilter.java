package com.acupofjava;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
// import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class WordBundleFilter {
    public static void main(String[] args) throws IOException, ParseException {
        // get file
        InputStream fileInputStream = WordBundleFilter.class.getResourceAsStream("bundled_words.json");
        InputStreamReader reader = new InputStreamReader(fileInputStream, "UTF-8");

        // create parser
        JSONParser parser = new JSONParser();

        // deserialize
        @SuppressWarnings("unchecked")
        HashMap<String, List<String>> JSONWords = (HashMap<String, List<String>>) parser.parse(reader);
        HashMap<String, List<String>> unCuratedWords = new HashMap<>();
        HashMap<String, List<String>> curatedWords = new HashMap<>();
        //TODO save uncuratedWords to file

        JSONWords.forEach((key, value) -> unCuratedWords.put(key,new ArrayList<>(value)));


    }

    public void filterWords(HashMap<String, List<String>> JSONWords) {
        HashMap<String, List<String>> easyWords = new HashMap<>();
        int EASY_MINIMUM_KEY_LENGTH = 3;
        int EASY_MAXIMUM_KEY_LENGTH = 4;
        int EASY_MINIMUM_NUMBER_OF_PERMUTATIONS = 5;
        int EASY_MAXIMUM_NUMBER_OF_PERMUTATIONS = 8;

        HashMap<String, List<String>> mediumWords = new HashMap<>();
        int MEDIUM_MINIMUM_KEY_LENGTH = 4;
        int MEDIUM_MAXIMUM_KEY_LENGTH = 6;
        int MEDIUM_MINIMUM_NUMBER_OF_PERMUTATIONS = 2;
        int MEDIUM_MAXIMUM_NUMBER_OF_PERMUTATIONS = 6;

        HashMap<String, List<String>> hardWords = new HashMap<>();
        int HARD_MINIMUM_KEY_LENGTH = 6;
        int HARD_MAXIMUM_KEY_LENGTH = 8;
        int HARD_MINIMUM_NUMBER_OF_PERMUTATIONS = 1;
        int HARD_MAXIMUM_NUMBER_OF_PERMUTATIONS = 1;

        for (Entry<String, List<String>> permutaitonEntry : JSONWords.entrySet()) {
            String key = permutaitonEntry.getKey();
            List<String> value = permutaitonEntry.getValue();
            if (filterBasedOnLevel(permutaitonEntry, EASY_MINIMUM_KEY_LENGTH, EASY_MAXIMUM_KEY_LENGTH, EASY_MINIMUM_NUMBER_OF_PERMUTATIONS, EASY_MAXIMUM_NUMBER_OF_PERMUTATIONS)) {
                easyWords.put(key, new ArrayList<>(value));
            }

            if (filterBasedOnLevel(permutaitonEntry, MEDIUM_MINIMUM_KEY_LENGTH, MEDIUM_MAXIMUM_KEY_LENGTH, MEDIUM_MINIMUM_NUMBER_OF_PERMUTATIONS, MEDIUM_MAXIMUM_NUMBER_OF_PERMUTATIONS)) {
                mediumWords.put(key, new ArrayList<>(value));
            }

            if (filterBasedOnLevel(permutaitonEntry, HARD_MINIMUM_KEY_LENGTH, HARD_MAXIMUM_KEY_LENGTH, HARD_MINIMUM_NUMBER_OF_PERMUTATIONS, HARD_MAXIMUM_NUMBER_OF_PERMUTATIONS)) {
                hardWords.put(key, new ArrayList<>(value));
            }
        }

        for (Entry<String, List<String>> filteredWord : hardWords.entrySet()) {
            System.out.println(filteredWord);
        }
    }

    private static boolean filterBasedOnLevel(Entry<String, List<String>> entry, int minKeyLength, int maxKeyLength, int minPermutation, int maxPermutation) {
        return entry.getKey().length() >= minKeyLength && entry.getKey().length() <= maxKeyLength
                && entry.getValue().size() >= minPermutation && entry.getValue().size() <= maxPermutation;
    }
}
