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

    public static final Color BACKGROUND = new Color(25, 25, 61);
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

        JButton quitButton = createButton("Quit");

        Box gameScreen = createScreen(BACKGROUND, stackVertically(
                createHealthDisplay(hp.getHP()),
                createLabel(firstScramble.scramble(0), new Color(154, 201, 79)),
                stackHorizontally(createTextField(""), createButton("Submit")),
                quitButton));

        Box gameOverScreen = createScreen(BACKGROUND, stackVertically(
                createLabel("Game Over", Color.RED),
                quitButton));

        Box victoryScreen = createScreen(Color.BLUE, stackVertically(
                createLabel("Victory", Color.GREEN),
                createButton("Restart"),
                quitButton));

        Box mainScene = Box.createHorizontalBox();
        mainScene.add(gameScreen);
        JFrame frame = createFrame(mainScene);
        frame.setVisible(true);

        // Checks if user entered the correct answer and responds accordingly
        createButton("Submit").addActionListener(clickEvent -> {
            String userInputText = createTextField("").getText();
            boolean match = currentScrambleOption.get().matches(userInputText);

            if (match) {
                // VICTORY
                if (challenges.hasNext()) {
                    // User still has words left to solve
                    currentScrambleOption.set(challenges.next());
                    System.out.println("WIN");
                    createLabel(firstScramble.scramble(0), new Color(154, 201, 79))
                            .setText(currentScrambleOption.get().scramble(0));
                } else {
                    // If user beats the game
                    createButton("Submit").setEnabled(false);
                    mainScene.remove(gameScreen);
                    mainScene.add(victoryScreen);
                    frame.pack();
                    mainScene.repaint();
                }

            } else {
                switch (hp.hit()) {
                    case ALIVE -> {
                        createHealthDisplay(hp.getHP()).remove(0);
                        createHealthDisplay(hp.getHP()).repaint();
                    }
                    case DEAD -> {
                        System.out.println("You've lost the game");
                        createButton("Submit").setEnabled(false);
                        mainScene.remove(gameScreen);
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
        var heart = new JLabel("❤️");
        heart.setForeground(Color.RED);
        return heart;
    }

    private static JLabel createLabel(String text, Color textColor) {
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
