package xyz.nikulski.element;

import static xyz.nikulski.main.Utils.*;

import org.newdawn.slick.opengl.Texture;

public class Tile {
	private float x, y, angle;
	private Texture texture;
	private TileType type;

	/**
	 * 
	 * @param x x-Position
	 * @param y y-Position
	 * @param angle Angle
	 * @param type TileType
	 */
	public Tile(float x, float y, float angle, TileType type) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.texture = (Texture) loadMedia(type.getTexName(), "png");
		this.angle = angle;
	}
	
	/**
	 * Draw Tile with given parameters
 	 */
	public void draw() {
		drawQuadTex(texture, x, y, angle);
	}

	/**
	 * @return Returns x-Position
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x New x
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @return Returns y-Position
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y New y
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * @return Returns current texture
	 */
	public Texture getTexture() {
		return texture;
	}

	/**
	 * @param texture New texture
	 */
	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	/**
	 * @return Returns current type
	 */
	public TileType getType() {
		return type;
	}

	/**
	 * @param type New TileType
	 */
	public void setType(TileType type) {
		this.type = type;
	}

	/**
	 * @return Returns current angle
	 */
	public float getAngle() {
		return angle;
	}
	
	/**
	 * @param angle New Angle
	 */
	public void setAngle(float angle) {
		this.angle = angle;
	}

}
