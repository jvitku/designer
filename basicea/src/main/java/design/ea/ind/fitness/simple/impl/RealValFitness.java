package design.ea.ind.fitness.simple.impl;

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
	public void setFitness(Double value) { this.myVal = value; }

	@Override
	public void setValid(boolean valid) { this.isValid = valid; }

	@Override
	public boolean isValid() { return this.isValid; }
	
}
