package test.utils;

public class Constant implements FunctionOfTime{

	double val;
	
	//assume arbitrary step
	public Constant(double val_in){
		val = val_in;
	}

	@Override
	public double getVal(double time) {
		return val;
	}
}
