package com.acupofjava;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.*;

public class App {

    public static final Color BACKGROUND = new Color(25, 25, 61);
    public static final List<String> words = List.of(
            "hello", "world", "cat", "wow", "live", "evil", "veil", "vile",
            "cat", "act", "no", "on", "bat", "tab");

    public static void main(String[] args) {
        List<ScrambleOption> challengeList = validWordsPermutation(words.stream()).entrySet().stream()
                .map(ScrambleOption::fromEntry)
                .filter(scrambleOption -> {
                    try {
                        scrambleOption.scramble(0);
                        return true;
                    } catch (ImpossiblePermutationException e) {
                        return false;
                    }
                })
                .toList();

        for (ScrambleOption s : challengeList) {
            System.out.println(s);
        }

        Iterator<ScrambleOption> challenges = challengeList.iterator();

        ScrambleOption firstScramble;
        if (challenges.hasNext())
            firstScramble = challenges.next();
        else {
            throw new NoSuchElementException("No challenges found!");
        }

        AtomicReference<ScrambleOption> currentScrambleOption = new AtomicReference<>(firstScramble);
        Hitpoints hp = new Hitpoints(3);
        JLabel scrambledWordLabel = createScrambledWordLabel(firstScramble.scramble(0));

        // Text field to take user input
        TextField userInput = createUserInputTextField();
        JButton submitButton = createButton("Submit");
        JButton quitButton = createButton("Quit");
        // Displays the number of hearts
        Box healthDisplay = createHealthDisplay(3);
        // Displays scrambled word
        Box wordLabel = createWordLabel(scrambledWordLabel);
        // Displays text field for user input and submit button
        Box inputArea = createInputArea(userInput, submitButton);
        // Displays number of hearts, scrambled word, text field for user input and
        // submit / quit button
        Box gameScene = createGameScreen(healthDisplay, wordLabel, inputArea, quitButton);
        JLabel gameOverText = new JLabel("Game Over");
        gameOverText.setForeground(Color.RED);
        gameOverText.setFont(new Font("Arial", Font.BOLD, 40));
        Box gameOverScreen = createGameOverScreen(gameOverText, quitButton);

        Box mainScene = Box.createHorizontalBox();
        // Adds all the displays (number of hearts, scrambled word, text field for user
        // input and submit button) to mainscene
        mainScene.add(gameScene);
        JFrame frame = createFrame(mainScene);
        frame.setVisible(true);

        // Checks if user entered the correct answer and responds accordingly
        submitButton.addActionListener(clickEvent -> {
            String userInputText = userInput.getText();
            boolean match = currentScrambleOption.get().matches(userInputText);

            if (match) {
                // VICTORY
                if (challenges.hasNext()) {
                    currentScrambleOption.set(challenges.next());
                    System.out.println("WIN");
                    scrambledWordLabel.setText(currentScrambleOption.get().scramble(0));
                } else {
                    submitButton.setEnabled(false);
                    mainScene.remove(gameScene);
                    mainScene.add(gameOverScreen);
                    frame.pack();
                    mainScene.repaint();
                }

            } else {
                switch (hp.hit()) {
                    case ALIVE -> {
                        healthDisplay.remove(0);
                        healthDisplay.repaint();
                    }
                    case DEAD -> {
                        System.out.println("You've lost the game");
                        submitButton.setEnabled(false);
                        mainScene.remove(gameScene);
                        mainScene.add(gameOverScreen);
                        frame.pack();
                        mainScene.repaint();

                    }
                }
            }
        });

        // Quits game
        quitButton.addActionListener(e -> System.exit(0));
    }

    // Adds specified number of hearts as health display
    private static Box createHealthDisplay(int hp) {
        Box healthDisplay = Box.createHorizontalBox();
        for (int i = 0; i < hp; i++)
            healthDisplay.add(createHeart());
        return healthDisplay;
    }

    // Main window of the game
    private static JFrame createFrame(Box mainScene) {
        JFrame frame = new JFrame("Scrambler");
        frame.add(mainScene);
        frame.setMinimumSize(new Dimension(300, 500));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }

    private static JComponent createHeart() {
        var heart = new JLabel("❤️");
        heart.setForeground(Color.RED);
        return heart;
    }

    private static Box createInputArea(TextField userInput, JButton submitButton) {
        Box bottomBox = Box.createHorizontalBox();
        bottomBox.add(Box.createHorizontalStrut(10));
        bottomBox.add(userInput);
        bottomBox.add(Box.createHorizontalStrut(10));
        bottomBox.add(submitButton);
        bottomBox.add(Box.createHorizontalStrut(10));
        return bottomBox;
    }

    private static Box createWordLabel(JLabel scrambledWordLabel) {
        Box topBox = Box.createHorizontalBox();
        topBox.add(Box.createHorizontalStrut(10));
        topBox.add(scrambledWordLabel);
        topBox.add(Box.createHorizontalStrut(10));
        return topBox;
    }

    private static JLabel createScrambledWordLabel(String text) {
        JLabel scrambledWordLabel = new JLabel(text);
        scrambledWordLabel.setFont(new Font("Arial", Font.BOLD, 40));
        scrambledWordLabel.setForeground(new Color(154, 201, 79));
        return scrambledWordLabel;
    }

    private static JButton createButton(String title) {
        JButton button = new JButton(title);
        button.setFocusable(false);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        return button;
    }

    private static Box createSceneTemplate(JComponent collectionOfComponents, Color backgroundColor) {
        Box innerBox = Box.createVerticalBox();
        innerBox.add(Box.createGlue());
        innerBox.add(collectionOfComponents);
        innerBox.add(Box.createGlue());
        Box outerBox = Box.createHorizontalBox();
        outerBox.add(Box.createGlue());
        outerBox.add(innerBox);
        outerBox.add(Box.createGlue());
        outerBox.setOpaque(true);
        outerBox.setBackground(backgroundColor);
        return outerBox;
    }

    private static Box createGameScreen(JComponent healthBox, JComponent wordBox, JComponent inputBox,
                                        JComponent quitButton) {
        Box outerBox = Box.createVerticalBox();
        outerBox.add(Box.createVerticalStrut(100));
        outerBox.add(healthBox);
        outerBox.add(wordBox);
        outerBox.add(inputBox);
        outerBox.add(quitButton);
        outerBox.add(Box.createVerticalStrut(100));
        return createSceneTemplate(outerBox, BACKGROUND);
    }

    private static Box createGameOverScreen(JLabel gameOverText, JButton quitButton) {
        Box gameOverBox = Box.createHorizontalBox();
        gameOverBox.add(Box.createGlue());
        gameOverBox.add(gameOverText);
        gameOverBox.add(Box.createGlue());
        Box quitButtonBox = Box.createHorizontalBox();
        quitButtonBox.add(Box.createGlue());
        quitButtonBox.add(quitButton);
        quitButtonBox.add(Box.createGlue());
        Box inner = Box.createVerticalBox();
        inner.add(Box.createGlue());
        inner.add(gameOverBox);
        inner.add(quitButtonBox);
        inner.add(Box.createGlue());
        return createSceneTemplate(inner, BACKGROUND);
    }

    private static TextField createUserInputTextField() {
        TextField userInput = new TextField("");
        userInput.setMaximumSize(new Dimension(150, 25));
        return userInput;
    }

    public static Map<String, Set<String>> validWordsPermutation(Stream<String> dictionary) {
        return dictionary.collect(
                Collectors.toMap(
                        word -> {
                            char[] characters = word.toCharArray();
                            Arrays.sort(characters);
                            return new String(characters);
                        },
                        Collections::singleton,
                        (existingValue, newValue) -> Stream.concat(existingValue.stream(), newValue.stream())
                                .collect(Collectors.toSet())));
    }
}
