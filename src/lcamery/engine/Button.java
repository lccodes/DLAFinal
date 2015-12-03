package lcamery.engine;

import java.awt.geom.Rectangle2D;

import lcamery.game.support.Screen;
import support.Vec2i;

public class Button extends Rectangle2D {
	private Vec2i loc;
	private Vec2i size;
	private String theString;
	private Screen screen;
	
	public Button(String s, Screen screen) {
		this.theString = s;
		this.screen = screen;
	}
	
	@Override
	public Rectangle2D createIntersection(Rectangle2D arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle2D createUnion(Rectangle2D arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int outcode(double arg0, double arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setRect(double arg0, double arg1, double arg2, double arg3) {
		loc = new Vec2i((int) arg0, (int) arg1);
		size = new Vec2i((int) arg2, (int) arg3);
	}

	@Override
	public double getHeight() {
		return size.y;
	}
	
	public int getStringLen(){
		return this.theString.length();
	}
	
	public void backspace(){
		if (this.theString.length() > 0){
		this.theString = this.theString.substring(0, this.theString.length()-1);
		}
	}
	
	public void appendChar(char c){
		if (this.theString.length() < 20){
		StringBuilder sb = new StringBuilder();
		sb.append(this.theString);
		sb.append(c);
		this.theString = sb.toString();
		}
	}

	@Override
	public double getWidth() {
		return size.x;
	}

	@Override
	public double getX() {
		return loc.x;
	}

	@Override
	public double getY() {
		return loc.y;
	}
	
	public String getString() {
		return theString;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Screen getScreen() {
		return this.screen;
	}
	
	public void setScreen(Screen s){
		this.screen = s;
	}

}