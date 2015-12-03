package lcamery.game.support;

import java.awt.geom.Rectangle2D;

import support.Vec2i;

public class Button extends Rectangle2D {
	private Vec2i loc;
	private Vec2i size;
	private String theString;
	
	public Button(String s) {
		this.theString = s;
	}
	
	@Override
	public Rectangle2D createIntersection(Rectangle2D arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle2D createUnion(Rectangle2D arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int outcode(double arg0, double arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setRect(double arg0, double arg1, double arg2, double arg3) {
		loc = new Vec2i((int) arg0, (int) arg1);
		size = new Vec2i((int) arg2, (int) arg3);
	}

	@Override
	public double getHeight() {
		return size.y;
	}

	@Override
	public double getWidth() {
		return size.x;
	}

	@Override
	public double getX() {
		return loc.x;
	}

	@Override
	public double getY() {
		return loc.y;
	}
	
	public String getString() {
		return theString;
	}
	

	@Override
	public boolean isEmpty() {
		return false;
	}

}
