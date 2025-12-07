package com.blilliam.circleGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Upgrade {
	GameObject gameObj;
	static int waveNum;
	public boolean isUpgrading = false;
	Rectangle[] boxes;
	

	Upgrade(GameObject gameObj) {
			this.gameObj = gameObj;
	}
	public void update() {
		
	}
	public void draw(Graphics2D g2) {
		boxes = new Rectangle[4];
		
		int rectWidth = 300;  // rectangle width
        int rectHeight = 450; // rectangle height
        
		int spacing = (AppPanel.WIDTH - (4 * rectWidth)) / 5;

		g2.setFont(new Font("Arial", Font.BOLD, 24));

        for (int i = 0; i < 4; i++) {
            int x = spacing + i * (rectWidth + spacing);
            int y = (AppPanel.HEIGHT - rectHeight) / 2;

            // store rectangle for click detection
            boxes[i] = new Rectangle(x, y, rectWidth, rectHeight);

            // draw fill
            g2.setColor(Color.BLACK);
            g2.fill(boxes[i]);

            // draw border
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(4));
            g2.draw(boxes[i]);

            String label = "Box " + (i + 1);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            FontMetrics fm = g2.getFontMetrics();

            int textX = x + (rectWidth - fm.stringWidth(label)) / 2;
            int textY = y + (rectHeight - fm.getHeight()) / 2 + fm.getAscent();

            g2.drawString(label, textX, textY);
        }

	}

	public void mouseClick(int x, int y) {
		for (int i = 0; i < boxes.length; i++) {
	        if (boxes[i].contains(x, y)) {
	            isUpgrading = false;
	            // call game logic here
	        }
	    }
	}
}
