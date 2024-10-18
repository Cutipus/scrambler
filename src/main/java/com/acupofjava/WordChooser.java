package com.acupofjava;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.util.*;

public class WordChooser {
    public static void main(String[] args) {
        File selectedWordsFile = FileSystems.getDefault()
                .getPath("src", "main", "resources", "com", "acupofjava", "selected_words.json")
                .toFile();
        File bundledWordsFile = FileSystems.getDefault()
                .getPath("src", "main", "resources", "com", "acupofjava", "bundled_words.json")
                .toFile();

        HashMap<String, List<String>> allTheWords = loadJsonFile(bundledWordsFile);
        HashMap<String, List<String>> selectedWords = loadJsonFile(selectedWordsFile);

        if (Objects.isNull(allTheWords)) {
            System.out.println("Couldn't load all the words!");
            return;
        }

        if (Objects.isNull(selectedWords)) {
            System.out.println("Couldn't load selected words!");
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Type word: ");
                String userInputWord;
                try {
                    userInputWord = scanner.nextLine();
                } catch (NoSuchElementException e) {
                    break;
                }

                String sortedUserInputWord = sortString(userInputWord);

                if (selectedWords.containsKey(sortedUserInputWord)) {
                    System.out.println("Word already added!");
                    continue;
                }

                if (!allTheWords.containsKey(sortedUserInputWord)) {
                    System.out.println("Couldn't find this word: " + userInputWord);
                    continue;
                }

                List<String> permutations = allTheWords.get(sortedUserInputWord);
                System.out.print(String.format("%s | cancel? (n/!): ", permutations));

                // confirm
                try {
                    String confirmationInput = scanner.nextLine();
                    if (confirmationInput.startsWith("n") || confirmationInput.startsWith("!")) {
                        System.out.println("cancelled");
                        continue;
                    }
                } catch (NoSuchElementException e) {
                    break;
                }

                selectedWords.put(sortedUserInputWord, permutations);
            }
        }

        saveToJsonFile(selectedWords, selectedWordsFile);
    }

    private static String sortString(String userInputWord) {
        char[] userInputLetters = userInputWord.toCharArray();
        Arrays.sort(userInputLetters);
        return String.valueOf(userInputLetters);
    }

    public static HashMap<String, List<String>> loadJsonFile(File selectedWordsFile) {
        HashMap<String, List<String>> JSONWords;
        try (FileInputStream fileInputStream = new FileInputStream(selectedWordsFile);
                InputStreamReader reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
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

    private static void saveToJsonFile(HashMap<String, List<String>> selectedWords, File selectedWordsFile) {
        try (FileOutputStream outputStream = new FileOutputStream(selectedWordsFile);
                OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {
            JSONObject.writeJSONString(selectedWords, writer);
        } catch (IOException e) {
            System.out.println("Couldn't write the file! ");
            e.printStackTrace();
        }
    }

}
