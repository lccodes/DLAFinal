package lcamery.engine.shapes;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.LinkedList;
import java.util.List;

import lcamery.engine.entities.Collidable;
import support.Vec2f;

public class Polygon extends Collidable {
	private LinkedList<Vec2f> points;
	private Vec2f offset;
	
	public Polygon(List<Vec2f> points) {
		if (points.size() < 3) {
			throw new IllegalArgumentException();
		}
		offset = new Vec2f(0,0);
		this.points = new LinkedList<Vec2f>(points);
	}
	
	@Override
	public LinkedList<Vec2f> getPoints() {
		LinkedList<Vec2f> p = new LinkedList<Vec2f>();
		for (Vec2f v : points) {
			p.add(new Vec2f(v.x+offset.x, v.y+offset.y));
		}
		
		return p;
	}

	@Override
	public boolean contains(Vec2f point) {
		LinkedList<Vec2f> p = this.getPoints();
		Vec2f last = null;
		for (Vec2f x : p) {
			if(last != null) {
				Vec2f edge = last.minus(x);
				Vec2f top = point.minus(last);
				if (edge.cross(top) < 0) {
					return false;
				}
			}
			last = x;
		}
		Vec2f edge = p.get(p.size()-1).minus(p.get(0));
		Vec2f top = point.minus(p.get(p.size()-1));
		if (edge.cross(top) < 0) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean isCollided(Collidable o) {
		return o.isCollidedWithPolygon(this);
	}

	@Override
	public boolean isCollidedWithAAB(AAB o) {
		return o.isCollidedWithPolygon(this);
	}

	@Override
	public boolean isCollidedWithCircle(Circle o) {
		return o.isCollidedWithPolygon(this);
	}

	@Override
	public boolean isCollidedWithCoumpound(CompoundShape compoundShape) {
		return compoundShape.isCollidedWithPolygon(this);
	}

	@Override
	public void draw(Graphics2D g, boolean fill) {
		Path2D path = new Path2D.Float();
		path.moveTo(points.get(0).x+offset.x, points.get(0).y+offset.y);
		for (int i = 1; i < points.size(); i++) {
			path.lineTo(points.get(i).x+offset.x, points.get(i).y+offset.y);
		}
		path.lineTo(points.get(0).x+offset.x, points.get(0).y+offset.y);
		if (fill) {
			g.fill(path);
		}
		g.draw(path);
	}

	@Override
	public void move(float x, float y) {
		offset = offset.plus(x,y);
	}

	@Override
	public Vec2f getLocation() {
		return this.points.get(0).plus(offset.x, offset.y);
	}

	@Override
	public void setLocation(Vec2f where) {
		throw new IllegalArgumentException("Unimplemented");
	}

}
