package com.blilliam.circleGame;

import java.awt.Rectangle;

public class UpgradeBox extends Rectangle {

	private static final long serialVersionUID = 1L;

	public int cost = 5; // base cost for all upgrades
	private final int type;
	private final GameObject gameObj;

	public UpgradeBox(GameObject gameObj, int type) {
		this.gameObj = gameObj;
		this.type = type;
	}

	// Apply upgrade + increase cost
	public void upgrade() {

		switch (type) {
		case 1:
			Bullet.dmg++;
		case 2:
			gameObj.player1.speed *= 1.2;
		case 3:
			gameObj.player1.bulletTeir++;
		case 4:
			gameObj.player1.delay *= 0.85;
		case 5:
			gameObj.player1.health += 3;
		}

		// Increase this upgrade's cost by ×1.5
		cost = (int) Math.ceil(cost * 1.5);
	}

	public String description() {
		switch (type) {
		case 1:
			return "Bullet Damage\n+1 damage";
		case 2:
			return "Player Speed\n+50% movement";
		case 3:
			return "Bullet Amount\n+1 bullet";
		case 4:
			return "Attack Speed\n15% faster fire";
		case 5:
			return "Player Health\n+3 HP";
		default:
			return "";
		}
	}
}
