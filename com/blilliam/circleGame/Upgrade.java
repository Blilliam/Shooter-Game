package com.blilliam.circleGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class Upgrade {

	GameObject gameObj;
	public boolean isUpgrading = false;

	UpgradeBox[] boxes;
	final int numberOfBoxes = 5;

	final int rectWidth = 300;
	final int rectHeight = 450;

	// customize these however you want
	int[] costs = { 5, 5, 5, 5, 5 };

	public Upgrade(GameObject gameObj) {
		this.gameObj = gameObj;
		boxes = new UpgradeBox[numberOfBoxes];

		for (int i = 0; i < numberOfBoxes; i++) {
			boxes[i] = new UpgradeBox(gameObj, i + 1);
			boxes[i].cost = 5; // base cost
		}
	}

	public void draw(Graphics2D g2) {

		g2.setFont(new Font("Arial", Font.BOLD, 24));
		FontMetrics fm = g2.getFontMetrics();

		int spacing = (AppPanel.WIDTH - rectWidth * numberOfBoxes) / (numberOfBoxes + 1);
		int y = (AppPanel.HEIGHT - rectHeight) / 2;

		// ===== EXIT BUTTON =====
		int exitW = 100;
		int exitH = 50;
		int exitX = AppPanel.WIDTH - exitW - 30;
		int exitY = 30;

		g2.setColor(Color.BLACK);
		g2.fillRect(exitX, exitY, exitW, exitH);

		g2.setColor(Color.BLUE);
		g2.setStroke(new BasicStroke(4));
		g2.drawRect(exitX, exitY, exitW, exitH);

		g2.setColor(Color.WHITE);
		String exit = "EXIT";
		int exitTextX = exitX + (exitW - fm.stringWidth(exit)) / 2;
		int exitTextY = exitY + (exitH - fm.getHeight()) / 2 + fm.getAscent();
		g2.drawString(exit, exitTextX, exitTextY);

		// ===== UPGRADE BOXES =====
		for (int i = 0; i < numberOfBoxes; i++) {

			int x = spacing + i * (rectWidth + spacing);

			boxes[i].setBounds(x, y, rectWidth, rectHeight);

			// fill
			g2.setColor(Color.BLACK);
			g2.fillRect(x, y, rectWidth, rectHeight);

			// border
			g2.setColor(Color.BLUE);
			g2.setStroke(new BasicStroke(4));
			g2.drawRect(x, y, rectWidth, rectHeight);

			// multiline text
			String[] lines = boxes[i].description().split("\n");
			g2.setColor(Color.WHITE);

			int totalH = lines.length * fm.getHeight();
			int textY = y + (rectHeight - totalH) / 2 + fm.getAscent();

			for (String line : lines) {
				int textX = x + (rectWidth - fm.stringWidth(line)) / 2;
				g2.drawString(line, textX, textY);
				textY += fm.getHeight();
			}

			// cost text
			String costLabel = "Cost: " + boxes[i].cost;
			int costX = x + (rectWidth - fm.stringWidth(costLabel)) / 2;
			int costY = y + rectHeight - fm.getDescent() - 12;
			g2.drawString(costLabel, costX, costY);
		}
	}

	public void mouseClick(int mx, int my) {

		// EXIT button
		int exitW = 100;
		int exitH = 50;
		int exitX = AppPanel.WIDTH - exitW - 30;
		int exitY = 30;

		if (mx >= exitX && mx <= exitX + exitW && my >= exitY && my <= exitY + exitH) {

			isUpgrading = false;
			return;
		}

		// Upgrade boxes
		for (int i = 0; i < numberOfBoxes; i++) {
			if (boxes[i] != null && boxes[i].contains(mx, my)) {
				if (gameObj.player1.totalCoins >= boxes[i].cost) {
					gameObj.player1.totalCoins -= boxes[i].cost;
					boxes[i].upgrade();
				}
				return;
			}
		}
	}
}
