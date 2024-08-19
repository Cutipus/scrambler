package com.acupofjava;

import java.awt.*;
import java.util.List;

import javax.swing.*;

public class App {

    public static void main(String[] args) {
        // Holds list of words to be unscrambled
        List<String> words = List.of("hello", "world", "cat", "wow");

        WordGameLogic game = new WordGameLogic(words.get(0), 3);

        // Stores scrambled word
        JLabel scrambledWordLabel = createScrambledWordLabel(scrambleWord(game.getWord()));
        // Text field to take user input
        TextField userInput = createUserInputTextField();
        JButton submitButton = createButton("Submit");
        JButton quitButton = createButton("Quit");
        // Displays the number of hearts
        Box healthDisplay = createHealthDisplay(game.getHP());
        // Displays scrambled word
        Box wordLabel = createWordLabel(scrambledWordLabel);
        // Displays text field for user input and submit button
        Box inputArea = createInputArea(userInput, submitButton);
        // Displays number of hearts, scrambled word, text field for user input and
        // submit / quit button
        Box gameScene = createOuterBox(healthDisplay, wordLabel, inputArea, quitButton);
        JLabel gameOverText = new JLabel("Game Over");
        Box gameOverScreen = createGameOverScreen(gameOverText, quitButton);

        Box mainScene = Box.createHorizontalBox();
        // Adds all the displays (number of hearts, scrambled word, text field for user
        // input and submit button) to mainscene
        mainScene.add(gameScene);
        // mainScene.add(gameOverScreen);
        JFrame frame = createFrame(mainScene);
        frame.setVisible(true);

        // Checks if user entered the correct answer and responds accordingly
        submitButton.addActionListener(e -> {
            String userInputText = userInput.getText();

            if (game.tryWord(userInputText)) {
                System.out.println("WIN");
                scrambledWordLabel.setText("VICTORY");
            } else {
                System.out.println("LOSE");
                if (game.getHP() == 0) {
                    System.out.println("You've lost the game");
                    submitButton.setEnabled(false);
                    mainScene.remove(gameScene);
                    mainScene.add(gameOverScreen);
                    frame.pack();
                    mainScene.repaint();
                } else {
                    healthDisplay.remove(0);
                    healthDisplay.repaint();
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
        outerBox.setBackground(new Color(25, 25, 61));

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
        Box outer = Box.createHorizontalBox();
        outer.setOpaque(true);
        outer.setBackground(Color.RED);
        Box inner = Box.createVerticalBox();
        inner.setOpaque(true);
        inner.setBackground(Color.BLUE);
        inner.add(gameOverText);
        inner.add(quitButton);
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

    public static String scrambleWord(String word) {
        return new StringBuilder(word).reverse().toString();
    }
}
