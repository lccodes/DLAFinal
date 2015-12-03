package lcamery.engine;

import support.Vec2i;

public abstract class Node implements Comparable<Node> {
	private Vec2i cord;
	private int distToStart = Integer.MAX_VALUE;
	private int estimatedDistance = Integer.MAX_VALUE;
	private Node prev = null;
	
	public Vec2i getCoords() {
		return cord;
	}
	
	public boolean impassible() {
		return false;
	}
	
	public void setDistance(int dist) {
		this.distToStart = dist;
	}
	
	public int getDistance() {
		return distToStart;
	}
	
	public void setPrev(Node t) {
		prev = t;
	}
	
	public Node getPrev() {
		return prev;
	}
	
	public int getEstimate() {
		return this.estimatedDistance;
	}
	
	public void setEstimate(Node goal) {
		this.estimatedDistance = (int) Math.sqrt(Math.pow((double) (goal.getCoords().x - this.getCoords().x) , 2.0) +
				Math.pow((double) (goal.getCoords().y - this.getCoords().y) , 2.0));
	}
	
	public void clearEstimate() {
		this.estimatedDistance = Integer.MAX_VALUE;
	}

	@Override
	public int compareTo(Node arg0) {
		return Integer.compare(this.estimatedDistance, arg0.getEstimate());
	}

}
