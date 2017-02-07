package xyz.nikulski.main;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.ImageIOImageData;
import org.newdawn.slick.util.ResourceLoader;

import net.sf.image4j.codec.ico.ICODecoder;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import static xyz.nikulski.main.Controller.*;
import static xyz.nikulski.main.Utils.*;
import static xyz.nikulski.main.Clock.*;

public class Main {
	private static Main main;
	private static int FPS = 0;

	public Main() {}

	/**
	 * Initiate Main Game class
	 */
	public void initGame() {
		try {
			// Set display specification: size, title, resizable, position
			Display.setDisplayMode(new DisplayMode(wWidth, wHeight));
			Display.setTitle(loadString("window_name"));
			Display.setResizable(false);
			Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
			int x = (int) ((dimension.getWidth() - wWidth) / 2);
			// int y = (int) ((dimension.getHeight() - wHeight) / 2);
			int y = 0;
			Display.setLocation(x, y);
			
			// Load display icon in ico
			List<BufferedImage> pacmanIcon = ICODecoder
					.read(stream2file(ResourceLoader.getResourceAsStream("res/ico/pacman.ico")));
			Display.setIcon(
					new ByteBuffer[] { new ImageIOImageData().imageToByteBuffer(pacmanIcon.get(0), false, false, null), // 8x8
							new ImageIOImageData().imageToByteBuffer(pacmanIcon.get(1), false, false, null), // 16x16
							new ImageIOImageData().imageToByteBuffer(pacmanIcon.get(2), false, false, null), // 32x32
							new ImageIOImageData().imageToByteBuffer(pacmanIcon.get(3), false, false, null) // 64x64
					});
			
			// Create display
			Display.create();
			
			// Keep frames synchron with monitor
			Display.setVSyncEnabled(true);
			
		} catch (IOException | LWJGLException e) {
			e.printStackTrace();
		}

		// Enable capabilities
		glEnable(GL_TEXTURE_2D); // 2D-texturing
		glDisable(GL_LIGHTING); // Color lightning
		glEnable(GL_BLEND);
		
		// Color mixture
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		// Sets matrix mode to GL_PROJECTION
		glMatrixMode(GL_PROJECTION);
		
		// Loads matrix, also callable as glLoadMatrix()
		glLoadIdentity();
		
		// Sets orientations points: left, right, bottom, top, znear, zfar (last parameters for 3D)
		glOrtho(0, wWidth, wHeight, 0, 1, -1);
		
		// Sets matrix mode to GL_MODELVIEW
		glMatrixMode(GL_MODELVIEW);
		
		// Initiate
		initController();
		initClock();
	}
	
	/**
	 * Main endless gameloop
	 */	
	public static void gameloop() {
		glClear(GL_COLOR_BUFFER_BIT);
		
		updateDelta();
		updateCharacters();
		updateFPS();
		progress();
		drawCharacters();

		// Updates display with made changes
		Display.update();
		Display.sync(FPS);

		if (Display.isCloseRequested()) {
			AL.destroy(); // Audio controller destroy
			Display.destroy(); // Display destroy
			System.exit(0);
		}
	}
	
	/**
	 * Main class which gets called on game start
	 * 
	 * @param args Start parameters
	 */
	public static void main(String[] args) {
		main = new Main();
		main.initGame();
		while (true)
			gameloop();
	}
}
