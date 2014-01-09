package design.ea.matrix.individual.impl;

import design.ea.matrix.individual.Fitness;

public class InverseError implements Fitness{
	
	private float val;
	private float error = Float.MAX_VALUE;
	
	public final float MAX = 10000;
	
	public InverseError(){
		val = 0;
	}

	public void set(float error){
		if(error ==0)
			val = MAX;
		else
			val = 1/error;
	}
	
	@Override
	public float get() {
		return val;
	}

	@Override
	public void reset() {
		val =0;
	}

	@Override
	public void setError(float error) { this.error = error; }

	@Override
	public float getError() { return this.error; }

}
