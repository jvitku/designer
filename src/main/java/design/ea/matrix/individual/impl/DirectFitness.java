package design.ea.matrix.individual.impl;

import design.ea.matrix.individual.Fitness;

public class DirectFitness implements Fitness{

	private float fit;
	
	public DirectFitness(){
		this.fit = 0;
	}
	
	@Override
	public float get() {
		return fit;
	}

	@Override
	public void set(float val) {
		this.fit = val;
	}

	@Override
	public void reset() {
		this.fit = 0;
	}

	@Override
	public void setError(float error) {
		System.out.println("this is direct fitness, set it directly.. will use inverse of this errorn now..");
		this.fit = 1/error;
	}

	@Override
	public float getError() {
		return 1/fit;
	}

}
