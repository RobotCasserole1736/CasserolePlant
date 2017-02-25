package utils;

public class SimpleDerivative {
	private double prevVal = 0;
	
	/**
	 * Reset the derivative back to zero-state
	 */
	public void reset(){
		prevVal = 0;
	}
	
	/**
	 * Generate a new derivation result for the present timestep
	 * @param input new value for the signal we're taking the derivative of
	 * @return new value of the running derivative.
	 */
	public double derivate(double input){
		double result = (input - prevVal)/SimTime.dt_s;
		prevVal = input ;
		return result;
	}
}
