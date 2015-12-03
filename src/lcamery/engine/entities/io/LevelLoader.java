package lcamery.engine.entities.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lcamery.engine.entities.Collidable;
import lcamery.engine.entities.PhysicsEntity;
import lcamery.engine.entities.World;
import lcamery.engine.shapes.AAB;
import lcamery.engine.shapes.Circle;
import lcamery.engine.shapes.Polygon;
import support.CS1971LevelReader;
import support.LevelData;
import support.CS1971LevelReader.InvalidLevelException;
import support.LevelData.ConnectionData;
import support.LevelData.EntityData;
import support.LevelData.ShapeData;

public class LevelLoader {
	final Map<String, Class<?>> entityClasses;
	
	public LevelLoader(Map<String, Class<?>> setup) {		
		entityClasses = setup;
	}

	public World loadWorld(String file) {
		World w = new World();
		w.stop();
		
		try {
			LevelData level = CS1971LevelReader.readLevel(new File(file));
			Map<String, PhysicsEntity<Map<String,String>, String>> entities = this.loadEntities(w, level);
			if (entities.isEmpty()) {
				throw new InvalidLevelException("No entities");
			}
			this.connect(w, entities, level);
			
		} catch (FileNotFoundException | InvalidLevelException e) {
			System.out.println(e.getMessage());
			return null;
		}
		
		return w;
	}
	
	private void connect(World w, Map<String, PhysicsEntity<Map<String,String>, String>> entities, LevelData level) throws InvalidLevelException {
		for (ConnectionData cd : level.getConnections()) {
			PhysicsEntity<Map<String,String>, String> target = entities.get(cd.getTarget());
			PhysicsEntity<Map<String,String>, String> source = entities.get(cd.getSource());
			
			Connection<Map<String, String>, String> c = 
					new Connection<Map<String, String>, String>(cd.getProperties(),target.getInput());
			Output<Map<String, String>, String> o = new Output<Map<String, String>, String>();
			o.connect(c);
			source.setOutput(o);
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, PhysicsEntity<Map<String,String>, String>> loadEntities(World w, LevelData level) throws InvalidLevelException {
		Map<String, PhysicsEntity<Map<String,String>, String>> constructedEntities 
			= new HashMap<String, PhysicsEntity<Map<String,String>, String>>();
		List<? extends EntityData> rawEntities = level.getEntities();
		for (EntityData e : rawEntities) {
			List<Collidable> shapes = new LinkedList<Collidable>();
			for (ShapeData d : e.getShapes()) {
				switch(d.getType()) {
				case CIRCLE:
					shapes.add(new Circle(d.getRadius(), d.getCenter()));
					break;
				case BOX:
					shapes.add(new AAB(d.getMin(), d.getMax().minus(d.getMin())));
					break;
				case POLY:
					shapes.add(new Polygon(d.getVerts()));
					break;
				default:
					throw new InvalidLevelException("Unrecognized shape");
				}
			}
			
			if (shapes.size() == 0) {
				throw new InvalidLevelException("Shapeless entity");
			}
			
			try {
				Class<?> x = entityClasses.get(e.getEntityClass());
				if (x == null) {
					continue;
				}
				Constructor<?> cons = x.getConstructor(World.class, List.class, Map.class);
				PhysicsEntity<Map<String,String>, String> entity =
						(PhysicsEntity<Map<String,String>, String>) 
						cons.newInstance(new Object[]{w, shapes, e.getProperties()});
				constructedEntities.put(e.getName(), entity);
				w.queueAdd(entity);
			} catch (NoSuchMethodException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvocationTargetException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return constructedEntities;
	}
}
