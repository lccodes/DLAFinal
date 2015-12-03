package lcamery.engine.entities;

import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import lcamery.engine.entities.io.Relay;
import support.Vec2f;

public class World {
	private LinkedList<PhysicsEntity<?,?>> entities;
	private Set<PhysicsEntity<?,?>> addList;
	private Set<PhysicsEntity<?,?>> removeList;
	private boolean stop;
	
	public World() {
		stop = false;
		entities = new LinkedList<PhysicsEntity<?,?>>();
		addList = new HashSet<PhysicsEntity<?,?>>();
		removeList = new HashSet<PhysicsEntity<?,?>>();
	}
	
	public void toggleRelays() {
		for (PhysicsEntity<?,?> e : entities) {
			if (e instanceof Relay) {
				((Relay) e).toggle();
			}
		}
	}
	

	public void draw(Graphics2D g) {
		for (PhysicsEntity<?,?> e : entities) {
			e.draw(g);
		}
	}
	
	public void tick(long nanos) {
		if (stop) {
			return;
		}
		
		this.checkCollisions();
		
		for (PhysicsEntity<?,?> e : addList) {
			boolean add = true;
			for (PhysicsEntity<?,?> already : entities) {
				if (e.name.equals(already.name)) {
					add = false;
					break;
				}
			}
			if (add)
				entities.add(e);
		}
		
		for (PhysicsEntity<?,?> e : removeList) {
			for (PhysicsEntity<?,?> ent : entities) {
				if (ent.name.equals(e.name)) {
					removeList.add(ent);
				}
			}
		}
		
		for (PhysicsEntity<?,?> e : removeList) {
			entities.remove(e);
		}
		
		addList.clear();
		removeList.clear();
		for (PhysicsEntity<?,?> e : entities) {
			e.tick(nanos/10000000);
		}
	}
	
	public void queueAdd(PhysicsEntity<?,?> e) {
		addList.add(e);
	}
	
	public void queueRemove(PhysicsEntity<?,?> e) {
		removeList.add(e);
	}
	
	public void checkCollisions() {
		if (stop) {
			return;
		}
		
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).setColliding(false);
		}
		
		for (int i = 0; i < entities.size(); i++) {
			for (int j = i+1; j < entities.size(); j++) {
				if (entities.get(i).isCollided(entities.get(j))) {
					Collision c = new Collision.Builder()
						.addFirst(entities.get(i))
						.addSecond(entities.get(j))
						.setMtv(entities.get(i).computeMTV(entities.get(j)))
						.build();

					if (c.getMtv() == null) {
						continue;
					}
					//Set collided
					//Engine apply correctional force
					if (!entities.get(j).isInvisible()) {
						entities.get(i).setColliding(true);
						entities.get(i).engineCollide(c);
						entities.get(i).collide(c);
					}
					if (!entities.get(i).isInvisible()) {
						entities.get(j).setColliding(true);
						entities.get(j).engineCollide(c.negateMTV());
						entities.get(j).collide(c.negateMTV());
					}
					//Let game deal with other aspects of collision
					
					
				}
			}
		}
	}
	
	public boolean contains(PhysicsEntity<?,?> e) {
		return entities.contains(e);
	}
	
	public int typeLeft(int i) {
		int x = 0;
		for (PhysicsEntity<?,?> e : entities) {
			if (e.type() == i) {
				x++;
			}
		}
		
		return x;
	}
	
	public void stop() {
		stop = true;
	}
	
	public void start() {
		stop = false;
	}
	
	/*
	 * Tests for a specific entity's 
	 */
	public boolean collides(PhysicsEntity<?,?> e) {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).isCollided(e)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Used to apply some force across all entities i.e. gravity
	 */
	public void applyForce(Vec2f force) {
		for (PhysicsEntity<?,?> e : entities) {
			e.applyForce(force);
		}
	}
	
	/*
	 * Sends a raycast onto a list of entities
	 * @return collision : ray collision object
	 */
	public Collision raycast(Ray r, PhysicsEntity<?,?> start) {
		Vec2f inter = null;
		PhysicsEntity<?,?> closest = null;
		float dist = Float.POSITIVE_INFINITY;
		for(PhysicsEntity<?,?> e : entities) {
			if (e != start) {
				Collision c = e.getShape().raycast(r, e);
				if (c != null && start.getShape().getLocation().dist2(c.getIntersect()) < dist) {
					dist = start.getShape().getLocation().dist2(c.getIntersect());
					closest = e;
					inter = c.getIntersect();
				}
			}
		}
		
		if (closest == null) {
			return null;
		}
		
		return new Collision.Builder()
			.addFirst(closest)
			.setRay(r)
			.setIntersect(inter)
			.build();
	}

	/*
	 * List of raycasts
	 */
	public List<Collision> listCast(PhysicsEntity<?,?> entity, float radius) {
		List<Collision> list = new LinkedList<Collision>();
		for (PhysicsEntity<?,?> e : entities) {
			Ray r = new Ray.RayBuilder()
				.addOrigin(entity.getShape().getLocation())
				.addAngle(e.getShape().getLocation().minus(entity.getShape().getLocation()).normalized())
				.build();
			Collision c = this.raycast(r, entity);
			if (c != null) {
				list.add(c);
			}
		}
		return list;
	}
	
	public PhysicsEntity<?,?> getByPredicate(Predicate<PhysicsEntity<?,?>> name) {
		for (PhysicsEntity<?,?> pe : entities) {
			if (name.test(pe)) {
				return pe;
			}
		}
		
		return null;
	}
}
