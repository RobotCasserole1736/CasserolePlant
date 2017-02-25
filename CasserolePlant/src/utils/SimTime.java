package utils;
/**
 * Static class to track global simulation time (since all components update at the same
 * fixed time step)
 * @author Chris Gerth
 *
 */

public class SimTime {
	private static double curTime_s;
	private static double prevTime_s;
	private static double dt_s;

	/**
	 * Reset time back to start of simulatino (t = 0)
	 */
	public void reset() {
		curTime_s = 0;
		prevTime_s = 0;
		dt_s = 0;
		
	}


	/**
	 * Update the simulation time by one step.
	 * @param dt_s_in delta time from last loop to this loop. Must remain pretty constant, otherwise the math will break.
	 */
	public void step(double dt_s_in) {
		dt_s = dt_s_in;
		prevTime_s = curTime_s;
		curTime_s += dt_s;
	}
	
}
