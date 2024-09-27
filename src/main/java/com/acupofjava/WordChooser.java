package com.acupofjava;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class WordChooser {

    public static void main(String[] args) {
        final HashMap<String, List<String>> loadedDictionary;
        try {
            loadedDictionary = loadDictionary();
        } catch (IOException | ParseException e) {
            System.out.println("Can't load dictionary");
            e.printStackTrace();
            return;
        }


        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Type word: ");
            String userInputWord = scanner.nextLine();
            if (userInputWord.equals("q")) {
                break;
            }

            char[] userInputLetters = userInputWord.toCharArray();
            Arrays.sort(userInputLetters);
            String sortedUserInputWord = String.valueOf(userInputLetters);

            System.out.println(loadedDictionary.get(sortedUserInputWord));


        }
    }

    public static HashMap<String, List<String>> loadDictionary() throws IOException, ParseException {
        HashMap<String, List<String>> JSONWords;
        try (
                InputStream fileInputStream = WordBundleFilter.class.getResourceAsStream("bundled_words.json");
                InputStreamReader reader = new InputStreamReader(fileInputStream, "UTF-8")) {
            JSONParser parser = new JSONParser();
            @SuppressWarnings("unchecked")
            HashMap<String, List<String>> _JSON = (HashMap<String, List<String>>) parser.parse(reader);
            JSONWords = _JSON;
        }

        // put in temporary hashmap
        HashMap<String, List<String>> unCuratedWords = new HashMap<>();
        JSONWords.forEach((key, value) -> unCuratedWords.put(key, new ArrayList<>(value)));
        return unCuratedWords;
    }

}
