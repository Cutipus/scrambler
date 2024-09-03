package com.acupofjava;

import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import javax.swing.*;

public class App {
    private static final String HEARTSHAPE_PATH = "heartshape-32x32.png";
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

        Box healthDisplay = createHealthDisplay(game.getHP());
        JLabel challengeWordLabel = createLabel(Color.RED.darker().darker(), game.generateScramble());
        JTextField userInput = createTextField("");
        JButton submitButton = createButton(DARK_BLUE, Color.MAGENTA, Color.BLACK, "Submit");
        Box gameScreen = createScreen(Color.CYAN, Color.MAGENTA, stackVertically(
                healthDisplay,
                challengeWordLabel,
                stackHorizontally(userInput, submitButton),
                createQuitButton(DARK_BLUE, Color.MAGENTA, SUNSET_PURPLE, "Quit")));

        Box gameOverScreen = createScreen(OXBLOOD_RED, NEON_PURPLE,
                stackVertically(
                        createLabel(BRIGHT_ORANGE, "YOU DIED"),
                        stackHorizontally(
                                createButton(BRIGHT_ORANGE.darker(), BRIGHT_ORANGE.brighter(), Color.BLACK,
                                        "Restart"),
                                createQuitButton(NEON_PURPLE.brighter().brighter(), OXBLOOD_RED.darker(), SUNSET_PURPLE,
                                        "Quit"))));

        JButton restartButton = createButton(SUNSET_YELLOW, SUNSET_RED, SUNSET_PURPLE, "Restart");
        Box victoryScreen = createScreen(SUNSET_YELLOW, SUNSET_RED, stackVertically(
                createLabel(SUNSET_RED_PURPLE, "Victory"),
                restartButton,
                createQuitButton(SUNSET_ORANGE, SUNSET_RED, SUNSET_BLUE, "Quit")));

        Box screenSwitcher = Box.createHorizontalBox();
        screenSwitcher.add(victoryScreen);
        JFrame frame = createFrame(screenSwitcher);
        frame.setVisible(true);

        restartButton.addActionListener(clickEvent -> {
            screenSwitcher.remove(victoryScreen);
            screenSwitcher.add(gameScreen);
            frame.pack();
            screenSwitcher.repaint();
        });

        // Checks if user entered the correct answer and responds accordingly
        submitButton.addActionListener(e -> {
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
        });
    }

    private static JButton createQuitButton(Color topColor, Color bottomColor, Color textColor, String text) {
        JButton quitButton = new GradiantButton(topColor, bottomColor, text);
        quitButton.setForeground(textColor);
        quitButton.setFocusable(false);
        quitButton.setFont(new Font("Arial", Font.BOLD, 15));
        quitButton.addActionListener(e -> System.exit(0));
        return quitButton;
    }

    private static JFrame createFrame(Box mainScene) {
        JFrame frame = new JFrame("Scrambler");
        frame.add(mainScene);
        frame.setMinimumSize(new Dimension(400, 500));
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }

    private static Box createHealthDisplay(int hp) {
        var hearts = new JComponent[hp];
        for (int i = 0; i < hearts.length; i++)
            hearts[i] = createHeart();
        return stackHorizontally(hearts);
    }

    private static JComponent createHeart() {
        URL resource;
        if (Objects.isNull(resource = App.class.getResource(HEARTSHAPE_PATH)))
            throw new RuntimeException("Heart icon not found: " + HEARTSHAPE_PATH);
        var heart = new JLabel(new ImageIcon(resource));
        return heart;
    }

    private static JLabel createLabel(Color textColor, String text) {
        JLabel gameOverText = new JLabel(text);
        gameOverText.setForeground(textColor);
        gameOverText.setFont(new Font("Arial", Font.BOLD, 40));
        return gameOverText;
    }

    private static JTextField createTextField(String initialText) {
        JTextField userInput = new JTextField(initialText);
        userInput.setMaximumSize(new Dimension(150, 25));
        return userInput;
    }

    private static JButton createButton(Color topColor, Color bottomColor, Color textColor, String text) {
        JButton button = new GradiantButton(topColor, bottomColor, text);
        button.setForeground(textColor);
        button.setFocusable(false);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        return button;
    }

    private static Box createScreen(Color topColor, Color bottomColor, JComponent collectionOfComponents) {
        Box screen = centerHorizontally(centerVertically(collectionOfComponents));
        GradiantPanel backgroundScreen = new GradiantPanel(topColor, bottomColor);
        backgroundScreen.add(screen);
        return backgroundScreen;
    }

    private static Box stackHorizontally(JComponent... components) {
        Box box = Box.createHorizontalBox();
        for (JComponent component : components)
            box.add(centerVertically(component));
        return box;
    }

    private static Box stackVertically(JComponent... components) {
        Box outerBox = Box.createVerticalBox();
        for (JComponent component : components)
            outerBox.add(centerHorizontally(component));
        return outerBox;
    }

    private static Box centerHorizontally(JComponent component) {
        Box gameOverBox = Box.createHorizontalBox();
        gameOverBox.add(Box.createGlue());
        gameOverBox.add(component);
        gameOverBox.add(Box.createGlue());
        return gameOverBox;
    }

    private static Box centerVertically(JComponent component) {
        Box innerBox = Box.createVerticalBox();
        innerBox.add(Box.createGlue());
        innerBox.add(component);
        innerBox.add(Box.createGlue());
        return innerBox;
    }
}
