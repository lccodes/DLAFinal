package lcamery.engine.entities;

import support.Vec2f;

public class Collision {
	private final PhysicsEntity<?, ?> first;
	private final PhysicsEntity<?, ?> second;
	private final Vec2f mtv, intersect;
	private final Ray ray;
	private final boolean stat;
	
	private Collision(PhysicsEntity<?, ?> first, PhysicsEntity<?, ?> second, Vec2f mtv, Ray ray, Vec2f intersect) {
		this.first = first;
		this.second = second;
		this.mtv = mtv;
		if (first != null && second != null) {
			this.stat = (this.first.isStatic() || this.second.isStatic());
		} else {
			stat = false;
		}
		this.ray = ray;
		this.intersect = intersect;
	}
	
	public PhysicsEntity<?, ?> getFirst() {
		return first;
	}
	
	public PhysicsEntity<?, ?> getSecond() {
		return second;
	}
	
	public Vec2f getMtv() {
		return mtv;
	}
	
	public Vec2f getIntersect() {
		return intersect;
	}
	
	public Ray getRay() {
		return ray;
	}

	public static class Builder {
		private PhysicsEntity<?, ?> first;
		private PhysicsEntity<?, ?> second;
		private Vec2f mtv, intersect;
		private Ray ray;
		
		public Builder() {
			first = null;
			second = null;
			mtv = null;
			ray = null;
		}
		
		public Builder addFirst(PhysicsEntity<?, ?> first) {
			this.first = first;
			return this;
		}
		
		public Builder addSecond(PhysicsEntity<?, ?> second) {
			this.second = second;
			this.ray = null;
			return this;
		}
		
		public Builder setMtv(Vec2f mtv) {
			this.mtv = mtv;
			return this;
		}
		
		public Builder setIntersect(Vec2f inter) {
			this.intersect = inter;
			return this;
		}
		
		public Builder setRay(Ray ray) {
			this.second = null;
			this.mtv = null;
			this.ray = ray;
			return this;
		}
		
		public Collision build() {
			return new Collision(first, second, mtv, ray, intersect);
		}
		
	}
	
	public Collision negateMTV() {
		return new Collision(second, first, mtv.smult(-1), ray, intersect);
	}

	public boolean isStatic() {
		return stat;
	}

}
