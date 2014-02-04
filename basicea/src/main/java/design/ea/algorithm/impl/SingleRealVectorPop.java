package design.ea.algorithm.impl;

import design.ea.algorithm.AbsSingleObjPopulation;
import design.ea.algorithm.Population;
import design.ea.ind.individual.Individual;
import design.ea.ind.individual.impl.RealVectorSingleObj;

/**
 * EA with individuals that have:
 * <ul>
 * <li>single-objective fitness function </li>
 * <li>genome composed of real-valued genes</li>
 * </ul>
 * 
 *  For usage see the {@link design.ea.algorithm.EvolutionaryAlgorithm}.
 *   
 * @author Jaroslav Vitku
 *
 */
public class SingleRealVectorPop extends AbsSingleObjPopulation{

	private int vectorLength;
	private float minVal, maxVal;

	public SingleRealVectorPop(int size, int vectorLength, float minVal, float maxVal) {
		super(size);
		this.init(size, vectorLength, minVal, maxVal);
	}

	public SingleRealVectorPop(int size, int vectorLength, boolean minimize, float minVal, float maxVal) {
		super(size, minimize);
		this.init(size, vectorLength, minVal, maxVal);
	}

	private void init(int size, int vectorLength, float minVal, float maxVal){
		if(minVal>maxVal){
			System.err.println("ERROR: minVal and maxVal have incorrect values");
			float tmp = minVal;
			minVal = maxVal;
			maxVal = tmp;
		}
		if(vectorLength<1){
			System.err.println("ERROR: vectorLength has to be >= 1");
			vectorLength = 1;
		}

		this.vectorLength = vectorLength;
		pop = new Individual[size];
		for(int i=0; i<pop.length; i++){
			pop[i] = new RealVectorSingleObj(vectorLength, minimize,minVal, maxVal);
		}
		this.reset(true);
	}

	public Population clone(){
		SingleRealVectorPop op = new SingleRealVectorPop(this.size, 
				this.vectorLength, this.minimize, this.minVal, this.maxVal);

		for(int i=0; i<pop.length; i++){
			op.setInd(i, pop[i].clone());
		}
		return op;
	}
}
