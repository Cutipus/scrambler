package com.acupofjava;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.*;

public class App {

    public static final Color MAIN_SCREEN_BG_COLOR = new Color(25, 25, 61);
    public static final Color GAME_OVER_BG_COLOR = new Color(131, 14, 14);
    public static final Color VICTORY_LABEL_COLOR = new Color(154, 201, 79);
    public static final Color GAME_OVER_LABEL_COLOR = Color.RED;
    public static final Color VICTORY_SCREEN_BG_COLOR = Color.BLUE;
    public static final List<String> words = List.of(
            "hello", "world", "cat", "wow", "live", "evil", "veil", "vile",
            "cat", "act", "no", "on", "bat", "tab");


    public static void main(String[] args) {
        Iterator<ScrambleOption> challenges = getChallenges(words);

        ScrambleOption firstScramble;
        if (challenges.hasNext())
            firstScramble = challenges.next();
        else {
            throw new NoSuchElementException("No challenges found!");
        }

        AtomicReference<ScrambleOption> currentScrambleOption = new AtomicReference<>(firstScramble);
        Hitpoints hp = new Hitpoints(3);

        Box healthDisplay = createHealthDisplay(hp.getHP());
        JLabel challengeWordLabel = createLabel(VICTORY_LABEL_COLOR, firstScramble.scramble(0));
        JTextField userInput = createTextField("");
        JButton submitButton = createButton("Submit");
        Box gameScreen = createScreen(MAIN_SCREEN_BG_COLOR, stackVertically(
                healthDisplay,
                challengeWordLabel,
                stackHorizontally(userInput, submitButton),
                createQuitButton()));

        Box gameOverScreen = createScreen(GAME_OVER_BG_COLOR, stackVertically(
                createLabel(GAME_OVER_LABEL_COLOR, "Game Over"),
                createQuitButton()));

        JButton restartButton = createButton("Restart");
        Box victoryScreen = createScreen(VICTORY_SCREEN_BG_COLOR, stackVertically(
                createLabel(VICTORY_LABEL_COLOR, "Victory"),
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
            PlayResult playResult = play(hp,currentScrambleOption,challenges,userGuess);
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

    public static PlayResult play(Hitpoints hp, AtomicReference<ScrambleOption> currentWord, Iterator<ScrambleOption> challenges, String userGuess) {
        if (currentWord.get().matches(userGuess)) {
            if (challenges.hasNext()) {
                currentWord.set(challenges.next());
                return new PlayResult.Right(currentWord.get().scramble((int) (Math.random() * Integer.MAX_VALUE)));
            } else {
                return new PlayResult.Victory();
            }
        } else {
            if (hp.hit()) {
                return new PlayResult.Wrong(hp.getHP());
            } else {
                return new PlayResult.Defeat();
            }
        }
    }

    private static JButton createQuitButton() {
        JButton quitButton = createButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));
        return quitButton;
    }

    private static JFrame createFrame(Box mainScene) {
        JFrame frame = new JFrame("Scrambler");
        frame.add(mainScene);
        frame.setMinimumSize(new Dimension(300, 500));
        frame.setResizable(false);
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
        var heart = new JLabel("❤");
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

    private static Box createScreen(Color backgroundColor, JComponent collectionOfComponents) {
        Box screen = centerHorizontally(centerVertically(collectionOfComponents));
        screen.setOpaque(true);
        screen.setBackground(backgroundColor);
        return screen;
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

    private static Iterator<ScrambleOption> getChallenges(List<String> list) {
        List<ScrambleOption> challengeList = validWordsPermutation(list.stream()).entrySet().stream()
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
        return challenges;
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
