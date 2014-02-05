package design.ea.ind.individual.impl;

import design.ea.ind.fitness.simple.impl.RealValFitness;
import design.ea.ind.genome.vector.impl.BinaryVector;
import design.ea.ind.individual.AbsIndividual;
import design.ea.ind.individual.Individual;

/**
 * An individual with:
 * <ul>
 * <li>Genome: vector of binary values</li>
 * <li>Fitness: one real value</li>
 * </ul>
 *  
 * @author Jaroslav Vitku
 *
 */
public class BinaryVectorSingleObj extends AbsIndividual{

	private static final long serialVersionUID = 7720280379793214914L;
	
	private int genomeSize;
	private final boolean minimize;

	public BinaryVectorSingleObj(int genomeSize, 
			boolean minimize){

		this.minimize = minimize;
		this.genomeSize = genomeSize;

		this.fitness = new RealValFitness(minimize);
		this.genome = new BinaryVector(genomeSize);
	}

	@Override
	public Individual clone(){
		BinaryVectorSingleObj out = 
				new BinaryVectorSingleObj(genomeSize,minimize);
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
