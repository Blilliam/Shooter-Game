package com.blilliam.circleGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player extends Entity {
	public int totalCoins = 0;
	public int score = 0;
	public boolean isHit = false;
	private long timer = 0;
	GameObject gameObj;
	
	public int bulletTeir = 1;

	public double health = 5;
	public double speed = 5;
	public double delay = 400;

	private long hitTimer = 0;
	private long hitDelay = 1000;
	
	double angle = 0; // radians
	
	
	BufferedImage sprite;

	public Player(GameObject gameObj) {
		radius = 30;
		this.gameObj = gameObj;
		setX(AppPanel.WIDTH / 2);
		setY(AppPanel.HEIGHT - 60);
		
		getPlayerImage(); 
		angle = Math.toRadians(270);

	}
	
	public void getPlayerImage() {
		
		try {
		    sprite = ImageIO.read(getClass().getResource("/Images/Nova Drift - Edited.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}

	public void update() {
		if (isDead) {
			return;
		}
		for (Coin e : gameObj.coins) {
			if (Entity.circleCollision(this, e)) {
				totalCoins += e.value;
				e.isDead = true;
			}
		}

		if (health == 0) {
			isDead = true;
		}
		movement();
		collisionWIthEnemies();
		createBullets();
	}

	public void createBullets() {
		if (MouseInput.mousePressed) {
			if (System.currentTimeMillis() - timer <= delay) return;

		    double centerX = getX() + radius; // player center
		    double centerY = getY() + radius; // player center

		    int numBullets = Math.min(bulletTeir, 5); // max 5 bullets
		    double spread = 30; // total spread in degrees

		    // If tier = 1 → 0 offset (single bullet)
		    // If tier > 1 → bullets evenly spaced within spread
		    for (int i = 0; i < numBullets; i++) {
		        double offset = 0;
		        if (numBullets > 1) {
		            offset = -spread / 2 + (spread / (numBullets - 1)) * i;
		        }
		        double bulletAngle = Math.toDegrees(angle) + offset;
		        gameObj.bullets.add(new Bullet(gameObj, centerX, centerY, bulletAngle));
		    }

		    timer = System.currentTimeMillis();
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
	    // Calculate vector from player to mouse
	    double dx = MouseInput.mouseX - (getX() + radius);
	    double dy = MouseInput.mouseY - (getY() + radius);
	    
	    // Calculate angle to mouse
	    angle = Math.atan2(dy, dx);

	    // Move forward toward mouse with W
	    if (gameObj.keyH.up) { // W key
	        setX(getX() + Math.cos(angle) * speed);
	        setY(getY() + Math.sin(angle) * speed);
	    }

	    // Move backward away from mouse with S
	    if (gameObj.keyH.down) { // S key
	        setX(getX() - Math.cos(angle) * speed);
	        setY(getY() - Math.sin(angle) * speed);
	    }

	    // Keep player inside screen bounds
	    if (getX() < 0) setX(0);
	    if (getY() < 0) setY(0);
	    if (getX() > AppPanel.WIDTH - radius * 2) setX(AppPanel.WIDTH - radius * 2);
	    if (getY() > AppPanel.HEIGHT - radius * 2) setY(AppPanel.HEIGHT - radius * 2);
	}



	public void draw(Graphics2D g2) {
	    if (isDead) return;

	    int drawX = (int) getX();
	    int drawY = (int) getY();
	    int size = radius * 2;

	    if (sprite != null) {
	        // Save original transform
	        AffineTransform old = g2.getTransform();

	        // Move to center of sprite
	        g2.translate(drawX + size / 2, drawY + size / 2);

	        // Rotate
	        g2.rotate(angle);

	        // Draw centered
	        g2.drawImage(sprite, -size / 2, -size / 2, size, size, null);

	        // Restore transform
	        g2.setTransform(old);
	    }

	    // hit flash overlay
	    if (isHit) {
	        Color overlayWhite = new Color(255, 255, 255, 128);
	        g2.setColor(overlayWhite);
	        g2.fillOval(drawX, drawY, size, size);
	    }
	}


}
