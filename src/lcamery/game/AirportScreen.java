package lcamery.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import lcamery.engine.Engine;
import lcamery.game.support.Screen;

public class AirportScreen extends Screen {
	private final Destination dest;
	private Map<String, Color> colors;
	private Map<String, Font> fonts;
	private Rectangle2D club;

	public AirportScreen(Engine e, Destination d) {
		super(e);
		this.dest = d;
		colors = new HashMap<String, Color>();
		float[] vals = Color.RGBtoHSB(90, 50, 200, null);
		colors.put("blue", Color.getHSBColor(vals[0], vals[1], vals[2]));
		
		fonts = new HashMap<String, Font>();
		fonts.put("main", new Font("TimesNewRoman", Font.ITALIC, 25));
		
		this.club = new Rectangle2D.Double(50,50, (int) (e.getSize().x*.2), (int) (e.getSize().y*.1));
	}
	
	@Override
	public void onDraw(Graphics2D g) {
		g.setColor(colors.get("blue"));
		Rectangle2D rect = new Rectangle2D.Double(0,0, e.getSize().x, e.getSize().y);
		g.fill(rect);
		g.draw(rect);
		
		g.setColor(Color.GRAY);
		g.fill(club);
		g.draw(club);
		
		g.setColor(Color.BLACK);
		g.setFont(fonts.get("main"));
		g.drawString("Sky Club", 50 + (int) (e.getSize().x*.15) - g.getFontMetrics().stringWidth("Sky Club"),
				60 + (int) (e.getSize().y*.1) - g.getFontMetrics().getHeight());
		
		int y = e.getSize().y/2;
		g.drawString(dest.CODE, (int) (this.e.getSize().x * .07), y);
		g.drawString(dest.DESTINATION, (int) (this.e.getSize().x * .2), y);
		g.drawString(dest.getTime(), (int) (this.e.getSize().x * .5), y);
		String stat = "DELAYED";
		
		g.drawString(stat, (int) (this.e.getSize().x * .7), y);
		g.drawString(dest.getGate(), (int) (this.e.getSize().x * .85), y);
	}
	
	@Override
	public void onMousePressed(MouseEvent e) {
		if (club.contains(e.getX(), e.getY())) {
			this.e.push(new TextScreen(this.e, new String[] {"Lorem ipsum test test i am testing","it's a dope day to test"}, fonts.get("main"),Color.WHITE));
		}
	}

}
