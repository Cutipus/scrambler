package com.acupofjava;

import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.*;

public class App {

    public static void main(String[] args) {
        // Holds list of words to be unscrambled
        List<String> words = List.of("hello", "world", "cat", "wow");
        // Stores one word from the
        String currentWord = words.get(0);
        // Determines how much health player has, changing the number changes the number of hearts
        AtomicInteger hp = new AtomicInteger(3);

        // Stores scrambled word
        JLabel scrambledWordLabel = createScrambledWordLabel(scrambleWord(currentWord));
        // Text field to take user input
        TextField userInput = createUserInputTextField();
        JButton submitButton = createSubmitButton();
        // Displays the number of hearts
        Box healthDisplay = createHealthDisplay(hp.get());
        // Displays scrambled word
        Box wordLabel = createWordLabel(scrambledWordLabel);
        // Displays text field for user input and submit button
        Box inputArea = createInputArea(userInput, submitButton);
        // Displays number of hearts, scrambled word, text field for user input and submit button
        Box gameScene = createOuterBox(healthDisplay, wordLabel, inputArea);
        JPanel gameOverScene = new JPanel();
        Box mainScene = Box.createHorizontalBox();
        // Adds all the displays (number of hearts, scrambled word, text field for user input and submit button) to mainscene
        mainScene.add(gameScene);
        JFrame frame = createFrame(mainScene);
        frame.setVisible(true);

        // Checks if user entered the correct answer and responds accordingly
        submitButton.addActionListener(e -> {
            String userInputText = userInput.getText();
            if (userInputText.equals(currentWord)) {
                System.out.println("WIN");
                scrambledWordLabel.setText("VICTORY");
            } else {
                System.out.println("LOSE");
                hp.decrementAndGet();
                if (hp.get() == 0) {
                    System.out.println("You've lost the game");
                    submitButton.setEnabled(false);
                    mainScene.remove(gameScene);
                    mainScene.add(gameOverScene);
                    frame.pack();
                } else {
                    healthDisplay.remove(0);
                    healthDisplay.repaint();
                }
            }
        });

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

    private static Box createOuterBox(Box healthBox, Box wordBox, Box inputBox) {
        Box outerBox = Box.createVerticalBox();
        outerBox.setOpaque(true);
        outerBox.setBackground(new Color(25, 25, 61));

        outerBox.add(Box.createVerticalStrut(100));
        outerBox.add(healthBox);
        outerBox.add(wordBox);
        outerBox.add(inputBox);
        outerBox.add(Box.createVerticalStrut(100));
        return outerBox;
    }

    private static JLabel createScrambledWordLabel(String text) {
        JLabel scrambledWordLabel = new JLabel(text);
        scrambledWordLabel.setFont(new Font("Arial", Font.BOLD, 40));
        scrambledWordLabel.setForeground(new Color(154, 201, 79));
        return scrambledWordLabel;
    }

    private static JButton createSubmitButton() {
        JButton submitButton = new JButton("Submit");
        submitButton.setFocusable(false);
        submitButton.setFont(new Font("Arial", Font.BOLD, 15));
        return submitButton;
    }

    private static TextField createUserInputTextField() {
        TextField userInput = new TextField("");
        userInput.setMaximumSize(new Dimension(150, 25));
        return userInput;
    }

    public static String scrambleWord(String word) {
        return new StringBuilder(word).reverse().toString();
    }
}
