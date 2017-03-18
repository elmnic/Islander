package islander;

import java.awt.*;
import javax.swing.*;

public class Map extends JPanel {

	private static final long serialVersionUID = 1L;
	private int[][] map;
	private int[][] mapNew;
	private int width;
	private int height;
	private int scale;
	private double ratio;
	public static final int WATER = 0;
	public static final int LAND = 1;
	public static final int BRIDGE = 2;
	public static final int PLAYER = 3;
	public static final int PLACEDBRIDGE = 4;
	public static final int GOAL = 5;
	private Color waterColor = new Color(24, 35, 226);// Blue
	private Color landColor = new Color(12, 210, 16);// Green
	private Color bridgeColor = new Color(130, 78, 14);// Brown
	private Color placedBridgeColor = new Color(206, 124, 22);// Lighter brown
	private Color playerColor = new Color(247, 13, 9);// Red
	private Color goalColor = new Color(255, 18, 206);// Pink
	private int gridWidth = 0;
	private boolean grid = true;
	private boolean simultaneous = true; // Refine the blocks on the map all at
											// once, or one by one and thus
											// influencing the next block
	private Player player;
	private Goal goal;
	private Bridge bridge;
	private int playerWasOn;
	private boolean Jesus = false;
	private boolean won = false;

	public Map(int x, int y, double ratio, int scale) {
		map = new int[x][y];
		mapNew = new int[x][y];
		width = x;
		height = y;
		this.scale = scale;
		this.ratio = ratio;
		player = new Player(x, y);
		bridge = new Bridge(map, ratio);
		goal = new Goal(x, y);
	}

	public void buildMap() {
		// Fills the board with '0' and '1'
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (ratio < Math.random()) {
					map[i][j] = mapNew[i][j] = WATER;
				} else {
					map[i][j] = mapNew[i][j] = LAND;
				}
			}
		}
		for (int i = 0; i < 10; i++) {
			// refineMap();
		}
		player.setSpawnWidth(6);
		player.setSpawnHeight(5);
		player.setPlayerInv(0);
		bridge.setBridgeOnMap(0);
	}

	public void refineMap() {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				// Land
				if (map[i][j] == 1) {
					if (adjacentLand(i, j, LAND) < 4) {
						if (simultaneous) {
							mapNew[i][j] = WATER;
						} else {
							map[i][j] = WATER;
						}
					}
				} // Water
				else {
					if (adjacentLand(i, j, LAND) >= 4) {
						if (simultaneous) {
							mapNew[i][j] = LAND;
						} else {
							map[i][j] = LAND;
						}
					}
				}
			}
		}
		if (simultaneous) {
			for (int i = 0; i < map.length; i++) {
				System.arraycopy(mapNew[i], 0, map[i], 0, map[0].length);
			}
		}
	}

	public void updateMap(int z, boolean placeBridge) {
		int[][] playerXY = player.getPlayerXY();

		// Updates playerXY and paints the player's previous position with
		// playerWasOn
		player.move(playerXY[0][0], playerXY[0][1], z, playerWasOn);
		if (map[playerXY[0][0]][playerXY[0][1]] != PLAYER) {
			playerWasOn = map[playerXY[0][0]][playerXY[0][1]];
		}
		if (map[playerXY[0][0]][playerXY[0][1]] == WATER && !Jesus) {
			if (placeBridge && player.getPlayerInv() > 0) {
				placeBridge(playerXY);
			} else {
				// Moves the player in the opposite direction if the player
				// tries to go on water and isn't Jesus
				switch (z) {
				case 0:
					z = 2;
					break;
				case 1:
					z = 3;
					break;
				case 2:
					z = 0;
					break;
				case 3:
					z = 1;
					break;
				}
				stayOffWater(playerXY, z, playerWasOn);
			}
			// Gets the block the player will be on and uses it to paint it back
			// when the player moves away again
		} else {
			paintNewPlayerPos(playerXY);
		}
		if (playerWasOn == GOAL) {
			won = true;
		}
	}

	// Keeps the player off water...
	public void stayOffWater(int[][] playerXY, int z, int playerWasyOn) {
		// Block which the player went FROM into water
		playerWasOn = playerXY[0][2];

		// Moves the player in the opposite direction
		player.move(playerXY[0][0], playerXY[0][1], z, playerWasOn);
		map[playerXY[0][0]][playerXY[0][1]] = PLAYER;
	}

	// Places a bridge block in the direction the player last tried to go, if
	// the next block in that direction is water
	public void placeBridge(int[][] playerXY) {
		playerWasOn = PLACEDBRIDGE;
		player.setPlayerInv(player.getPlayerInv() - 1);

		// Paints the block the player is on
		map[playerXY[0][0]][playerXY[0][1]] = PLAYER;
		map[playerXY[1][0]][playerXY[1][1]] = playerXY[0][2]; // ID of the block
																// that was
																// there before
																// the player
	}

	// Paints the player's new position and paints it's previous position back
	// to its original state
	public void paintNewPlayerPos(int[][] playerXY) {
		// Paints the block the player is on
		map[playerXY[0][0]][playerXY[0][1]] = PLAYER;

		// Picks up the bridge block and adds it to the player's inventory
		if (playerWasOn == BRIDGE) {
			player.pickUpBridge();
			bridge.setBridgeOnMap(bridge.getBridgeOnMap() - 1);
		}

		// Paints the block the player WAS on back to its original state
		map[playerXY[1][0]][playerXY[1][1]] = playerXY[0][2]; // ID of the block
																// that was
																// there before
																// the player
	}

	// Refines the map
	public int adjacentLand(int x, int y, int search) {
		int found = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if ((x + i >= 0) && (y + j >= 0) && (x + i < map.length) && (y + j < map[0].length)
						&& (map[x + i][y + j] == search)) {
					found++;
				}
			}
		}
		// Subtracts the instance where it finds itself
		if (found != 0) {
			found--;
		}
		return found;
	}

	// Adds the player
	public void addPlayer(boolean newGame) {
		playerWasOn = LAND;
		int[][] playerXY = player.getPlayerXY();

		if (newGame) {
			player.createPlayer();
		}
		if (map[playerXY[0][0]][playerXY[0][1]] == LAND) {
			map[playerXY[0][0]][playerXY[0][1]] = PLAYER;
			player.setCount(0);
		} else {
			addPlayer(true);
		}
	}

	// Adds the initial bridge blocks on the map
	public void addBridge() {
		bridge.createBridge();
		int[][] bridgeMap = bridge.getBridgeMap();

		for (int i = 0; i < bridgeMap.length; i++) {
			for (int j = 0; j < bridgeMap[0].length; j++) {
				if (map[i][j] == LAND) {
					map[i][j] = bridgeMap[i][j];
				}
			}
		}
	}

	public void addGoal(boolean newGame) {
		int[] goalXY = goal.getGoal();
		if (newGame) {
			goal.createGoal();
		}
		if (map[goalXY[0]][goalXY[1]] == LAND) {
			map[goalXY[0]][goalXY[1]] = GOAL;
		} else {
			addGoal(true);
		}
	}

	public void increaseBridge() {
		player.setPlayerInv(player.getPlayerInv() + 10);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setPreferredSize(new Dimension(width * scale, height * scale));
		g.setColor(Color.black);
		if (!won) {
			setBackground(Color.blue);
			// Grid lines
			if (grid) {
				gridWidth = 1;
				// Horizontal lines
				for (int i = 0; i < height + 1; i++) {
					g.drawLine(0, i * scale, width * scale, i * scale);
				}
				// Vertical lines
				for (int j = 0; j < width + 1; j++) {
					g.drawLine(j * scale, 0, j * scale, height * scale);
				}
			} else {
				gridWidth = 0;
			}

			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[0].length; j++) {
					if (map[i][j] == WATER) {// Water color
						g.setColor(waterColor);
					} else if (map[i][j] == LAND) {// Land color
						g.setColor(landColor);
					} else if (map[i][j] == PLAYER) {// Player color, Red and
														// White
						if (!Jesus) {
							g.setColor(playerColor);
						} else {
							g.setColor(Color.white);
						}
					} else if (map[i][j] == BRIDGE) {// Bridge color
						g.setColor(bridgeColor);
					} else if (map[i][j] == PLACEDBRIDGE) {// Placed bridge
															// color
						g.setColor(placedBridgeColor);
					} else if (map[i][j] == GOAL) {// Goal color
						g.setColor(goalColor);
					}
					g.fillRect(i * scale + gridWidth, j * scale + gridWidth, scale - gridWidth, scale - gridWidth);
				}
			}

			// The player spawn area
			g.setColor(Color.RED);
			g.drawLine(width * scale / player.getSpawnWidth() - 3, 0, width * scale / player.getSpawnWidth() - 3,
					height * scale / player.getSpawnHeight());
			g.drawLine(0, height * scale / player.getSpawnHeight(), width * scale / player.getSpawnWidth() - 3,
					height * scale / player.getSpawnHeight());
			// The goal spawn area
			g.setColor(Color.RED);
			g.drawLine((width - (width / goal.getSpawnWidth())) * scale,
					(height - (height / goal.getSpawnHeight())) * scale, width * scale,
					(height - (height / goal.getSpawnHeight())) * scale);
			g.drawLine((width - (width / goal.getSpawnWidth())) * scale,
					(height - (height / goal.getSpawnHeight())) * scale,
					(width - (width / goal.getSpawnWidth())) * scale, height * scale);

		} else {
			setBackground(goalColor);
			String text = "You Won!";
			FontMetrics fm = g.getFontMetrics();
			g.setFont(getFont().deriveFont(50.0f));
			int totalWidth = (fm.stringWidth(text) * 2) + 4;

			// Baseline
			int x = (getWidth() - totalWidth) / 2;
			int y = (getHeight() - fm.getHeight()) / 2;
			g.drawString(text, x, y + ((fm.getDescent() + fm.getAscent()) / 2));
		}
	}

	public boolean isGrid() {
		return grid;
	}

	public void setGrid(boolean grid) {
		this.grid = grid;
	}

	public int[][] getMap() {
		return map;
	}

	public boolean isJesus() {
		return Jesus;
	}

	public void setJesus(boolean jesus) {
		Jesus = jesus;
	}

	public int getPlayerInv() {
		return player.getPlayerInv();
	}

	public void setPlayerInv(int i) {
		player.setPlayerInv(i);
	}

	public int getBridgeOnMap() {
		return bridge.getBridgeOnMap();
	}

	public void setBridgeOnMap(int i) {
		bridge.setBridgeOnMap(i);
	}

	public boolean isWon() {
		return won;
	}

	public void setWon(boolean won) {
		this.won = won;
	}
}
