package design.ea.ind.fitness.simple.impl;

import java.util.Random;

import design.ea.ind.fitness.simple.SingleObjectiveFitness;

/**
 * Single-objective fitness value of type Double.
 * 
 * @author Jaroslav Vitku
 *
 */
public class RealValFitness implements SingleObjectiveFitness<Double>{

	public static final double DEF_VAL = -1.0;
	private boolean isValid = false;
	private Double myVal;

	Random r = new Random();

	public RealValFitness(Double initVal){
		this.isValid = true;
		this.myVal = initVal;
	}

	public RealValFitness(){
		this.isValid = false;
		this.myVal = DEF_VAL;
	}

	@Override
	public Double getFitness() { return this.myVal; }

	@Override
	public void setFitness(Double value) {
		this.myVal = value;
		this.setValid(true);
	}

	@Override
	public void setValid(boolean valid) { this.isValid = valid; }

	@Override
	public boolean isValid() { return this.isValid; }

	@Override
	public void reset(boolean randomize) {
		if(randomize){
			this.myVal = r.nextDouble();
		}
		this.setValid(false);
	}
}
