package com.acupofjava;

import java.awt.*;
import javax.swing.*;

public class App {

    public static void main(String[] args) {
        Box outerBox = Box.createVerticalBox();
        outerBox.setOpaque(true);
        outerBox.setBackground(new Color(25, 25, 61));

        Box topBox = createTopBox();
        Box bottomBox = createBottomBox();

        outerBox.add(Box.createVerticalStrut(100));
        outerBox.add(topBox);
        outerBox.add(bottomBox);
        outerBox.add(Box.createVerticalStrut(100));

        JFrame frame = new JFrame("Scrambler");
        frame.add(outerBox);
        frame.setMinimumSize(new Dimension(300, 500));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static Box createTopBox() {
        Box topBox = Box.createHorizontalBox();
        JLabel scrambledWordLabel = createScrambledWordLabel();
        topBox.add(Box.createHorizontalStrut(10));
        topBox.add(scrambledWordLabel);
        topBox.add(Box.createHorizontalStrut(10));
        return topBox;
    }

    private static JLabel createScrambledWordLabel() {
        JLabel scrambledWordLabel = new JLabel("uhakcip");
        scrambledWordLabel.setFont(new Font("Arial", Font.BOLD, 40));
        scrambledWordLabel.setForeground(new Color(154, 201, 79));
        return scrambledWordLabel;
    }

    private static Box createBottomBox() {
        Box bottomBox = Box.createHorizontalBox();
        TextField userInput = createUserInputTextField();
        JButton submitButton = createSubmitButton();
        bottomBox.add(Box.createHorizontalStrut(10));
        bottomBox.add(userInput);
        bottomBox.add(Box.createHorizontalStrut(10));
        bottomBox.add(submitButton);
        bottomBox.add(Box.createHorizontalStrut(10));
        return bottomBox;
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

}
