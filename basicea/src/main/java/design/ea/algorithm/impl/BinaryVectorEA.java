package design.ea.algorithm.impl;

import design.ea.algorithm.AbstractEA;
import design.ea.strategies.crossover.OnePointCrossover;
import design.ea.strategies.mutation.BinaryUniformMutation;
import design.ea.strategies.selection.RouletteWheel;

/**
 * EA which works with individuals composed of binary-valued vectors. 
 *  
 * @author Jaroslav Vitku
 *
 */
public class BinaryVectorEA extends AbstractEA{

	public static final String name = "BinaryVectorEA";

	public BinaryVectorEA(int  vectorLength, boolean minimize, int generations, int popSize){
		super(vectorLength, minimize, generations, popSize);
		this.init();
	}

	public BinaryVectorEA(int  vectorLength, int generations, int popSize){
		super(vectorLength, generations, popSize);
		this.init();
	}

	private void init(){
		pop = new SingleBinaryVectorPop(popSize, vectorLength, minimize);		
		destiny = new SingleBinaryVectorPop(popSize, vectorLength, minimize);

		select = new RouletteWheel();
		mutate = new BinaryUniformMutation();
		cross = new OnePointCrossover<Boolean>();
		mutate.setPMut(super.pMut);
		cross.setPCross(super.pCross);
	}

}
