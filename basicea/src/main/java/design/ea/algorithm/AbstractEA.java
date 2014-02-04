package design.ea.algorithm;

import design.ea.ind.individual.Individual;
import design.ea.strategies.Crossover;
import design.ea.strategies.Mutation;
import design.ea.strategies.Selection;

public abstract class AbstractEA implements EvolutionaryAlgorithm{

	public static final String name = "AbstractEA"; 
	protected String me = "["+name+"] ";

	public static final boolean DEF_MINIMIZE = true; 
	protected final boolean minimize;		// maximize or minimize fitness?
	
	protected final int vectorLength;
	protected int maxGen, popSize;

	protected AbsSingleObjPopulation pop;		// source population (eval-select)
	protected AbsSingleObjPopulation destiny;	// target population (mutate-cross-save)

	protected int current = 0;
	protected int gen;
	
	protected boolean wantsEval;
	protected int bestOne;

	protected double pMut = 0.05;
	protected double pCross = 0.9;
	
	public Selection select;
	public Mutation mutate;
	public Crossover cross; 
	
	public AbstractEA(int  vectorLength, int generations, int popSize){
		this.minimize = DEF_MINIMIZE;
		this.vectorLength = vectorLength;
		this.init(generations, popSize); // min and max not used here
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
	 */
	public AbstractEA(int  vectorLength, boolean minimize, int generations, int popSize){
		this.vectorLength = vectorLength;
		this.minimize = minimize;
		this.init(generations, popSize);
	}

	private void init(int generations, int popSize){
		maxGen = generations;
		this.popSize = popSize;
		this.bestOne = 0;		// bestInd
		gen = 0;
		wantsEval = true;
	}
	
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
	

	/**
	 * Implement this in order to implement particular behaviour
	 * of the operators (e.g. steady-state vs. generational etc).
	 * 
	 * After calling this method, the evaluation of Individuals
	 * will start from the first one in the {@link #pop}.
	 * 
	 *  @see #nextIndividual()
	 */
	private void applyEAOperators() {
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
