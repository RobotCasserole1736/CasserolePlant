package components;

import utils.ConversionConstants;
import utils.PlantComponent;
import utils.SimpleIntegral;

public class RotatingMassWithFriction implements PlantComponent {
	
	//Constants
	public final double BEARING_FRICTIONAL_COEF_NperRADpS = 0.0004;
	
	//Derived constants
	public final double radius_m;
	public final double mass_kg;
	public final double momentOfInertia_kgpm2;
	
	//Inputs
	private double shaftAppliedTorque_Nm = 0;
	
	//State
	private double shaftAccel_radpersec2 = 0;
	private SimpleIntegral speedIntegral;
	private double netTorqueNm;

	//Outputs
	private double shaftSpeed_RPM = 0;
	private double shaftSpeed_radpersec = 0;
	
	
	
	/**
	 * Model of a solid cylinder of a given radius and weight, supported
	 * by fairly low-friction bearings.
	 * @param radius_in radius of the mass (in inches)
	 * @param mass_lbs mass of the mass (in pounds)
	 */
	public RotatingMassWithFriction(double radius_in, double mass_lbs){
		radius_m = radius_in*ConversionConstants.IN_TO_M;
		mass_kg = mass_lbs*ConversionConstants.LBS_TO_KG;
		momentOfInertia_kgpm2 = 0.5 * mass_kg * radius_m * radius_m;//Presume solid cylinder
		speedIntegral = new SimpleIntegral();
	}


	@Override
	public void reset() {
		shaftAccel_radpersec2 = 0;
		shaftSpeed_radpersec = 0;
		
	}


	@Override
	public void update() {
		//Newton's second law for rotation
		netTorqueNm = shaftAppliedTorque_Nm - BEARING_FRICTIONAL_COEF_NperRADpS*shaftSpeed_radpersec;
		shaftAccel_radpersec2 = netTorqueNm/momentOfInertia_kgpm2;
		
		//Integrate to get shaft speed
		shaftSpeed_radpersec = speedIntegral.integrate(shaftAccel_radpersec2);
		
		//convert
		shaftSpeed_RPM = shaftSpeed_radpersec*ConversionConstants.RADpSEC_TO_RPM;
	}
	
	
	public double getShaftAccel_radpersec2() {
		return shaftAccel_radpersec2;
	}


	public double getShaftSpeed_RPM() {
		return shaftSpeed_RPM;
	}


	public double getShaftSpeed_radpersec() {
		return shaftSpeed_radpersec;
	}


	public void setShaftAppliedTorque_Nm(double shaftAppliedTorque_Nm) {
		this.shaftAppliedTorque_Nm = shaftAppliedTorque_Nm;
	}

}
