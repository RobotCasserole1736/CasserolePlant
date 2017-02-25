package utils;

public interface PlantComponent {

	//Reset component to zero-state
	public void reset();
	//main step function for simulation
	public void update();
}
