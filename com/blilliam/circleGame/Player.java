package com.blilliam.circleGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Player extends Entity {
	public int totalCoins;
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

	public Player(GameObject gameObj) {
		radius = 30;
		this.gameObj = gameObj;
		setX(AppPanel.WIDTH / 2);
		setY(AppPanel.HEIGHT - 60);

	}

	public void update() {
		for (Coin e : gameObj.coins) {
			if (Entity.circleCollision(this, e)) {
				totalCoins += e.value;
				e.isDead = true;
			}
		}
//		if (score >= 1000) {
//			delay = 50;
//			playerTeir = 5;
//		} else if (score >= 300) {
//			delay = 100;
//			playerTeir = 4;
//		} else if (score >= 100) {
//			delay = 200;
//			playerTeir = 3;
//		} else if (score >= 25) {
//			delay = 300;
//			playerTeir = 2;
//		}

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
		Color playerColor;

		if (isHit) {
			long elapsed = System.currentTimeMillis() - hitTimer;
			double progress = (double) elapsed / hitDelay;
			if (progress > 1.0)
				progress = 1.0;

			// Interpolate between red (255,0,0) and yellow (255,255,0)
			int green = (int) (255 * progress); // goes from 0 → 255
			playerColor = new Color(255, green, 0);
		} else {
			playerColor = Color.WHITE;
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
