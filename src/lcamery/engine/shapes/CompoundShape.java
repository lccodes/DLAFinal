package lcamery.engine.shapes;

import java.awt.Graphics2D;
import java.util.LinkedList;

import lcamery.engine.entities.Collidable;
import support.Vec2f;

public class CompoundShape extends Collidable {
	private LinkedList<Collidable> parts;
	
	public CompoundShape(LinkedList<Collidable> a) {
		parts = a;
		if (a == null) {
			parts = new LinkedList<Collidable>();
		}
	}

	public CompoundShape(Collidable[] collidables) {
		parts = new LinkedList<Collidable>();
		if (collidables != null) {
			for (Collidable a : collidables) {
				parts.add(a);
			}
		}
	}

	public void draw(Graphics2D g, boolean fill) {
		for (Collidable s : parts) {
			s.draw(g, fill);
		}
	}
	
	public CompoundShape addShape(Collidable s) {
		parts.add(s);
		return this;
	}
	
	public boolean isCollided(Collidable o) {
		return o.isCollidedWithCoumpound(this);
	}
	
	public boolean isCollidedWithAAB(AAB o) {
		for (Collidable s : parts) {
			if(s.isCollidedWithAAB(o)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isCollidedWithCircle(Circle o) {
		for (Collidable s : parts) {
			if(s.isCollidedWithCircle(o)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isCollidedWithCoumpound(CompoundShape compoundShape) {
		for (Collidable c : parts) {
			if (c.isCollidedWithCoumpound(compoundShape)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean contains(Vec2f point) {
		for (Collidable c : parts) {
			if (c.contains(point)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void move(float x, float y) {
		for (Collidable s : parts) {
			s.move(x, y);
		}
	}

	@Override
	public Vec2f getLocation() {
		return parts.get(0).getLocation();
	}

	@Override
	public LinkedList<Vec2f> getPoints() {		
		LinkedList<Vec2f> points = new LinkedList<Vec2f>();
		for (Collidable p : parts) {
			points.addAll(p.getPoints());
		}
		
		return points;
	}

	@Override
	public void setLocation(Vec2f where) {
		for (Collidable p : parts) {
			p.setLocation(where);
		}
	}

}
