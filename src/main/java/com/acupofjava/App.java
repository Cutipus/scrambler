package com.acupofjava;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;

public class App {
    private static final int STARTING_HP = 3;

    public static void main(String[] args) throws ParseException, IOException {
        final Map<String, Set<String>> words = loadJsonFile("selected_words.json");
        Game game = new Game(words, STARTING_HP);
        UILogic ui = new UILogic(game);
        ui.start();
    }

    // file --> fileInputStream --> InputStreamReader --> Json Parser ==> Json Object
    public static Map<String, Set<String>> loadJsonFile(String fileName) throws FileNotFoundException, IOException, ParseException {
        Map<String, Set<String>> wordMap = new HashMap<>();
        try (InputStream inputStream = App.class.getResourceAsStream(fileName);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            JSONParser parser = new JSONParser();
            @SuppressWarnings("unchecked")
            Map<String, List<String>> parseOutput = (Map<String, List<String>>) parser.parse(inputStreamReader);
            for (Entry<String, List<String>> entry : parseOutput.entrySet()) {
                wordMap.put(entry.getKey(), new HashSet<>(entry.getValue()));
            }
        }
        return wordMap;
    }
}