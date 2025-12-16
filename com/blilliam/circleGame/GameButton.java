package com.blilliam.circleGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class GameButton {
GameObject go;
	
	int x;
	int y;
	int w;
	int h;
	Runnable clickFunc;
	String buttonText;

	public GameButton(GameObject go, String buttonText, int x, int y, int w, int h, Runnable clickFunc) {
		this.go = go;
		this.buttonText = buttonText;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.clickFunc = clickFunc;
	}
	
	public void update() {
		if (contains(go.mouseHandler.x, go.mouseHandler.y)) {
			// hovering 
			if (go.mouseHandler.isLeft) {
				// clicking button
				clickFunc.run();
			}
		}
	}
	
	public void draw(Graphics2D g2) {
		g2.setColor(Color.BLACK);
		g2.fillRect(x, y, w, h);
		
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 50));
		
		FontMetrics fm = g2.getFontMetrics();
		
		int x1 = (AppPanel.WIDTH - fm.stringWidth(buttonText)) / 2;

		g2.drawString(buttonText, x1, AppPanel.HEIGHT/2 + 15);
	}
	
	private boolean contains(int mx, int my) {
		return (mx > x && mx < x + w && my > y && my < y + h);
	}
}
