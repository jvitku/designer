package design.ea.algorithm.impl;

import design.ea.algorithm.AbstractSingleThreadEA;
import design.ea.strategies.crossover.OnePointCrossover;
import design.ea.strategies.mutation.RealGaussianUniformMutation;
import design.ea.strategies.selection.RouletteWheel;

/**
 * EA which works with individuals composed of real-valued vectors. 
 *  
 * @author Jaroslav Vitku
 *
 */
public class RealVectorEA extends AbstractSingleThreadEA{

	public static final String name = "RealVectorEA";
	protected float min,max;


	public RealVectorEA(int  vectorLength, boolean minimize, int generations, 
			int popSize, float minw, float maxw){
		
		super(vectorLength, minimize, generations, popSize);
		this.initialize(minw, maxw);
	}

	public RealVectorEA(int  vectorLength, int generations, int popSize, 
			float minw, float maxw){
		
		super(vectorLength, generations, popSize);
		this.initialize(minw, maxw);
	}

	private void initialize(float minw, float maxw){
		this.min = minw;
		this.max = maxw;

		pop = new SingleRealVectorPop(popSize, vectorLength, minimize, minw, maxw);		
		destiny = new SingleRealVectorPop(popSize, vectorLength, minimize, minw, maxw);

		select = new RouletteWheel();
		mutate = new RealGaussianUniformMutation();
		cross = new OnePointCrossover<Float>();
		mutate.setPMut(super.pMut);
		cross.setPCross(super.pCross);
	}
}

