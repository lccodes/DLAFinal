package lcamery.engine.entities.io;

import java.awt.Graphics2D;
import java.util.List;
import java.util.Map;

import lcamery.engine.entities.Collidable;
import lcamery.engine.entities.Collision;
import lcamery.engine.entities.World;
import support.Vec2f;

public class Relay extends lcamery.engine.entities.PhysicsEntity<Map<String, String>, String> {
	private Output<Map<String, String>, String> out;
	private boolean open;

	public Relay(World w, List<Collidable> shapes, Map<String, String> properties) {
		super(w, shapes, properties);
		this.open = false;
		Relay r = this;
		this.in = new Input<Map<String,String>,String>() {
			@Override
			public String run(Map<String,String> args) {
				return r.dispatch(args);
			}
		};
	}

	@Override
	public void collide(Collision c) {
		
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
		if (open) {
			out.run();
			return "success";
		}
		
		return "failure";
	}
	
	@Override
	public boolean isInvisible() {
		return true;
	}
	
	public void toggle() {
		this.open = !this.open;
	}

}