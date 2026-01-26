package com.blilliam.circleGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class GameObject {
	Player player1 = new Player(this);
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	ArrayList<Coin> coins = new ArrayList<Coin>();
	WaveSystem waves = new WaveSystem(this);
	Upgrade upgrades = new Upgrade(this);
	KeyboardInput keyH;
	MouseInput mouseHandler;

	int startButtonWidth = 300;
	int startButtonHeight = 100;
	GameButton startButton;

	public static GameState state = GameState.START;

	public GameObject(KeyboardInput keyH, MouseInput mouseHandler) {
		this.keyH = keyH;
		this.mouseHandler = mouseHandler;

		startButtonWidth = 300;
		startButtonHeight = 100;
		startButton = new GameButton(AppPanel.WIDTH / 2 - startButtonWidth / 2,
				AppPanel.HEIGHT / 2 - startButtonHeight / 2, startButtonWidth, startButtonHeight, "START",
				this::startGame);
	}

	private void startGame() {
		state = GameState.PLAY;
	}

	public void update() {
		if (state == GameState.PLAY) {
			waves.update();

			enemies.removeIf(e -> e.isDead);
			coins.removeIf(c -> c.isDead);

			player1.update();
			enemies.forEach(e -> e.update());
			bullets.removeIf(b -> b.isDead);
			bullets.forEach(b -> b.update());

		} else if (state == GameState.START) {
			// Update the start button (hover + click handled)
			startButton.update();

			// Clear click flag after updates
			MouseInput.update();
		} else if (state == GameState.UPGRADING) {
			for (int i = 0; i < coins.size(); i++) {
				player1.totalCoins += coins.get(0).value;
				coins.remove(0);
			}
			upgrades.update();
			MouseInput.update();
		}
	}

	public void draw(Graphics2D g2) {
		if (state == GameState.PLAY) {
			
			g2.setColor(Color.BLACK);
		    g2.fillRect(0, 0, AppPanel.WIDTH, AppPanel.HEIGHT);

			player1.draw(g2);
			enemies.forEach(e -> e.draw(g2));
			bullets.forEach(b -> b.draw(g2));
			coins.forEach(c -> c.draw(g2));

			drawStats(g2);

		} else if (state == GameState.UPGRADING) {
			g2.setColor(new Color(0, 0, 0, 150)); // dark semi-transparent overlay
			g2.fillRect(0, 0, AppPanel.WIDTH, AppPanel.HEIGHT);

			upgrades.draw(g2);
			drawStats(g2);

		} else if (state == GameState.START) {
			g2.setColor(new Color(30, 30, 80, 200)); // dark blue overlay
			g2.fillRect(0, 0, AppPanel.WIDTH, AppPanel.HEIGHT);

			startButton.draw(g2);
		}

		if (player1.isDead) {
			g2.setColor(Color.RED);
			g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 100));
			FontMetrics fmDeath = g2.getFontMetrics();
			int xDead = (AppPanel.WIDTH - fmDeath.stringWidth("GAME OVER")) / 2;
			g2.drawString("GAME OVER", xDead, (int) (AppPanel.HEIGHT / 2));
		}
	}

	private void drawStats(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
		FontMetrics fm = g2.getFontMetrics();

		String s1 = "Score: " + player1.score;
		String s2 = "Wave Number: " + WaveSystem.waveNum;
		String s3 = "Coins: " + player1.totalCoins;

		String s4 = "Damage: " + Bullet.dmg;
		String s5 = "Speed: " + player1.maxSpeed;
		String s6 = "Attack Delay: " + player1.delay;
		String s7 = "Health: " + player1.health;
		String s8 = "Bullet Speed: " + Bullet.speed;
		String s9 = "Bullet Tier: " + player1.bulletTeir;

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

}
