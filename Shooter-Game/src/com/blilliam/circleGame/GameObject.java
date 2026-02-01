package com.blilliam.circleGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class GameObject {
	Player player1;
	ArrayList<Enemy> enemies;
	ArrayList<Bullet> bullets;
	ArrayList<Exp> exp;
	WaveSystem waves;
	Upgrade upgrades;
	KeyboardInput keyH;
	MouseInput mouseHandler;

	int startButtonWidth;
	int startButtonHeight;
	GameButton startButton;

	int exitControlButtonWidth;
	int exitControlButtonHeight;
	GameButton exitControlButton;

	int controlButtonWidth;
	int controlButtonHeight;
	GameButton controlButton;

	BufferedImage sprite;

	public static GameState state;

	public GameObject(KeyboardInput keyH, MouseInput mouseHandler) {
		this.keyH = keyH;
		this.mouseHandler = mouseHandler;

		state = GameState.START; // force START first

		enemies = new ArrayList<>();
		bullets = new ArrayList<>();
		exp = new ArrayList<>();

		player1 = new Player(this);
		waves = new WaveSystem(this);
		upgrades = new Upgrade(this);

		startButtonWidth = 300;
		startButtonHeight = 100;
		
		startButton = new GameButton(AppPanel.WIDTH / 2 - startButtonWidth / 2,
				AppPanel.HEIGHT / 2 - startButtonHeight / 2, startButtonWidth, startButtonHeight, "START",
				this::startGame);

		controlButton = new GameButton(AppPanel.WIDTH / 2 - startButtonWidth / 2,
				AppPanel.HEIGHT / 2 - startButtonHeight / 2 + 230 + controlButtonHeight / 2, startButtonWidth,
				startButtonHeight, "CONTROLS", this::showControls);

		exitControlButton = new GameButton(AppPanel.WIDTH / 2 - startButtonWidth / 2,
				AppPanel.HEIGHT / 2 + startButtonHeight / 2 + 50, startButtonWidth, startButtonHeight, "EXIT BACK",
				this::toStart);
		getBackgroundImage();
	}

	private void startGame() {
		state = GameState.PLAY;
	}

	private void showControls() {
		state = GameState.CONTROLS;
	}

	private void toStart() {
		player1 = new Player(this);
		enemies.clear();
		bullets.clear();
		exp.clear();
		WaveSystem.waveNum = 1;
		state = GameState.START;

	}

	public void update() {
		if (state == GameState.PLAY) {
			ScoreManager.checkAndUpdateHighScore(player1.score);
			
			waves.update();

			enemies.removeIf(e -> e.isDead);
			exp.removeIf(c -> c.isDead);
			exp.forEach(c -> c.update());

			player1.update();
			enemies.forEach(e -> e.update());
			bullets.removeIf(b -> b.isDead);
			bullets.forEach(b -> b.update());

			if (player1.isDead) {
				state = GameState.DEAD;
			}
			if (state == GameState.PLAY) {
			    if (player1.totalUpgradesAvailible > 0 && keyH.upgradePressed) {
			        state = GameState.UPGRADING;
			        keyH.upgradePressed = false;

			        player1.expCollectedForUpgrade = false; // reset XP collection
			        upgrades.reset(); // reset upgrade selection
			    }
			}


		} else if (state == GameState.START) {
			// Update the start button (hover + click handled)
			startButton.update();
			controlButton.update();

			// Clear click flag after updates
			MouseInput.update();
		} else if (state == GameState.UPGRADING) {

			// Only collect exp **once when entering UPGRADING**
			if (!player1.expCollectedForUpgrade) {
				while (!exp.isEmpty()) {
					player1.currExp += exp.get(0).value;
					exp.remove(0);
				}
				player1.expCollectedForUpgrade = true;
			}

			upgrades.update(); // lets player select upgrades

			// Only go back to PLAY **after player confirms upgrade or no upgrades left**
			if (player1.totalUpgradesAvailible == 0 && upgrades.hasFinishedUpgrading()) {
				state = GameState.PLAY;
				player1.expCollectedForUpgrade = false; // reset for next time
			}

			MouseInput.update();

		} else if (state == GameState.CONTROLS) {
			exitControlButton.update();
			MouseInput.update();
		} else if (state == GameState.DEAD) {
			exitControlButton.update();
			MouseInput.update();
		}

	}

	public void draw(Graphics2D g2) {
		if (state == GameState.PLAY) {

			// ---- BACKGROUND (always first) ----
			if (sprite != null) {
				int imgW = sprite.getWidth();
				int imgH = sprite.getHeight();

				double scale = Math.max((double) AppPanel.WIDTH / imgW, (double) AppPanel.HEIGHT / imgH);

				int drawW = (int) (imgW * scale);
				int drawH = (int) (imgH * scale);

				int x = (AppPanel.WIDTH - drawW) / 2;
				int y = (AppPanel.HEIGHT - drawH) / 2;

				g2.drawImage(sprite, x, y, drawW, drawH, null);

			} else {
				g2.setColor(Color.BLACK);
				g2.fillRect(0, 0, AppPanel.WIDTH, AppPanel.HEIGHT);
			}

			player1.draw(g2);
			enemies.forEach(e -> e.draw(g2));
			bullets.forEach(b -> b.draw(g2));
			exp.forEach(c -> c.draw(g2));

			drawXPBar(g2);

			drawStats(g2);

		} else if (state == GameState.UPGRADING) {
			g2.setColor(new Color(0, 0, 0, 150)); // dark semi-transparent overlay
			g2.fillRect(0, 0, AppPanel.WIDTH, AppPanel.HEIGHT);

			upgrades.draw(g2);
			drawStats(g2);

		} else if (state == GameState.START) {

			g2.setColor(new Color(30, 30, 80, 200)); // dark blue overlay
			g2.fillRect(0, 0, AppPanel.WIDTH, AppPanel.HEIGHT);

			int highScore = ScoreManager.loadHighScore();

			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));

			String hs = "High Score: " + highScore;
			FontMetrics fm = g2.getFontMetrics();
			int x = (AppPanel.WIDTH - fm.stringWidth(hs)) / 2;

			g2.drawString(hs, x, AppPanel.HEIGHT / 2 + 120);

			startButton.draw(g2);
			controlButton.draw(g2);
		} else if (state == GameState.CONTROLS) {
			drawControls(g2);
			exitControlButton.draw(g2);
		} else if (state == GameState.DEAD) {
			g2.setColor(Color.BLACK);
			g2.fillRect(0, 0, AppPanel.WIDTH, AppPanel.HEIGHT);

			int highScore = ScoreManager.loadHighScore();

			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));

			String hs = "High Score: " + highScore;
			FontMetrics fmHS = g2.getFontMetrics();
			int xHS = (AppPanel.WIDTH - fmHS.stringWidth(hs)) / 2;

			g2.drawString(hs, xHS, AppPanel.HEIGHT / 2 + 60);

			g2.setColor(Color.RED);
			g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 100));
			FontMetrics fmDeath = g2.getFontMetrics();
			int xDead = (AppPanel.WIDTH - fmDeath.stringWidth("GAME OVER")) / 2;
			g2.drawString("GAME OVER", xDead, AppPanel.HEIGHT / 2);

			exitControlButton.draw(g2);
		}

	}

	private void drawStats(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
		FontMetrics fm = g2.getFontMetrics();

		String s1 = "Score: " + player1.score;

		String s4 = "Damage: " + Bullet.dmg;
		String s5 = "Speed: " + player1.maxSpeed;
		String s6 = "Attack Delay: " + player1.delay;
		String s7 = "Health: " + player1.health;
		String s8 = "Bullet Speed: " + Bullet.speed;
		String s9 = "Bullet Tier: " + player1.bulletTeir;

		int x1 = (AppPanel.WIDTH - fm.stringWidth(s1)) / 2;
		g2.drawString(s1, x1, 30);

		g2.drawString(s4, 0, 30);
		g2.drawString(s5, 0, 60);
		g2.drawString(s6, 0, 90);
		g2.drawString(s7, 0, 120);
		g2.drawString(s8, 0, 150);
		g2.drawString(s9, 0, 180);
	}

	public void drawControls(Graphics2D g2) {
		g2.setColor(new Color(30, 30, 80, 200)); // dark blue overlay
		g2.fillRect(0, 0, AppPanel.WIDTH, AppPanel.HEIGHT);

		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
		FontMetrics fm = g2.getFontMetrics();

		String s1 = "Foward: W";
		String s2 = "Backwards: S";
		String s3 = "Boost: Shift";
		String s4 = "Shoot: Left Click";

		int x1 = (AppPanel.WIDTH - fm.stringWidth(s1)) / 2;
		int x2 = (AppPanel.WIDTH - fm.stringWidth(s2)) / 2;
		int x3 = (AppPanel.WIDTH - fm.stringWidth(s3)) / 2;
		int x4 = (AppPanel.WIDTH - fm.stringWidth(s4)) / 2;

		g2.drawString(s1, x1, AppPanel.HEIGHT / 2 - 100);
		g2.drawString(s2, x2, AppPanel.HEIGHT / 2 - 70);
		g2.drawString(s3, x3, AppPanel.HEIGHT / 2 - 40);
		g2.drawString(s4, x4, AppPanel.HEIGHT / 2 - 10);
	}

	public void getBackgroundImage() {

		try {
			sprite = ImageIO.read(getClass().getResource("/Images/GreenPlanetBackground.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void drawXPBar(Graphics2D g2) {
		if (player1.totalUpgradesAvailible != 0) {
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
			FontMetrics fm = g2.getFontMetrics();

			String s2 = "Upgrades Availible: " + player1.totalUpgradesAvailible;
			int x2 = (AppPanel.WIDTH - fm.stringWidth(s2)) / 2;
			g2.drawString(s2, x2, AppPanel.HEIGHT - 70);
		}
		// Bar position & size
		int barWidth = 300;
		int barHeight = 25;
		int x = (AppPanel.WIDTH - barWidth) / 2;
		int y = AppPanel.HEIGHT - 50; // below score stats

		// Percentage filled
		float percent = Math.min(1.0f, (float) player1.currExp / player1.expToUpgrade);

		// Background
		g2.setColor(new Color(50, 50, 50, 180));
		g2.fillRect(x, y, barWidth, barHeight);

		// Filled portion
		g2.setColor(new Color(0, 200, 255));
		g2.fillRect(x, y, (int) (barWidth * percent), barHeight);

		// Border
		g2.setColor(Color.WHITE);
		g2.drawRect(x, y, barWidth, barHeight);

		// Text
		g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		String text = player1.currExp + " / " + player1.expToUpgrade + " XP";
		int textWidth = g2.getFontMetrics().stringWidth(text);
		g2.drawString(text, x + (barWidth - textWidth) / 2, y + barHeight - 5);
	}

}
