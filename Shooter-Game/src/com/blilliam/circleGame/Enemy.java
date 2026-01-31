package com.blilliam.circleGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Enemy extends Entity {

	GameObject gameObj;

	private int teir = 1;
	private int health;

	private int flashTimer = 0;
	
	int deathTimer = -1;
	
	int shockwaveRadius = 0;
	int shockwaveMax = 80;
	boolean shockwaveActive = false;

	public int getTeir() {
		return teir;
	}

	public void setTeir(int teir) {
		this.teir = Math.max(1, Math.min(teir, 5)); // clamp between 1–5
		this.health = this.teir;
		updateSize();
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = Math.max(0, health);
		updateSize();
	}

	// random color
	public int r255 = (int) (Math.random() * 255);
	public int g255 = (int) (Math.random() * 255);
	public int b255 = (int) (Math.random() * 255);

	public Enemy(GameObject gameObj) {
		this.gameObj = gameObj;

		setX(AppPanel.WIDTH * Math.abs(Math.random() - 0.1));
		setY(50);

		dx = (Math.random() * 10) - 5;
		dy = (Math.random() * 10) - 5;

		while (dx == 0)
			dx = (Math.random() * 3) + 2;
		while (dy == 0)
			dy = (Math.random() * 3) + 2;

		// teir 1–5
		teir = (int) (Math.random() * 5) + 1;
		health = teir;

		updateSize();
	}

	public Enemy(GameObject gameObj, int teir) {
		this(gameObj);
		setTeir(teir);
	}

	public Enemy(GameObject gameObj, int teir, double x, double y) {
		this(gameObj);
		setTeir(teir);
		setX(x);
		setY(y);
	}

	// size = 10 + (health × 5)
	public void updateSize() {
		radius = 10 + (health * 8);
	}

	public void flash() {
	    flashTimer = 5;
	    shockwaveActive = true;
	    shockwaveRadius = 0;

	    if (health <= 0) {
	        deathTimer = 10; // frames to show shockwave
	    }
	}


	public void update() {
		setX(getX() + dx);
		setY(getY() + dy);

		if (flashTimer > 0)
			flashTimer--;
		
		if (shockwaveActive) {
		    shockwaveRadius += 4;  // speed of expansion

		    if (shockwaveRadius > shockwaveMax) {
		        shockwaveActive = false;
		    }
		}

		// bounce
		int diameter = radius * 2;

		// Horizontal wrap
		if (getX() + diameter < 0) {
		    setX(AppPanel.WIDTH);
		}
		else if (getX() > AppPanel.WIDTH) {
		    setX(-diameter);
		}

		// Vertical wrap
		if (getY() + diameter < 0) {
		    setY(AppPanel.HEIGHT);
		}
		else if (getY() > AppPanel.HEIGHT) {
		    setY(-diameter);
		}

		if (health <= 0) {
		    dx = 0;
		    dy = 0;

		    if (deathTimer > 0) {
		        deathTimer--;
		    } else if (deathTimer == 0) {
		        isDead = true;
		        gameObj.player1.score += teir;
		    }
		}

	}

	public void draw(Graphics2D g2) {
		
		if (shockwaveActive) {
		    int alpha = 120 - (shockwaveRadius * 120 / shockwaveMax);
		    alpha = Math.max(alpha, 0);

		    g2.setStroke(new BasicStroke(4)); // thickness of ring
		    g2.setColor(new Color(255, 255, 255, alpha));

		    int size = shockwaveRadius * 2;
		    int offset = shockwaveRadius;

		    g2.drawOval(
		        (int) getX() + radius - offset,
		        (int) getY() + radius - offset,
		        size,
		        size
		    );
		}

		if (flashTimer > 0) {
			g2.setColor(Color.WHITE);
		} else {
			g2.setColor(new Color(255, g255, b255, 150));
		}

		g2.fillArc((int) getX(), (int) getY(), radius * 2, radius * 2, 0, 360);

		g2.setColor(new Color(r255, g255, b255, 100));
		g2.fillArc((int) getX()+radius/2, (int) getY()+radius/2, radius, radius, 0, 360);
	}
}
