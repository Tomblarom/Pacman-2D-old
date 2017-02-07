package xyz.nikulski.main;

import static xyz.nikulski.main.Utils.*;
import static xyz.nikulski.main.Controller.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

import javax.imageio.ImageIO;

import xyz.nikulski.element.Tile;

public class Grid {
	private Tile[][] layout;
	private int[][] path;
	private int dots;

	private static TrueTypeFont loadFont100;
	private static TrueTypeFont loadFont60;
	private static TrueTypeFont loadFont40;

//	private boolean hasDoor = false;

	/**
	 * Initiate Grid for game
	 */
	public Grid() {
		
		// Initiate map
		BufferedImage map = null;
		try {
			map = ImageIO.read(stream2file(ResourceLoader.getResourceAsStream("res/map-layout-1.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Set up arrays with field sizes
		path = new int[28][31];
		layout = new Tile[28][31];
		
		// Loop above layout and fill with Tiles
		for (int x = 0; x < layout.length; x++) {
			for (int y = 0; y < layout[x].length; y++) {

				// Read hex code from image at x and y
				Color c = new Color(map.getRGB(x * 32 + 16, y * 32 + 16));
				
				// Compare current hex code
				/**
				 * Hier auskommentierte codezeilen sind für einen Cave für die Geister
				 * der mittig platziert werden würde. Siehe /res/map-layout.png
				 */
				if (c.equals(Color.decode("#FF0000"))) {
					layout[x][y] = new Tile(x * tSize, y * tSize, 0, TileType.WALL_CORNER);

				} else if (c.equals(Color.decode("#FF8000"))) {
					layout[x][y] = new Tile(x * tSize, y * tSize, 90, TileType.WALL_CORNER);

				} else if (c.equals(Color.decode("#FFFF00"))) {
					layout[x][y] = new Tile(x * tSize, y * tSize, 180, TileType.WALL_CORNER);

				} else if (c.equals(Color.decode("#80FF00"))) {
					layout[x][y] = new Tile(x * tSize, y * tSize, 270, TileType.WALL_CORNER);

				} else if (c.equals(Color.decode("#00FF00"))) {
					layout[x][y] = new Tile(x * tSize, y * tSize, 0, TileType.WALL_PIPE);

				} else if (c.equals(Color.decode("#00FF80"))) {
					layout[x][y] = new Tile(x * tSize, y * tSize, 90, TileType.WALL_PIPE);

				} else if (c.equals(Color.decode("#FF0001"))) {
					// layout[x][y] = new Tile(x * tSize, y * tSize, 0,
					// TileType.WALL_CORNER_D);
					layout[x][y] = new Tile(x * tSize, y * tSize, 0, TileType.BLANK);

				} else if (c.equals(Color.decode("#FF8002"))) {
					// layout[x][y] = new Tile(x * tSize, y * tSize, 90,
					// TileType.WALL_CORNER_D);
					layout[x][y] = new Tile(x * tSize, y * tSize, 0, TileType.BLANK);

				} else if (c.equals(Color.decode("#FFFF03"))) {
					// layout[x][y] = new Tile(x * tSize, y * tSize, 180,
					// TileType.WALL_CORNER_D);
					layout[x][y] = new Tile(x * tSize, y * tSize, 0, TileType.BLANK);

				} else if (c.equals(Color.decode("#80FF04"))) {
					// layout[x][y] = new Tile(x * tSize, y * tSize, 270,
					// TileType.WALL_CORNER_D);
					layout[x][y] = new Tile(x * tSize, y * tSize, 0, TileType.BLANK);

				} else if (c.equals(Color.decode("#00FF05"))) {
					// layout[x][y] = new Tile(x * tSize, y * tSize, 0,
					// TileType.WALL_PIPE_D);
					layout[x][y] = new Tile(x * tSize, y * tSize, 0, TileType.BLANK);

				} else if (c.equals(Color.decode("#00FF86"))) {
					// layout[x][y] = new Tile(x * tSize, y * tSize, 90,
					// TileType.WALL_PIPE_D);
					layout[x][y] = new Tile(x * tSize, y * tSize, 0, TileType.BLANK);

				} else if (c.equals(Color.decode("#6181bc"))) {
					layout[x][y] = new Tile(x * tSize, y * tSize, 0, TileType.BLANK);
					// if (!hasDoor)
					// layout[x][y] = new Tile(x * tSize, y * tSize, 0,
					// TileType.WALL_DOOR);
					// layout[x][y] = new Tile(x * tSize, y * tSize, 0,
					// TileType.WALL_DOOR);
					// else
					// layout[x][y] = new Tile(x * tSize, y * tSize, 180,
					// TileType.WALL_DOOR);
					// hasDoor = true;

				} else if (c.equals(Color.decode("#0080FF"))) {
					layout[x][y] = new Tile(x * tSize, y * tSize, 0, TileType.DOT_POWERPILL);

				} else if (c.equals(Color.decode("#FFFFFF"))) {
					layout[x][y] = new Tile(x * tSize, y * tSize, 0, TileType.DOT_NORMAL);

				} else {
					layout[x][y] = new Tile(x * tSize, y * tSize, 0, TileType.BLANK);
				}
			}
		}

		// Read fonts
		Font loadFont = (Font) loadMedia("res/media/game_over", "ttf");
		loadFont100 = new TrueTypeFont(loadFont.deriveFont(100f), false);
		loadFont60 = new TrueTypeFont(loadFont.deriveFont(60f), false);
		loadFont40 = new TrueTypeFont(loadFont.deriveFont(40f), false);
	}
	
	/**
	 * Draws grid elements
	 */
	public void draw() {
		// Draw map
		dots = 0;
		for (int x = 0; x < layout.length; x++) {
			for (int y = 0; y < layout[x].length; y++) {
				layout[x][y].draw();
				if (layout[x][y].getType() == TileType.DOT_NORMAL)
					dots++;
			}
		}

		// Draw stats
		Color.white.bind();
		loadFont100.drawString(lWidth + 20, 20, loadString("rank_title"), Color.yellow);
		loadFont60.drawString(lWidth + 20, 120, loadString("rank_score") + String.valueOf(SCORE), Color.blue);
		loadFont100.drawString(lWidth + 20, 160, loadString("rank_status_" + status), Color.white);
		loadFont40.drawString(wWidth - 110, wHeight - 40, loadString("copyright"), Color.red);

		Collections.sort(HIGH_SCORE, new Comparator<Integer>() {
			@Override
			public int compare(Integer i1, Integer i2) {
				if (i1 < i2)
					return 1;
				if (i1 > i2)
					return -1;
				return 0;
			}
		});
		
		// Draw highscore table
		loadFont100.drawString(lWidth + 20, 470, loadString("rank_highscore"), Color.orange);
		for (int pos = 1; pos < 8; pos++) {
			if (HIGH_SCORE.size() > pos)
				loadFont60.drawString(lWidth + 20, Integer.parseInt("5" + pos * 2 + "0"),
						String.valueOf(HIGH_SCORE.get(pos - 1)), Color.orange);
		}
		Color.white.bind();

		// Draw lives
		for (int i = 0; i < lives + 1; i++) {
			drawQuadTex((Texture) loadMedia("res/elements/live", "png"), lWidth + 140 + i * 30, 125);
		}
	}

	/**
	 * Checks if tile with x y is a valid tile for characters.
	 * 
	 * @param x x-Coordinate
	 * @param y y-Coordinate
	 * @return true / false
	 */
	public boolean isMoveable(int x, int y) {
		Tile t = getTile(x, y);

		if (t == null) {
			return false;
		} else {
			return !t.getType().isWall();
		}
	}

	/**
	 * Returns Tile from x and y
	 * 
	 * @param x x-Coordinate
	 * @param y y-Coordinate
	 * @return Tile on x and y
	 */
	public Tile getTile(int x, int y) {
		if (x >= 0 && x < layout.length && y >= 0 && y < layout[x].length) {
			return layout[x][y];
		} else
			return null;
	}
	
	/**
	 * Sets Tile from x and y
	 * 
	 * @param x x-Coordinate
	 * @param y y-Coordinate
	 * @param tile New tile
	 */
	public void setTile(int x, int y, Tile tile) {
		layout[x][y] = tile;
	}

	/**
	 * @return Returns dot count
	 */
	public int getDots() {
		return dots;
	}

	/**
	 * Resets path array to Integer.MAX_VALUE
	 */
	public void resetPath() {
		for (int x = 0; x < path.length; x++) {
			for (int y = 0; y < path[x].length; y++) {
				path[x][y] = Integer.MAX_VALUE;
			}
		}
	}

	/**
	 * Recalculate new path array
	 * 
	 * @param px
	 *            x-Coordinate of target
	 * @param py
	 *            y-Coordinate of target
	 * @return calculated path array
	 */
	public int[][] updatePath(int px, int py) {
		findPath(px, py, 0);
		return path;
	}

	/**
	 * Return path array
	 * 
	 * @return current path array
	 */
	public int[][] getPath() {
		return path;
	}

	/**
	 * Recursive calculating of shortest way to target. Continue calculating if a shorter way
	 * was found. Unseeded tiles stay at Integer.MAX_VALUE.
	 * 
	 * @param x
	 *            x-Coordinate of tile
	 * @param y
	 *            y-Coordinate of tile
	 * @param c
	 *            Gone steps from start
	 */
	private void findPath(int x, int y, int c) {
		if (path[x][y] > c) {
			path[x][y] = c;
		} else {
			return;
		}

		if (isMoveable(x - 1, y)) {
			findPath(x - 1, y, c + 1);
		}

		if (isMoveable(x + 1, y)) {
			findPath(x + 1, y, c + 1);
		}

		if (isMoveable(x, y - 1)) {
			findPath(x, y - 1, c + 1);
		}

		if (isMoveable(x, y + 1)) {
			findPath(x, y + 1, c + 1);
		}
	}
}
