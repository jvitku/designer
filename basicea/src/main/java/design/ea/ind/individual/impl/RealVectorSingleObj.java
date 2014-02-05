package design.ea.ind.individual.impl;

import design.ea.ind.fitness.simple.impl.RealValFitness;
import design.ea.ind.genome.vector.impl.RealVector;
import design.ea.ind.individual.AbsIndividual;
import design.ea.ind.individual.Individual;

/**
 * An individual with:
 * <ul>
 * <li>Genome: vector of real values</li>
 * <li>Fitness: one real value</li>
 * </ul>
 *  
 * @author Jaroslav Vitku
 *
 */
public class RealVectorSingleObj extends AbsIndividual{

	private static final long serialVersionUID = 781574685031005234L;
	
	private int genomeSize;
	private float minVal, maxVal;
	private final boolean minimize;

	public RealVectorSingleObj(int genomeSize, 
			boolean minimize, float minVal,float maxVal){

		this.minimize = minimize;
		this.genomeSize = genomeSize;
		this.minVal = minVal;
		this.maxVal = maxVal;

		this.fitness = new RealValFitness(minimize);
		this.genome = new RealVector(genomeSize, minVal,maxVal);
	}

	@Override
	public Individual clone(){
		RealVectorSingleObj out = new RealVectorSingleObj(genomeSize,
				minimize, maxVal, minVal);
		out.genome = this.genome.clone();
		out.fitness = this.fitness.clone();
		return out;
	}

	@Override
	public String toString(){
		return " G: "+fitness.toString()+" "+genome.toString();
		//return out +" G: "+SL.toStr(((RealVector)genome).getVector());
	}

}
