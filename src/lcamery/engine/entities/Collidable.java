package lcamery.engine.entities;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import lcamery.engine.shapes.AAB;
import lcamery.engine.shapes.Circle;
import lcamery.engine.shapes.CompoundShape;
import lcamery.engine.shapes.Polygon;
import support.Vec2f;

public abstract class Collidable {
	
	public abstract boolean isCollided(Collidable o);
	public abstract boolean isCollidedWithAAB(AAB o);
	public abstract boolean isCollidedWithCircle(Circle o);

	public abstract boolean isCollidedWithCoumpound(CompoundShape compoundShape);
	
	public abstract void draw(Graphics2D g, boolean fill);
	
	public abstract void move(float x, float y);
	public abstract Vec2f getLocation();
	public abstract LinkedList<Vec2f> getPoints();
	public abstract boolean contains(Vec2f p);
	public abstract void setLocation(Vec2f where);
	
	/*
	 * Once getpoints is defined, this method is universal.
	 */
	public Vec2f project(Vec2f axis) {
		float min = axis.dot(this.getPoints().getFirst());
		float max = min;
		for (Vec2f v : this.getPoints()) {
			float proj = axis.dot(v);
			if (proj > max) {
				max = proj;
			} else if (proj < min) {
				min = proj;
			}
		}
		
		return new Vec2f(min, max);
	}
	
	/*
	 * Once getpoints is defined, this method is universal.
	 */
	public Set<Vec2f> getAxes(LinkedList<Vec2f> other) {
		Vec2f last = null;
		Set<Vec2f> axes = new HashSet<Vec2f>();
		for (Vec2f p : this.getPoints()) {
			if (last != null) {
				Vec2f vec = last.minus(p);
				vec = new Vec2f(vec.y*-1, vec.x);
				axes.add(vec);
			}
			last = p;
		}
		Vec2f vec = last.minus(this.getPoints().getFirst());
		vec = new Vec2f(vec.y*-1, vec.x);
		axes.add(vec);
		return axes;
	}
	
	/*
	 * The colliding formula is universal to polygons
	 */
	public boolean isCollidedWithPolygon(Polygon o) {
		Set<Vec2f> all = o.getAxes(null);
		all.addAll(this.getAxes(o.getPoints()));
		for (Vec2f a : all) {
			Vec2f one = o.project(a);
			Vec2f two = this.project(a);
			
			if (one.y < two.x || two.y < one.x) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * Universal MTV formula
	 */
	public Vec2f computeMTV(Collidable other) {
		float min = Float.POSITIVE_INFINITY;
		Vec2f mtv = null;
		Set<Vec2f> all = other.getAxes(this.getPoints());
		all.addAll(this.getAxes(other.getPoints()));
		for (Vec2f axis : all) {
			Float mtv1d = Collidable.intervalMTV(this.project(axis.normalized()), 
					other.project(axis.normalized()));
			
			if (mtv1d == null) {
				return null;
				//throw new IllegalStateException();
			} else if (Math.abs(mtv1d) < min) {
				min = Math.abs(mtv1d);
				mtv = axis.normalized().smult(mtv1d);
			} 
		}
		
		if (mtv == null) {
			return null;
			//throw new IllegalStateException();
		}

		return mtv;
	}
	
	private static Float intervalMTV(Vec2f a, Vec2f b) {
		float aRight = b.y - a.x;
		float aLeft = a.y - b.x;
		if (aLeft <= 0 || aRight <= 0) {
			return null;
		} else if (aRight <= aLeft) {
			return aRight;
		} else {
			return -aLeft;
		}
	}
	
	/*
	 * Get all the edges of a shape
	 */
	public Map<Vec2f, Vec2f> getEdges() {
		Vec2f last = null;
		Map<Vec2f, Vec2f> edges = new HashMap<Vec2f, Vec2f>();
		for (Vec2f p : this.getPoints()) {
			if (last != null) {
				edges.put(last, p);
			}
			last = p;
		}
		edges.put(last, this.getPoints().getFirst());
		return edges;
	}
	
	/*
	 * Raycasting
	 * @param source : entity that the ray is hitting
	 * @param point : origin of ray
	 * @param angle : direction of ray
	 * @return ray : vector from origin to nearest point
	 */
	public Collision raycast(Ray ray, PhysicsEntity<?,?> toadd) {
		Map<Vec2f, Vec2f> edges = this.getEdges();
		float min = Float.POSITIVE_INFINITY;
		for(Map.Entry<Vec2f, Vec2f> e : edges.entrySet()) {
			float p1 = e.getKey().minus(ray.getOrigin()).cross(ray.getAngle());
			float p2 = e.getValue().minus(ray.getOrigin()).cross(ray.getAngle());
			Vec2f perp = e.getKey().minus(e.getValue());
			perp = new Vec2f(perp.y*-1, perp.x).normalized();
			if (p1*p2 > 0) {
				continue;
			}
			
			float to = e.getValue().minus(ray.getOrigin()).dot(perp)/
					ray.getAngle().dot(perp);
			if (to > 0 && to < min) {
				min = to;
			}
		}
		
		return new Collision.Builder()
			.addFirst(toadd)
			.setIntersect(ray.getOrigin().plus(ray.getAngle().smult(min)))
			.setRay(ray)
			.build();
	}

}
