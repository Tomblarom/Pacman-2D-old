package xyz.nikulski.main;

import static xyz.nikulski.main.Clock.*;
import static xyz.nikulski.main.Utils.*;

import java.util.ArrayList;

import org.newdawn.slick.openal.Audio;

import xyz.nikulski.element.Ghost;
import xyz.nikulski.element.Pacman;

public class Controller {

	// All game objects
	public static Grid g;
	public static Pacman p;
	public static Ghost g1;
	public static Ghost g2;
	public static Ghost g3;
	public static Ghost g4;

	// Game relevant values
	public static float powerpill; // "Kraftpille"
	public static boolean hasMoved; // Did already pacman move?"
	public static int status = 4; // 0 ingame; 1 gameover
	public static int lives; // Left lives
	public static int delta; // Global delta for this frame
	public static int SCORE = 0;
	public static ArrayList<Integer> HIGH_SCORE = new ArrayList<>();

	// Audio imports
	public static Audio pacmanDies = (Audio) loadMedia("res/media/pacman_dies", "ogg");
	public static Audio pacmanOpeningSong1 = (Audio) loadMedia("res/media/new/pacman_song1", "ogg");
	public static Audio pacmanOpeningSong2 = (Audio) loadMedia("res/media/new/pacman_song2", "ogg");
	public static Audio pacmanPower = (Audio) loadMedia("res/media/new/pacman_power1", "ogg");
	public static Audio pacmanEatingGhost = (Audio) loadMedia("res/media/pacman_eating_ghost", "ogg");

	// Initialize Controller
	public static void initController() {
		if (status != 0) {
			g = new Grid();
			lives = 2;
			HIGH_SCORE.add(SCORE);
			SCORE = 0;
		}
		p = new Pacman(lWidth / 2 - 16, tSize * 23);
		g1 = new Ghost("clyde", 12 * tSize, 12 * tSize);
		g2 = new Ghost("inky", 12 * tSize, 16 * tSize);
		g3 = new Ghost("blinky", 15 * tSize, 12 * tSize);
		g4 = new Ghost("pinky", 15 * tSize, 16 * tSize);
		if (!pacmanOpeningSong1.isPlaying() && !pacmanOpeningSong2.isPlaying()) {
			if (status == 2)
				pacmanOpeningSong2.playAsSoundEffect(1.0f, 1.0f, false);
			else
				pacmanOpeningSong1.playAsSoundEffect(1.0f, 1.0f, false);
		}
		powerpill = 0;
		hasMoved = false;
	}

	/**
	 *  Update delta value
	 */
	public static void updateDelta() {
		delta = getDelta();
	}

	/**
	 *  Handle game states and react
	 */
	public static void progress() {

		// Calculate powerpill time
		powerpill -= (delta / 1000.0);

		// Ghost contact
		if (collidesWith(p, g1, 4) || collidesWith(p, g2, 4) || collidesWith(p, g3, 4) || collidesWith(p, g4, 4)) {

			if (!isInvulnerable() && lives != 0) {
				lives -= 1;
				pacmanDies.playAsSoundEffect(1.0f, 1.0f, false);
				initController();

			} else if (!isInvulnerable() && lives == 0) {
				status = 1;
				initController();

			} else if (isInvulnerable()) {
				getCollidingGhost().reset();
				pacmanEatingGhost.playAsSoundEffect(1.0f, 1.0f, false);
				SCORE += 100;

			}

		} else if (g.getDots() == 0 && hasMoved) {
			status = 2;
			initController();
		}

	}

	/**
	 * Update every character
	 */
	public static void updateCharacters() {
		p.update();
		g1.update();
		g2.update();
		g3.update();
		g4.update();
	}

	/**
	 * Draw every character
	 */
	public static void drawCharacters() {
		g.draw();
		p.draw();
		g1.draw();
		g2.draw();
		g3.draw();
		g4.draw();
	}

	/**
	 * Check if pacman is still invulnerable
	 * 
	 * @return true / false
	 */
	public static boolean isInvulnerable() {
		return (powerpill > 0);
	}

	/**
	 * Get ghost, which is currently colliding with pacman
	 * 
	 * @return Ghost which collides with pacamn
	 */
	private static Ghost getCollidingGhost() {
		if (collidesWith(p, g1, 4)) {
			return g1;

		} else if (collidesWith(p, g2, 4)) {
			return g2;

		} else if (collidesWith(p, g3, 4)) {
			return g3;

		} else if (collidesWith(p, g4, 4)) {
			return g4;

		} else {
			return null;
		}
	}
}
