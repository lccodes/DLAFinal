package lcamery.engine.support;

import support.Vec2f;
import support.Vec2i;

public class MouseDirection {
	private int lastY;
	private int lastX;
	
	public MouseDirection(int firstX, int firstY) {
		lastY = firstY;
		lastX = firstX;
	}
	
	public void setY(int Y) {
		lastY = Y;
	}
	
	public void setX(int X) {
		lastX = X;
	}
	
	public int getVertical(Vec2i cord) {
		int toret = lastY - cord.y;
		if (Math.abs(toret) > 10) {
			toret /= 10;
		}
		lastY = cord.y;
		
		return -1*toret;
	}
	
	public int getHorizontal(Vec2i cord) {
		int toret = lastX - cord.x;
		if (Math.abs(toret) > 10) {
			toret /= 10;
		}
		lastX = cord.x;
		
		return -1*toret;
	}

	public Vec2f getLoc() {
		return new Vec2f(lastX, lastY);
	}

}
