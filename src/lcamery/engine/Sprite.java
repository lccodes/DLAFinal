package lcamery.engine;

import java.util.LinkedList;

import support.Vec2i;

public class Sprite {
	public static final int MAX_HEALTH = 100;
	protected LinkedList<Node> path = null;
	protected Vec2i gridLocation;
	protected Vec2i drawLocation;
	protected boolean player;
	protected int whatToDraw = 0;
	protected int health = MAX_HEALTH;
	protected LinkedList<Vec2i> imageLoc;
	
	/*
	 * Constructor
	 * @param : bufferedimage to display
	 * @param : Vec2i on the board
	 */
	public Sprite(LinkedList<Vec2i> imageLoc, Vec2i location, boolean player) {
		this.imageLoc = imageLoc;
		this.gridLocation = location;
		this.player = player;
		this.drawLocation = location.smult(25);
	}
	
	public void setPath(LinkedList<Node> newPath) {
		path = newPath;
	}
	
	public boolean hasPath() {
		return path == null;
	}
	
	public Vec2i getLocation() {
		return gridLocation;
	}
	
	public Vec2i getDrawLocation() {
		return drawLocation;
	}

	public boolean enroute() {
		return (path != null);
	}

	public boolean getPlayer() {
		return this.player;
	}
	
	public Vec2i getImageLocation() {
		return imageLoc.get(whatToDraw);
	}
	
	public void takeDamage() {
		health-=10;
	}
	
	public int getHeatlh() {
		return this.health;
	}
	
	public boolean fullHealth() {
		return this.health == Sprite.MAX_HEALTH;
	}
}
