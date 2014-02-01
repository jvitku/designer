package design.ea.algorithm.impl;

import tools.utils.LU;
import design.ea.algorithm.AbsSingleObjPopulation;
import design.ea.algorithm.AbstractEA;
import design.ea.ind.individual.Individual;
import design.ea.strategies.Crossover;
import design.ea.strategies.TwoGenomes;
import design.ea.strategies.Mutation;
import design.ea.strategies.Selection;
import design.ea.strategies.impl.OnePointCrossover;
import design.ea.strategies.impl.RouletteWheel;
import design.ea.strategies.impl.UniformMutation;
import design.ea.ind.fitness.simple.impl.RealValFitness;

public abstract class SimpleEA extends AbstractEA{
	
	Selection select;
	Mutation mutate;
	Crossover cross; 
	
	public SimpleEA(int  vectorLength, int generations, int popSize, float maxw, float minw){
	//public SimpleEA(int INdim, int OUTdim, int generations, int popSize, float maxw, float minw) {
		super(vectorLength, generations, popSize, maxw, minw);

		select = new RouletteWheel();
		mutate = new UniformMutation();
		cross = new OnePointCrossover();
		mutate.setPMut(super.pMut);
		cross.setPCross(super.pCross);
	}

	@Override
	protected void applyEAOperators() {

		Individual tmp = pop.get(bestOne).clone();
		destiny.setInd(0, pop.get(bestOne).clone());	// copy the best one (elitism)
		select.resetSelection(pop);
		
		System.out.println("----------------------------------\n'+" +
				"Best ind is no: "+bestOne+", fitness is: "+
				((RealValFitness)pop.get(bestOne)).getFitness());
		
		// TODO print out
		/*
		if(tmp instanceof HNNInd)
			((HNNInd)tmp).printMatrix();
		else if(!(tmp instanceof old.design.ea.vector.hnn.simple.HNNInd))
			System.out.println(LU.toStr(tmp.getWeights())+"\n");
		*/
		
		int copied = 1;
		float[] a,b;
		int[] selected = new int[2];
		TwoGenomes two;
		
		while(copied<destiny.size()){
			selected = select.select(2);
			a = pop.get(selected[0]).getMatrixEncoder().getVector();
			b = pop.get(selected[1]).getMatrixEncoder().getVector();
			two = cross.cross(a, b);
			two = mutate.mutate(two, pop.get(selected[0]).getMatrixEncoder().isBinary());
			
			copied = this.storeThemTo(destiny, copied, two, tmp);
		}
		pop = destiny;
	}
	
	public String getActualWeights(){
		return LU.toStr(pop.get(super.current).getWeights());
	}
	

	public float getBestFitness(){
		return pop.get(this.bestOne).getFitness().get();
		
	}
	
	private int storeThemTo(AbsSingleObjPopulation pop, int startIndex, TwoGenomes tg, Individual tmp){
		
		if(startIndex<pop.size){
			Individual ta = tmp.clone();
			((Individual)ta).getMatrixEncoder().setVector(tg.a);
			pop.set(startIndex, ta);
			startIndex++;
		}
		if(startIndex<pop.size){
			Individual tb = tmp.clone();
			tb.getMatrixEncoder().setVector(tg.b);
			pop.set(startIndex, tb);
			startIndex++;
		}
		return startIndex;
	}

}
