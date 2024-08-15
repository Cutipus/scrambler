package com.acupofjava;

import java.awt.*;
import java.util.List;

import javax.swing.*;

public class App {

    public static void main(String[] args) {
        List<String> words = List.of("hello", "world", "cat", "wow");
        String currentWord = words.get(0);

        JLabel scrambledWordLabel = createScrambledWordLabel(scrambleWord(currentWord));
        TextField userInput = createUserInputTextField();
        JButton submitButton = createSubmitButton();
        Box healthDisplay = Box.createHorizontalBox();
        healthDisplay.add(createHeart());
        healthDisplay.add(createHeart());
        healthDisplay.add(createHeart());
        healthDisplay.add(createHeart());
        Box wordLabel = createWordLabel(scrambledWordLabel);
        Box inputArea = createInputArea(userInput, submitButton);
        Box outerBox = createOuterBox(healthDisplay, wordLabel, inputArea);

        submitButton.addActionListener(e -> {
            String userInputText = userInput.getText();
            if (userInputText.equals(currentWord)) {
                System.out.println("WIN");
                scrambledWordLabel.setText("VICTORY");
            } else {
                System.out.println("LOSE");
                healthDisplay.remove(0);
                healthDisplay.repaint();
            }
        });

        JFrame frame = new JFrame("Scrambler");
        frame.add(outerBox);
        frame.setMinimumSize(new Dimension(300, 500));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
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
