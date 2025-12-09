package com.blilliam.circleGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class GameObject {
	Player player1 = new Player(this);
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	ArrayList<Coin> coins = new ArrayList<Coin>();
	WaveSystem waves = new WaveSystem(this);
	Upgrade upgrades = new Upgrade(this);
	KeyboardInput keyH;

	
	public static GameState state = GameState.PLAY;
	
	public GameObject(KeyboardInput keyHandler) {
		this.keyH = keyHandler;
	}

	public void update() {
		if (state == GameState.PLAY) {
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
	}
	public void draw(Graphics2D g2) {
		if (state == GameState.PLAY) {
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
			
			
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
			
			FontMetrics fm = g2.getFontMetrics();

			String s1 = "Score: " + this.player1.score;
			String s2 = "Wave Number: " + WaveSystem.waveNum;
			String s3 = "Coins: " + this.player1.totalCoins;
			
			String s4 = "Damage: " + Bullet.dmg;
			String s5 = "Speed: " + this.player1.speed;
			String s6 = "Atack Delay: " + this.player1.delay;
			String s7 = "Health: " + this.player1.health;
			String s8 = "Bullet Speed: " + Bullet.speed;
			String s9 = "Bullet Teir: " + player1.bulletTeir;

			int x1 = (AppPanel.WIDTH - fm.stringWidth(s1)) / 2;
			int x2 = (AppPanel.WIDTH - fm.stringWidth(s2)) / 2;
			int x3 = (AppPanel.WIDTH - fm.stringWidth(s3)) / 2;

			g2.drawString(s1, x1, 30);
			g2.drawString(s2, x2, 60);
			g2.drawString(s3, x3, 90);
			
			g2.drawString(s4, 0, 30);
			g2.drawString(s5, 0, 60);
			g2.drawString(s6, 0, 90);
			g2.drawString(s7, 0, 120);
			g2.drawString(s8, 0, 150);
			g2.drawString(s9, 0, 180);
		} else if (state == GameState.UPGRADING){
			g2.setColor(new Color(0, 0, 255, 100));
			g2.fillRect(0, 0, AppPanel.WIDTH, AppPanel.HEIGHT);
			upgrades.draw(g2);
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
			
			FontMetrics fm = g2.getFontMetrics();

			String s1 = "Score: " + this.player1.score;
			String s2 = "Wave Number: " + WaveSystem.waveNum;
			String s3 = "Coins: " + this.player1.totalCoins;
			
			String s4 = "Damage: " + Bullet.dmg;
			String s5 = "Speed: " + this.player1.speed;
			String s6 = "Atack Delay: " + this.player1.delay;
			String s7 = "Health: " + this.player1.health;
			String s8 = "Bullet Speed: " + Bullet.speed;
			String s9 = "Bullet Teir: " + player1.bulletTeir;

			int x1 = (AppPanel.WIDTH - fm.stringWidth(s1)) / 2;
			int x2 = (AppPanel.WIDTH - fm.stringWidth(s2)) / 2;
			int x3 = (AppPanel.WIDTH - fm.stringWidth(s3)) / 2;

			g2.drawString(s1, x1, 30);
			g2.drawString(s2, x2, 60);
			g2.drawString(s3, x3, 90);
			
			g2.drawString(s4, 0, 30);
			g2.drawString(s5, 0, 60);
			g2.drawString(s6, 0, 90);
			g2.drawString(s7, 0, 120);
			g2.drawString(s8, 0, 150);
			g2.drawString(s9, 0, 180);
		}
		
		
		if (player1.isDead) {
			g2.setColor(Color.RED);
			g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 100));
			FontMetrics fmDeath = g2.getFontMetrics();
			int xDead = (AppPanel.WIDTH - fmDeath.stringWidth("GAME OVER")) / 2;
			g2.drawString("GAME OVER", xDead, (int) (AppPanel.HEIGHT / 2));
		}
		
	}
}
