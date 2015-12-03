package lcamery.engine.entities;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import support.Vec2f;

public abstract class PhysicsEntity<B,T> extends Entity<B, T> {
	protected Collidable shape;
	protected Vec2f velocity, acc, impulse, force;
	protected World w;
	protected float mass, elasticity;
	protected boolean stat, collided;
	public final String name;
	
	public PhysicsEntity(World w,
			List<Collidable> shapes, 
				Map<String, String> properties) {
		this.shape = shapes.get(0);
		if (properties.containsKey("velocity")) {
			String vel = properties.get("velocity");
			String[] xy = vel.split(":");
			this.velocity = new Vec2f(Float.parseFloat(xy[0]), 
					Float.parseFloat(xy[1]));
		} else {
			this.velocity = new Vec2f(0,0);
		}
		this.w = w;
		if (properties.containsKey("acceleration")) {
			String vel = properties.get("acceleration");
			String[] xy = vel.split(":");
			this.acc = new Vec2f(Float.parseFloat(xy[0]), 
					Float.parseFloat(xy[0]));
		} else {
			this.acc = new Vec2f(0,0);
		}
		if (properties.containsKey("mass")) {
			this.mass = Float.parseFloat(properties.get("mass"));
		} else {
			throw new IllegalArgumentException("No mass");
		}
		this.impulse = new Vec2f(0,0);
		this.force = new Vec2f(0,0);
		if (properties.containsKey("elasticity")) {
			this.elasticity = Float.parseFloat(properties.get("elasticity"));
		} else {
			throw new IllegalArgumentException("No elasticity");
		}
		if (properties.containsKey("name")) {
			this.name = properties.get("name");
		} else {
			throw new IllegalArgumentException("No name");
		}
		this.stat = false;
		this.collided = false;
	}
	
	/*
	 * Moves the entity by the diff and then updates
	 * based on the time difference and the acceleration
	 * @param dif : the mandatory movement
	 * @param time : the time stamp for the acceleration movement 
	 */
	public void move(Vec2f dif, long time) {
		float t = (float) time / 100000000;
		shape.move(dif.x, dif.y);
		velocity = velocity.plus(new Vec2f(acc.smult(t).x, acc.smult(t).y));
		shape.move(velocity.x, velocity.y);
	}
	
	public void move(Vec2f dif) {
		shape.move(dif.x, dif.y);
	}
	
	public void setSpeed(Vec2f velocity) {
		this.velocity = velocity;
	}
	
	public void setAcceleration(Vec2f acc) {
		this.acc = acc;
	}
	
	public Collidable getShape() {
		return this.shape;
	}
	
	public boolean isCollided(PhysicsEntity<?,?> o) {
		if (o == this) {
			return false;
		}
		return this.shape.isCollided(o.getShape());
	}
	
	public void applyForce(Vec2f force) {
		this.force = this.force.plus(force);
	}
	
	public void applyImpulse(Vec2f impulse) {
		this.impulse = this.impulse.plus(impulse);
	}
	
	/*
	 * Provided for game side logic on collision
	 */
	public abstract void collide(Collision c);
	
	/*
	 * Maintains phsyics
	 * @Note: no need to override for static
	 */
	public void engineCollide(Collision c) {
		if (c.getSecond().isStatic()) {
			this.move(c.getMtv());
		} else {
			this.move(c.getMtv().sdiv(2));
		}
		
		Vec2f first = c.getFirst().getVelocity().projectOnto(c.getMtv());
		Vec2f second = c.getSecond().getVelocity().projectOnto(c.getMtv());
		if (((Float) first.x).isNaN() || ((Float) first.y).isNaN() || ((Float) second.x).isNaN()
				|| ((Float) second.y).isNaN()) {
			return;
		}

		float COR = (float) Math.sqrt(c.getFirst().getElastic() * c.getSecond().getElastic());
		if (c.getSecond().isStatic()) {
			this.applyImpulse((second.minus(first)).smult(c.getSecond().getMass()*(1+COR)));
		} else {
			this.applyImpulse(second.minus(first).smult(
					c.getFirst().getMass()*c.getSecond().getMass()*(1+COR)
					/(c.getFirst().getMass() + c.getSecond().getMass())));
		}
	}
	
	private Vec2f getVelocity() {
		return this.velocity;
	}

	private double getElastic() {
		return this.elasticity;
	}

	/*
	 * Called every world tick
	 */
	public void tick(float t) {
		this.velocity = this.velocity.plus(force.sdiv(mass).smult(t).plus(impulse.sdiv(mass)));
		this.shape.move(this.velocity.smult(t).x, this.velocity.smult(t).y);
		this.force = new Vec2f(0,0);
		this.impulse = new Vec2f(0,0);
	}
	
	public Vec2f computeMTV(PhysicsEntity<?,?> e) {
		return this.shape.computeMTV(e.getShape());
	}

	public float getMass() {
		return mass;
	}

	public abstract boolean isStatic();
	
	public void setElasticity(float e) {
		this.elasticity = e;
	}
	
	public void setStatic(boolean newStat) {
		this.stat = newStat;
	}

	public void setColliding(boolean b) {
		this.collided = b;
	}
	
	public boolean getCollided() {
		return this.collided;
	}

	public void setColor(Color blue) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isInvisible() {
		return false;
	}

}
