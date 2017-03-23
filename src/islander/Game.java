package islander;

public class Game {

	private double m_lastTime = System.currentTimeMillis();
	private double m_actualFps = 0.0;
	private Map map;
	
	public Game() {
		//map = Map.getInstance();
	}
	
	public void run() {

		// TODO: Refactor updates to update map and entities independently
//		while (true) {
//			
//			if (newFrame()) {
//				//map.updateMap(z, placeBridge);
//			}
//			
//		}
		
	}

	private boolean newFrame() {
		double currentTime = System.currentTimeMillis();
		double delta = currentTime - m_lastTime;
		boolean rv = (delta > Const.FRAME_INCREMENT);
		if (rv) {
			m_lastTime += Const.FRAME_INCREMENT;
			if (delta > 10 * Const.FRAME_INCREMENT) {
				m_lastTime = currentTime;
			}
			m_actualFps = 1000 / delta;
		}
		return rv;
	}

}
