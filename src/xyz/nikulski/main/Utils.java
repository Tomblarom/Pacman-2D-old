package xyz.nikulski.main;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import org.apache.commons.io.IOUtils;
import xyz.nikulski.element.Character;

public class Utils {
	// Layout sizes
	public static final int tSize = 32;
	public static final int lWidth = 28 * tSize;
	public static final int lHeight = 31 * tSize;

	// Window sizes
	public static final int wWidth = lWidth + 8 * tSize;
	public static final int wHeight = lHeight;
	
	/**
	 * Check for collision
	 * 
	 * @param c1 Character 1
	 * @param c2 Character 2
	 * @param bump bumper
	 * @return if there's a collision
	 */
	public static boolean collidesWith(Character c1, Character c2, int bump) {
		return 
				   c1.getX() >= c2.getX() - bump
				&& c1.getX() <= c2.getX() + bump
				&& c1.getY() >= c2.getY() - bump
				&& c1.getY() <= c2.getY() + bump;
	}

	/**
	 * @param tex Main texture
	 * @param x x-Coordinate
	 * @param y y-Coordinate
	 */
	public static void drawQuadTex(Texture tex, float x, float y) {
		tex.bind();
		glTranslatef(x, y, 0);
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);
			glTexCoord2f(1, 0);
			glVertex2f(tSize, 0);
			glTexCoord2f(1, 1);
			glVertex2f(tSize, tSize);
			glTexCoord2f(0, 1);
			glVertex2f(0, tSize);
		}
		glEnd();
		glLoadIdentity();
	}

	/**
	 * 
	 * @param tex Main texture
	 * @param eye Eye texture
	 * @param x x-Coordinate
	 * @param y y-Coordinate
	 */
	public static void drawQuadTex(Texture tex, Texture eye, float x, float y) {
		tex.bind();
		glTranslatef(x, y, 0);
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);
			glTexCoord2f(1, 0);
			glVertex2f(tSize, 0);
			glTexCoord2f(1, 1);
			glVertex2f(tSize, tSize);
			glTexCoord2f(0, 1);
			glVertex2f(0, tSize);
		}
		glEnd();
		glLoadIdentity();
		eye.bind();
		glTranslatef(x, y, 0);
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);
			glTexCoord2f(1, 0);
			glVertex2f(tSize, 0);
			glTexCoord2f(1, 1);
			glVertex2f(tSize, tSize);
			glTexCoord2f(0, 1);
			glVertex2f(0, tSize);
		}
		glEnd();
		glLoadIdentity();
	}

	/**
	 * 
	 * @param tex Main texture
	 * @param x x-Coordinate
	 * @param y y-Coordinate
	 * @param angle Angle
	 */
	public static void drawQuadTex(Texture tex, float x, float y, float angle) {
		tex.bind();
		glTranslatef(x, y, 0);
		glTranslatef(16, 16, 0);
		glRotatef(angle, 0, 0, 1);
		glTranslatef(-16, -16, 0);
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);
			glTexCoord2f(1, 0);
			glVertex2f(tSize, 0);
			glTexCoord2f(1, 1);
			glVertex2f(tSize, tSize);
			glTexCoord2f(0, 1);
			glVertex2f(0, tSize);
		}
		glEnd();
		glLoadIdentity();
	}

	/**
	 * 
	 * @param tex Main texture
	 * @param eye Eye texture
	 * @param x x-Coordinate
	 * @param y y-Coordinate
	 * @param angle Angle
	 */
	public static void drawQuadTex(Texture tex, Texture eye, float x, float y, float angle) {
		tex.bind();
		glTranslatef(x, y, 0);
		glTranslatef(16, 16, 0);
		glRotatef(angle, 0, 0, 1);
		glTranslatef(-16, -16, 0);
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);
			glTexCoord2f(1, 0);
			glVertex2f(tSize, 0);
			glTexCoord2f(1, 1);
			glVertex2f(tSize, tSize);
			glTexCoord2f(0, 1);
			glVertex2f(0, tSize);
		}
		glEnd();
		glLoadIdentity();
		eye.bind();
		glTranslatef(x, y, 0);
		glTranslatef(16, 16, 0);
		glRotatef(angle, 0, 0, 1);
		glTranslatef(-16, -16, 0);
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);
			glTexCoord2f(1, 0);
			glVertex2f(tSize, 0);
			glTexCoord2f(1, 1);
			glVertex2f(tSize, tSize);
			glTexCoord2f(0, 1);
			glVertex2f(0, tSize);
		}
		glEnd();
		glLoadIdentity();
	}

	/**
	 * Load different media files from resources
	 * 
	 * @param path Path of media file
	 * @param ft Filetype of media file
	 * @return Media file
	 */
	public static Object loadMedia(String path, String ft) {
		try {
			// Read from resources into InputStream
			InputStream is = ResourceLoader.getResourceAsStream(path + "." + ft);
			if (ft.equals("png")) {
				return (Texture) TextureLoader.getTexture("PNG", is);

			} else if (ft.equals("ttf")) {
				return (Font) Font.createFont(Font.TRUETYPE_FONT, is);

			} else if (ft.equals("ogg")) {
				return (Audio) AudioLoader.getAudio("OGG", is);
			}
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Return a string from file from given keyword
	 * 
	 * @param keyword Keyword for needed String
	 * @return String for keyword
	 */
	public static String loadString(String keyword) {
		ResourceBundle bundle = ResourceBundle.getBundle("res.strings", Locale.GERMAN);
		return bundle.getString(keyword);
	}

	/**
	 * Returns a random number between min and max
	 * 
	 * @param min Minimal number
	 * @param max Maximum number
	 * @return Random number between min and max
	 */
	public static int getRanNum(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}

	/**
	 * @param min Minimal number
	 * @param value Value to check
	 * @param max Maximum number
	 * @return Returns if value is between min and max
	 */
	public static boolean between(int min, int value, int max) {
		if (min < value && value < max)
			return true;
		else
			return false;
	}
	

	/**
	 * Enum to set the direction of characters
	 */
	public enum Direction {
		NONE(0, 0), RIGHT(1, 0), LEFT(-1, 0), UP(0, -1), DOWN(0, 1);

		private int dx;
		private int dy;

		Direction(int x, int y) {
			dx = x;
			dy = y;
		}

		public int getDx() {
			return dx;
		}

		public int getDy() {
			return dy;
		}
	}

	/**
	 * Enum to set the TileType of a character
	 */
	public enum TileType {
		BLANK("res/elements/blank", false), WALL_CORNER("res/elements/wall-corner", true), WALL_PIPE(
				"res/elements/wall-pipe",
				true), DOT_POWERPILL("res/elements/dot-powerpill", false), DOT_NORMAL("res/elements/dot-normal",
						false), WALL_CORNER_D("res/elements/wall-corner-d", true), WALL_PIPE_D(
								"res/elements/wall-pipe-d", true), WALL_DOOR("res/elements/wall-door", true);

		private String textureName;
		private boolean isWall;

		TileType(String textureName, boolean isWall) {
			this.textureName = textureName;
			this.isWall = isWall;
		}

		public String getTexName() {
			return textureName;
		}

		public void setTexName(String textureName) {
			this.textureName = textureName;
		}

		public boolean isWall() {
			return isWall;
		}
	}
	
	// Temp files
	public static final String PREFIX = "stream2file";
    public static final String SUFFIX = ".tmp";

    public static File stream2file (InputStream in) throws IOException {
        final File tempFile = File.createTempFile(PREFIX, SUFFIX);
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    }
}
