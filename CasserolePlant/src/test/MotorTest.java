package test;

import components.MotorAndController;
import components.RotatingMassWithFriction;
import test.utils.Constant;
import test.utils.FunctionOfTime;
import test.utils.Pulse;
import utils.SimTime;

public class MotorTest {
	
	//Simulation components
	static MotorAndController motorUnderTest;
	static RotatingMassWithFriction simpleDyno;
	
	//Simulation time constants
	public final static double sim_length_sec = 10;
	public final static double step_size_sec = 0.02;
	
	//Input Signals
	static FunctionOfTime voltage  = new Constant(12.0);
	static FunctionOfTime motorCmd = new Pulse(1,7,0,1);
	
	//Derived constants
	public final static int num_sim_steps = (int)Math.ceil(sim_length_sec/step_size_sec);
	
	//State variables
	static double shaftSpeedRPM = 0;
	static double shaftTorqueNm= 0;
	static double currentDrawA= 0;
	
	//Things that should be run once before the sim starts
	public static void simInit(){
		motorUnderTest.setBrakeModeActive(false);
		//ensure zero state
		motorUnderTest.reset();
		simpleDyno.reset();
		SimTime.reset();
		shaftSpeedRPM = 0;
	}
	
	//What should happen every simulation step
	public static void simStep(){
		//Solve Motor
		motorUnderTest.setShaftSpeed_RPM(shaftSpeedRPM);
		motorUnderTest.setControllerInputVoltage_V(voltage.getVal(SimTime.curTime_s));
		motorUnderTest.setMotorCmd(motorCmd.getVal(SimTime.curTime_s));
		motorUnderTest.update();
		shaftTorqueNm = motorUnderTest.getShaftOutputTorque_NM();
		currentDrawA = motorUnderTest.getCurrentDraw_A();
		
		//Solve Dyno
		simpleDyno.setShaftAppliedTorque_Nm(shaftTorqueNm);
		simpleDyno.update();
		shaftSpeedRPM = simpleDyno.getShaftSpeed_RPM();
	}
	
	//Do stuff every loop to record what happened.s
	public static void storeResults(){
		System.out.println("\n==========================");
		System.out.println("Time: " + Double.toString(SimTime.curTime_s));
		System.out.println("Speed (RPM): " + Double.toString(shaftSpeedRPM));
		System.out.println("Current (A): " + Double.toString(currentDrawA));
	}
	
	//Main entry point - the magic starts here!
	public static void main(String[] args) {
		motorUnderTest = new MotorAndController();
		simpleDyno = new RotatingMassWithFriction(5.0,0.1);
		
		simInit();
		
		//Run sim
		for(int step = 0; step < num_sim_steps; step++){
			SimTime.step(step_size_sec);
			simStep();
			storeResults();
		}
		
	}

}
