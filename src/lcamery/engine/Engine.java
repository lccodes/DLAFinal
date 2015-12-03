package lcamery.engine;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

import lcamery.game.support.Screen;
import support.SwingFrontEnd;
import support.Vec2i;

public class Engine extends SwingFrontEnd {
	private ArrayList<Screen> screenList = new ArrayList<Screen>();
	private int currentScreen = -1;
	private Vec2i size;
	
	public Engine() {
		super(null, false);
		throw new IllegalArgumentException();
	}

	private Engine(String title, boolean fullscreen) {
		super(title, fullscreen);
	}
	
	public static class EngineBuilder {
		private String title;
		private boolean fullscreen;
		
		public EngineBuilder() {
			this.title = null;
			this.fullscreen = false;
		}
		
		public EngineBuilder addTitle(String title) {
			this.title = title;
			return this;
		}
		
		public EngineBuilder setFullscreen(boolean full) {
			this.fullscreen = full;
			return this;
		}
		
		public Engine build() {
			return new Engine(title, fullscreen);
		}
	}

	@Override
	protected void onTick(long nanosSincePreviousTick) {
		screenList.get(currentScreen).onTick(nanosSincePreviousTick);
	}

	@Override
	protected void onDraw(Graphics2D g) {
		screenList.get(currentScreen).onDraw(g);
	}

	@Override
	protected void onKeyTyped(KeyEvent e) {
		screenList.get(currentScreen).onKeyTyped(e);
	}

	@Override
	protected void onKeyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_Q && currentScreen != 0) {
			screenList.get(currentScreen).onPop();
			pop();
		} else {
			screenList.get(currentScreen).onKeyPressed(e);
		}
	}

	@Override
	protected void onKeyReleased(KeyEvent e) {
		screenList.get(currentScreen).onKeyReleased(e);
	}

	@Override
	protected void onMouseClicked(MouseEvent e) {
		screenList.get(currentScreen).onMouseClicked(e);
	}

	@Override
	protected void onMousePressed(MouseEvent e) {
		screenList.get(currentScreen).onMousePressed(e);
	}

	@Override
	protected void onMouseReleased(MouseEvent e) {
		screenList.get(currentScreen).onMouseReleased(e);
	}

	@Override
	protected void onMouseDragged(MouseEvent e) {
		screenList.get(currentScreen).onMouseDragged(e);
	}

	@Override
	protected void onMouseMoved(MouseEvent e) {
		screenList.get(currentScreen).onMouseMoved(e);
	}

	@Override
	protected void onMouseWheelMoved(MouseWheelEvent e) {
		screenList.get(currentScreen).onMouseWheelMoved(e);
	}

	@Override
	protected void onResize(Vec2i newSize) {
		this.size = newSize;
		screenList.get(currentScreen).onResize(newSize);
	}
	
	public void push(Screen s) {
		screenList.add(s);
		currentScreen++;
	}
	
	public void pop() {
		screenList.get(currentScreen).cleanup();
		screenList.remove(currentScreen);
		currentScreen--;
	}
	
	public Vec2i getSize() {
		return this.size;
	}

}
