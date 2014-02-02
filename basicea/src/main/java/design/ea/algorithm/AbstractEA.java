package design.ea.algorithm;

import design.ea.algorithm.impl.SingleVectorPop;
import design.ea.ind.individual.Individual;

public abstract class AbstractEA implements EvolutionaryAlgorithm{
	
	public static final String name = "AbstractEA"; 
	protected String me = "["+name+"] ";
	
	protected final int vectorLength;
	protected int maxGen, popSize;
	
	protected AbsSingleObjPopulation pop;		// source population (eval-select)
	protected AbsSingleObjPopulation destiny;	// target population (mutate-cross-save)
	
	protected int current = 0;
	protected int gen;
	protected float min,max;
	protected boolean wantsEval;
	protected int bestOne;
	
	protected double pMut = 0.05;
	protected double pCross = 0.9;
	
	public AbstractEA(int  vectorLength, int generations, int popSize, float minw, float maxw){
		this.vectorLength = vectorLength;
		maxGen = generations;
		this.popSize = popSize;
		this.bestOne = 0;		// bestInd
		max = maxw;
		min = minw;
		gen = 0;
		pop = new SingleVectorPop(popSize, vectorLength, min, max);		
		destiny = new SingleVectorPop(popSize, vectorLength, min, max);
		
		wantsEval = true;
	}

	/**
	 * Implement this in order to implement particular behaviour
	 * of the operators (e.g. steady-state vs. generational etc).
	 * 
	 * After calling this method, the evaluation of Individuals
	 * will start from the first one in the {@link #pop}.
	 * 
	 *  @see #nextIndividual()
	 */
	protected abstract void applyEAOperators();
	
	@Override
	public void setProbabilities(double pMut, double pCross){
		this.pMut = pMut;
		this.pCross = pCross;
	}
	
	@Override
	public void nextIndividual(){
		// remember the best individual
		if(pop.get(current).getFitness().betterThan(pop.get(bestOne).getFitness())){
			bestOne = current;
		}
		current++;
		System.out.println(me+"---------------gen: "+gen+" / "+maxGen+
				" ---- ind no: "+current+" / "+this.pop.size());

		if(current == pop.size()){
			this.nextGeneration();
		}
	}
	
	protected void nextGeneration(){
		if(gen>=maxGen){
			wantsEval = false;
			System.out.println(me+"evolution ended.");
		}
		System.out.println(me+"Generation ended here, apply operators now!");
		current = 0;
		gen++;
		
		this.applyEAOperators();
		bestOne = 0;		// reset this 
	}
	
	@Override
	public boolean wantsEval(){ return wantsEval; }

	@Override
	public int generation(){ return gen; }
	
	@Override
	public int currentOne(){ return current; }
	
	@Override
	public int bestOne(){ return bestOne; }
	
	@Override
	public int getBest(){ return bestOne; }
	
	@Override
	public Individual getIndNo(int no){ return pop.get(no); }
	
	@Override
	public Individual getBestInd(){ return pop.get(this.bestOne); }
	
}
