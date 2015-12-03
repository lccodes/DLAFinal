package lcamery.engine.entities;

import java.awt.Graphics2D;

import lcamery.engine.entities.io.Input;
import lcamery.engine.entities.io.Output;

public abstract class Entity<B,T> {
	protected Input<B,T> in;
	
	public abstract void draw(Graphics2D g);
	
	public abstract int type();
	
	public Input<B, T> getInput() {
		return in;
	}
	
	public abstract void setOutput(Output<B, T> out);
	
	public abstract T dispatch(B b);

}
