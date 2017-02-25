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
	private boolean brakeModeActive = false;
	
	//State
	private double emfVoltage_V;
	private double appliedVoltage_V;
	
			
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
		//Calculate EMF Voltage
		emfVoltage_V = shaftSpeed_RPM*speedToVemfRatio;
		
		//Calculate applied 
		appliedVoltage_V = controllerInputVoltage_V*motorCmd;
				
		//Calculate current draw
		currentDraw_A = (appliedVoltage_V - emfVoltage_V)/esr_ohm;
		
		//Calculate Torque
		shaftOutputTorque_NM = currentDraw_A*currentToTorqueRatio;
		
	}

	public double getShaftOutputTorque_NM() {
		return shaftOutputTorque_NM;
	}

	public double getCurrentDraw_A() {
		return currentDraw_A;
	}

	public void setControllerInputVoltage_V(double controllerInputVoltage_V) {
		this.controllerInputVoltage_V = controllerInputVoltage_V;
	}

	public void setMotorCmd(double motorCmd) {
		this.motorCmd = motorCmd;
	}

	public void setShaftSpeed_RPM(double shaftSpeed_RPM) {
		this.shaftSpeed_RPM = shaftSpeed_RPM;
	}

	public void setBrakeModeActive(boolean brakeModeActive) {
		this.brakeModeActive = brakeModeActive;
	}

}
