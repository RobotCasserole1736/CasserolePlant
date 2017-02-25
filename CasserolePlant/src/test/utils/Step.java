package test.utils;

public class Step implements FunctionOfTime{

	double start_val;
	double end_val;
	double step_time;
	
	//Assume unit step
	public Step(double step_time_in){
		start_val = 0.0;
		end_val = 1.0;
		step_time = step_time_in;
	}
	
	//Assume step from zero to val
	public Step(double step_time_in, double end_val_in){
		start_val = 0.0;
		end_val = end_val_in;
		step_time = step_time_in;
	}
	
	//assume arbitrary step
	public Step(double step_time_in, double start_val_in, double end_val_in){
		start_val = start_val_in;
		end_val = end_val_in;
		step_time = step_time_in;
	}

	@Override
	public double getVal(double time) {
		if(time < step_time){
			return start_val;
		} else {
			return end_val;
		}
	}
}
