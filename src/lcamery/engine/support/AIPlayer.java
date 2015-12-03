package lcamery.engine.support;

import lcamery.engine.ai.Selector;

public class AIPlayer implements Player {
	private Selector nextAction;
	private long tick;

	@Override
	public void play() {
		nextAction.update(tick);
	}
	
	public void tick(long tick) {
		this.tick = tick;
	}

	@Override
	public boolean controlable() {
		return false;
	}

}
