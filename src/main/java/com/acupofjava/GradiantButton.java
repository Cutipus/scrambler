package com.acupofjava;

import javax.swing.*;
import java.awt.*;

class GradiantButton extends JButton {
    // TODO: move to comps
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
