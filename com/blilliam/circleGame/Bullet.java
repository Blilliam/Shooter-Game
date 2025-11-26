package com.blilliam.circleGame;

import java.awt.Color;
import java.awt.Graphics2D;

public class Bullet extends Entity {
	GameObject gameObj;

	public Bullet(GameObject gameObj, double x, double y) {
		this.gameObj = gameObj;
		radius = 10;
		setY(y - radius);
		setX(x - 3 * 3);
	}
	
	public void update() {
		setY(getY() - 10);
		
		for (Enemy e : gameObj.enemies) {
			if (Entity.circleCollision(this, e)) {
				e.setHealth(e.getHealth() - 1);
				isDead = true;
			}
		}
	}
	
	public void draw(Graphics2D g2) {
		g2.setColor(new Color(255, 0, 0));
		g2.fillArc((int)getX(), (int)getY(), this.radius * 2, this.radius * 2, 0, 360);
	}
}
