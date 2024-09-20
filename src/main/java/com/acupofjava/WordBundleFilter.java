package com.acupofjava;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
// import java.util.Arrays;
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

        // find all permutation sets that are larger than 10
        for (Entry<String, List<String>> permutaitonEntry : words.entrySet()) {
            if (permutaitonEntry.getValue().size() > 10) {
                System.out.println(permutaitonEntry);
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
