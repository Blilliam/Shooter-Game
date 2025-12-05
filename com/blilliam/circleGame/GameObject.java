package com.blilliam.circleGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class GameObject {
	Player player1 = new Player(this);
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	ArrayList<Coin> coins = new ArrayList<Coin>();
	WaveSystem waves = new WaveSystem(this);
	KeyboardInput keyH;
	
	public GameObject(KeyboardInput keyHandler) {
		this.keyH = keyHandler;
		
		
	}

	public void update() {
		waves.update();
		
		enemies.removeIf((e) -> e.isDead);
		
		coins.removeIf((e) -> e.isDead);
		
		player1.update();
		
		enemies.forEach(e -> {
			e.update();
		});
		
		bullets.removeIf((e) -> e.isDead);
		
		bullets.forEach(b -> {
			b.update();
		});
	}
	public void draw(Graphics2D g2) {
		g2.setColor(new Color(0, 0, 0));
		g2.fillRect(0, 0, AppPanel.WIDTH, AppPanel.HEIGHT);
		player1.draw(g2);
		
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g2);
		}
		
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).draw(g2);
		}

		for (int i = 0; i< coins.size(); i ++) {
			coins.get(i).draw(g2);
		}
	}
}
