package lcamery.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import lcamery.engine.Engine;
import lcamery.game.support.Screen;

public class TextScreen extends Screen {
	private final String[] text;
	private final Font font;
	private final Color color;

	public TextScreen(Engine e, String[] text, Font f, Color c) {
		super(e);
		this.text = text;
		this.font = f;
		this.color = c;
	}
	
	@Override
	public void onDraw(Graphics2D g) {
		g.setColor(Color.DARK_GRAY);
		Rectangle2D rect = new Rectangle2D.Double(0,0, e.getSize().x, e.getSize().y);
		g.fill(rect);
		g.draw(rect);
		
		g.setFont(font);
		g.setColor(color);
		int y = e.getSize().y/3;
		for (String t : text) {
			g.drawString(t, e.getSize().x/2 - g.getFontMetrics().stringWidth(t)/2, y);
			y += g.getFontMetrics().getHeight()+5;
		}
	}
	
	@Override
	public void onMousePressed(MouseEvent e) {
		this.e.pop();
	}

}
