package lcamery.engine.ai;

import java.util.LinkedList;

public class Selector {
	private LinkedList<Action> actions;
	
	public Selector(LinkedList<Action> actions) {
		this.actions = actions;
	}
	
	public BTStatus update(long tick) {
		for (Action a : actions) {
			BTStatus stat = a.update(tick);
			if (stat.equals(BTStatus.SUCCESS)) {
				return stat;
			}
		}
		
		return BTStatus.FAILURE;
	}

}
