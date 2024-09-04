package com.acupofjava;

import java.util.List;

public class App {
    private static final int STARTING_HP = 3;
    public static final List<String> words = List.of(
            "hello", "world", "cat", "wow", "live", "evil", "veil", "vile",
            "cat", "act", "no", "on", "bat", "tab");

    public static void main(String[] args) {
        Game game = new Game(words, new Hitpoints(STARTING_HP));
        UILogic ui = new UILogic(game);

        ui.start();
    }
}