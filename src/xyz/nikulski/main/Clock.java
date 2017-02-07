package xyz.nikulski.main;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

import static xyz.nikulski.main.Utils.*;

public class Clock {
  
    /** time at last frame */
    public static long lastFrame;
     
    /** frames per second */
    public static int fps;
    
    /** last fps time */
    public static long lastFPS;

	public static void initClock() {
		getDelta();
		lastFPS = getTime();
	}
	
	/** 
     * Calculate how many milliseconds have passed 
     * since last frame.
     * 
     * @return milliseconds passed since last frame 
     */
	public static int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;
		return delta;
	}
	
	/**
     * Get the accurate system time
     * 
     * @return The system time in milliseconds
     */
    public static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
     
    /**
     * Calculate the FPS and set it in the title bar
     */
    public static void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle(loadString("window_name") + "    " + fps + " FPS");
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }

}
