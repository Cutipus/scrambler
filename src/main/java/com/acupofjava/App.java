package com.acupofjava;

import java.util.Map;
import java.util.Set;

public class App {
    private static final int STARTING_HP = 3;
    public static final Map<String, Set<String>> words = Map.of(
            "eilv", Set.of("evil", "levi", "live", "veil", "vile", "vlei"),
            "ehllo", Set.of("hello"),
            "oww", Set.of("wow"));

    public static void main(String[] args) {
        Game game = new Game(words, STARTING_HP);
        UILogic ui = new UILogic(game);

        ui.start();
    }
}