package design.ea.algorithm.impl;

import design.ea.algorithm.AbsSingleObjPopulation;
import design.ea.algorithm.AbstractEA;
import design.ea.ind.individual.Individual;
import design.ea.strategies.Crossover;
import design.ea.strategies.Mutation;
import design.ea.strategies.Selection;
import design.ea.strategies.crossover.OnePointCrossover;
import design.ea.strategies.mutation.RealGaussianUniformMutation;
import design.ea.strategies.selection.RouletteWheel;

public class RealVectorEA extends AbstractEA{

	public Selection select;
	public Mutation mutate;
	public Crossover cross; 

	public RealVectorEA(int  vectorLength, boolean minimize, int generations, int popSize, float minw, float maxw){
		super(vectorLength, minimize, generations, popSize, minw, maxw);

		this.init();
	}

	public RealVectorEA(int  vectorLength, int generations, int popSize, float minw, float maxw){
		super(vectorLength, generations, popSize, minw, maxw);

		this.init();
	}

	private void init(){
		select = new RouletteWheel();
		mutate = new RealGaussianUniformMutation();
		cross = new OnePointCrossover<Float>();
		mutate.setPMut(super.pMut);
		cross.setPCross(super.pCross);
	}

	@Override
	protected void applyEAOperators() {

		destiny.setInd(0, pop.get(bestOne).clone());	// copy the best one (elitism=1)
		select.resetSelection(pop);

		System.out.println("GEN: "+gen+" best ind("+
				this.bestOne+"): "+pop.get(bestOne).toString());

		int copied = 1;
		Individual[] sel;

		while(copied<destiny.size()){
			sel = select.select(2);
			cross.cross(sel[0], sel[1]);
			mutate.mutate(sel);
			copied = this.storeThemTo(pop, copied, sel);
		}
		pop = destiny;
	}

	/**
	 * Store array of individuals into a given population. Discard any individuals in case
	 * that there is not enough room in the target population.
	 * 
	 * @param pop target population 
	 * @param startIndex first index where to store them (inclusive)
	 * @param individuals array of Individuals to store 
	 * @return first free index in the population.
	 */
	private int storeThemTo(AbsSingleObjPopulation pop, int startIndex, Individual[] individuals){

		for(int i=0; i<individuals.length; i++){
			// while the target pop is not full
			if(i+startIndex==pop.size()){
				return i+startIndex; 
			}
			pop.set(i+startIndex, individuals[i]);
		}
		return individuals.length+startIndex;
	}

}
