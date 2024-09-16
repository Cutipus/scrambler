package com.acupofjava;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.Duration;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class UILogic {

    private final Game game;
    private final JFrame frame = Comps.createFrame();;

    private Container currentScreen;
    private final GameOverScreen gameOverScreen = new GameOverScreen(this);
    private final GameScreen gameScreen = new GameScreen(this);
    private final VictoryScreen victoryScreen = new VictoryScreen(this);

    public UILogic(Game game) {
        this.game = game;
        gameScreen.update(game.getHP(), game.scrambleWord());
        currentScreen = gameScreen.getScreen();
        frame.add(currentScreen);
    }

    public void onQuitActionPressed() {
        System.exit(0);
    }

    public void onRestartActionPressed() {
        game.restart();
        gameScreen.update(game.getHP(), game.scrambleWord());
        changeScreen(gameScreen.getScreen());
    }

    public void onSubmitActionPressed(String userGuess) {
        PlayResult playResult = game.play(userGuess);
        switch (playResult) {
            case PlayResult.Wrong(int hpLeft) -> {
                System.out.println("Player was wrong, " + hpLeft);
                gameScreen.update(hpLeft);
                frame.pack();
            }

            case PlayResult.Right(String nextChallenge) -> {
                System.out.println("Player was right, " + nextChallenge);
                gameScreen.setChallengeWord(nextChallenge);
            }

            case PlayResult.Defeat(Duration totalTime, WordStat longest, WordStat shortest) -> {
                System.out.println("Player DEFEATED, it took " + totalTime.toSeconds() + " seconds");
                changeScreen(gameOverScreen.getScreen());
            }

            case PlayResult.EpicDefeat(WordStat wordThatDefeatedPlayer) -> {
                System.out.println("Player EPICLY DEFEATED: " + wordThatDefeatedPlayer);
                changeScreen(gameOverScreen.getScreen());
            }

            case PlayResult.Victory(int hpLeft, Duration totalTime, WordStat longest, WordStat shortest) -> {
                System.out.println("Player WON, it took " + totalTime.toSeconds() + " seconds");
                victoryScreen.update(hpLeft, totalTime, longest, shortest);
                changeScreen(victoryScreen.getScreen());
            }

            case PlayResult.FlawlessVictory(Duration totalTime, WordStat longest, WordStat shortest) -> {
                System.out.println("Player WON SPECTACULARLY, it took " + totalTime.toSeconds() + " seconds");
                changeScreen(victoryScreen.getScreen());
            }
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

class VictoryScreen {
    private static final Color NEON_PURPLE = Color.decode("#BC13FE");
    private static final Color SUNSET_YELLOW = Color.decode("#ffbf15");
    private static final Color SUNSET_ORANGE = Color.decode("#f2541B");
    private static final Color SUNSET_RED = Color.decode("#c91853");
    private static final Color SUNSET_RED_PURPLE = Color.decode("#a8186e");
    private static final Color SUNSET_BLUE = Color.decode("#301d7d");

    private UILogic uiLogic;

    JLabel heartsRemaining = Comps.createLabel(NEON_PURPLE, "PLACEHOLDER", 15);
    JLabel longest = Comps.createLabel(NEON_PURPLE, "PLACEHOLDER", 15);
    JLabel shortest = Comps.createLabel(NEON_PURPLE, "PLACEHOLDER", 15);

    Container victoryScreen = Comps.createScreen(SUNSET_YELLOW, SUNSET_RED, Comps.stackVertically(
            Comps.createLabel(SUNSET_RED_PURPLE, "Victory", 40),
            Box.createRigidArea(new Dimension(0, 30)),
            heartsRemaining,
            longest,
            shortest,
            Box.createRigidArea(new Dimension(0, 30)),
            Comps.createButton(
                    SUNSET_ORANGE,
                    SUNSET_RED,
                    SUNSET_BLUE,
                    "Restart",
                    e -> uiLogic.onRestartActionPressed()),
            Box.createRigidArea(new Dimension(0, 30)),
            Comps.createButton(
                    SUNSET_ORANGE,
                    SUNSET_RED,
                    SUNSET_BLUE,
                    "Quit",
                    e -> uiLogic.onQuitActionPressed())));

    public VictoryScreen(UILogic uiLogic) {
        this.uiLogic = uiLogic;
    }

    public Container getScreen() {
        return victoryScreen;
    }

    public void update(int hpLeft, Duration timeTaken, WordStat longestTimeTaken, WordStat shortestTimeTaken) {
        heartsRemaining.setText(String.format("you had %d hearts remaining", hpLeft));
        longest.setText(String.format("Longest: %d:%02d to finish",
                longestTimeTaken.timeTaken().toMinutesPart(), longestTimeTaken.timeTaken().toSecondsPart()));
        shortest.setText(String.format("Shortest: %d:%02d to finish",
                shortestTimeTaken.timeTaken().toMinutesPart(), shortestTimeTaken.timeTaken().toSecondsPart()));
    }
}

class GameScreen {

    private static final Color SUNSET_PURPLE = Color.decode("#6a0487");
    private static final Color DARK_BLUE = Color.decode("#303D4A");

    Box healthDisplay = Comps.createHealthDisplay(0);
    JTextField userInput = Comps.createTextField("");
    JLabel challengeWordLabel = Comps.createLabel(Color.RED.darker().darker(), "PLACEHOLDER", 40);;

    UILogic uiLogic;
    Container gameScreen = Comps.createScreen(Color.CYAN, Color.MAGENTA, Comps.stackVertically(
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
                            e -> uiLogic.onSubmitActionPressed(userInput.getText()))),
            Box.createRigidArea(new Dimension(0, 30)),
            Comps.createButton(
                    DARK_BLUE,
                    Color.MAGENTA,
                    SUNSET_PURPLE,
                    "Quit",
                    e -> uiLogic.onQuitActionPressed())));

    public GameScreen(UILogic uiLogic) {
        this.uiLogic = uiLogic;
        userInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    uiLogic.onSubmitActionPressed(userInput.getText());
                    userInput.setText("");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    public void setChallengeWord(String nextChallenge) {
        challengeWordLabel.setText(nextChallenge);
    }

    public Container getScreen() {
        return gameScreen;
    }

    public void update(int hp, String challengeWord) {
        challengeWordLabel.setText(challengeWord);
        updateHP(hp);
        healthDisplay.repaint();
    }

    public void update(int hpLeft) {
        updateHP(hpLeft);
    }

    private void updateHP(int targetHP) {
        int hpDifference = targetHP - healthDisplay.getComponents().length;
        if (hpDifference > 0) {
            for (int i = hpDifference; i > 0; i--) {
                healthDisplay.add((Comps.createHeart()));
            }
        } else if (hpDifference < 0) {
            for (int i = hpDifference; i < 0; i++) {
                healthDisplay.remove(0);
            }
        } else {
        }
    }
}

class GameOverScreen {
    private static final Color BRIGHT_ORANGE = Color.decode("#ffac00");
    private static final Color OXBLOOD_RED = Color.decode("#4A0000");
    private static final Color NEON_PURPLE = Color.decode("#BC13FE");
    private static final Color SUNSET_PURPLE = Color.decode("#6a0487");

    UILogic uiLogic;
    Container gameOverScreen = Comps.createScreen(OXBLOOD_RED, NEON_PURPLE, Comps.stackVertically(
            Comps.createLabel(BRIGHT_ORANGE, "YOU DIED", 40),
            Comps.stackHorizontally(
                    Comps.createButton(
                            BRIGHT_ORANGE.darker(),
                            BRIGHT_ORANGE.brighter(),
                            Color.BLACK,
                            "Restart",
                            e -> uiLogic.onRestartActionPressed()),
                    Box.createRigidArea(new Dimension(15, 0)),
                    Comps.createButton(
                            NEON_PURPLE.brighter().brighter(),
                            OXBLOOD_RED.darker(),
                            SUNSET_PURPLE,
                            "Quit",
                            e -> uiLogic.onQuitActionPressed()))));;

    public GameOverScreen(UILogic uiLogic) {
        this.uiLogic = uiLogic;
    }

    public Container getScreen() {
        return gameOverScreen;
    }
}