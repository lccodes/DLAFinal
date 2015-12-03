package lcamery.engine;

import java.util.Set;

import lcamery.engine.support.AIPlayer;
import lcamery.engine.support.HumanPlayer;
import lcamery.engine.support.Player;

public class Master {
	private Player p1;
	private Player p2;
	boolean turn = true;
	private int actionsLeft = 3;
	
	public Master(boolean player1, boolean player2) {
		if (player1) {
			p1 = new HumanPlayer();
		}else{
			p1 = new AIPlayer();
		}
		if (player2) {
			p2 = new HumanPlayer();
		}else {
			p2 = new AIPlayer();
		}
	}
	
	public boolean whoseTurn() {
		return turn;
	}
	
	public boolean isOver(Set<? extends Sprite> spriteSet) {
		boolean player = false;
		boolean first = true;
		for (Sprite s : spriteSet) {
			if (first) {
				player = s.getPlayer();
				first = false;
			} else {
				if (player != s.getPlayer()) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public void nextTurn() {
		actionsLeft = 3;
		if (turn) {
			p1.play();
			turn = false;
		} else {
			p2.play();
			turn = true;
		}
	}
	
	public void actionTaken() {
		this.actionsLeft--;
		if (this.turnOver()) {
			this.nextTurn();
		}
	}
	
	public boolean turnOver() {
		return this.actionsLeft == 0;
	}

	public void tick(long nanosSincePreviousTick) {
		p1.tick(nanosSincePreviousTick);
		p2.tick(nanosSincePreviousTick);
	}
	
	public boolean controlTurn() {
		return (p1.controlable() && turn)
				|| (p2.controlable() && !turn);
	}
	
	public void takeTurn() {
		if(turn) {
			p1.play();
		} else {
			p2.play();
		}
	}

}
