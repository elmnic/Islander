package islander;

public class Goal {
	private int maxX;
	private int maxY;
	private int spawnWidth = 6;
	private int spawnHeight = 5;
	private int count = 0;
	private int[] goal = new int[2];
	
	public Goal(int x, int y) {
		maxX = x;
		maxY = y;
	}
	public void createGoal() {
		int minX = maxX - (maxX / spawnWidth);
		int minY = maxY - (maxY / spawnHeight);
		int x = minX + (int)(Math.random() * ((maxX - minX)));
		int y = minY + (int)(Math.random() * ((maxY - minY)));

		goal[0] = x;
		goal[1] = y;
		
		count++;
//		System.out.println("X: " + playerX + " Y: " + playerY);
//		System.out.println("Attempts: " + count);

		//Increases the area for the possible spawn location
		if (count > 150) {
			count = 0;
			spawnWidth--;
			spawnHeight--;
			if (spawnWidth == 2) {
				spawnWidth++;
			}
			if (spawnHeight == 2) {
				spawnHeight++;
			}
		}
	}
	public int[] getGoal() {
		return goal;
	}
	public int getSpawnWidth() {
		return spawnWidth;
	}
	public int getSpawnHeight() {
		return spawnHeight;
	}
}
