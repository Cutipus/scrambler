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

    public static void main(String[] args) {
        // Holds list of words to be unscrambled
        List<String> words = List.of("hello", "world", "cat", "wow", "live", "evil", "veil", "vile", "cat", "act", "no",
                "on", "bat", "tab");
        Map<String, Set<String>> wordPermutations = validWordsPermutation(words.stream());

        Iterator<String> challengeKeyList = wordPermutations.keySet().stream().iterator();
        String nextChallengeKey = challengeKeyList.next();
        var firstScramble = new ScrambleOption(nextChallengeKey, wordPermutations.get(nextChallengeKey));

        AtomicReference<ScrambleOption> scrambles = new AtomicReference<>(firstScramble);
        Hitpoints hp = new Hitpoints(3);

        boolean goodWord = false;
        String scrambled = null;
        while (!goodWord) {
            try {
                scrambled = scrambles.get().scramble((int) (Math.random() * Integer.MAX_VALUE));
                goodWord = true;
            } catch (ImpossiblePermutationException e) {
                String nextChallenge = challengeKeyList.next();
                scrambles.set(
                        new ScrambleOption(nextChallenge, wordPermutations.get(nextChallenge)));
            }
        }
        JLabel scrambledWordLabel = createScrambledWordLabel(scrambled);

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
        Box gameScene = createOuterBox(healthDisplay, wordLabel, inputArea, quitButton);
        JLabel gameOverText = new JLabel("Game Over");
        gameOverText.setForeground(Color.RED);
        gameOverText.setFont(new Font("Arial", Font.BOLD, 40));
        Box gameOverScreen = createGameOverScreen(gameOverText, quitButton);

        Box mainScene = Box.createHorizontalBox();
        // Adds all the displays (number of hearts, scrambled word, text field for user
        // input and submit button) to mainscene
        mainScene.add(gameScene);
        // mainScene.add(gameOverScreen);
        JFrame frame = createFrame(mainScene);
        frame.setVisible(true);

        // Checks if user entered the correct answer and responds accordingly
        submitButton.addActionListener(clickEvent -> {
            String userInputText = userInput.getText();
            boolean match = scrambles.get().matches(userInputText); // TODO: match against scramble option

            if (match) {
                // VICTORY
                if (challengeKeyList.hasNext()) {
                    String nextChallengeKey_ = challengeKeyList.next();
                    scrambles.set(new ScrambleOption(nextChallengeKey_, wordPermutations.get(nextChallengeKey_)));
                    System.out.println("WIN");

                    boolean goodWord_ = false;
                    String scrambled_ = null;
                    while (!goodWord_) {
                        try {
                            scrambled_ = scrambles.get().scramble((int) (Math.random() * Integer.MAX_VALUE));
                            goodWord_ = true;
                        } catch (ImpossiblePermutationException e) {
                            String nextChallenge = challengeKeyList.next();
                            scrambles.set(
                                    new ScrambleOption(nextChallenge,
                                            wordPermutations.get(nextChallenge)));
                        }
                    }
                    scrambledWordLabel.setText(scrambled_);
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

    private static Box createOuterBox(JComponent healthBox, JComponent wordBox, JComponent inputBox,
            JComponent quitButton) {
        Box outerBox = Box.createVerticalBox();
        outerBox.setOpaque(true);
        outerBox.setBackground(BACKGROUND);

        outerBox.add(Box.createVerticalStrut(100));
        outerBox.add(healthBox);
        outerBox.add(wordBox);
        outerBox.add(inputBox);
        outerBox.add(quitButton);
        outerBox.add(Box.createVerticalStrut(100));
        return outerBox;
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
        Box outer = Box.createHorizontalBox();
        outer.setOpaque(true);
        outer.setBackground(BACKGROUND);
        outer.add(Box.createGlue());
        outer.add(inner);
        outer.add(Box.createGlue());
        return outer;
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
