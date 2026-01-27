package com.blilliam.circleGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

public class Player extends Entity {
	public int totalCoins = 0;
	public int score = 0;
	public boolean isHit = false;
	private long timer = 0;
	GameObject gameObj;
	//trail
	ArrayList<TrailPoint> trail = new ArrayList<>();

	int baseTrailSize = 18;
	int boostTrailSize = 28;
	int maxTrailLength = 35;

	
	
	public int bulletTeir = 1;

	public double health = 5;
	
	//movement
	double velX = 0;
	double velY = 0;

	
	double accel = 0.6;     // acceleration strength
	double maxSpeed = 6;
	double friction = 0.96; // closer to 1 = more drift
	
	// --- Boost ---
	double boostAccel = 1.2;
	double boostMaxSpeed = 10;
	boolean boosting = false;


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
		for (int i = 0; i < trail.size(); i++) {
		    TrailPoint p = trail.get(i);
		    p.fade();
		    if (p.isDead()) {
		        trail.remove(i);
		        i--;
		    }
		}


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
	    // Vector to mouse
	    double dx = MouseInput.mouseX - (getX() + radius);
	    double dy = MouseInput.mouseY - (getY() + radius);

	    // Face mouse
	    angle = Math.atan2(dy, dx);

	    // Boost check (Shift)
	    boolean boosting = gameObj.keyH.boosting;

	    // Choose accel & max speed
	    double currentAccel = boosting ? boostAccel : accel;
	    double currentMaxSpeed = boosting ? boostMaxSpeed : maxSpeed;

	    // Thrust forward (W)
	    if (gameObj.keyH.up) {
	        velX += Math.cos(angle) * currentAccel;
	        velY += Math.sin(angle) * currentAccel;
	    }

	    // Thrust backward (S)
	    if (gameObj.keyH.down) {
	        velX -= Math.cos(angle) * currentAccel;
	        velY -= Math.sin(angle) * currentAccel;
	    }

	    // Clamp speed
	    double speed = Math.sqrt(velX * velX + velY * velY);
	    if (speed > currentMaxSpeed) {
	        velX = (velX / speed) * currentMaxSpeed;
	        velY = (velY / speed) * currentMaxSpeed;
	    }

	    // Apply movement
	    setX(getX() + velX);
	    setY(getY() + velY);

	    // Friction (more slide while boosting)
	 // Are we thrusting?
	    boolean thrusting = gameObj.keyH.up || gameObj.keyH.down;

	    // Friction rules
	    double currentFriction;
	    if (thrusting) {
	        currentFriction = 0.99;
	    } else if (boosting) {
	        currentFriction = 0.98; // optional: extra slide while boosting
	    } else {
	        currentFriction = friction;
	    }

	    // Apply friction
	    velX *= currentFriction;
	    velY *= currentFriction;

	    // Stop tiny jitter
	    if (Math.abs(velX) < 0.01) velX = 0;
	    if (Math.abs(velY) < 0.01) velY = 0;

	    // Screen bounds + velocity cancel
	    double screenW = AppPanel.WIDTH;
	    double screenH = AppPanel.HEIGHT;
	    double size = radius * 2;

	    // Horizontal wrap
	    boolean wrapped = false;

	    if (getX() + size < 0) {
	        setX(screenW);
	        wrapped = true;
	    }
	    else if (getX() > screenW) {
	        setX(-size);
	        wrapped = true;
	    }

	    if (getY() + size < 0) {
	        setY(screenH);
	        wrapped = true;
	    }
	    else if (getY() > screenH) {
	        setY(-size);
	        wrapped = true;
	    }

	    if (wrapped) {
	        clearTrailOnWrap();
	    }

	 // Add trail at center
	    trail.add(new TrailPoint(
	        getX() + radius,
	        getY() + radius,
	        velX,
	        velY,
	        boosting
	    ));

	    // Limit trail length
	    if (trail.size() > maxTrailLength) {
	        trail.remove(0);
	    }


	}
	
	private void clearTrailOnWrap() {
	    trail.clear();
	}





	public void draw(Graphics2D g2) {
	    if (isDead) return;

	 // ===== ADVANCED TRAIL =====
	    for (int i = 0; i < trail.size(); i++) {
	        TrailPoint p = trail.get(i);

	        // Progress (older = smaller)
	        float t = i / (float) trail.size();

	        // Stretch based on speed
	        double speed = Math.sqrt(p.vx * p.vx + p.vy * p.vy);
	        int stretch = (int)(speed * 3);

	        int size = (int)((p.boosting ? boostTrailSize : baseTrailSize) * (1 - t));
	        int alpha = (int)(p.alpha * 140 * (1 - t));

	        // Color shift
	        Color coreColor;
	        Color glowColor;

	        if (p.boosting) {
	            coreColor = new Color(180, 220, 255, alpha); // icy blue
	            glowColor = new Color(120, 180, 255, alpha / 2);
	        } else {
	            coreColor = new Color(255, 255, 255, alpha);
	            glowColor = new Color(255, 255, 255, alpha / 2);
	        }

	        // Direction of movement
	        double angle = Math.atan2(p.vy, p.vx);
	        double ox = Math.cos(angle) * stretch;
	        double oy = Math.sin(angle) * stretch;

	        // Glow (big, soft)
	        g2.setColor(glowColor);
	        g2.fillOval(
	            (int)(p.x - size / 2 - ox * 0.5),
	            (int)(p.y - size / 2 - oy * 0.5),
	            size + 10,
	            size + 10
	        );

	        // Core trail
	        g2.setColor(coreColor);
	        g2.fillOval(
	            (int)(p.x - size / 2 - ox),
	            (int)(p.y - size / 2 - oy),
	            size,
	            size
	        );
	    }


	    
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
	    if (boosting) {
	        AffineTransform old = g2.getTransform();

	        // Move to player center
	        g2.translate(drawX + size / 2, drawY + size / 2);
	        g2.rotate(angle);

	        // Flame color
	        g2.setColor(new Color(255, 140, 0, 180));

	        // Draw flame BEHIND ship
	        g2.fillOval(-size / 2 - 18, -6, 18, 12);

	        g2.setTransform(old);
	    }
	}


}
