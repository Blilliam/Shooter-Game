package com.blilliam.circleGame;

import java.awt.*;
import java.awt.geom.Path2D;
import javax.swing.*;

public class TrianglePath2D extends JPanel {

    public void paintComponent(Graphics g, double[] pt1, double[] pt2, double[] pt3) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Create a Path2D object
        Path2D.Double trianglePath = new Path2D.Double();

        // Define vertices and construct the path
        trianglePath.moveTo(pt1[0], pt1[1]);
        trianglePath.lineTo(pt2[0], pt2[1]);
        trianglePath.lineTo(pt3[0], pt3[1]);
        trianglePath.closePath(); // Close the triangle

        // Set color and draw/fill
        g2d.setColor(Color.RED);
        g2d.fill(trianglePath); // Fill the triangle
        g2d.setColor(Color.BLACK);
        g2d.draw(trianglePath); // Draw the outline
    }
}