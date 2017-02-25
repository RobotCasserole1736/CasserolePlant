package utils;

/**
 * Super-de-duper simple discrete-time integration for simulation
 * @author Chris Gerth
 *
 */
public class SimpleIntegral {
	private double accumulator = 0;
	
	/**
	 * Reset the integrator back to zero-state
	 */
	public void reset(){
		accumulator = 0;
	}
	
	/**
	 * Generate a new integration result for the present timestep
	 * @param input new value for the signal being integrated
	 * @return new value of the running integration.
	 */
	public double integrate(double input){
		accumulator += input * SimTime.dt_s;
		return accumulator;
	}

}
