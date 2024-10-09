package com.acupofjava;

import javax.swing.*;
import java.awt.*;

public class GradiantPanel extends Box {
    // TODO: move to comps

    private Color topColor;
    private Color bottomColor;

    public GradiantPanel(Color topColor, Color bottomColor) {
        super(BoxLayout.X_AXIS);
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
