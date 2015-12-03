package lcamery.engine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import support.Vec2i;

public class SpriteManager {
	private HashMap<String, BufferedImage> spriteMap;
	
	public SpriteManager() {
		spriteMap = new HashMap<String, BufferedImage>();
	}
	
	public void loadSpriteSheet(String location, String name) {
		BufferedImage newImage = null;
		try {
			newImage = ImageIO.read(new File(location));
		} catch (IOException e) {
			System.out.println("[ERROR] could not load sprite from " + location);
		}
		
		if (newImage != null) {
			spriteMap.put(name, newImage);
		}
	}
	
	public BufferedImage getSpriteSheet(String name) {
		return spriteMap.get(name);
	}
	
	public BufferedImage drawSprite(Sprite s) {
		return spriteMap.get("tank").getSubimage(s.getImageLocation().x, s.getImageLocation().y, 25, 25);
	}
	
	public BufferedImage drawTile(Vec2i loc) {
		return spriteMap.get("tank").getSubimage(loc.x, loc.y, 25, 25);
	}

}
