package com.acupofjava;

import java.util.Map;
import java.util.Set;

public class App {
    private static final int STARTING_HP = 3;

    public static void main(String[] args) {
        // TODO: use selected_words.json
        final Map<String, Set<String>> words = Map.of(
                "eilv", Set.of("evil", "levi", "live", "veil", "vile", "vlei"),
                "ehllo", Set.of("hello"),
                "oww", Set.of("wow"));
        Game game = new Game(words, STARTING_HP);
        UILogic ui = new UILogic(game);

        ui.start();
    }
}