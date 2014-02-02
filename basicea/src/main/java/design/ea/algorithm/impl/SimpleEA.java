package design.ea.algorithm.impl;

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
import design.ea.ind.genome.vector.impl.RealVector;

public abstract class SimpleEA extends AbstractEA{
	
	Selection select;
	Mutation mutate;
	Crossover cross; 
	
	public SimpleEA(int  vectorLength, int generations, int popSize, float maxw, float minw){
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
		destiny.setInd(0, pop.get(bestOne).clone());	// copy the best one (elitism=1)
		select.resetSelection(pop);
		
		System.out.println("----------------------------------\n'+" +
				"Best ind is no: "+bestOne+", fitness is: "+
				((RealValFitness)pop.get(bestOne)).getFitness());
		
		System.out.println("best ind: "+pop.get(bestOne).toString());
		
		int copied = 1;
		Float[] a;
		Float[] b;
		int[] selected = new int[2];
		TwoGenomes two;
		
		while(copied<destiny.size()){
			selected = select.select(2);							// select two guys
			a = ((RealVector)pop.get(selected[0]).getGenome()).getVector();
			b = ((RealVector)pop.get(selected[1]).getGenome()).getVector();
			//b = pop.get(selected[1]).getMatrixEncoder().getVector();
			two = cross.cross(a, b);
			two = mutate.mutate(two, false);
			//two = mutate.mutate(two, pop.get(selected[0]).getMatrixEncoder().isBinary());
			
			copied = this.storeThemTo(destiny, copied, two, tmp); //TODO
		}
		pop = destiny;
	}

	
	private int storeThemTo(AbsSingleObjPopulation pop, int startIndex, TwoGenomes tg, Individual tmp){
		
		if(startIndex<pop.size()){
			Individual first = pop.get(startIndex);
			RealVector rv = (RealVector)first.getGenome();
			rv.setVector(tg.a);
			first.getFitness().setValid(false);	// TODO move this into the coross and mutate methods
			startIndex++;
		}
		if(startIndex<pop.size()){
			Individual second = pop.get(startIndex);
			RealVector rv = (RealVector)second.getGenome();
			rv.setVector(tg.b);
			second.getFitness().setValid(false);	// TODO move this into the coross and mutate methods
			startIndex++;
		}
		return startIndex;
	}
	
	/*
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
	}*/

}
