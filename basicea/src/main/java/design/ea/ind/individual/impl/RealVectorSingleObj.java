package design.ea.ind.individual.impl;

import ctu.nengoros.util.SL;
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

	private int genomeSize;
	private float minVal, maxVal;

	public RealVectorSingleObj(int genomeSize, 
			float minVal,float maxVal){

		this.genomeSize = genomeSize;
		this.minVal = minVal;
		this.maxVal = maxVal;

		this.fitness = new RealValFitness();
		this.genome = new RealVector(genomeSize, minVal,maxVal);
	}

	@Override
	public Individual clone(){
		RealVectorSingleObj out = new RealVectorSingleObj(genomeSize,
				maxVal, minVal);
		out.genome = this.genome.clone();
		out.fitness = this.fitness.clone();
		return out;
	}

	@Override
	public String toString(){
		String out = "F";
		if(this.fitness.isValid())
			out+="[ok]=";
		else
			out+="[xx]=";
		
		out +=((RealValFitness)fitness).getFitness();
		return out +" G: "+SL.toStr(((RealVector)genome).getVector());
	}

}
