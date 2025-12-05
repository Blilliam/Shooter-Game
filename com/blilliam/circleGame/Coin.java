package com.blilliam.circleGame;

import java.awt.Color;
import java.awt.Graphics2D;

public class Coin extends Entity{
	GameObject gameObj;
	public int value;
	
	public Coin(GameObject gameObj, int value, double x, double y) {
		this.gameObj = gameObj;
		this.value = value;
		
		radius = 10 + (value * 2);
		
		dx = 0;
		dy = 0;
		
		setX(x);
		setY(y);
		
	}
	
	public void draw(Graphics2D g2) {
		g2.setColor(Color.YELLOW);
		g2.fillArc((int) getX(), (int) getY(), radius * 2, radius * 2, 0, 360);
	}
}
