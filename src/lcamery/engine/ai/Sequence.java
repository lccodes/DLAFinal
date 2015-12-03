package lcamery.engine.ai;

import java.util.LinkedList;

public class Sequence {
	LinkedList<Action> actions;
	
	public BTStatus update(float tick) {
		BlackBoard b = null;
		for (Action a : actions) {
			a.giveBoard(b);
			if (a.update(tick) == BTStatus.FAILURE) {
				return BTStatus.FAILURE;
			}
			b = a.getBoard();
		}
		
		return BTStatus.SUCCESS;
	}

}
