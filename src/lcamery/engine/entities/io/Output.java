package lcamery.engine.entities.io;

import java.util.LinkedList;
import java.util.List;

public class Output<B, T> {
	private List<Connection<B,T>> connections;
	
	public Output() {
		connections = new LinkedList<Connection<B,T>>();
	}
	
	public void connect(Connection<B, T> c) {
		connections.add(c);
	}
	
	public List<T> run() {
		List<T> t = new LinkedList<T>();
		for (Connection<B, T> c: connections) {
			t.add(c.run());
		}
		
		return t;
	}
}
