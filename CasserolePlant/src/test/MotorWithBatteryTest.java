package test;


import components.Battery;
import components.MotorAndController;
import components.RotatingMassWithFriction;
import test.utils.CasserolePlotter;
import test.utils.CasseroleSimSignal;
import test.utils.Constant;
import test.utils.FunctionOfTime;
import test.utils.Pulse;
import test.utils.Step;
import utils.SimTime;


public class MotorWithBatteryTest {
	
	//Simulation components
	static Battery batUnderTest;
	static MotorAndController motor;
	static RotatingMassWithFriction simpleDyno;
	
	//Simulation time constants
	public final static double sim_length_sec = 10;
	public final static double major_step_size_sec = 0.02;
	public final static double minor_step_size_sec = 0.00002;
	
	//Input Signals
	static FunctionOfTime voltage  = new Constant(12.0);
	static FunctionOfTime motorCmd = new Pulse(0.5,7.5,0,1);
	
	
	//Derived constants
	public final static int num_sim_steps = (int)Math.ceil(sim_length_sec/major_step_size_sec);
	
	//Outputs Arrays
	static double[] o_time = new double[num_sim_steps];
	static double[] o_speed = new double[num_sim_steps];
	static double[] o_current = new double[num_sim_steps];
	static double[] o_voltage = new double[num_sim_steps];
	static double[] o_batChargePct = new double[num_sim_steps];

	
	//State variables
	static double shaftSpeedRPM = 0;
	static double shaftTorqueNm = 0;
	static double currentDrawA= 0;
	static double systemVoltage = 0;
	static double batChargePct = 0;
	
	
	//Things that should be run once before the sim starts
	public static void simInit(){
		motor.setBrakeModeActive(false);
		//ensure zero state
		motor.reset();
		simpleDyno.reset();
		batUnderTest.reset();
		SimTime.reset();
	}
	
	//Determine, based on inputs, if we need a tighter solver loop.
	public static boolean needMinorSteps(double goalTime){
		if(Math.abs(motorCmd.getVal(goalTime) - motorCmd.getVal(SimTime.prevTime_s)) > 0.25){
			System.out.println("Test");
			return true;
		} else {
			return false;
		}
	}
	
	//What should happen every simulation step
	public static void simStep(){
		//Solve Battery
		batUnderTest.setCurrentDraw_A(currentDrawA*10);
		batUnderTest.update();
		systemVoltage = batUnderTest.getOutputVoltage_V();
		batChargePct = batUnderTest.getChargePct();
		
		//Solve Motor
		motor.setShaftSpeed_RPM(shaftSpeedRPM);
		motor.setControllerInputVoltage_V(systemVoltage);
		motor.setMotorCmd(motorCmd.getVal(SimTime.curTime_s));
		motor.update();
		shaftTorqueNm = motor.getShaftOutputTorque_NM();
		currentDrawA = motor.getCurrentDraw_A();
		
		//Solve Dyno
		simpleDyno.setShaftAppliedTorque_Nm(shaftTorqueNm);
		simpleDyno.update();
		shaftSpeedRPM = simpleDyno.getShaftSpeed_RPM();
	}
	
	//Do stuff every loop to record what happened.
	public static void storeResults(int step){
		//System.out.println("\n==========================");
		//System.out.println("Time: " + Double.toString(SimTime.curTime_s));
		o_time[step] = SimTime.curTime_s;
		o_speed[step] = shaftSpeedRPM;
		o_current[step] = currentDrawA;
		o_voltage[step] = systemVoltage;
		o_batChargePct[step] = batChargePct;
	}
	
	public static void postProcResults(){
		CasserolePlotter plotter = new CasserolePlotter(o_time);
		plotter.addGraph("RPM", new CasseroleSimSignal("shaftSpeed", "RPM", o_time, o_speed));
		plotter.addGraph("A",  new CasseroleSimSignal("motorCurrent", "A", o_time,o_current));
		plotter.addGraph("V",  new CasseroleSimSignal("systemVoltage", "V", o_time,o_voltage));
		plotter.addGraph("Percent",  new CasseroleSimSignal("batChargePct", "Percent", o_time,o_batChargePct));
		plotter.displayPlot();
		
	}
	
	//Main entry point - the magic starts here!
	public static void main(String[] args) {
		motor = new MotorAndController();
		simpleDyno = new RotatingMassWithFriction(25.0,0.5);
		batUnderTest = new Battery();
		
		simInit();
		
		//Run sim
		for(int step = 0; step < num_sim_steps; step++){
			double dt;
			double goalTime = SimTime.curTime_s + major_step_size_sec;
			if(needMinorSteps(goalTime)){ //poor-man's variable step solver
				dt = minor_step_size_sec;
			} else {
				dt = major_step_size_sec;
			}
			while(SimTime.curTime_s < goalTime){
				SimTime.step(dt);
				simStep();
			}
			storeResults(step);
		}
		
		postProcResults();
		
	}

}
