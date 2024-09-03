package com.acupofjava;

import javax.swing.*;
import java.awt.*;

public class GradiantPanel extends Box {


    private Color topColor, bottomColor;

    public GradiantPanel(Color topColor, Color bottomColor) {
        super(BoxLayout.X_AXIS);
        this.topColor = topColor;
        this.bottomColor = bottomColor;
        //as;dlkas;djkas;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth(), h = getHeight();
        GradientPaint gp = new GradientPaint((float) w /2, 0, topColor, (float) w /2, h, bottomColor);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}
