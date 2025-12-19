package com.blilliam.circleGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class Coin extends Entity{
	GameObject gameObj;
	public int value;
	public static double valueMult = 1;
	
	public Coin(GameObject gameObj, int value, double x, double y) {
		this.gameObj = gameObj;
		this.value = value;
		value = (int) Math.ceil(value * valueMult);
		
		
		radius = 10 + (value * 2);
		
		dx = 0;
		dy = 0;
		
		setX(x);
		setY(y);
		
	}
	
	public void draw(Graphics2D g2) {
	    g2.setColor(Color.YELLOW);
	    g2.fillArc((int) getX(), (int) getY(), radius * 2, radius * 2, 0, 360);

	    String label = "" + value;
	    g2.setColor(new Color(165, 42, 42));
	    g2.setFont(new Font("Arial", Font.BOLD, 24));

	    FontMetrics fm = g2.getFontMetrics();
	    int textWidth = fm.stringWidth(label);
	    int textHeight = fm.getHeight();

	    // Circle width/height = radius * 2
	    int diameter = radius * 2;

	    int textX = (int) (x + (diameter - textWidth) / 2);
	    int textY = (int) (y + (diameter - textHeight) / 2 + fm.getAscent());

	    g2.drawString(label, textX, textY);
	}

}
