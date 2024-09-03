package com.acupofjava;

import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import javax.swing.*;

public class App {

    private static final String HEARTSHAPE_PATH = "heartshape-32x32.png";
    private static final int STARTING_HP = 3;
    public static final Color DARKER_BLUE = Color.decode("#222B35");
    public static final Color DARK_BLUE = Color.decode("#303D4A");
    public static final Color STEEL_BLUE = Color.decode("#8497B0");
    public static final Color GAME_OVER_BG_COLOR = new Color(131, 14, 14);
    public static final Color GAME_OVER_LABEL_COLOR = Color.RED;
    public static final Color VICTORY_SCREEN_BG_COLOR = Color.BLUE;
    public static final List<String> words = List.of(
            "hello", "world", "cat", "wow", "live", "evil", "veil", "vile",
            "cat", "act", "no", "on", "bat", "tab");

    public static void main(String[] args) {
        Game game = new Game(words, new Hitpoints(STARTING_HP));

        Box healthDisplay = createHealthDisplay(game.getHP());
        JLabel challengeWordLabel = createLabel(STEEL_BLUE, game.generateScramble());
        JTextField userInput = createTextField("");
        JButton submitButton = createButton("Submit");
        Box gameScreen = createScreen(Color.CYAN, Color.MAGENTA, stackVertically(
                healthDisplay,
                challengeWordLabel,
                stackHorizontally(userInput, submitButton),
                createQuitButton()));

        Box gameOverScreen = createScreen(GAME_OVER_BG_COLOR, Color.RED, stackVertically(
                createLabel(GAME_OVER_LABEL_COLOR, "Game Over"),
                createQuitButton()));

        JButton restartButton = createButton("Restart");
        Box victoryScreen = createScreen(VICTORY_SCREEN_BG_COLOR, Color.RED, stackVertically(
                createLabel(STEEL_BLUE, "Victory"),
                restartButton,
                createQuitButton()));

        Box screenSwitcher = Box.createHorizontalBox();
        screenSwitcher.add(gameScreen);
        JFrame frame = createFrame(screenSwitcher);
        frame.setVisible(true);

        restartButton.addActionListener(clickEvent -> {
            // restart iterator
            // reset hearts
            // change scene
            screenSwitcher.remove(victoryScreen);
            screenSwitcher.add(gameScreen);
            frame.pack();
            screenSwitcher.repaint();
            // reset text
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

    private static JButton createQuitButton() {
        JButton quitButton = createButton("Quit");
        quitButton.setBackground(Color.decode("#06c4c4"));
        quitButton.setForeground(Color.MAGENTA);
        quitButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createBevelBorder(0,
                        STEEL_BLUE, STEEL_BLUE,
                        Color.MAGENTA, Color.MAGENTA),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        quitButton.addActionListener(e -> System.exit(0));
        return quitButton;
    }

    private static JFrame createFrame(Box mainScene) {
        JFrame frame = new JFrame("Scrambler");
        frame.add(mainScene);
        frame.setMinimumSize(new Dimension(300, 500));
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
        heart.setForeground(GAME_OVER_LABEL_COLOR);
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

    private static JButton createButton(String text) {
        JButton button = new JButton(text);
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
