package components;

import utils.ConversionConstants;
import utils.PlantComponent;
import utils.SimTime;

public class Battery implements PlantComponent {
	//Constants
	public final double NOMINAL_VOC_V = 13.0;
	public final double NOMINAL_ESR_OHM = 0.025;
	public final double CHARGE_CAPACITY_AH = 14.0;
	public final double ESR_DISCHARGE_GAIN = 0.0; //Need to tune these to accurately model the discharge curve of the battery.
	public final double VOC_DISCHARGE_GAIN = 0.0;
	
	//Derived Constants
	
	//Inputs
	private double currentDraw_A;
	
	//State
	private double presentCharge_AH;
	private double presentESR_ohm;
	private double presentVoc_V;
	
	//Outputs
	private double outputVoltage_V;
	
	
	@Override
	public void reset() {
		presentCharge_AH = CHARGE_CAPACITY_AH;
		presentESR_ohm = NOMINAL_ESR_OHM;
		presentVoc_V = NOMINAL_VOC_V;
		
	}
	@Override
	public void update() {
		//Update charge 
		presentCharge_AH -= currentDraw_A * (SimTime.dt_s*ConversionConstants.SEC_TO_HOURS);
		
		//Update battery parameters based on charge
		presentESR_ohm = NOMINAL_ESR_OHM*(1+ESR_DISCHARGE_GAIN*(CHARGE_CAPACITY_AH/presentCharge_AH));
		presentVoc_V = NOMINAL_VOC_V*(1-VOC_DISCHARGE_GAIN*(CHARGE_CAPACITY_AH/presentCharge_AH));
		
		//Calculate output voltage
		outputVoltage_V = presentVoc_V - currentDraw_A*presentESR_ohm;
	}
	
	public double getChargePct(){
		return 100.0 * (presentCharge_AH/CHARGE_CAPACITY_AH);
	}
	
	public void setCurrentDraw_A(double current){
		currentDraw_A = current;
	}
	
	public double getOutputVoltage_V(){
		return outputVoltage_V;
	}

}
