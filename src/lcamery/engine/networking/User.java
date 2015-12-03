package lcamery.engine.networking;

/*
 * NOTE: This class is gross and annoying because that is all that kyro can handle. 
 * Always convert to GameUser ASAP
 */
public class User {
	public String name;
	public int health = 100;
	public float x = 960/2;
	public float y = 540/2;
	public float vx = 0;
	public float vy = 0;
}
