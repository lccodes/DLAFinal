package lcamery.game;

import lcamery.engine.Engine;

public final class Launch {
	public static void main(String[] args) {
		Engine e = new Engine.EngineBuilder()
				.addTitle("Airports")
				.setFullscreen(false)
				.build();
		e.setDebugMode(false);
		e.push(new StartScreen(e));
		e.startup();
	}
}
