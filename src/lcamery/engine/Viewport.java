package lcamery.engine;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import lcamery.engine.support.MouseDirection;
import support.Vec2f;
import support.Vec2i;

/**
 * Viewport maps game coordinates onto screen coordinates and allows for
 * zoomings and panning.
 * 
 * @author lcamery
 *
 */
public class Viewport {
	private MouseDirection md = null;
	private Vec2f translate = new Vec2f(0,0);
	private Vec2f scale = new Vec2f(1,1);
	private Vec2i size;
	private int shakeSeq, numtimes;
	private boolean shake;
	
	/* Builder only constructor */
	private Viewport(Vec2i size) {
		this.size = size;
		shakeSeq = -200;
		shake = false;
		numtimes = 100;
	}
	
	/**
	 * Builder class for Viewport
	 * @author lcamery
	 *
	 */
	public static class ViewportBuilder {
		private Vec2i size;
		
		/*
		 * Sets the size of the viewport's pan range
		 * @param vec.x = x coord boundary
		 * @param vec.y = y coord boundary
		 */
		public ViewportBuilder setSize(Vec2i vec) {
			this.size = vec;
			return this;
		}
		
		public Viewport build(){
			if (size == null) {
				throw new IllegalStateException();
			}
			
			return new Viewport(size);
		}
	}
	
	/*
	 * Pans the graphics object left, right, up, and down
	 * This actually modifies the displayed graphics
	 */
	public void transformGraphics(Graphics2D g) {
		AffineTransform t = new AffineTransform();
		t.scale(scale.x, scale.y);
		t.translate(translate.x, translate.y);

		if (shake) {
			t.translate(shakeSeq, 0);
			shakeSeq *= -1;
			numtimes--;
			if (numtimes == 0) {
				shake = false;
				numtimes = 100;
			}
		}
		g.setTransform(t);
	}
	
	public Point2D transformPoint(Point2D p) {
		AffineTransform t = new AffineTransform();
		t.scale(scale.x, scale.y);
		t.translate(translate.x, translate.y);
		return t.transform(p, null);
	}
	
	/*
	 * Responds to action listeners and alters the current pan
	 */
	public void changePan(Vec2f diff) {
		translate = translate.plus(diff);
	}
	
	public void changeMouse(Vec2i where) {
		md = new MouseDirection(where.x,where.y);
	}
	
	/*
	 * Pans with mouse direction
	 */
	public void mouseMove(Vec2i orig) {
		Vec2i cord = orig;

		if (md == null) {
			md = new MouseDirection(orig.x, orig.y);
		}
		
		int horz = md.getHorizontal(cord);
		int vert = md.getVertical(cord);
		
		this.changePan(new Vec2f(horz, vert));
	}
	
	public void zoomIn(float mult) {
		Vec2f oldCenter = this.screenToGame(new Vec2i(size.x/2,size.y/2));
		scale = scale.smult(mult);
		Vec2f newCenter = this.screenToGame(new Vec2i(size.x/2,size.y/2));
		translate = translate.plus(newCenter.minus(oldCenter));
	}
	
	public void zoomOut() {
		Vec2f oldCenter = this.screenToGame(new Vec2i(size.x/2,size.y/2));
		scale = scale.sdiv(1.05f);
		Vec2f newCenter = this.screenToGame(new Vec2i(size.x/2,size.y/2));
		translate = translate.plus(newCenter.minus(oldCenter));
	}
	
	public Vec2f screenToGame(Vec2i loc) {
		Vec2f newLoc = new Vec2f(loc);
		newLoc = newLoc.pdiv(scale);
		newLoc = newLoc.minus(translate);
		return newLoc;
	}
	
	public Vec2f screenToGame(Vec2f loc) {
		loc = loc.pdiv(scale);
		loc = loc.minus(translate);
		return loc;
	}
	
	public Vec2f gameToScreen(Vec2f loc) {
		loc = loc.plus(translate);
		loc = loc.pmult(scale);
		return loc;
	}
	
	public AffineTransform getAffine() {
		AffineTransform t = new AffineTransform();
		t.scale(scale.x, scale.y);
		t.translate(translate.x, translate.y);
		
		return t;
	}
	
	public void resize(Vec2i s) {
		float scaleX = this.scale.x * ((float)s.x/this.size.x);
		float scaleY = this.scale.y * ((float)s.y/this.size.y);
		this.scale = new Vec2f(scaleX,scaleY);

		this.size = s;
	}
	
	public Vec2i getSize() {
		return this.size;
	}
	
	/*
	 * Scales a vector
	 */
	public Vec2f scale(Vec2f tos) {
		return this.scale.pmult(tos);
	}
	
	public Vec2f getMouse() {
		if (md == null) {
			md = new MouseDirection(500,500);
		}
		return md.getLoc();
	}

	/*
	 * Screen shake method
	 */
	public void startShake() {
		shake = true;
	}
	
	/*
	 * Screen shake method
	 * @param amt : how severe to shake
	 * @param thres : when to cut off shake
	 * @param ticks : how long to shake
	 */
	public void shake(int severity, int length) {
		this.shake = true;
		this.shakeSeq = severity;
		this.numtimes = length;
	}
}
