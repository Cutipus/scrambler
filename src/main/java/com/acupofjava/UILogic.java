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
    private final FlawlessVictoryScreen flawlessVictoryScreen = new FlawlessVictoryScreen(this);
    private final EpicDefeatScreen epicDefeatScreen = new EpicDefeatScreen(this);

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
                gameScreen.update(nextChallenge);
            }

            case PlayResult.Defeat(Duration totalTime, WordStat longest, WordStat shortest) -> {
                System.out.println("Player DEFEATED, it took " + totalTime.toSeconds() + " seconds");
                gameOverScreen.update(totalTime, longest, shortest);
                changeScreen(gameOverScreen.getScreen());
            }

            case PlayResult.EpicDefeat(WordStat wordThatDefeatedPlayer) -> {
                System.out.println("Player EPICLY DEFEATED: " + wordThatDefeatedPlayer);
                epicDefeatScreen.update(wordThatDefeatedPlayer);
                changeScreen(epicDefeatScreen.getScreen());
            }

            case PlayResult.Victory(int hpLeft, Duration totalTime, WordStat longest, WordStat shortest) -> {
                System.out.println("Player WON, it took " + totalTime.toSeconds() + " seconds");
                victoryScreen.update(hpLeft, totalTime, longest, shortest);
                changeScreen(victoryScreen.getScreen());
            }

            case PlayResult.FlawlessVictory(Duration totalTime, WordStat longest, WordStat shortest) -> {
                System.out.println("Player WON SPECTACULARLY, it took " + totalTime.toSeconds() + " seconds");
                flawlessVictoryScreen.update(totalTime, longest, shortest);
                changeScreen(flawlessVictoryScreen.getScreen());
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

    public Container getScreen() {
        return gameScreen;
    }

    public void update(int hp, String challengeWord) {
        challengeWordLabel.setText(challengeWord);
        update(hp);
        healthDisplay.repaint();
    }

    public void update(String nextChallenge) {
        challengeWordLabel.setText(nextChallenge);
    }

    public void update(int targetHP) {
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

class VictoryScreen {
    private static final Color NEON_PURPLE = Color.decode("#BC13FE");
    private static final Color SUNSET_YELLOW = Color.decode("#ffbf15");
    private static final Color SUNSET_ORANGE = Color.decode("#f2541B");
    private static final Color SUNSET_RED = Color.decode("#c91853");
    private static final Color SUNSET_RED_PURPLE = Color.decode("#a8186e");
    private static final Color SUNSET_BLUE = Color.decode("#301d7d");

    private UILogic uiLogic;

    JLabel heartsRemaining = Comps.createLabel(NEON_PURPLE, "PLACEHOLDER", 15);
    JLabel longestTimeTakenLabel = Comps.createLabel(NEON_PURPLE, "PLACEHOLDER", 15);
    JLabel shortestTimeTakenLabel = Comps.createLabel(NEON_PURPLE, "PLACEHOLDER", 15);

    Container victoryScreen = Comps.createScreen(SUNSET_YELLOW, SUNSET_RED, Comps.stackVertically(
            Comps.createLabel(SUNSET_RED_PURPLE, "Victory", 40),
            Box.createRigidArea(new Dimension(0, 30)),
            heartsRemaining,
            longestTimeTakenLabel,
            shortestTimeTakenLabel,
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

        longestTimeTakenLabel.setText(String.format("Longest word was '%s', it took %d:%02d.%04d to finish",
                longestTimeTaken.word(),
                longestTimeTaken.timeTaken().toMinutesPart(),
                longestTimeTaken.timeTaken().toSecondsPart(),
                longestTimeTaken.timeTaken().toMillisPart()));

        shortestTimeTakenLabel.setText(String.format("Shortest word was '%s', it took %d:%02d.%04d to finish",
                shortestTimeTaken.word(),
                shortestTimeTaken.timeTaken().toMinutesPart(),
                shortestTimeTaken.timeTaken().toSecondsPart(),
                shortestTimeTaken.timeTaken().toMillisPart()));
    }
}

class FlawlessVictoryScreen {
    private static final Color NEON_PURPLE = Color.decode("#BC13FE");
    private static final Color SOME_GREEN = Color.decode("#32a852");
    private static final Color SUNSET_ORANGE = Color.decode("#f2541B");
    private static final Color SUNSET_RED = Color.decode("#c91853");
    private static final Color SUNSET_RED_PURPLE = Color.decode("#a8186e");
    private static final Color SUNSET_BLUE = Color.decode("#301d7d");

    private JLabel wordThatTookLongestLabel = Comps.createLabel(NEON_PURPLE, "PLACEHOLDER", 15);
    private JLabel wordThatTookShortestLabel = Comps.createLabel(NEON_PURPLE, "PLACEHOLDER", 15);
    private JLabel totalTimeLabel = Comps.createLabel(NEON_PURPLE, "PLACEHOLDER", 15);

    UILogic uiLogic;
    Container flawlessVictoryContainer = Comps.createScreen(SOME_GREEN, SUNSET_RED, Comps.stackVertically(
            Comps.createLabel(SUNSET_RED_PURPLE, "FLAWLESS Victory", 40),
            Box.createRigidArea(new Dimension(0, 30)),
            totalTimeLabel,
            wordThatTookLongestLabel,
            wordThatTookShortestLabel,
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

    public FlawlessVictoryScreen(UILogic uiLogic) {
        this.uiLogic = uiLogic;
    }

    public Container getScreen() {
        return flawlessVictoryContainer;
    }

    public void update(Duration totalTime, WordStat wordThatTookLongest, WordStat wordThatTookShortest) {
        totalTimeLabel.setText(
                String.format("Total: %d:%02d time taken", totalTime.toMinutesPart(), totalTime.toSecondsPart()));

        wordThatTookLongestLabel.setText(String.format("Longest word was '%s', it took %d:%02d.%04d to finish",
                wordThatTookLongest.word(),
                wordThatTookLongest.timeTaken().toMinutesPart(),
                wordThatTookLongest.timeTaken().toSecondsPart(),
                wordThatTookLongest.timeTaken().toMillisPart()));

        wordThatTookShortestLabel.setText(String.format("Shortest word was '%s', it took %d:%02d.%04d to finish",
                wordThatTookShortest.word(),
                wordThatTookShortest.timeTaken().toMinutesPart(),
                wordThatTookShortest.timeTaken().toSecondsPart(),
                wordThatTookShortest.timeTaken().toMillisPart()));
    }
}

class GameOverScreen {
    private static final Color BRIGHT_ORANGE = Color.decode("#ffac00");
    private static final Color OXBLOOD_RED = Color.decode("#4A0000");
    private static final Color NEON_PURPLE = Color.decode("#BC13FE");
    private static final Color SUNSET_PURPLE = Color.decode("#6a0487");

    UILogic uiLogic;

    JLabel totalTimeLabel = Comps.createLabel(OXBLOOD_RED, "PLACEHOLDER", 25);
    JLabel wordThatTookLongestLabel = Comps.createLabel(OXBLOOD_RED, "PLACEHOLDER: ", 25);
    JLabel wordThatTookShortestLabel = Comps.createLabel(OXBLOOD_RED, "PLACEHOLDER", 25);

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
                            e -> uiLogic.onQuitActionPressed()))));

    public GameOverScreen(UILogic uiLogic) {
        this.uiLogic = uiLogic;
    }

    public Container getScreen() {
        return gameOverScreen;
    }

    public void update(Duration totalTime, WordStat wordThatTookLongest, WordStat wordThatTookShortest) {
        totalTimeLabel.setText(String.format("It took you %d:%02d% to solve everything!",
                totalTime.toMinutesPart(),
                totalTime.toSecondsPart()));

        wordThatTookLongestLabel.setText(String.format("Longest word was '%s', it took %d:%02d.%04d to finish",
                wordThatTookLongest.word(),
                wordThatTookLongest.timeTaken().toMinutesPart(),
                wordThatTookLongest.timeTaken().toSecondsPart(),
                wordThatTookLongest.timeTaken().toMillisPart()));

        wordThatTookShortestLabel.setText(String.format("Shortest word was '%s', it took %d:%02d.%04d to finish",
                wordThatTookShortest.word(),
                wordThatTookShortest.timeTaken().toMinutesPart(),
                wordThatTookShortest.timeTaken().toSecondsPart(),
                wordThatTookShortest.timeTaken().toMillisPart()));
    }
}

class EpicDefeatScreen {
    private static final Color BRIGHT_ORANGE = Color.decode("#ffac00");
    private static final Color OXBLOOD_RED = Color.decode("#4A0000");
    private static final Color NEON_PURPLE = Color.decode("#BC13FE");
    private static final Color SUNSET_PURPLE = Color.decode("#6a0487");

    UILogic uiLogic;
    JLabel wordThatDefeatedPlayerLabel = Comps.createLabel(OXBLOOD_RED, "THIS WORD DEFEATED YOU: ", 25);
    Container epicDefeatContainer = Comps.createScreen(OXBLOOD_RED, NEON_PURPLE, Comps.stackVertically(
            Comps.createLabel(BRIGHT_ORANGE, "YOU DIED", 40),
            wordThatDefeatedPlayerLabel,
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

    public EpicDefeatScreen(UILogic uiLogic) {
        this.uiLogic = uiLogic;
    }

    public Container getScreen() {
        return epicDefeatContainer;
    }

    public void update(WordStat wordThatDefeatedPlayer) {
        wordThatDefeatedPlayerLabel.setText(String.format("Longest word was '%s', it took %d:%02d.%04d to finish",
                wordThatDefeatedPlayer.word(),
                wordThatDefeatedPlayer.timeTaken().toMinutesPart(),
                wordThatDefeatedPlayer.timeTaken().toSecondsPart(),
                wordThatDefeatedPlayer.timeTaken().toMillisPart()));
    }
}
