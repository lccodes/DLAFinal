package lcamery.engine.shapes;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import lcamery.engine.entities.Collidable;
import support.Vec2f;

public class AAB extends Collidable {
	private Vec2f location;
	private Vec2f size;
	
	public AAB(Vec2f loc, Vec2f size) {
		this.location = loc;
		this.size = size;
	}

	@Override
	public boolean contains(Vec2f p) {
		if (location.x > p.x || p.x > location.x+size.x) {
			return false;
		}
		
		if (location.y > p.y || p.y > location.y+size.y) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean isCollided(Collidable o) {
		return o.isCollidedWithAAB(this);
	}

	@Override
	public boolean isCollidedWithCoumpound(CompoundShape compoundShape) {
		return compoundShape.isCollidedWithAAB(this);
	}

	@Override
	public void draw(Graphics2D g, boolean fill) {
		Rectangle2D r = new Rectangle2D.Double(location.x, location.y, size.x, size.y);
		if (fill){
			g.fill(r);
		}
		g.draw(r);
	}

	@Override
	public boolean isCollidedWithAAB(AAB o) {
		return (location.x <= o.getLocation().x+o.getSize().x && location.x+size.x >= o.getLocation().x
				&& location.y <= o.getLocation().y + o.getSize().y && location.y+size.y >= o.getLocation().y);
	}

	@Override
	public boolean isCollidedWithCircle(Circle o) {
		return o.isCollidedWithAAB(this);
	}

	@Override
	public void move(float x, float y) {
		location = location.plus(x, y);
	}
	
	@Override
	public boolean equals(Object o) {
		return this == o;
	}

	@Override
	public Vec2f getLocation() {
		return location;
	}
	
	public Vec2f getSize() {
		return size;
	}
	
	@Override
	public LinkedList<Vec2f> getPoints() {
		LinkedList<Vec2f> points = new LinkedList<Vec2f>();
		points.add(new Vec2f(this.location.x, this.location.y));
		points.add(new Vec2f(this.location.x, this.location.y+size.y));
		points.add(new Vec2f(this.location.x+size.x, this.location.y+size.y));
		points.add(new Vec2f(this.location.x+size.x, this.location.y));
		
		return points;
	}

	@Override
	public void setLocation(Vec2f where) {
		this.location = where;
	}

}
