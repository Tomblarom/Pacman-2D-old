package xyz.nikulski.element;

import org.newdawn.slick.opengl.Texture;

import xyz.nikulski.main.Utils.Direction;

public abstract class Character {
	protected Texture texture;
	protected int x, y, speed, bump;
	protected Direction direction;
	protected String nick;

	/**
	 * @param texture Character texture
	 * @param x x-Position
	 * @param y y-Position
	 */
	public Character(Texture texture, int x, int y) {
		this.texture = texture;
		this.x = x;
		this.y = y;
	}

	protected abstract void update();

	protected abstract void draw();

	/**
	 * @return x-Position
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return y-Position
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param x New x-Position
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @param y New y-Position
	 */
	public void setY(int y) {
		this.y = y;
	}
}
