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
        backgroundScreen.setLayout(new BorderLayout());
        backgroundScreen.add(Box.createRigidArea(new Dimension(300,50)), BorderLayout.PAGE_START);
        backgroundScreen.add(componentToPutInCenter, BorderLayout.CENTER);
        backgroundScreen.add(Box.createRigidArea(new Dimension(300,50)), BorderLayout.PAGE_END);
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

    public static JLabel createLabel(Color textColor, String text, int fontSize) {
        JLabel gameOverText = new JLabel(text);
        gameOverText.setForeground(textColor);
        gameOverText.setFont(new Font("Arial", Font.BOLD, fontSize));
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

    public static Box stackHorizontally(Component... components) {
        Box box = Box.createHorizontalBox();
        for (Component component : components) {
            if (component instanceof JComponent jcomp)
                jcomp.setAlignmentY(Component.CENTER_ALIGNMENT);
            box.add(component);
        }
        box.setAlignmentY(Component.CENTER_ALIGNMENT);
        box.setAlignmentX(Component.CENTER_ALIGNMENT);
        return box;
    }

    public static Box stackVertically(Component... components) {
        Box box = Box.createVerticalBox();
        for (Component component : components) {
            if (component instanceof JComponent jcomp)
                jcomp.setAlignmentX(Component.CENTER_ALIGNMENT);
            box.add(component);
        }
        return box;
    }
}

class GradiantButton extends JButton {
    private Color topColor;
    private Color bottomColor;

    GradiantButton(Color topColor, Color bottomColor, String text) {
        super(text);
        setContentAreaFilled(false);
        this.topColor = topColor;
        this.bottomColor = bottomColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        final Graphics2D g2d = (Graphics2D) g.create();
        g2d.setPaint(new GradientPaint(
                new Point(0, 0),
                topColor,
                new Point(0, getHeight()),
                bottomColor));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();

        super.paintComponent(g);
    }
}

class GradiantPanel extends JPanel {
    private Color topColor;
    private Color bottomColor;

    public GradiantPanel(Color topColor, Color bottomColor) {
        super();
        this.topColor = topColor;
        this.bottomColor = bottomColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g.create();
        g2d.setPaint(new GradientPaint(
                new Point(getWidth() / 2, 0),
                topColor,
                new Point(getWidth() / 2, getHeight()),
                bottomColor));
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}
