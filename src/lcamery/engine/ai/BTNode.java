package lcamery.engine.ai;

public interface BTNode {
	
	/*
	 * Updates the node based on the most recent tick
	 * @param float : tick time
	 */
	BTStatus update(float tick);
	
	/*
	 * Resets the node state
	 */
	void reset();

}
