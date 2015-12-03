package lcamery.engine;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Set;

import lcamery.engine.Node;
import support.Vec2i;

public class Pathfinding {
	
	/*
	 * A* wrapper that resets all the references
	 */
	public static LinkedList<? extends Node> getSafePath(Node[][] board, Vec2i start, Vec2i fin, Master m, Iterable<? extends Sprite> spriteSet) {
		LinkedList<? extends Node> toReturn = Pathfinding.getPath(board, start, fin, m, spriteSet);
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j].setDistance(Integer.MAX_VALUE);
				board[i][j].clearEstimate();
				board[i][j].setPrev(null);
			}
		}
		
		return toReturn;
	}
	
	/**
	 * A* implementation over Tile graph
	 * @param start : coordinates of start
	 * @param fin : coordinates of end
	 * @return LinkedList<Tile> representing path
	 */
	private static LinkedList<? extends Node> getPath(Node[][] board, Vec2i start, Vec2i fin, Master m, Iterable<? extends Sprite> spriteSet) {
		LinkedList<Node> path = new LinkedList<Node>();
		PriorityQueue<Node> open = new PriorityQueue<Node>();
		open.add(board[start.x][start.y]);
		HashSet<Node> done = new HashSet<Node>();
		if (board[fin.x][fin.y].impassible()) {
			return null;
		}
		for (Sprite s : spriteSet) {
			if (s.getPlayer() == m.whoseTurn() && s.getLocation().equals(new Vec2i(fin.y,fin.x))) {
				return null;
			}
		}
		
		board[start.x][start.y].setDistance(0);
		while (!open.isEmpty()) {
			Node curr = open.poll();
			if (fin.x == curr.getCoords().x && fin.y == curr.getCoords().y) {
				Node t = curr;
				while (t.getPrev() != null) {
					path.addFirst(t);
					t = t.getPrev();
				}
				
				return path;
			}

			done.add(curr);
			
			for (Node neigh : Pathfinding.passableNeighbors(board, curr, m, spriteSet)) {
				if (done.contains(neigh)) {
					continue;
				}
				
				if (neigh.getDistance() > curr.getDistance() + 1) {
					neigh.setDistance(curr.getDistance() + 1);
					neigh.setPrev(curr);
					neigh.setEstimate(board[fin.x][fin.y]);
				}
				open.add(neigh);
			}
		}
		return null;
	}
	
	/*
	 * Helper for A*
	 * Finds passable neighbors for any tile
	 */
	private static Set<Node> passableNeighbors(Node[][] board, Node t, Master m, Iterable<? extends Sprite> spriteSet) {
		Set<Node> neighs = new HashSet<Node>();
		Vec2i loc = t.getCoords();
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (j == i || (j != 0 && i != 0)) {
					continue;
				}else if (loc.x + i >= 0 && loc.x + i < board[0].length && loc.y + j >=0 && loc.y + j < board.length &&
						!board[(loc.x+i)][(loc.y+j)].impassible()) {
					neighs.add(board[loc.x+i][loc.y+j]);
				}
			}
		}

		for (Sprite s : spriteSet) {
			if (s.getPlayer() != m.whoseTurn() && neighs.contains(board[s.getLocation().y][s.getLocation().x])) {
				neighs.remove(board[s.getLocation().y][s.getLocation().x]);
			}
		}
		
		return neighs;
	}
}
