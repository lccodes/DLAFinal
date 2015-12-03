package lcamery.engine.ai;

import java.util.LinkedList;

public class Action implements BTNode {
	private LinkedList<Action> children = null;
	private int last = 0;
	private Do d = null;
	private ActionBuilder build;
	BlackBoard board = null;
	
	public Action() {
		throw new IllegalArgumentException();
	}
	
	private Action(LinkedList<Action> children, Do d, ActionBuilder build) {
		this.children = children;
		this.d = d;
		this.build = build;
	}
	
	public static class ActionBuilder {
		private LinkedList<Action> children = null;
		private Do d = null;
		
		public ActionBuilder addAction(Action a) {
			this.d = null;
			if (children == null) {
				children = new LinkedList<Action>();
			}
				
			children.add(a);
			return this;
		}
		
		public ActionBuilder addDo(Do d) {
			this.children = null;
			this.d = d;
			return this;
		}
		
		public Action build() {
			return new Action(this.children, this.d, this);
		}
 	}

	@Override
	public BTStatus update(float tick) {
		if (this.d == null) {
			children.get(last).giveBoard(this.board);
			BTStatus ret = children.get(last).update(tick);
			tick = (tick + 1) % children.size();
			return ret;
		} else if (this.children == null) {
			return d.take(this.board);
		}
		
		return null;
	}

	@Override
	public void reset() {
		Action temp = build.build();
		this.children = temp.getChildren();
		this.d = temp.getDo();
		this.last = 0;
	}
	
	private LinkedList<Action> getChildren() {
		return this.children;
	}
	
	private Do getDo() {
		return this.d;
	}

	public void giveBoard(BlackBoard b) {
		this.board = b;
	}
	
	public BlackBoard getBoard() {
		return this.board;
	}

}
