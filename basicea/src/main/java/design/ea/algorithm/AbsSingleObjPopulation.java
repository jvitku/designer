package design.ea.algorithm;

import design.ea.ind.fitness.simple.impl.RealValFitness;
import design.ea.ind.individual.Individual;

/**
 * Support for individuals with real valued fitness values.  
 * 
 * @author Jaroslav Vitku
 *
 */
public abstract class AbsSingleObjPopulation implements Population{

	public static final boolean DEF_MIN = false;
	protected Individual [] pop;
	protected final boolean minimize;
	protected int size;

	public AbsSingleObjPopulation(int size){
		this.size = size;
		this.minimize = DEF_MIN;
	}
	
	public AbsSingleObjPopulation(int size, boolean minimize){
		this.size = size;
		this.minimize = minimize;
	}

	public void setInd(int no, Individual ind){ pop[no] = ind; }

	public int size(){ return size; }

	public Individual get(int i){ return pop[i]; }

	public void set(int i, Individual in){ pop[i] = in; }

	public double[] getArrayOfFitnessVals(){
		double[] f = new double[pop.length];
		for(int i=0; i<pop.length; i++){
			if(!pop[i].getFitness().isValid()){
				System.err.println("WARNING: the Fitness value of individual no: "
						+i+" is not valid!");
			}
			f[i] = ((RealValFitness)pop[i].getFitness()).getValue();
		}
		return f;
	}


	@Override
	public void reset(boolean randomize) {
		for(int i=0; i<pop.length; i++){
			pop[i].reset(randomize);
		}
	}
	
	@Override
	public abstract Population clone();
}
