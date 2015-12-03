package lcamery.engine.entities.io;

public abstract class Input<B, T> {
	
	/*
	 * Inputs apply their function on run
	 * @input : map that function will use
	 */
	public abstract T run(B args);
}
