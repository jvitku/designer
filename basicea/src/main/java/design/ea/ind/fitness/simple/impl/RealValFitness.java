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
	private final boolean minimize;

	Random r = new Random();

	/**
	 * Create fitness with specified initial which is valid. 
	 * @param initVal initial value of the fitness
	 */
	public RealValFitness(boolean minimize, Double initVal){
		this.minimize = minimize;
		this.isValid = true;
		this.myVal = initVal;
	}

	/**
	 * Creates fitness with randomized value in the interval
	 * [0,1], which is not valid.
	 */
	public RealValFitness(boolean minimize){
		this.minimize = minimize;
		this.isValid = false;
		this.reset(true);
	}

	@Override
	public Double getValue() { return this.myVal; }

	@Override
	public void setValue(Double value) {
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
		if(!(f instanceof RealValFitness)){
			System.err.println("ERROR: cannot compare given fitness with RealValFitness");
			return false;
		}
		if(this.minimize){
			return this.myVal.doubleValue() < ((RealValFitness)f).getValue().doubleValue();
		}else{
			return this.myVal.doubleValue() > ((RealValFitness)f).getValue().doubleValue();
		}
	}

	@Override
	public Fitness clone(){
		RealValFitness out = new RealValFitness(this.minimize);
		out.myVal = this.myVal;
		out.isValid = this.isValid;
		return (Fitness)out;
	}

	@Override
	public String toString(){
		String out = "F";
		if(this.isValid())
			out+="[ok]=";
		else
			out+="[xx]=";
		return out +this.myVal;
	}
}
