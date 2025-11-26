package com.blilliam.circleGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Enemy extends Entity {
	
	GameObject gameObj;
	
	private int teir = 1;
	private int health;
	
	public int getTeir() {
		return teir;
	}

	public void setTeir(int teir) {
		this.teir = teir;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int r255 = (int)(Math.random() * 255);
	public int g255 = (int)(Math.random() * 255);
	public int b255 = (int)(Math.random() * 255);
	
	public Enemy(GameObject gameObj) {
		this.gameObj = gameObj;
		setX(AppPanel.WIDTH * Math.abs(Math.random() - 0.1));
		setY(50);
		
		dx = (Math.random() * 10) - 5;
		dy = (Math.random() * 10) - 5;
			while (dx == 0) {
				dx = (Math.random() * 3) + 2;
			}
			while (dy == 0) {
				dy = (Math.random() * 3) + 2;
			}
			radius = (int)(Math.random() * 10) + 10;
		
			
		teir = (int)(Math.random() * 4) + 1;
			
		radius += teir * 2;
		
		health = teir;
	}
	
	public Enemy(GameObject gameObj, int teir) {
		this(gameObj);
		this.teir = teir;
		this.radius += teir * 2;
	}
	
	public void update() {
		setX(getX() + dx);
		setY(getY() + dy);
		if (getY() > AppPanel.HEIGHT - (radius *2)) {
			setY(AppPanel.HEIGHT - (radius *2));
			dy = -dy;
		}
		if (getX() > AppPanel.WIDTH - (radius *2)) {
			setX(AppPanel.WIDTH - (radius *2));
			dx = -dx;
		}
		if (getY() < 0) {
			setY(0);
			dy = -dy;
		}
		if (getX() < 0) {
			setX(0);
			dx = -dx;
		}
		if (health == 0) {
			isDead = true;
			gameObj.player1.score += teir;
		}
		
	}
	
	public void draw(Graphics2D g2) {
		g2.setColor(new Color(r255, g255, b255));
		g2.fillArc((int)getX(), (int)getY(), this.radius * 2, this.radius * 2, 0, 360);
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Malgum Gothic", Font.PLAIN, 30));
		g2.drawString("Score: " + gameObj.player1.score, AppPanel.WIDTH / 2 - 40, 30);
	}
}