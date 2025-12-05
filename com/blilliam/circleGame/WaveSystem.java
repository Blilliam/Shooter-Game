package com.blilliam.circleGame;

public class WaveSystem {
	GameObject gameObj;
	static int waveNum;
	
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
		if (waveNum % 10 == 0) {
			gameObj.enemies.add(new Enemy(gameObj, waveNum * (waveNum * 5)));
		} else if (waveNum > 10) {
			for (int i = 0; i < Math.pow(2, waveNum); i++) {
				int randomTeir = (int)(Math.random() * 5) + 5;
				gameObj.enemies.add(new Enemy(gameObj, randomTeir));
			}
		} else {
			for (int i = 0; i < Math.pow(2, waveNum); i++) {
				int randomTeir = (int)(Math.random() * 4) + 1;
				gameObj.enemies.add(new Enemy(gameObj, randomTeir));
			}
		}
	}
}
