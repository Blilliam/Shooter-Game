package com.blilliam.circleGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Random;

public class Upgrade {

    GameObject gameObj;

    UpgradeBox[] totalBoxes;
    UpgradeBox[] currBoxes;
    final int numberOfTotalBoxes = 8;
    final int numberOfCurrBoxes = 3;

    final int rectWidth = 300;
    final int rectHeight = 450;

    int[] costs = {5, 5, 5, 5, 5, 5, 5, 5};

    public Upgrade(GameObject gameObj) {
        this.gameObj = gameObj;
        totalBoxes = new UpgradeBox[numberOfTotalBoxes];

        for (int i = 0; i < numberOfTotalBoxes; i++) {
            totalBoxes[i] = new UpgradeBox(gameObj, i + 1, 0, 0);
            totalBoxes[i].cost = costs[i];
        }

        randomCurrBox();
    }

    public void draw(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g2.getFontMetrics();

        // ===== BACKGROUND =====
        g2.setColor(new Color(20, 20, 50, 200)); // dark blue semi-transparent
        g2.fillRect(0, 0, AppPanel.WIDTH, AppPanel.HEIGHT);

        int spacing = (AppPanel.WIDTH - rectWidth * numberOfCurrBoxes) / (numberOfCurrBoxes + 1);
        int y = (AppPanel.HEIGHT - rectHeight) / 2;

        // ===== EXIT BUTTON =====
        int exitW = 100, exitH = 50;
        int exitX = AppPanel.WIDTH - exitW - 30;
        int exitY = 30;

        g2.setColor(new Color(30, 30, 30)); 
        g2.fillRect(exitX, exitY, exitW, exitH);

        g2.setColor(new Color(0, 120, 255)); // bright blue border
        g2.setStroke(new BasicStroke(4));
        g2.drawRect(exitX, exitY, exitW, exitH);

        g2.setColor(Color.WHITE);
        String exit = "EXIT";
        int exitTextX = exitX + (exitW - fm.stringWidth(exit)) / 2;
        int exitTextY = exitY + (exitH - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(exit, exitTextX, exitTextY);

        // ===== UPGRADE BOXES =====
        for (int i = 0; i < numberOfCurrBoxes; i++) {
            UpgradeBox box = currBoxes[i];
            int x = spacing + i * (rectWidth + spacing);
            box.x = x;
            box.y = y;
            box.w = rectWidth;
            box.h = rectHeight;

            // Draw using box's own draw method (handles animation + hover)
            box.draw(g2);
        }
    }

    public void update() {
        // Handle EXIT button click
        int exitW = 100, exitH = 50;
        int exitX = AppPanel.WIDTH - exitW - 30;
        int exitY = 30;
        if (MouseInput.mouseClicked) {
            int mx = MouseInput.mouseX;
            int my = MouseInput.mouseY;

            if (mx >= exitX && mx <= exitX + exitW && my >= exitY && my <= exitY + exitH) {
                gameObj.state = GameState.PLAY;
                MouseInput.update(); // consume click
                return;
            }

            // Handle upgrade box clicks
            for (int i = 0; i < numberOfCurrBoxes; i++) {
                UpgradeBox box = currBoxes[i];
                if (box != null && box.isHovering()) {
                    if (gameObj.player1.totalCoins >= box.cost) {
                        gameObj.player1.totalCoins -= box.cost;
                        box.upgrade();
                        randomCurrBox();
                    }
                    MouseInput.update(); // consume click
                    return;
                }
            }
        }

        // Update animations for all boxes
        for (UpgradeBox box : currBoxes) {
            box.updateAnimation();
        }
    }

    public void randomCurrBox() {
        currBoxes = new UpgradeBox[numberOfCurrBoxes];
        Random rand = new Random();
        boolean[] used = new boolean[numberOfTotalBoxes];

        int count = 0;
        while (count < numberOfCurrBoxes) {
            int r = rand.nextInt(numberOfTotalBoxes);
            if (!used[r]) {
                if (totalBoxes[r].type == 3 && gameObj.player1.bulletTeir == 5) continue;
                used[r] = true;
                currBoxes[count] = totalBoxes[r];
                currBoxes[count].startAnimation(); // start slide-in animation
                count++;
            }
        }
    }
}
