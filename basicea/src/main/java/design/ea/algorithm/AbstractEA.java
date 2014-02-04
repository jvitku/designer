package design.ea.algorithm;

import design.ea.algorithm.impl.SingleRealVectorPop;
import design.ea.ind.individual.Individual;

public abstract class AbstractEA implements EvolutionaryAlgorithm{

	public static final String name = "AbstractEA"; 
	protected String me = "["+name+"] ";

	public static final boolean DEF_MINIMIZE = true; 
	private final boolean minimize;		// maximize or minimize fitness?
	
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
	
	public AbstractEA(int  vectorLength, int generations, int popSize){
		this.minimize = DEF_MINIMIZE;
		this.vectorLength = vectorLength;
		this.init(generations, popSize, 0,1); // min and max not used here
	}
	
	public AbstractEA(int  vectorLength, boolean minimize, int generations, int popSize){
		this.vectorLength = vectorLength;
		this.minimize = minimize;
		this.init(generations, popSize, 0, 1);// min and max not used here
	}
	
	
	public AbstractEA(int  vectorLength, int generations, int popSize, float minw, float maxw){
		this.vectorLength = vectorLength;
		this.minimize = DEF_MINIMIZE;
		this.init(generations, popSize, minw, maxw);
	}

	/**
	 * Initialize the Evolutionary algorithm. 
	 * 
	 * Note that parameters {@link #min} and {@link #max} are respected
	 * by the algorithm, but are not respected by the mutation operator, 
	 * therefore these boundaries may be slightly violated when using 
	 * the {@link design.ea.strategies.mutation.BasicUniformMutation}. 
	 *  
	 * @param vectorLength length of genome vector
	 * @param minimize whether to find max. vs. min. value of fitness?
	 * @param generations max number of generations to run
	 * @param popSize size of the population
	 * @param minw minimum value of gene 
	 * @param maxw max. value of gene
	 * 
	 */
	public AbstractEA(int  vectorLength, boolean minimize, int generations, int popSize, float minw, float maxw){
		this.vectorLength = vectorLength;
		this.minimize = minimize;
		
		this.init(generations, popSize, minw, maxw);
	}

	private void init(int generations, int popSize, float minw, float maxw){
		maxGen = generations;
		this.popSize = popSize;
		this.bestOne = 0;		// bestInd
		max = maxw;
		min = minw;
		gen = 0;
		pop = new SingleRealVectorPop(popSize, vectorLength, minimize, min, max);		
		destiny = new SingleRealVectorPop(popSize, vectorLength, minimize, min, max);

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
		/*
		System.out.println(me+"---------------gen: "+gen+" / "+maxGen+
				" ---- ind no: "+current+" / "+this.pop.size());
		 */
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

	@Override
	public Individual getCurrentInd(){ return pop.get(this.current); }
}
