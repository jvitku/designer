package design.ea.algorithm.impl;

import design.ea.algorithm.AbsSingleObjPopulation;
import design.ea.algorithm.Population;
import design.ea.ind.individual.Individual;
import design.ea.ind.individual.impl.BinaryVectorSingleObj;

/**
 * EA with individuals that have:
 * <ul>
 * <li>single-objective fitness function </li>
 * <li>genome composed of boolean-valued genes</li>
 * </ul>
 * 
 *  For usage see the {@link design.ea.algorithm.EvolutionaryAlgorithm}.
 *   
 * @author Jaroslav Vitku
 *
 */
public class SingleBinaryVectorPop extends AbsSingleObjPopulation{

	private static final long serialVersionUID = 8429458292766807822L;
	private int vectorLength;

	public SingleBinaryVectorPop(int size, int vectorLength) {
		super(size);
		this.init(size, vectorLength);
	}

	public SingleBinaryVectorPop(int size, int vectorLength, boolean minimize) {
		super(size, minimize);
		this.init(size, vectorLength);
	}

	private void init(int size, int vectorLength){
		if(vectorLength<1){
			System.err.println("ERROR: vectorLength has to be >= 1");
			vectorLength = 1;
		}

		this.vectorLength = vectorLength;
		pop = new Individual[size];
		for(int i=0; i<pop.length; i++){
			pop[i] = new BinaryVectorSingleObj(vectorLength, minimize);
		}
		this.reset(true);
	}

	public Population clone(){
		SingleBinaryVectorPop op = new SingleBinaryVectorPop(this.size, 
				this.vectorLength, this.minimize);

		for(int i=0; i<pop.length; i++){
			op.setInd(i, pop[i].clone());
		}
		return op;
	}
}
