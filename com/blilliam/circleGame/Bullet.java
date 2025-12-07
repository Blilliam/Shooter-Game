package com.blilliam.circleGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Bullet extends Entity {
    GameObject gameObj;
    int angle = 270;

    public Bullet(GameObject gameObj, double x, double y) {
        this.gameObj = gameObj;
        radius = 10;
        setY(y);
        setX(x);
    }
    
    public Bullet(GameObject gameObj, double x, double y, int angle) {
        this(gameObj, x, y);
        this.angle = angle;
    }
    
    public void update() {
        int speed = 15;
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
                e.setHealth(e.getHealth() - 1);
                e.flash();

                if (e.getTeir() != 1 && e.getHealth() == 0) {
                	toAddEnemies.add(new Enemy(gameObj, e.getTeir() - 1, e.getX(), e.getY()));
                	toAddEnemies.add(new Enemy(gameObj, e.getTeir() - 1, e.getX(), e.getY()));
                }
                if (Math.random()>0.5) {
                	toAddCoins.add(new Coin(gameObj, e.getTeir(), e.getX(), e.getY()));
                }

                isDead = true;
            }
        }

        // Add new enemies AFTER loop
        gameObj.enemies.addAll(toAddEnemies);
        gameObj.coins.addAll(toAddCoins);
    }
    
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(255, 0, 0));
        g2.fillArc((int)getX(), (int)getY(), this.radius * 2, this.radius * 2, 0, 360);
    }
}
