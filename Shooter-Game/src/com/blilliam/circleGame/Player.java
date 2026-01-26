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
		if (gameObj.keyH.shooting) {
			if (bulletTeir == 1) {
				teirBullet1();
			}
			if (bulletTeir == 2) {
				teirBullet2();
			}
			if (bulletTeir == 3) {
				teirBullet3();
			}
			if (bulletTeir == 4) {
				teirBullet4();
			}
			if (bulletTeir == 5) {
				teirBullet5();
			}
		}
	}

	private void teirBullet1() {
		if (System.currentTimeMillis() - timer > delay) {
			gameObj.bullets.add(new Bullet(gameObj, getX() + radius, getY()));
			setY(getY() + 10);
			timer = System.currentTimeMillis();
		}
	}

	private void teirBullet2() {
		if (System.currentTimeMillis() - timer > delay) {
			gameObj.bullets.add(new Bullet(gameObj, getX() + radius - 20, getY()));
			gameObj.bullets.add(new Bullet(gameObj, getX() + radius + 20, getY()));
			setY(getY() + 7);
			timer = System.currentTimeMillis();
		}
	}

	private void teirBullet3() {
		if (System.currentTimeMillis() - timer > delay) {
			for (int i = 280; i >= 260; i -= 10) {
				gameObj.bullets.add(new Bullet(gameObj, getX() + radius, getY(), i));
			}
			setY(getY() + 3);
			timer = System.currentTimeMillis();
		}
	}

	private void teirBullet4() {
		if (System.currentTimeMillis() - timer > delay) {
			for (int i = 240; i <= 300; i += 10) {
				gameObj.bullets.add(new Bullet(gameObj, getX() + radius, getY(), i));
			}
			setY(getY() + 2);
			timer = System.currentTimeMillis();
		}
	}

	private void teirBullet5() {
		if (System.currentTimeMillis() - timer > delay) {
			for (int i = 0; i <= 360; i += 10) {
				gameObj.bullets.add(new Bullet(gameObj, getX() + radius, getY(), i));
			}
			setY(getY());
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
		if (getY() > AppPanel.HEIGHT - (radius * 2)) {
			setY(AppPanel.HEIGHT - (radius * 2));
		}
		if (getX() > AppPanel.WIDTH - (radius * 2)) {
			setX(AppPanel.WIDTH - (radius * 2));
		}
		if (getY() < 0) {
			setY(0);
		}
		if (getX() < 0) {
			setX(0);
		}
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
