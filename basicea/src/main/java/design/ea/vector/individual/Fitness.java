package design.ea.vector.individual;

public interface Fitness {


	public float get();
	
	public void set(float val);
	
	public void reset();
	
	// optional
	public void setError(float error);	
	public float getError();
}
