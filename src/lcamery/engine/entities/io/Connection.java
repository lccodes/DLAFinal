package lcamery.engine.entities.io;

public class Connection<B,T> {
	private Input<B,T> target;
	private B args;
	
	public Connection(B args, Input<B,T> in) {
		this.target = in;
		this.args = args;
	}

	public T run() {
		return target.run(args);
	}
}
