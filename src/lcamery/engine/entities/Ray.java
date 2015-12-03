package lcamery.engine.entities;

import support.Vec2f;

public class Ray {
	private final Vec2f origin;
	private final Vec2f angle;
	private Vec2f end;
	
	private Ray(Vec2f origin, Vec2f angle) {
		this.origin = origin;
		this.angle = angle;
	}
	
	public Vec2f getOrigin() {
		return origin;
	}
	
	public Vec2f getAngle() {
		return angle;
	}
	
	public void setEnd(Vec2f end) {
		this.end = end;
	}
	
	public Vec2f getEnd() {
		return this.end;
	}
	
	public static class RayBuilder {
		private Vec2f origin;
		private Vec2f angle;
		
		public RayBuilder addOrigin(Vec2f origin) {
			this.origin = origin;
			return this;
		}
		
		public RayBuilder addAngle(Vec2f angle) {
			this.angle = angle;
			return this;
		}
		
		public Ray build() {
			return new Ray(this.origin, this.angle);
		}
	}

	public Vec2f scale(float centerp) {
		return this.origin.plus(this.angle.smult(centerp));
	}
}
