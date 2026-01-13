package com.blilliam.circleGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Random;

public class Upgrade {

	GameObject gameObj;

	UpgradeBox[] totalBoxes;
	int[] currBoxIndex = new int[3];
	final int numberOfTotalBoxes = 8;
	final int numberOfcurrBoxes = 3;

	final int rectWidth = 300;
	final int rectHeight = 450;

	int[] costs = { 5, 5, 5, 5, 5, 5, 5, 5 };

	int reRollCost = 3;

	public Upgrade(GameObject gameObj) {
		this.gameObj = gameObj;
		totalBoxes = new UpgradeBox[numberOfTotalBoxes];

		for (int i = 0; i < numberOfTotalBoxes; i++) {
			totalBoxes[i] = new UpgradeBox(gameObj, i + 1, 0, 0);
			totalBoxes[i].cost = costs[i];
		}

		randomCurrBox();
	}

	// =========================
	// DRAW
	// =========================
	public void draw(Graphics2D g2) {
		g2.setFont(new Font("Arial", Font.BOLD, 24));
		FontMetrics fm = g2.getFontMetrics();

		// ===== BACKGROUND =====
		g2.setColor(new Color(20, 20, 50, 200));
		g2.fillRect(0, 0, AppPanel.WIDTH, AppPanel.HEIGHT);

		int spacing = (AppPanel.WIDTH - rectWidth * numberOfcurrBoxes) / (numberOfcurrBoxes + 1);
		int y = (AppPanel.HEIGHT - rectHeight) / 2;

		// ===== EXIT BUTTON =====
		int exitW = 100, exitH = 50;
		int exitX = AppPanel.WIDTH - exitW - 30;
		int exitY = 30;

		g2.setColor(new Color(30, 30, 30));
		g2.fillRect(exitX, exitY, exitW, exitH);

		g2.setColor(new Color(0, 120, 255));
		g2.setStroke(new BasicStroke(4));
		g2.drawRect(exitX, exitY, exitW, exitH);

		g2.setColor(Color.WHITE);
		String exit = "EXIT";
		int exitTextX = exitX + (exitW - fm.stringWidth(exit)) / 2;
		int exitTextY = exitY + (exitH - fm.getHeight()) / 2 + fm.getAscent();
		g2.drawString(exit, exitTextX, exitTextY);

		// ===== RE-ROLL BUTTON =====
		int reRollW = 150, reRollH = 50;
		int reRollX = AppPanel.WIDTH - reRollW - 30;
		int reRollY = AppPanel.HEIGHT - reRollH - 30;

		g2.setColor(new Color(30, 30, 30));
		g2.fillRect(reRollX, reRollY, reRollW, reRollH);

		g2.setColor(new Color(0, 120, 255));
		g2.setStroke(new BasicStroke(4));
		g2.drawRect(reRollX, reRollY, reRollW, reRollH);

		g2.setColor(Color.WHITE);
		String reRoll = "RE-ROLL";
		int reRollTextX = reRollX + (reRollW - fm.stringWidth(reRoll)) / 2;
		int reRollTextY = reRollY + (reRollH - fm.getHeight()) / 2 + fm.getAscent();
		g2.drawString(reRoll, reRollTextX, reRollTextY);

		// ===== UPGRADE BOXES =====
		for (int i = 0; i < numberOfcurrBoxes; i++) {
			UpgradeBox box = totalBoxes[currBoxIndex[i]];
			int x = spacing + i * (rectWidth + spacing);

			box.x = x;
			box.y = y;
			box.w = rectWidth;
			box.h = rectHeight;

			box.draw(g2);
		}
	}

	// =========================
	// UPDATE
	// =========================
	public void update() {

		int exitW = 100, exitH = 50;
		int exitX = AppPanel.WIDTH - exitW - 30;
		int exitY = 30;

		int reRollW = 150, reRollH = 50;
		int reRollX = AppPanel.WIDTH - reRollW - 30;
		int reRollY = AppPanel.HEIGHT - reRollH - 30;

		if (MouseInput.mouseClicked) {
			int mx = MouseInput.mouseX;
			int my = MouseInput.mouseY;

			// EXIT BUTTON
			if (mx >= exitX && mx <= exitX + exitW && my >= exitY && my <= exitY + exitH) {

				gameObj.state = GameState.PLAY;
				MouseInput.update();
				return;
			}

			// RE-ROLL BUTTON
			if (mx >= reRollX && mx <= reRollX + reRollW && my >= reRollY && my <= reRollY + reRollH) {
				if (gameObj.player1.totalCoins >= reRollCost) {
					randomCurrBox();
					gameObj.player1.totalCoins -= reRollCost;
					reRollCost *= 2;
					MouseInput.update();
					return;
				} else {
					MouseInput.update();
					return;
				}
			}

			// UPGRADE BOXES
			for (int i = 0; i < numberOfcurrBoxes; i++) {
				if (totalBoxes[currBoxIndex[i]] != null && totalBoxes[currBoxIndex[i]].isHovering()) {
					if (gameObj.player1.totalCoins >= totalBoxes[currBoxIndex[i]].cost) {
						gameObj.player1.totalCoins -= totalBoxes[currBoxIndex[i]].cost;
						totalBoxes[currBoxIndex[i]].upgrade();
						randomCurrBox();
					}
					MouseInput.update();
					return;
				}
			}
		}

	// Update animations
	for(int i = 0; i < numberOfcurrBoxes; i++) {
		totalBoxes[currBoxIndex[i]].updateAnimation();
	}}

	// =========================
	// RANDOMIZE BOXES (FIXED)
	// =========================
	public void randomCurrBox() {
		Random rand = new Random();

		int count = 0;
		while (count < numberOfcurrBoxes) {
			int r = rand.nextInt(numberOfTotalBoxes);

			// if (totalBoxes[r].type == 3 && gameObj.player1.bulletTeir == 5)
			// continue;
			
			if (Arrays.asList(currBoxIndex).contains(r)) {
				continue;
			} 
			
			currBoxIndex[count] = r;
			
			// currBoxes[count] = copy;
			totalBoxes[currBoxIndex[r]].startAnimation();
			count++;

		}
	}
}
