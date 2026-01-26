package com.blilliam.circleGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Bullet extends Entity {
    GameObject gameObj;
    double angle = 270;
    static int dmg = 1;
    static int speed = 15;
    public static double dropRate = 1;
    
    BufferedImage bulletImage;

    public Bullet(GameObject gameObj, double centerX, double centerY, double angle) {
        this.gameObj = gameObj;
        this.angle = angle;
        radius = 10;

        // Store the bullet's CENTER as its logical position
        setX(centerX);
        setY(centerY);

        loadBulletImage();
    }
    
    public void loadBulletImage() {
		
		try {
			bulletImage = ImageIO.read(getClass().getResource("/Images/Bullet.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
   
    
    public void update() {
        double radianAngle = (angle * Math.PI) / 180;
        dx = Math.cos(radianAngle) * speed;
        dy = Math.sin(radianAngle) * speed;
        
        setX(getX() + dx);
        setY(getY() + dy);

        // Temporary list for adding new enemies safely
        List<Coin> toAddCoins = new ArrayList<>();
        List<Enemy> toAddEnemies = new ArrayList<>();
        
        for (Enemy e : gameObj.enemies) {
            if (Entity.circleCollision(this, e)) {
                e.setHealth(e.getHealth() - dmg);
                e.flash();
                
                if (e.getHealth() == 0) {
	                if (e.getTeir() != 1) {
	                	toAddEnemies.add(new Enemy(gameObj, e.getTeir() - 1, e.getX(), e.getY()));
	                	toAddEnemies.add(new Enemy(gameObj, e.getTeir() - 1, e.getX(), e.getY()));
	                }
	                
	                for (int i = 0; i < (int) dropRate; i++) {
	                	toAddCoins.add(new Coin(gameObj, e.getTeir(), e.getX() + (i * 5), e.getY() + (i * 5)));
	                }
	                
	                if (dropRate % 1 > Math.random()) {
	                	toAddCoins.add(new Coin(gameObj, e.getTeir(), e.getX(), e.getY()));
	                }
	                
	                isDead = true;
                }
                break;
            }
        }
        
        // Add new enemies AFTER loop
        gameObj.enemies.addAll(toAddEnemies);
        gameObj.coins.addAll(toAddCoins);
    }
    
    public void draw(Graphics2D g2) {
        if (bulletImage != null) {
            int size = radius * 10; // size of bullet image

            // Save original transform
            var old = g2.getTransform();

            // Move to bullet center
            g2.translate(getX() + radius, getY() + radius);

            // Rotate around center: offset by +90° to align sprite
            g2.rotate(Math.toRadians(angle + 90));

            // Draw centered
            g2.drawImage(bulletImage, -size / 2, -size / 2, size, size, null);

            // Restore original transform
            g2.setTransform(old);
        } else {
            g2.setColor(new Color(255, 0, 0));
            g2.fillArc((int)getX(), (int)getY(), this.radius * 2, this.radius * 2, 0, 360);
        }
    }


}
