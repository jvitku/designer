package design.ea.algorithm;

import design.ea.ind.individual.Individual;

public abstract class AbstractSingleThreadEA extends AbstractEA implements SingleThreadEA{

	public static final String name = "AbstractSingleThreadEA";

	protected int current = 0;
	protected boolean wantsEval;

	public AbstractSingleThreadEA(int  vectorLength, int generations, int popSize){
		super(vectorLength, generations, popSize);
	}

	public AbstractSingleThreadEA(int  vectorLength, boolean minimize, int generations, int popSize){
		super(vectorLength, minimize, generations, popSize);
	}

	@Override
	protected void init(int generations, int popSize){
		super.init(generations, popSize);
		this.wantsEval = false;
	}

	@Override
	public void nextIndividual(){
		// remember the best individual
		if(!pop.get(current).getFitness().isValid()){
			System.err.println("WARNING: called nextIndividual() and the current individusal ("+current+") is not evaluated!");
		}else if(pop.get(current).getFitness().betterThan(pop.get(bestOne).getFitness())){
			bestOne = current;
		}
		current++;
		if(current == pop.size()){
			this.nextGeneration();
		}
	}

	protected void nextGeneration(){
		if(gen >= maxGen){
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
	public int getBestIndex(){
		// if the current one is valid and its fitness is higher that the best one
		if(pop.get(this.current).getFitness().isValid())
			if(pop.get(this.current).getFitness().betterThan(
					pop.get(this.bestOne).getFitness()))
				return this.current;

		return bestOne;
	}

	@Override
	public boolean wantsEval(){ return wantsEval; }

	@Override
	public int getCurrentIndex(){ return current; }

	@Override
	public Individual getIndNo(int no){ return pop.get(no); }

	@Override
	public Individual getCurrent(){ return pop.get(this.current); }

	@Override
	public boolean isTheLastOne() { return current == pop.size()-1; }

}
