package lcamery.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lcamery.engine.Engine;
import lcamery.game.Destination.DestinationBuilder;
import lcamery.game.support.Screen;

public class StartScreen extends Screen {
	private Map<String, Color> colors;
	private Map<String, Font> fonts;
	private List<Destination> destinations;
	private boolean next;
	private int wait = 200;
	private Destination selected;

	public StartScreen(Engine e) {
		super(e);
		next = false;
		colors = new HashMap<String, Color>();
		fonts = new HashMap<String, Font>();
		destinations = new LinkedList<Destination>();
		selected = null;
		
		Set<String> cities = new HashSet<String>();
		cities.add("London");
		cities.add("New York (JFK)");
		cities.add("Paris - ORLY");
		cities.add("Cape Town");
		cities.add("Tangier");
		cities.add("Washington D.C.");
		cities.add("Buenos Aires");
		cities.add("Madrid");
		cities.add("Sydney");
		cities.add("Zurich");
		cities.add("Dublin");
		cities.add("New York (LGA)");
		cities.add("Amsterdam");
		cities.add("Glasgow");
		cities.add("Barcelona");
		
		Set<String> codes = new HashSet<String>();
		codes.add("DL");
		codes.add("BA");
		codes.add("AF");
		codes.add("KL");
		codes.add("EK");
		codes.add("LH");
		
		for (int i = 0; i < 20; i++) {
			DestinationBuilder db = new DestinationBuilder();
			
			int randomCode = (int) (Math.random() * codes.size());
			int randomCity = (int) (Math.random() * cities.size());
			int j = 0;
			for (String c : cities) {
				if (j++ == randomCity) {
					db.addDest(c);
					break;
				}
			}
			j = 0;
			for (String c : codes) {
				if (j++ == randomCode) {
					db.addCode(c);
					break;
				}
			}
			destinations.add(db.build());
		}
		fonts.put("bodoni", new Font("Bodoni", Font.PLAIN, 50));
		fonts.put("tnr", new Font("TimesNewRoman", Font.PLAIN, 25));
		float[] vals = Color.RGBtoHSB(200, 10, 40, null);
		colors.put("maroon", Color.getHSBColor(vals[0], vals[1], vals[2]));
		vals = Color.RGBtoHSB(50, 10, 220, null);
		colors.put("darkblue", Color.getHSBColor(vals[0], vals[1], vals[2]));
		
		Collections.sort(destinations, new Comparator<Destination>() {
			@Override
			public int compare(Destination arg0, Destination arg1) {
				return arg0.getTime().compareTo(arg1.getTime());
			}
		});
	}
	
	@Override
	public void onTick(long nanos) {
		if (next) {
			wait--;
		}
		if (wait <= -100) {
			this.e.push(new AirportScreen(this.e, selected));
		}
	}
	
	@Override
	public void onDraw(Graphics2D g) {
		Rectangle2D background = new Rectangle2D.Double(0,0, this.e.getSize().x, this.e.getSize().y);
		g.setColor(colors.get("maroon"));
		g.fill(background);
		g.draw(background);
		
		Rectangle2D board = new Rectangle2D.Double(50, 50, this.e.getSize().x-100, this.e.getSize().y-100);
		g.setColor(Color.BLACK);
		g.fill(board);
		g.draw(board);
		
		g.setColor(colors.get("darkblue"));
		g.setFont(fonts.get("bodoni"));
		g.drawString("DEPARTURES", e.getSize().x/2 - g.getFontMetrics().stringWidth("DEPARTURES")/2, 100);
		
		int y = 150;
		g.setFont(fonts.get("tnr"));
		g.setColor(Color.WHITE);
		for (Destination d : destinations) {
			if (y >= e.getSize().y-75) {
				break;
			}
			if (!next || wait >= 125 || d.getStatus() != 0) {
				g.drawString(d.CODE, (int) (this.e.getSize().x * .07), y);
				g.drawString(d.DESTINATION, (int) (this.e.getSize().x * .2), y);
				g.drawString(d.getTime(), (int) (this.e.getSize().x * .5), y);
				String stat = "ON TIME";
				if (d.getStatus() != 0) {
					stat = "DELAYED";
				}
				g.drawString(stat, (int) (this.e.getSize().x * .7), y);
				g.drawString(d.getGate(), (int) (this.e.getSize().x * .85), y);
			}
			y+=30;
		}
	}
	
	@Override
	public void onMousePressed(MouseEvent me) {
		int y = 150;
		if (next) {
			return;
		}
		for (Destination d : destinations) {
			if (me.getY() > y - 30 && me.getY() < y ) {
				d.setStatus(1);
				next = true;
				selected = d;
			}
			y += 30;
		}
	}

}
