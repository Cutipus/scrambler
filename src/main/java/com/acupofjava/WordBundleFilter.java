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
        HashMap<String, List<String>> words = (HashMap<String, List<String>>) parser.parse(reader);

        HashMap<String, List<String>> filteredWord = new HashMap<>();
        for (Entry<String, List<String>> permutaitonEntry : words.entrySet()) {
            String key = permutaitonEntry.getKey();
            List<String> value = permutaitonEntry.getValue();
            if (key.length() <= 3) {
                filteredWord.put(key, new ArrayList<>(value));
            }
        }


        // this is how you find all permutations of a specific word:

        // String unsortedWord = "telephone";
        // char[] wordChars = unsortedWord.toCharArray();
        // Arrays.sort(wordChars);
        // String sortedWord = new String(wordChars);
        // System.out.println(sortedWord);

        // switch (words.get(sortedWord)) {
        // case null -> {
        // System.out.println("no such word");
        // }

        // case List<String> validWords -> {
        // System.out.println("these are the words: " + String.join(", ", validWords));
        // }
        // }

    }
}
