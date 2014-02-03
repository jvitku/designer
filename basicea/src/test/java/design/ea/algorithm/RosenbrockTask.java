package design.ea.algorithm;

import static org.junit.Assert.*;

import org.junit.Test;

import design.ea.algorithm.impl.SimpleEA;
import design.ea.ind.fitness.simple.impl.RealValFitness;
import design.ea.ind.genome.vector.impl.RealVector;
import design.ea.ind.individual.Individual;
import design.ea.tasks.Rosenbrock;

public class RosenbrockTask {
	
	@Test
	public void maximize(){
		// EA setup
		int len = 2;
		int popSize = 50;
		int gens = 70;
		float minw = -2, maxw = 2;	

		SimpleEA ea = new SimpleEA(len, false, gens, popSize, minw, maxw);
		ea.setProbabilities(0.05, 0.8);
		assertTrue(ea.wantsEval());
		assertTrue(ea.generation()==0);
		assertTrue(ea.currentOne()==0);

		while(ea.wantsEval()){

			Individual ind = ea.getCurrentInd();
			Float[] val = ((RealVector)ind.getGenome()).getVector();
			double f = Rosenbrock.eval(val[0], val[1]);
			((RealValFitness)ind.getFitness()).setValue(f);
			
			ea.nextIndividual();
		}
		
		Double fitness = ((RealValFitness)ea.getBestInd().getFitness()).getValue();
		assertTrue(fitness>1000);
		
		System.out.println("==== The result is: "+ea.getBestInd().toString());
	}
	
	@Test
	public void minimize(){
		// EA setup
		int len = 2;
		int popSize = 50;
		int gens = 70;
		float minw = -2, maxw = 2;	

		SimpleEA ea = new SimpleEA(len, true, gens, popSize, minw, maxw);
		ea.setProbabilities(0.05, 0.8);
		assertTrue(ea.wantsEval());
		assertTrue(ea.generation()==0);
		assertTrue(ea.currentOne()==0);

		while(ea.wantsEval()){

			Individual ind = ea.getCurrentInd();
			Float[] val = ((RealVector)ind.getGenome()).getVector();
			double f = Rosenbrock.eval(val[0], val[1]);
			((RealValFitness)ind.getFitness()).setValue(f);
			
			ea.nextIndividual();
		}
		
		//Float[] genome = ((RealVector)ea.getBestInd().getGenome()).getVector();
		Double fitness = ((RealValFitness)ea.getBestInd().getFitness()).getValue();
		
		assertTrue(fitness < 2);
		System.out.println("==== The result is: "+ea.getBestInd().toString());
	}

}
