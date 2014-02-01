package design.ea.ind.fitness.simple.impl;

import java.util.Random;

import design.ea.ind.fitness.Fitness;
import design.ea.ind.fitness.simple.SingleObjectiveFitness;

/**
 * Single-objective fitness value of type Double.
 * 
 * @author Jaroslav Vitku
 *
 */
public class RealValFitness implements SingleObjectiveFitness<Double>{

	private boolean isValid = false;
	private Double myVal;

	Random r = new Random();

	/**
	 * Create fitness with specified initial which is valid. 
	 * @param initVal initial value of the fitness
	 */
	public RealValFitness(Double initVal){
		this.isValid = true;
		this.myVal = initVal;
	}

	/**
	 * Creates fitness with randomized value in the interval
	 * [0,1], which is not valid.
	 */
	public RealValFitness(){
		this.isValid = false;
		this.reset(true);
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

	@Override
	public boolean betterThan(Fitness f) {
		if(f instanceof RealValFitness){
			System.err.println("ERROR: cannot compare given fitness with RealValFitness");
			return false;
		}
		return this.myVal.doubleValue() > ((RealValFitness)f).getFitness().doubleValue();
	}
	
	@Override
	public Fitness clone(){
		RealValFitness out = new RealValFitness();
		out.myVal = this.myVal;
		out.isValid = this.isValid;
		return (Fitness)out;
	}
}
