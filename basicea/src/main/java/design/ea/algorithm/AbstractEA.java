package design.ea.algorithm;

import design.ea.algorithm.impl.SingleVectorPop;
import design.ea.ind.individual.Individual;

public abstract class AbstractEA implements EvolutionaryAlgorithm{
	
	public static final String name = "AbstractEA"; 
	protected String me = "["+name+"] ";
	
	protected int INdim, OUTdim;
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
	
	public AbstractEA(int INdim, int OUTdim, int generations, int popSize,
			float maxw, float minw){
		
		this.INdim = INdim;
		this.OUTdim = OUTdim;
		maxGen = generations;
		this.popSize = popSize;
		this.bestOne = 0;		// bestInd
		max = maxw;
		min = minw;
		gen = 0;
		pop = new SingleVectorPop(popSize);	//TODO
		destiny = new SingleVectorPop(popSize);
		
		wantsEval = true;
	}

	protected abstract void applyEAOperators();
	
	public void setProbabilities(double pMut, double pCross){
		this.pMut = pMut;
		this.pCross = pCross;
	}
	
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
	
	public boolean wantsEval(){ return wantsEval; }

	public int generation(){ return gen; }
	
	public int currentOne(){ return current; }
	
	public int bestOne(){ return bestOne; }
	
	public int getBest(){ return bestOne; }
	
	public Individual getIndNo(int no){ return pop.get(no); }
	
	public Individual getBestInd(){ return pop.get(this.bestOne); }
	
}
