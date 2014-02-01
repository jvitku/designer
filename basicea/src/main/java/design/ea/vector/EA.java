package design.ea.vector;

import design.ea.vector.individual.Individual;

public abstract class EA {
	
	protected String me = "[EA]: ";
	
	protected int INdim, OUTdim;
	protected int maxGen, popSize;
	
	protected Population pop;		// source population (eval-select)
	protected Population destiny;	// target population (mutate-cross-save)
	protected int actual = 0;
	protected int gen;
	protected float min,max;
	protected boolean wantsEval;
	protected int bestOne;
	
	protected double pMut = 0.05;
	protected double pCross = 0.9;
	
	public EA(int INdim, int OUTdim, int generations, int popSize,
			float maxw, float minw){
		this.INdim = INdim;
		this.OUTdim = OUTdim;
		maxGen = generations;
		this.popSize = popSize;
		this.bestOne = 0;		// bestInd
		max = maxw;
		min = minw;
		gen = 0;
		pop = new Population(popSize);
		destiny = new Population(popSize);
		
		wantsEval = true;
	}

	public void setProbabilities(double pMut, double pCross){
		this.pMut = pMut;
		this.pCross = pCross;
	}
	
	/**
	 * Override this with your own individuals
	 */
	public abstract void initPop();
	
	public boolean wantsEval(){
		return wantsEval;
	}

	public void nextIndividual(){
		
		// remember the best individual
		if(pop.get(actual).getFitness().get() > pop.get(bestOne).getFitness().get()){
			bestOne = actual;					
		}
		
		actual++;
		System.out.println(me+"---------------gen: "+gen+" / "+maxGen+
				" ---- ind no: "+actual+" / "+this.pop.size());

		if(actual == pop.size()){
			this.nextGeneration();
		}
	}
	
	public int generation(){
		//System.out.println("generation is: "+gen);
		return gen;
	}
	
	protected void nextGeneration(){
		if(gen>=maxGen){
			wantsEval = false;
			System.out.println(me+"evolution ended.");
		}
		System.out.println(me+"Generation ended here, apply operators now!");
		actual = 0;
		gen++;
		
		this.applyEAOperators();
		
		bestOne = 0;		// reset this 
	}
	
	protected abstract void applyEAOperators();
	
	
	public int actualOne(){
		return actual;
	}
	public int bestOne(){
		return bestOne;
	}
	
	
	
	public int getBest(){
		return bestOne;	
	}
	
	public Individual getIndNo(int no){
		return pop.get(no);
	}
	
	public Individual getBestInd(){
		return pop.get(this.bestOne);
	}
	
}
