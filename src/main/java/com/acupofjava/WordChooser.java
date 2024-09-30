package com.acupofjava;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class WordChooser {
    public static void main(String[] args) {
        HashMap<String, List<String>> loadedDictionary = loadDictionary();
        HashMap<String, List<String>> selectedWords = new HashMap<>();

        if (Objects.isNull(loadedDictionary)) {
            System.err.println("Couldn't load it!");
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.err.print("Type word: ");
                String userInputWord;
                try {
                    userInputWord = scanner.nextLine();
                } catch (NoSuchElementException e) {
                    break;
                }

                char[] userInputLetters = userInputWord.toCharArray();
                Arrays.sort(userInputLetters);
                String sortedUserInputWord = String.valueOf(userInputLetters);

                if (!loadedDictionary.containsKey(sortedUserInputWord)) {
                    System.err.println("No such word, quitting!");
                    break;
                }

                List<String> wordPermutations = loadedDictionary.get(sortedUserInputWord);
                selectedWords.put(sortedUserInputWord, wordPermutations);
            }
        }
    }

    public static HashMap<String, List<String>> loadDictionary() {
        HashMap<String, List<String>> JSONWords;
        try (
                InputStream fileInputStream = WordBundleFilter.class.getResourceAsStream("bundled_words.json");
                InputStreamReader reader = new InputStreamReader(fileInputStream, "UTF-8")) {
            JSONParser parser = new JSONParser();
            @SuppressWarnings("unchecked")
            HashMap<String, List<String>> _JSON = (HashMap<String, List<String>>) parser.parse(reader);
            JSONWords = _JSON;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }

        // put in temporary hashmap
        HashMap<String, List<String>> unCuratedWords = new HashMap<>();
        JSONWords.forEach((key, value) -> unCuratedWords.put(key, new ArrayList<>(value)));
        return unCuratedWords;
    }
}
