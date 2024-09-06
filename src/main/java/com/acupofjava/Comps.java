package com.acupofjava;

import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Objects;

import javax.swing.*;

public class Comps {
    private static final String HEARTSHAPE_PATH = "heartshape-32x32.png";

    public static JFrame createFrame() {
        JFrame frame = new JFrame("Scrambler");
        frame.setMinimumSize(new Dimension(400, 500));
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }

    public static Container createScreen(Color topColor, Color bottomColor, JComponent componentToPutInCenter) {
        GradiantPanel backgroundScreen = new GradiantPanel(topColor, bottomColor);
        Box boxToHoldComponentToPutInCenter = Box.createHorizontalBox();
        boxToHoldComponentToPutInCenter.add(Box.createHorizontalGlue());
        boxToHoldComponentToPutInCenter.add(componentToPutInCenter);
        boxToHoldComponentToPutInCenter.add(Box.createHorizontalGlue());
        backgroundScreen.add(boxToHoldComponentToPutInCenter, BorderLayout.CENTER);
        return backgroundScreen;
    }

    public static Box createHealthDisplay(int hp) {
        Box healthDisplay = Box.createHorizontalBox();
        for (int i = 0; i < hp; i++)
            healthDisplay.add(createHeart());
        return healthDisplay;
    }

    public static JComponent createHeart() {
        URL resource;
        if (Objects.isNull(resource = App.class.getResource(HEARTSHAPE_PATH)))
            throw new RuntimeException("Heart icon not found: " + HEARTSHAPE_PATH);
        JLabel heart = new JLabel(new ImageIcon(resource));
        return heart;
    }

    public static JLabel createLabel(Color textColor, String text) {
        JLabel gameOverText = new JLabel(text);
        gameOverText.setForeground(textColor);
        gameOverText.setFont(new Font("Arial", Font.BOLD, 40));
        return gameOverText;
    }

    public static JTextField createTextField(String initialText) {
        JTextField userInput = new JTextField(initialText);
        userInput.setMaximumSize(new Dimension(150, 25));
        userInput.setPreferredSize(new Dimension(150, 25));
        return userInput;
    }

    public static JButton createButton(Color topColor, Color bottomColor, Color textColor, String text,
                                       ActionListener l) {
        JButton button = new GradiantButton(topColor, bottomColor, text);
        button.setForeground(textColor);
        button.setFocusable(false);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.addActionListener(l);
        return button;
    }

    public static Box stackHorizontally(JComponent... components) {
        Box box = Box.createHorizontalBox();
        for (JComponent component : components) {
            component.setAlignmentY(Component.CENTER_ALIGNMENT);
            box.add(component);
        }
        return box;
    }

    public static Box stackVertically(JComponent... components) {
        Box outerBox = Box.createVerticalBox();
        for (JComponent component : components) {
            component.setAlignmentX(Component.CENTER_ALIGNMENT);
            outerBox.add(component);
        }
        outerBox.setAlignmentY(Component.CENTER_ALIGNMENT);
        outerBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        return outerBox;
    }
}
