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

		// Only check for wave completion while playing
		if (gameObj.state == GameState.PLAY && gameObj.enemies.size() == 0) {

			waveNum++;
			createEnemies();

			return;
		}
	}

	public void createEnemies() {
	    int numEnemies;
	    int baseTier;

	    // Gradually increase number of enemies
	    if (waveNum <= 10) {
	        numEnemies = waveNum + 2;       // Wave 1 = 3 enemies, wave 10 = 12
	        baseTier = 1;
	    } else if (waveNum <= 20) {
	        numEnemies = 10 + (waveNum - 10) * 2;  // Wave 11 = 12, Wave 20 = 30
	        baseTier = 3;
	    } else {
	        numEnemies = 30 + (waveNum - 20) * 3;  // Slowly increasing
	        baseTier = 5;
	    }

	    // Spawn enemies
	    for (int i = 0; i < numEnemies; i++) {
	        // Tier increases slightly with wave number
	        int tier = baseTier + (int)(Math.random() * 3);  // e.g., baseTier + 0-2
	        gameObj.enemies.add(new Enemy(gameObj, tier));
	    }

	    // Optional: Super boss every 10 waves
	    if (waveNum % 10 == 0) {
	        int bossTier = baseTier * 5; 
	        gameObj.enemies.add(new Enemy(gameObj, bossTier));
	    }
	}

}
