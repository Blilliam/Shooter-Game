package com.blilliam.circleGame;

public class WaveSystem {
	GameObject gameObj;
	int waveNum;
	
	WaveSystem(GameObject gameObj) {
		this.gameObj = gameObj;
		waveNum = 1;
		
		createEnemies();
	}
	
	public void update() {
		if (gameObj.enemies.size() == 0) {
			waveNum++;
			createEnemies();
		}
	}
	
	public void createEnemies() {
		for (int i = 0; i < Math.pow(2, waveNum); i++) {
			int randomTeir = (int)(Math.random() * 4) + 1;
			gameObj.enemies.add(new Enemy(gameObj, randomTeir));
		}
	}
}
