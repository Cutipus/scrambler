package com.acupofjava;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class WordBundleFilter {
    public static void main(String[] args) throws IOException, ParseException, ClassNotFoundException {
        // get file descriptor
        File uncuratedFile = FileSystems.getDefault()
                .getPath("src", "main", "resources", "com", "acupofjava", "uncurated.obj")
                .toFile();

        File curatedFile = FileSystems.getDefault()
                .getPath("src", "main", "resources", "com", "acupofjava", "curated.obj")
                .toFile();

        // create file
        if (uncuratedFile.createNewFile() || curatedFile.createNewFile()) {

            System.out.println("Created new file from JSON!");
            // load json
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

            // write to file
            try (
                    FileOutputStream outputStream = new FileOutputStream(uncuratedFile);
                    ObjectOutputStream objOutputStream = new ObjectOutputStream(outputStream)) {
                objOutputStream.writeObject(JSONWords);
            }
            try (
                    FileOutputStream outputStream = new FileOutputStream(curatedFile);
                    ObjectOutputStream objOutputStream = new ObjectOutputStream(outputStream)) {
                objOutputStream.writeObject(new HashMap<String, List<String>>());
            }
        } else {
            System.out.println("Cached file detected, reading from that one!");

            HashMap<String, List<String>> loadedUncuratedFile;
            try (
                    FileInputStream inputStream = new FileInputStream(uncuratedFile);
                    ObjectInputStream objInputStream = new ObjectInputStream(inputStream)) {
                @SuppressWarnings("unchecked")
                var _loaded = (HashMap<String, List<String>>) objInputStream.readObject();
                loadedUncuratedFile = _loaded;
            }

            HashMap<String, List<String>> loadedCuratedFile;
            try (
                    FileInputStream inputStream = new FileInputStream(curatedFile);
                    ObjectInputStream objInputStream = new ObjectInputStream(inputStream)) {
                @SuppressWarnings("unchecked")
                var _loaded = (HashMap<String, List<String>>) objInputStream.readObject();
                loadedCuratedFile = _loaded;
            }

            HashMap<String, List<String>> uncurated = new HashMap<>(loadedUncuratedFile);
            HashMap<String, List<String>> curated = new HashMap<>(loadedCuratedFile);

            for (Entry<String, List<String>> entry : loadedUncuratedFile.entrySet()) {
                var key = entry.getKey();
                var words = entry.getValue();

                System.out.println(String.format("Save [%s: %s]? Y/N/Q", key, words));
                @SuppressWarnings("resource")
                Scanner inpScanner = new Scanner(System.in);
                String input = inpScanner.nextLine().toLowerCase();
                if (input.equals("y")) {
                    System.out.println("I put it!");
                    curated.put(key, words);
                    uncurated.remove(key);
                } else if (input.equals("n")) {
                    System.out.println("I removed it!");
                    uncurated.remove(key);
                } else {
                    break;
                }
            }
            try (
                    FileOutputStream outputStream = new FileOutputStream(uncuratedFile);
                    ObjectOutputStream objOutputStream = new ObjectOutputStream(outputStream)) {
                objOutputStream.writeObject(uncurated);
            }
            try (
                    FileOutputStream outputStream = new FileOutputStream(curatedFile);
                    ObjectOutputStream objOutputStream = new ObjectOutputStream(outputStream)) {
                objOutputStream.writeObject(curated);
            }
            System.exit(0);
        }
    }

    public static void filterWords(HashMap<String, List<String>> JSONWords) {
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
            if (filterBasedOnLevel(permutaitonEntry, EASY_MINIMUM_KEY_LENGTH, EASY_MAXIMUM_KEY_LENGTH,
                    EASY_MINIMUM_NUMBER_OF_PERMUTATIONS, EASY_MAXIMUM_NUMBER_OF_PERMUTATIONS)) {
                easyWords.put(key, new ArrayList<>(value));
            }

            if (filterBasedOnLevel(permutaitonEntry, MEDIUM_MINIMUM_KEY_LENGTH, MEDIUM_MAXIMUM_KEY_LENGTH,
                    MEDIUM_MINIMUM_NUMBER_OF_PERMUTATIONS, MEDIUM_MAXIMUM_NUMBER_OF_PERMUTATIONS)) {
                mediumWords.put(key, new ArrayList<>(value));
            }

            if (filterBasedOnLevel(permutaitonEntry, HARD_MINIMUM_KEY_LENGTH, HARD_MAXIMUM_KEY_LENGTH,
                    HARD_MINIMUM_NUMBER_OF_PERMUTATIONS, HARD_MAXIMUM_NUMBER_OF_PERMUTATIONS)) {
                hardWords.put(key, new ArrayList<>(value));
            }
        }

        for (Entry<String, List<String>> filteredWord : easyWords.entrySet()) {
            System.out.println(filteredWord);
        }
    }

    private static boolean filterBasedOnLevel(Entry<String, List<String>> entry, int minKeyLength, int maxKeyLength,
            int minPermutation, int maxPermutation) {
        return entry.getKey().length() >= minKeyLength && entry.getKey().length() <= maxKeyLength
                && entry.getValue().size() >= minPermutation && entry.getValue().size() <= maxPermutation;
    }
}
