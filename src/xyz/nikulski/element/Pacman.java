package xyz.nikulski.element;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;
import xyz.nikulski.main.Utils.Direction;
import xyz.nikulski.main.Utils.TileType;

import static xyz.nikulski.main.Controller.*;
import static xyz.nikulski.main.Utils.*;

public class Pacman extends Character {

	// Decide when to change the mouth
	private int mState = 0;
	private int frame;

	/**
	 * @param x x-Position
	 * @param y y-Position
	 */
	public Pacman(int x, int y) {
		super((Texture) loadMedia("res/characters/pacman-0", "png"), x, y);
		nick = "pacman";
		speed = 4;
		direction = Direction.NONE; // View direction
	}

	/**
	 * @return Returns if pacman has moved, started the game
	 */
	public boolean hasMoved() {
		return hasMoved;
	}

	/**
	 * Update Pacman
	 */
	public void update() {
		// Pixel into Tile position
		int xT = x / tSize;
		int yT = y / tSize;

		if (!hasMoved) {
			// Waiting for start
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)) {
				direction = Direction.RIGHT;
				hasMoved = true;
				status = 0;

			} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)) {
				direction = Direction.LEFT;
				hasMoved = true;
				status = 0;
			}
			
		// Left / Right spawn
		} else if (x == 0 && y == 448 && direction == Direction.LEFT) {
			x = lWidth;
			x += direction.getDx() * speed;
			y += direction.getDy() * speed;

		} else if (x == lWidth - tSize && y == 448 && direction == Direction.RIGHT) {
			x = 0;
			x += direction.getDx() * speed;
			y += direction.getDy() * speed;

		// Change direction only on exact tile
		} else if (x % tSize == 0 && y % tSize == 0 && x < lWidth && y < lHeight) {

			// Reset path array
			g.resetPath();
			
			// Runs a new search with xT and yT
			g.updatePath(xT, yT);

			// Check keyboard input
			if ((Keyboard.isKeyDown(Keyboard.KEY_UP)  || Keyboard.isKeyDown(Keyboard.KEY_W)) && g.isMoveable(xT, yT - 1)) {
				direction = Direction.UP;

			} else if ((Keyboard.isKeyDown(Keyboard.KEY_RIGHT)  || Keyboard.isKeyDown(Keyboard.KEY_D)) && g.isMoveable(xT + 1, yT)) {
				direction = Direction.RIGHT;

			} else if ((Keyboard.isKeyDown(Keyboard.KEY_DOWN)  || Keyboard.isKeyDown(Keyboard.KEY_S)) && g.isMoveable(xT, yT + 1)) {
				direction = Direction.DOWN;

			} else if ((Keyboard.isKeyDown(Keyboard.KEY_LEFT)  || Keyboard.isKeyDown(Keyboard.KEY_A)) && g.isMoveable(xT - 1, yT)) {
				direction = Direction.LEFT;
			}

			if (g.isMoveable(xT + direction.getDx(), yT + direction.getDy())) {
				x += direction.getDx() * speed;
				y += direction.getDy() * speed;
			}

			// Eat dots
			if (g.getTile(xT, yT).getType() == TileType.DOT_NORMAL && g.getDots() != 0) {
				g.setTile(xT, yT, new Tile(xT * tSize, yT * tSize, 0, TileType.BLANK));
				SCORE += 1;

			// Eat powerpills
			} else if (g.getTile(xT, yT).getType() == TileType.DOT_POWERPILL && g.getDots() != 0) {
				g.setTile(xT, yT, new Tile(xT * tSize, yT * tSize, 0, TileType.BLANK));
				powerpill = 5;
				pacmanPower.playAsSoundEffect(1.0f, 1.0f, false);
//				SCORE += 100;

			}

		} else {
			x += direction.getDx() * speed;
			y += direction.getDy() * speed;
		}

		// Change mouth
		frame += delta;
		if (frame >= 30) {
			texture = (Texture) loadMedia("res/characters/pacman-" + mState, "png");
			if (mState != 4)
				mState++;
			else
				mState = 0;
			frame = 0;
		}
	}

	/**
	 *  Draw Pacman
	 */
	public void draw() {
		if (direction == Direction.UP)
			drawQuadTex(texture, x, y, 270);

		else if (direction == Direction.RIGHT || direction == Direction.NONE)
			drawQuadTex(texture, x, y);

		else if (direction == Direction.DOWN)
			drawQuadTex(texture, x, y, 90);

		else if (direction == Direction.LEFT)
			drawQuadTex(texture, x, y, 180);
	}

}
