package lcamery.game.support;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import lcamery.engine.Engine;
import support.Vec2i;

public abstract class Screen {
	protected Engine e;
	
	public Screen(Engine e) {
		this.e = e;
	}
	
	public void onTick(long nanosSincePreviousTick) {
		
	}

	public void onDraw(Graphics2D g) {
		
	}

	public void onKeyTyped(KeyEvent e) {
		
	}

	public void onKeyPressed(KeyEvent e) {
		
	}

	public void onKeyReleased(KeyEvent e) {
		
	}

	public void onMouseClicked(MouseEvent e){
		
	}

	public void onMousePressed(MouseEvent e) {
		
	}

	public void onMouseReleased(MouseEvent e) {
		
	}

	public void onMouseDragged(MouseEvent e) {
		
	}

	public void onMouseMoved(MouseEvent e) {
		
	}

	public void onMouseWheelMoved(MouseWheelEvent e) {
		
	}

	public void onResize(Vec2i newSize) {
		
	}
	
	public void cleanup(){
		
	}
	
	public Screen restart() {
		return null;
	}
	
	public Screen onPop() {
		return this;
	}

}
