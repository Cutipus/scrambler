package com.acupofjava;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.*;

public class App {
    private static final int STARTING_HP = 3;

    public static final Color BRIGHT_ORANGE = Color.decode("#ffac00");
    public static final Color OXBLOOD_RED = Color.decode("#4A0000");
    public static final Color NEON_PURPLE = Color.decode("#BC13FE");

    private static final Color SUNSET_YELLOW = Color.decode("#ffbf15");
    private static final Color SUNSET_ORANGE = Color.decode("#f2541B");
    private static final Color SUNSET_RED = Color.decode("#c91853");
    private static final Color SUNSET_RED_PURPLE = Color.decode("#a8186e");
    private static final Color SUNSET_PURPLE = Color.decode("#6a0487");
    private static final Color SUNSET_BLUE = Color.decode("#301d7d");
    public static final Color DARKER_BLUE = Color.decode("#222B35");
    public static final Color DARK_BLUE = Color.decode("#303D4A");
    public static final Color STEEL_BLUE = Color.decode("#8497B0");
    public static final Color VICTORY_SCREEN_BG_COLOR = Color.BLUE;
    public static final List<String> words = List.of(
            "hello", "world", "cat", "wow", "live", "evil", "veil", "vile",
            "cat", "act", "no", "on", "bat", "tab");

    public static void main(String[] args) {
        Game game = new Game(words, new Hitpoints(STARTING_HP));

        Box healthDisplay = Comps.createHealthDisplay(game.getHP());
        JLabel challengeWordLabel = Comps.createLabel(Color.RED.darker().darker(), game.generateScramble());
        JTextField userInput = Comps.createTextField("");
        JButton submitButton = Comps.createButton(
                DARK_BLUE,
                Color.MAGENTA,
                Color.BLACK,
                "Submit");
        Box gameScreen = Comps.createScreen(Color.CYAN, Color.MAGENTA, Comps.stackVertically(
                healthDisplay,
                challengeWordLabel,
                Comps.stackHorizontally(userInput, submitButton),
                Comps.createQuitButton(
                        DARK_BLUE,
                        Color.MAGENTA,
                        SUNSET_PURPLE,
                        "Quit")));

        Box gameOverScreen = Comps.createScreen(OXBLOOD_RED, NEON_PURPLE,
                Comps.stackVertically(
                        Comps.createLabel(BRIGHT_ORANGE, "YOU DIED"),
                        Comps.stackHorizontally(
                                Comps.createButton(
                                        BRIGHT_ORANGE.darker(),
                                        BRIGHT_ORANGE.brighter(),
                                        Color.BLACK,
                                        "Restart"),
                                Comps.createQuitButton(
                                        NEON_PURPLE.brighter().brighter(),
                                        OXBLOOD_RED.darker(),
                                        SUNSET_PURPLE,
                                        "Quit"))));

        JButton restartButton = Comps.createButton(
                SUNSET_YELLOW,
                SUNSET_RED,
                SUNSET_PURPLE,
                "Restart");
        Box victoryScreen = Comps.createScreen(SUNSET_YELLOW, SUNSET_RED, Comps.stackVertically(
                Comps.createLabel(SUNSET_RED_PURPLE, "Victory"),
                restartButton,
                Comps.createQuitButton(
                        SUNSET_ORANGE,
                        SUNSET_RED,
                        SUNSET_BLUE,
                        "Quit")));

        Box screenSwitcher = Box.createHorizontalBox();
        screenSwitcher.add(gameScreen);
        JFrame frame = Comps.createFrame(screenSwitcher);
        frame.setVisible(true);

        restartButton.addActionListener(clickEvent -> {
            screenSwitcher.remove(victoryScreen);
            screenSwitcher.add(gameScreen);
            frame.pack();
            screenSwitcher.repaint();
        });

        // Checks if user entered the correct answer and responds accordingly
        submitButton.addActionListener(userSubmit(game, healthDisplay, challengeWordLabel, userInput, gameScreen,
                gameOverScreen, victoryScreen,
                screenSwitcher, frame));

        userInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == '\n')
                    userSubmit(game, healthDisplay, challengeWordLabel, userInput, gameScreen,
                            gameOverScreen, victoryScreen,
                            screenSwitcher, frame).actionPerformed(null);
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

        });

    }

    private static ActionListener userSubmit(Game game, Box healthDisplay, JLabel challengeWordLabel,
            JTextField userInput, Box gameScreen, Box gameOverScreen, Box victoryScreen, Box screenSwitcher,
            JFrame frame) {
        return e -> {
            String userGuess = userInput.getText();
            PlayResult playResult = game.play(userGuess);
            switch (playResult) {
                case PlayResult.Defeat() -> {
                    System.out.println("You've lost the game");
                    screenSwitcher.remove(gameScreen);
                    screenSwitcher.add(gameOverScreen);
                    frame.pack();
                    screenSwitcher.repaint();
                }
                case PlayResult.Victory() -> {
                    screenSwitcher.remove(gameScreen);
                    screenSwitcher.add(victoryScreen);
                    frame.pack();
                    screenSwitcher.repaint();
                }
                case PlayResult.Wrong(int hpLeft) -> {
                    for (int i = 0; i < healthDisplay.getComponents().length - hpLeft; i++) {
                        healthDisplay.remove(0);
                    }
                    healthDisplay.repaint();
                }
                case PlayResult.Right(String nextWord) -> {
                    challengeWordLabel.setText(nextWord);
                }
            }
        };
    }

}
