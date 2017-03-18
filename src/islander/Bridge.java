package islander;

public class Bridge {

	private int bridgeOnMap = 0;
	private int[][] bridgeMap;
	private double ratio;

	public Bridge(int[][] bridgeMap, double ratio) {
		this.bridgeMap = bridgeMap;
		this.ratio = ratio;
	}

	public void createBridge() {
		for (int i = 0; i < bridgeMap.length; i++) {
			for (int j = 0; j < bridgeMap[0].length; j++) {
				if (ratio/5 > Math.random()) {
					if (bridgeMap[i][j] == Map.LAND) {
						bridgeMap[i][j] = Map.BRIDGE;
						bridgeOnMap++;
					}
				} 
			}
		}
	}

	public int getBridgeOnMap() {
		return bridgeOnMap;
	}

	public void setBridgeOnMap(int bridgeOnMap) {
		this.bridgeOnMap = bridgeOnMap;
	}
	
	public int[][] getBridgeMap() {
		return bridgeMap;
	}

}
