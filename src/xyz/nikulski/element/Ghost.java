package xyz.nikulski.element;

import static xyz.nikulski.main.Controller.*;
import static xyz.nikulski.main.Utils.*;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.opengl.Texture;

import xyz.nikulski.main.Controller;
import xyz.nikulski.main.Utils.Direction;

public class Ghost extends Character {
	private int resetX;
	private int resetY;
	private Texture eye;

	/**
	 * @param nick Nickname
	 * @param x x-Position
	 * @param y y-Position
	 */
	public Ghost(String nick, int x, int y) {
		super(null, x, y);
		if (nick.equals("blinky")) {
			texture = (Texture) loadMedia("res/characters/cpu1-blinky", "png");

		} else if (nick.equals("inky")) {
			texture = (Texture) loadMedia("res/characters/cpu2-inky", "png");

		} else if (nick.equals("clyde")) {
			texture = (Texture) loadMedia("res/characters/cpu3-clyde", "png");

		} else if (nick.equals("pinky")) {
			texture = (Texture) loadMedia("res/characters/cpu4-pinky", "png");
		}
		direction = Direction.NONE;
		this.nick = nick;
		resetX = x;
		resetY = y;
		speed = 4;
		eye = (Texture) loadMedia("res/characters/eye-" + getRanNum(0, 3), "png");
	}

	/**
	 * Update Ghost
	 */
	public void update() {
		int xT = x / tSize;
		int yT = y / tSize;
		
		if (!hasMoved) {
			// Waiting for start

			// Change direction only on edges
		} else if (x == 0 && y == 448 && direction == Direction.LEFT) {
			x = lWidth;
			x += direction.getDx() * speed;
			y += direction.getDy() * speed;

		} else if (x == lWidth - tSize && y == 448 && direction == Direction.RIGHT) {
			x = 0;
			x += direction.getDx() * speed;
			y += direction.getDy() * speed;

		} else if (x % tSize == 0 && y % tSize == 0) {

				int[][] path = g.getPath();

				List<Direction> dirs = new ArrayList<Direction>();

				for (int i = 1; i <= 4; i++) {
					if (g.isMoveable(xT + Direction.values()[i].getDx(), yT + Direction.values()[i].getDy())) {
						dirs.add(Direction.values()[i]);
					}
				}

				int fieldCount = path[xT][yT];
				Direction d = Direction.NONE;

				if (Controller.isInvulnerable()) {
					for (Direction dir : dirs) {
						if (path[xT + dir.getDx()][yT + dir.getDy()] > fieldCount) {
							fieldCount = path[xT + dir.getDx()][yT + dir.getDy()];
							d = dir;
						}
					}
				} else {
					for (Direction dir : dirs) {
						if (path[xT + dir.getDx()][yT + dir.getDy()] < fieldCount) {
							fieldCount = path[xT + dir.getDx()][yT + dir.getDy()];
							d = dir;
						}
					}
				}

				direction = d;

				// Check wall
				if (!g.getTile(xT + direction.getDx(), yT + direction.getDy()).getType().isWall()) {
					x += direction.getDx() * speed;
					y += direction.getDy() * speed;
				}

		} else {
			x += direction.getDx() * speed;
			y += direction.getDy() * speed;
		}
	}

	/**
	 *  Draw ghost
	 */
	public void draw() {
		if (direction == Direction.UP && !Controller.isInvulnerable()) {
			drawQuadTex(texture, (Texture) loadMedia("res/characters/eye-0", "png"), x, y);

		} else if (direction == Direction.RIGHT && !Controller.isInvulnerable()) {
			drawQuadTex(texture, (Texture) loadMedia("res/characters/eye-1", "png"), x, y);

		} else if (direction == Direction.DOWN && !Controller.isInvulnerable()) {
			drawQuadTex(texture, (Texture) loadMedia("res/characters/eye-2", "png"), x, y);

		} else if (direction == Direction.LEFT && !Controller.isInvulnerable()) {
			drawQuadTex(texture, (Texture) loadMedia("res/characters/eye-3", "png"), x, y);

		} else if (direction == Direction.NONE && !Controller.isInvulnerable()) {
			drawQuadTex(texture, eye, x, y);

		} else {
			drawQuadTex((Texture) loadMedia("res/characters/reverse-0", "png"), x, y);
		}
	}

	/**
	 * @return Returns a random direction regarding to in which direction the ghost currently looks
	 */
	private Direction getRandomDir() {
		int xT = x / tSize;
		int yT = y / tSize;
		int ran = getRanNum(0, 1);
		if (direction == Direction.UP && !g.getTile(xT + 1, yT).getType().isWall()
				|| direction == Direction.UP && !g.getTile(xT - 1, yT).getType().isWall()) {
			if (ran == 0)
				return Direction.RIGHT;
			else
				return Direction.LEFT;

		} else if (direction == Direction.RIGHT && !g.getTile(xT, yT - 1).getType().isWall()
				|| direction == Direction.RIGHT && !g.getTile(xT, yT + 1).getType().isWall()) {
			if (ran == 0)
				return Direction.UP;
			else
				return Direction.DOWN;

		} else if (direction == Direction.DOWN && !g.getTile(xT + 1, yT).getType().isWall()
				|| direction == Direction.DOWN && !g.getTile(xT - 1, yT).getType().isWall()) {
			if (ran == 0)
				return Direction.RIGHT;
			else
				return Direction.LEFT;

		} else if (direction == Direction.LEFT && !g.getTile(xT, yT + 1).getType().isWall()
				|| direction == Direction.LEFT && !g.getTile(xT, yT - 1).getType().isWall()) {
			if (ran == 0)
				return Direction.UP;
			else
				return Direction.DOWN;

		} else {
			return Direction.NONE;
		}
	}

	/**
	 * Resets Ghost to resetX / Y
	 */
	public void reset() {
		x = resetX;
		y = resetY;
	}

}
