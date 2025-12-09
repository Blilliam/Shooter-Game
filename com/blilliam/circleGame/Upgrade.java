package com.blilliam.circleGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Random;

public class Upgrade {

	GameObject gameObj;
	public boolean isUpgrading = false;

	UpgradeBox[] totalBoxes;
	UpgradeBox[] currBoxes;
	final int numberOfTotalBoxes = 4;
	final int numberOfCurrBoxes = 3;

	final int rectWidth = 300;
	final int rectHeight = 450;

	// customize these however you want
	int[] costs = { 5, 5, 5, 5, 5, 5 };

	public Upgrade(GameObject gameObj) {
		this.gameObj = gameObj;
		totalBoxes = new UpgradeBox[numberOfTotalBoxes];

		for (int i = 0; i < numberOfTotalBoxes; i++) {
			totalBoxes[i] = new UpgradeBox(gameObj, i + 1);
			totalBoxes[i].cost = costs[i]; // base cost
		}
		//generate the random boxes
		randomCurrBox();

	}

	public void draw(Graphics2D g2) {

		g2.setFont(new Font("Arial", Font.BOLD, 24));
		FontMetrics fm = g2.getFontMetrics();

		int spacing = (AppPanel.WIDTH - rectWidth * numberOfCurrBoxes) / (numberOfCurrBoxes + 1);
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
		for (int i = 0; i < numberOfCurrBoxes; i++) {

			int x = spacing + i * (rectWidth + spacing);

			currBoxes[i].setBounds(x, y, rectWidth, rectHeight);

			// fill
			g2.setColor(Color.BLACK);
			g2.fillRect(x, y, rectWidth, rectHeight);

			// border
			g2.setColor(Color.BLUE);
			g2.setStroke(new BasicStroke(4));
			g2.drawRect(x, y, rectWidth, rectHeight);

			// multiline text
			String[] lines = currBoxes[i].description().split("\n");
			g2.setColor(Color.WHITE);

			int totalH = lines.length * fm.getHeight();
			int textY = y + (rectHeight - totalH) / 2 + fm.getAscent();

			for (String line : lines) {
				int textX = x + (rectWidth - fm.stringWidth(line)) / 2;
				g2.drawString(line, textX, textY);
				textY += fm.getHeight();
			}

			// cost text
			String costLabel = "Cost: " + currBoxes[i].cost;
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
		for (int i = 0; i < numberOfCurrBoxes; i++) {
			if (currBoxes[i] != null && currBoxes[i].contains(mx, my)) {
				if (gameObj.player1.totalCoins >= currBoxes[i].cost) {
					gameObj.player1.totalCoins -= currBoxes[i].cost;
					currBoxes[i].upgrade();
					randomCurrBox();
				}
				return;
			}
		}
	}
	public void randomCurrBox() {
		currBoxes = new UpgradeBox[numberOfCurrBoxes];
		Random rand = new Random();
		boolean[] used = new boolean[numberOfTotalBoxes];

		int count = 0;
		while (count < 3) {
		    int r = rand.nextInt(6);
		    if (!used[r]) {
		    	if (totalBoxes[r].type == 3 && gameObj.player1.bulletTeir == 5) {
		    		continue;
		    	}
		        used[r] = true;
		        currBoxes[count] = totalBoxes[r];
		        count++;
		    }
		}
	}
}
