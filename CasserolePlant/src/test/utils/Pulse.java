package test.utils;

public class Pulse implements FunctionOfTime{

	double pulse_val;
	double nominal_val;
	double pulse_start_time;
	double pulse_end_time;
	
	//assume arbitrary step
	public Pulse(double pulse_start_time_in, double pulse_end_time_in, double nominal_val_in, double pulse_val_in){
		pulse_val = pulse_val_in;
		nominal_val = nominal_val_in;
		pulse_start_time = pulse_start_time_in;
		pulse_end_time = Math.max(pulse_start_time, pulse_end_time_in);
	}

	@Override
	public double getVal(double time) {
		if(time < pulse_start_time || time > pulse_end_time){
			return nominal_val;
		} else {
			return pulse_val;
		}
	}
}
