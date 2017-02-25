package components;

import utils.PlantComponent;

public class MotorAndController implements PlantComponent {
	
	//Constants
	public final double WIRING_RESISTANCE_OHM = 0.25; //Extra resistance from controller + wires
	public final double WINDING_INDUCTANCE_H = 0.0;//Unused
	public final double NOMINAL_VOLTAGE_V = 12.0;
	public final double STALL_CURRENT_A = 135.0;
	public final double STALL_TORQUE_NM = 2.42;
	public final double NO_LOAD_SPEED_RPM = 5310.0;
	public final double NO_LOAD_CURRENT_A = 2.7;
	
	//Derived constants
	public final double winding_resistance = NOMINAL_VOLTAGE_V/STALL_CURRENT_A;
	public final double esr_ohm = winding_resistance + WIRING_RESISTANCE_OHM;
	public final double currentToTorqueRatio = STALL_TORQUE_NM/STALL_CURRENT_A;
	public final double speedToVemfRatio = (NOMINAL_VOLTAGE_V - NO_LOAD_CURRENT_A*winding_resistance)/NO_LOAD_SPEED_RPM;
			
	//Inputs
	private double controllerInputVoltage_V = 0;
	private double motorCmd = 0; //FRC convention - -1 full reverse, 0 stop,1 full fwd.
	private double shaftSpeed_RPM = 0;	
	private boolean brakeModeActive;
	
	//State
	
			
	//Outputs
	private double shaftOutputTorque_NM = 0;
	private double currentDraw_A = 0;

	@Override
	public void reset() {
		shaftOutputTorque_NM = 0;
		currentDraw_A = 0;
		
	}

	@Override
	public void update() {
		//Calculate EMF voltage 
		
	}

}
