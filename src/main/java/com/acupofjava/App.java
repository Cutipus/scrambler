package com.acupofjava;
import java.awt.*;
import javax.swing.*;

public class App {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Test Title");

        JPanel outerBox = new JPanel();
        outerBox.setLayout(new BoxLayout(outerBox,BoxLayout.PAGE_AXIS));
        outerBox.setBackground(Color.BLACK);

        JLabel scrambledWordLabel = new JLabel("Hello1");
        scrambledWordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        outerBox.add(scrambledWordLabel);

        JPanel innerBox = new JPanel();
        innerBox.setLayout(new BoxLayout(innerBox, BoxLayout.LINE_AXIS));
        innerBox.setBackground(Color.RED);

        JLabel userInput = new JLabel("Hello2");
        userInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        innerBox.add(userInput);
        outerBox.add(innerBox);

        frame.add(outerBox);
        frame.pack();
        frame.setVisible(true);
        frame.setMinimumSize(new Dimension(300, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



    }
}
