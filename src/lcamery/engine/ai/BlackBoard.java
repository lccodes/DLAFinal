package lcamery.engine.ai;

public interface BlackBoard {
	void write(String k, String v);
	String get(String k);
}
