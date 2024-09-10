package com.acupofjava;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class UILogic {
    private static final Color BRIGHT_ORANGE = Color.decode("#ffac00");
    private static final Color OXBLOOD_RED = Color.decode("#4A0000");
    private static final Color NEON_PURPLE = Color.decode("#BC13FE");
    private static final Color SUNSET_YELLOW = Color.decode("#ffbf15");
    private static final Color SUNSET_ORANGE = Color.decode("#f2541B");
    private static final Color SUNSET_RED = Color.decode("#c91853");
    private static final Color SUNSET_RED_PURPLE = Color.decode("#a8186e");
    private static final Color SUNSET_PURPLE = Color.decode("#6a0487");
    private static final Color SUNSET_BLUE = Color.decode("#301d7d");
    private static final Color DARK_BLUE = Color.decode("#303D4A");
    @SuppressWarnings("unused")
    private static final Color DARKER_BLUE = Color.decode("#222B35");
    @SuppressWarnings("unused")
    private static final Color STEEL_BLUE = Color.decode("#8497B0");

    Game game;

    JFrame frame;
    Box healthDisplay;
    JTextField userInput;
    JLabel challengeWordLabel;

    Container gameOverScreen;
    Container gameScreen;
    Container victoryScreen;
    Container currentScreen;

    public UILogic(Game game) {
        this.game = game;
        healthDisplay = Comps.createHealthDisplay(game.getHP());
        challengeWordLabel = Comps.createLabel(Color.RED.darker().darker(), game.generateScramble());
        userInput = Comps.createTextField("");
        frame = Comps.createFrame();
        userInput = Comps.createTextField("");

        gameScreen = Comps.createScreen(Color.CYAN, Color.MAGENTA, Comps.stackVertically(
                healthDisplay,
                Box.createRigidArea(new Dimension(0, 30)),
                challengeWordLabel,
                Box.createRigidArea(new Dimension(0, 30)),
                Comps.stackHorizontally(
                        userInput,
                        Box.createRigidArea(new Dimension(15, 0)),
                        Comps.createButton(
                                DARK_BLUE,
                                Color.MAGENTA,
                                Color.BLACK,
                                "Submit",
                                e -> onSubmitActionPressed())),
                Box.createRigidArea(new Dimension(0, 30)),
                Comps.createButton(
                        DARK_BLUE,
                        Color.MAGENTA,
                        SUNSET_PURPLE,
                        "Quit",
                        e -> onQuitActionPressed())));

        gameOverScreen = Comps.createScreen(OXBLOOD_RED, NEON_PURPLE, Comps.stackVertically(
                Comps.createLabel(BRIGHT_ORANGE, "YOU DIED"),
                Comps.stackHorizontally(
                        Comps.createButton(
                                BRIGHT_ORANGE.darker(),
                                BRIGHT_ORANGE.brighter(),
                                Color.BLACK,
                                "Restart",
                                e -> onRestartActionPressed()),
                        Box.createRigidArea(new Dimension(15, 0)),
                        Comps.createButton(
                                NEON_PURPLE.brighter().brighter(),
                                OXBLOOD_RED.darker(),
                                SUNSET_PURPLE,
                                "Quit",
                                e -> onQuitActionPressed()))));

        victoryScreen = Comps.createScreen(SUNSET_YELLOW, SUNSET_RED, Comps.stackVertically(
                Comps.createLabel(SUNSET_RED_PURPLE, "Victory"),
                Box.createRigidArea(new Dimension(0, 30)),
                Comps.createButton(
                        SUNSET_ORANGE,
                        SUNSET_RED,
                        SUNSET_BLUE,
                        "Restart",
                        e -> onRestartActionPressed()),
                Box.createRigidArea(new Dimension(0, 30)),
                Comps.createButton(
                        SUNSET_ORANGE,
                        SUNSET_RED,
                        SUNSET_BLUE,
                        "Quit",
                        e -> onQuitActionPressed())));

        currentScreen = gameScreen;
        frame.add(currentScreen);

        userInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == '\n')
                    onSubmitActionPressed();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    void onQuitActionPressed() {
        System.exit(0);
    }

    void onRestartActionPressed() {
        game.restart();
        challengeWordLabel.setText(game.generateScramble());
        int HPToBeAdded = game.getHP() - healthDisplay.getComponents().length;
        for (int i = 0; i < HPToBeAdded; i++) {
            healthDisplay.add((Comps.createHeart()));
        }
        changeScreen(gameScreen);
    }

    void onSubmitActionPressed() {
        String userGuess = userInput.getText();
        PlayResult playResult = game.play(userGuess);
        switch (playResult) {
            case PlayResult.Defeat() -> changeScreen(gameOverScreen);

            case PlayResult.Victory() -> changeScreen(victoryScreen);

            case PlayResult.Wrong(int hpLeft) -> {
                for (int i = 0; i < healthDisplay.getComponents().length - hpLeft; i++) {
                    healthDisplay.remove(0);
                }
                healthDisplay.repaint();
                frame.pack();
            }

            case PlayResult.Right(String nextWord) -> challengeWordLabel.setText(nextWord);
        }
    }

    private void changeScreen(Container newScreen) {
        frame.remove(currentScreen);
        currentScreen = newScreen;
        frame.add(newScreen);
        frame.pack();
        frame.repaint();
    }

    public void start() {
        frame.setVisible(true);
    }
}
