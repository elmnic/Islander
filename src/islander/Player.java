package islander;

public class Player {

	private int width;
	private int height;
	private int playerX;
	private int playerY;
	private int[][] playerXY = new int[2][4];//[2] The player's current and previous position, [4] X, Y, ID of the block occupied by Player, and direction
	private int playerInv = 0;
	private int count = 0;
	private int spawnWidth = 6;
	private int spawnHeight = 5;

	public Player(int x, int y) {
		width = x;
		height = y;
	}

	public void createPlayer() {
		int x = (int) (Math.random() * (width / spawnWidth));
		int y = (int) (Math.random() * (height / spawnHeight));

		playerXY[0][0] = playerXY[1][0] = playerX = x;
		playerXY[0][1] = playerXY[1][1] = playerY = y;
		playerXY[1][2] = Map.LAND;
		
		count++;
//		System.out.println("X: " + playerX + " Y: " + playerY);
//		System.out.println("Attempts: " + count);

		//Increases the area for the possible spawn location
		if (count > 150) {
			count = 0;
			spawnWidth--;
			spawnHeight--;
			if (spawnWidth == 1) {
				spawnWidth++;
			}
			if (spawnHeight == 1) {
				spawnHeight++;
			}
		}
	}

	public void move(int x, int y, int direction, int playerWasOn) {
		//Turns the bridge block into LAND
		if (playerWasOn == Map.BRIDGE) {
			playerWasOn = Map.LAND;
		}
		playerXY[0][2] = playerXY[1][2] = playerWasOn;
		playerXY[0][3] = playerXY[1][3] = direction;

		//Replaces the player's previous position
		playerXY[1][0] = playerX = x;
		playerXY[1][1] = playerY = y;

		//Up
		if (direction == 0) {
			playerY--;
		} //Left
		else if (direction == 1) {
			playerX--;
		} //Down
		else if (direction == 2) {
			playerY++;
		} //Right
		else if (direction == 3) {
			playerX++;
		}
		updatePlayerPos();
	}

	public void updatePlayerPos() {

		boolean outside = false;

		//Prevents the player from going outside the map
		if (playerX < 0) {
			playerX++;
			outside = true;
		} else if (playerY < 0) {
			playerY++;
			outside = true;
		} else if (playerX > width - 1) {
			playerX--;
			outside = true;
		} else if (playerY > height - 1) {
			playerY--;
			outside = true;
		}
		/*when the player tries to go outside the map the current and old position become the same, but 
         the previous position changes to what it was AFTER the new position is changed*/
		if (outside) {
			playerXY[0][2] = Map.PLAYER;
			outside = false;
		}
		//New position
		playerXY[0][0] = playerX;
		playerXY[0][1] = playerY;
	}

	public void pickUpBridge() {
		//Adds a bridge block to playerInv
		playerInv++;
	}

	public int[][] getPlayerXY() {
		return playerXY;
	}

	public int getPlayerX() {
		return playerXY[0][0];
	}
	public int getPlayerY() {
		return playerXY[0][1];
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getSpawnWidth() {
		return spawnWidth;
	}

	public int getSpawnHeight() {
		return spawnHeight;
	}

	public void setSpawnWidth(int spawnWidth) {
		this.spawnWidth = spawnWidth;
	}

	public void setSpawnHeight(int spawnHeight) {
		this.spawnHeight = spawnHeight;
	}

	public int getPlayerInv() {
		return playerInv;
	}

	public void setPlayerInv(int playerInv) {
		this.playerInv = playerInv;
	}

}
