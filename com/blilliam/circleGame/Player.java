package com.blilliam.circleGame;

import java.awt.Color;
import java.awt.Graphics2D;

public class Player extends Entity {
	public double score = 0;
	public int health = 5;
	public int speed = 5;
	public boolean isHit = false;
	private long timer = 0;
	private long delay = 400;
	GameObject gameObj;
	
	private long hitTimer = 0;
	private long hitDelay = 1000;
	
	public Player(GameObject gameObj) {
		radius = 30;
		this.gameObj = gameObj;
		setX(AppPanel.WIDTH / 2);
		setY(AppPanel.HEIGHT - 60);
		
	}
	
	public void update() {
		if (health == 0) {
			isDead = true;
		}
		movement();
		collisionWIthEnemies();
		createBullets();
	}
	
	public void createBullets() {
		if (gameObj.keyH.shooting) {
			long currentTime = System.currentTimeMillis();
			if (currentTime - timer > delay) {
				gameObj.bullets.add(new Bullet(gameObj, getX() + radius - 5, getY()));
				setY(getY() + 10);
				timer = System.currentTimeMillis();
			}
			
			
		}
	}
	
	private void collisionWIthEnemies() {
		for (Enemy e : gameObj.enemies) {
			if (Entity.circleCollision(this, e)) {
				if (!isHit && System.currentTimeMillis() - hitTimer > hitDelay) {
					isHit = true;
					hitTimer = System.currentTimeMillis();
					health -= 1;
				}
			}
		}
		if (isHit && System.currentTimeMillis() - hitTimer > hitDelay) {
			isHit = false;
		}
	}
	
	public void movement() {
		if (gameObj.keyH.up) {
			setY(getY() - speed);
		}
		if (gameObj.keyH.down) {
			setY(getY() + speed);
		}
		if (gameObj.keyH.left) {
			setX(getX() - speed);
		}
		if (gameObj.keyH.right) {
			setX(getX() + speed);
		}
		if (getY() > AppPanel.HEIGHT - (radius *2)) {
			setY(AppPanel.HEIGHT - (radius *2));
		}
		if (getX() > AppPanel.WIDTH - (radius *2)) {
			setX(AppPanel.WIDTH - (radius *2));
		}
		if (getY() < 0) {
			setY(0);
		}
		if (getX() < 0) {
			setX(0);
		}
	}
	public void draw(Graphics2D g2) {
		Color playerColor;

	    if (isHit) {
	        long elapsed = System.currentTimeMillis() - hitTimer;
	        double progress = (double) elapsed / hitDelay;
	        if (progress > 1.0) progress = 1.0;

	        // Interpolate between red (255,0,0) and yellow (255,255,0)
	        int green = (int) (255 * progress); // goes from 0 → 255
	        playerColor = new Color(255, green, 0);
	    } else {
	        playerColor = Color.YELLOW;
	    }

	    g2.setColor(playerColor);
	    g2.fillOval((int) getX(), (int) getY(), this.radius * 2, this.radius * 2);
	    
	    if (isHit) {
	        Color overlayWhite = new Color(255, 255, 255, 128); // semi-transparent white
	        g2.setColor(overlayWhite);
	        g2.fillOval((int) getX(), (int) getY(), this.radius * 2, this.radius * 2);
	    }
	}
}