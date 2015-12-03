package lcamery.engine.shapes;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import lcamery.engine.entities.Collidable;
import lcamery.engine.entities.Collision;
import lcamery.engine.entities.PhysicsEntity;
import lcamery.engine.entities.Ray;
import support.Vec2f;

public class Circle extends Collidable {
	private float radius;
	private Vec2f loc;
	
	public Circle(float radius, Vec2f center) {
		this.radius = radius;
		this.loc = center;
	}

	@Override
	public boolean isCollided(Collidable o) {
		return o.isCollidedWithCircle(this);
	}

	@Override
	public boolean isCollidedWithCoumpound(CompoundShape compoundShape) {
		return compoundShape.isCollidedWithCircle(this);
	}

	@Override
	public boolean contains(Vec2f point) {
		Vec2f center = loc.plus(radius, radius);
		return (point.dist2(center) <= radius*radius);
	}

	@Override
	public void draw(Graphics2D g, boolean fill) {
		Ellipse2D e = new Ellipse2D.Double(loc.x,loc.y,radius*2,radius*2);
		g.draw(e);
		if (fill) {
			g.fill(e);
		}
	}

	@Override
	public boolean isCollidedWithAAB(AAB o) {
		float x = (float) Math.min(Math.max(loc.x+radius, o.getLocation().x), 
				o.getLocation().x+o.getSize().x);
		float y = (float) Math.min(Math.max(loc.y+radius, o.getLocation().y), 
				o.getLocation().y+o.getSize().y);
		return this.contains(new Vec2f(x,y));
	}

	@Override
	public boolean isCollidedWithCircle(Circle o) {
		float xdif = (float) (o.getLocation().x + o.getRadius() - this.getLocation().x - this.getRadius());
		float ydif = (float) (o.getLocation().y + o.getRadius() - this.getLocation().y - this.getRadius());
		float dist = (float) Math.sqrt((xdif*xdif) + (ydif*ydif));
		return dist <= (o.getRadius() + this.getRadius());
	}
	
	public double getRadius() {
		return this.radius;
	}
	
	public Vec2f getLocation() {
		return this.loc;
	}

	@Override
	public void move(float x, float y) {
		loc = loc.plus(x, y);
	}
	
	@Override
	public boolean equals(Object o) {
		return this == o;
	}
	
	@Override
	public Vec2f project(Vec2f axis) {
		LinkedList<Vec2f> p = new LinkedList<Vec2f>();
		Vec2f norm = axis.normalized().smult(radius);
		p.add(loc.plus(norm).plus(radius, radius));
		p.add(loc.minus(norm).plus(radius, radius));

		float min = axis.dot(p.getFirst());
		float max = min;
		for (Vec2f v : p) {
			float proj = axis.dot(v);
			if (proj > max) {
				max = proj;
			} else if (proj < min) {
				min = proj;
			}
		}
		
		return new Vec2f(min, max);
	}
	
	@Override
	public Set<Vec2f> getAxes(LinkedList<Vec2f> other) {
		Vec2f closest = other.getFirst();
		Vec2f adjust = loc.plus(radius, radius);
		Set<Vec2f> axes = new HashSet<Vec2f>();
		for (Vec2f o : other) {
			if (adjust.dist2(o) < adjust.dist2(closest)) {
				closest = o;
			}
		}
		axes.add(adjust.minus(closest).normalized());
		return axes;
	}

	@Override
	public LinkedList<Vec2f> getPoints() {
		LinkedList<Vec2f> list = new LinkedList<Vec2f>();
		list.add(loc.plus(radius,radius));
		return list;
	}
	
	@Override
	/*
	 * Raycasting
	 * @param source : entity that the ray is hitting
	 * @param point : origin of ray
	 * @param angle : direction of ray
	 */
	public Collision raycast(Ray ray, PhysicsEntity<?, ?> toadd) {
		final Vec2f center = this.getLocation().plus(radius, radius);
		if (!this.contains(ray.getOrigin())) {
			float centerp = center.minus(ray.getOrigin()).dot(ray.getAngle());
			Vec2f point = ray.getOrigin().plus(ray.getAngle().smult(centerp));
			if (centerp > 0 && this.contains(point)) {
				float x = center.dist(point);
				return new Collision.Builder()
					.setIntersect(ray.getOrigin().plus(
							ray.getAngle().smult(centerp - 
									(float)Math.sqrt(this.radius*this.radius - x*x))))
					.setRay(ray)
					.build();
			}
		} else {
			float centerp = center.minus(ray.getOrigin()).dot(ray.getAngle());
			Vec2f point = ray.getOrigin().plus(ray.getAngle().smult(centerp));
			if (!this.contains(point)) {
				return null;
			}
			float x = center.dist(point);
			return new Collision.Builder()
				.setIntersect(ray.getOrigin().plus(
						ray.getAngle().smult(centerp + 
								(float)Math.sqrt(this.radius*this.radius + x*x))))
				.setRay(ray)
				.build();
		}
		
		return null;
	}

	@Override
	public void setLocation(Vec2f where) {
		this.loc = where;
	}

}
