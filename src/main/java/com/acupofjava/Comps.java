package com.acupofjava;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Objects;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

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

    public static Box createHealthDisplay(int hp) {
        JComponent[] hearts = new JComponent[hp];
        for (int i = 0; i < hearts.length; i++)
            hearts[i] = createHeart();
        return stackHorizontally(hearts);
    }

    public static JComponent createHeart() {
        URL resource;
        if (Objects.isNull(resource = App.class.getResource(HEARTSHAPE_PATH)))
            throw new RuntimeException("Heart icon not found: " + HEARTSHAPE_PATH);
        var heart = new JLabel(new ImageIcon(resource));
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

    public static Box createScreen(Color topColor, Color bottomColor, JComponent collectionOfComponents) {
        Box screen = centerHorizontally(centerVertically(collectionOfComponents));
        GradiantPanel backgroundScreen = new GradiantPanel(topColor, bottomColor);
        backgroundScreen.add(screen);
        return backgroundScreen;
    }

    public static Box stackHorizontally(JComponent... components) {
        Box box = Box.createHorizontalBox();
        for (JComponent component : components)
            box.add(centerVertically(component));
        return box;
    }

    public static Box stackVertically(JComponent... components) {
        Box outerBox = Box.createVerticalBox();
        for (JComponent component : components)
            outerBox.add(centerHorizontally(component));
        return outerBox;
    }

    public static Box centerHorizontally(JComponent component) {
        Box gameOverBox = Box.createHorizontalBox();
        gameOverBox.add(Box.createGlue());
        gameOverBox.add(component);
        gameOverBox.add(Box.createGlue());
        return gameOverBox;
    }

    public static Box centerVertically(JComponent component) {
        Box innerBox = Box.createVerticalBox();
        innerBox.add(Box.createGlue());
        innerBox.add(component);
        innerBox.add(Box.createGlue());
        return innerBox;
    }

}
