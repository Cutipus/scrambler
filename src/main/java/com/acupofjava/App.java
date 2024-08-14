package com.acupofjava;

import java.awt.*;
import javax.swing.*;

public class App {

    public static void main(String[] args) {
        JLabel scrambledWordLabel = createScrambledWordLabel();
        TextField userInput = createUserInputTextField();
        JButton submitButton = createSubmitButton();
        submitButton.addActionListener(e -> {
            scrambledWordLabel.setText(userInput.getText());
        });
        Box topBox = createTopBox(scrambledWordLabel);
        Box bottomBox = createBottomBox(userInput, submitButton);
        Box outerBox = createOuterBox(topBox, bottomBox);

        JFrame frame = new JFrame("Scrambler");
        frame.add(outerBox);
        frame.setMinimumSize(new Dimension(300, 500));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static Box createBottomBox(TextField userInput, JButton submitButton) {
        Box bottomBox = Box.createHorizontalBox();
        bottomBox.add(Box.createHorizontalStrut(10));
        bottomBox.add(userInput);
        bottomBox.add(Box.createHorizontalStrut(10));
        bottomBox.add(submitButton);
        bottomBox.add(Box.createHorizontalStrut(10));
        return bottomBox;
    }

    private static Box createTopBox(JLabel scrambledWordLabel) {
        Box topBox = Box.createHorizontalBox();
        topBox.add(Box.createHorizontalStrut(10));
        topBox.add(scrambledWordLabel);
        topBox.add(Box.createHorizontalStrut(10));
        return topBox;
    }

    private static Box createOuterBox(Box topBox, Box bottomBox) {
        Box outerBox = Box.createVerticalBox();
        outerBox.setOpaque(true);
        outerBox.setBackground(new Color(25, 25, 61));


        outerBox.add(Box.createVerticalStrut(100));
        outerBox.add(topBox);
        outerBox.add(bottomBox);
        outerBox.add(Box.createVerticalStrut(100));
        return outerBox;
    }

    private static JLabel createScrambledWordLabel() {
        JLabel scrambledWordLabel = new JLabel("uhakcip");
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


}
