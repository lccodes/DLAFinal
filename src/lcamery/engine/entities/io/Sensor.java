package lcamery.engine.entities.io;

import java.awt.Graphics2D;
import java.util.List;
import java.util.Map;

import lcamery.engine.entities.Collidable;
import lcamery.engine.entities.Collision;
import lcamery.engine.entities.World;
import support.Vec2f;

public class Sensor extends lcamery.engine.entities.PhysicsEntity<Map<String, String>, String> {
	Output<Map<String, String>, String> out;

	public Sensor(World w, List<Collidable> shapes, Map<String, String> properties) {
		super(w, shapes, properties);
	}

	@Override
	public void collide(Collision c) {
		this.out.run();
	}
	
	@Override
	public void engineCollide(Collision c) {
		// Noop
	}
	
	@Override
	public void applyForce(Vec2f force) {
		//Noop
	}
	
	@Override
	public void applyImpulse(Vec2f impulse) {
		//Noop
	}
	
	@Override
	public void move(Vec2f where) {
		//Noop
	}

	@Override
	public boolean isStatic() {
		return false;
	}

	@Override
	public void draw(Graphics2D g) {
		//Noop
	}

	@Override
	public int type() {
		return 99;
	}

	@Override
	public void setOutput(Output<Map<String, String>, String> out) {
		this.out = out;
	}

	@Override
	public String dispatch(Map<String, String> b) {
		// Noop
		return null;
	}
	
	@Override
	public boolean isInvisible() {
		return true;
	}

}
