package components;

import utils.ConversionConstants;
import utils.PlantComponent;

public class RotatingMassWithFriction implements PlantComponent {
	
	//Constants
	
	//Derived constants
	public final double radius_m;
	public final double mass_kg;
	
	//Inputs
	private double shaftAppliedTorque_Nm = 0;
	
	//State
	private double shaftAccel_radpersec2 = 0;
	private double shaftSpeed_radpersec = 0;
	
	//Outputs
	private double shaftSpeed_RPM = 0;
	
	
	
	/**
	 * Model of a solid cylinder of a given radius and weight, supported
	 * by fairly low-friction bearings.
	 * @param radius_in radius of the mass (in inches)
	 * @param mass_lbs mass of the mass (in pounds)
	 */
	public RotatingMassWithFriction(double radius_in, double mass_lbs){
		radius_m = radius_in*ConversionConstants.IN_TO_M;
		mass_kg = mass_lbs*ConversionConstants.LBS_TO_KG;
	}


	@Override
	public void reset() {
		shaftAccel_radpersec2 = 0;
		shaftSpeed_radpersec = 0;
		
	}


	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
