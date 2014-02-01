package design.ea.ind.individual.impl;

import design.ea.ind.fitness.simple.impl.RealValFitness;
import design.ea.ind.genome.vector.impl.RealVector;
import design.ea.ind.individual.AbsIndividual;

/**
 * An individual with:
 * <ul>
 * <li>genome: vector of real values</li>
 * <li>fitness: one real value</li>
 * </ul>
 *  
 * @author Jaroslav Vitku
 *
 */
public class RealVectorSingleObj extends AbsIndividual{

	public RealVectorSingleObj(int genomeSize, 
			float maxVal,float minVal){
		
		this.fitness = new RealValFitness();
		this.genome = new RealVector(genomeSize, minVal,maxVal);
	}

}
