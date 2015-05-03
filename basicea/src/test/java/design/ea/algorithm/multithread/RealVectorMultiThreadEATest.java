package design.ea.algorithm.multithread;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

import design.ea.algorithm.impl.RealVectorMultiThreadEA;
import design.ea.ind.fitness.simple.impl.RealValFitness;
import design.ea.ind.individual.Individual;
import design.ea.tasks.Rosenbrock;
import design.ea.ind.genome.vector.impl.RealVector;


public class RealVectorMultiThreadEATest {

	@Test
	public void singleThread(){
		// EA setup
		int len = 2;
		int popSize = 50;
		int gens = 70;
		float minw = -2, maxw = 2;	

		RealVectorMultiThreadEA ea = new RealVectorMultiThreadEA(len, false, gens, popSize, minw, maxw);

		ea.setNoThreads(1);
		
		ea.setProbabilities(0.05, 0.8);
		assertTrue(ea.wantsEval());
		assertTrue(ea.generation()==0);

		long ID = new Random().nextLong();	// unique ID
		
		while(ea.wantsEval()){

			Individual ind = ea.popIndividual(ID);
			
			if(ind == null){
				if(!ea.wantsEval()){
					System.out.println("Evolution ends now! exiting!");
					return;
				}
				waitForOthers();	// wait
				continue;			// try again the same
			}
			
			Float[] val = ((RealVector)ind.getGenome()).getVector();
			double f = Rosenbrock.eval(val[0], val[1]);
			((RealValFitness)ind.getFitness()).setValue(f);
		}

		// Should be something about 2-3000
		Double fitness = ((RealValFitness)ea.getBest().getFitness()).getValue();
		assertTrue(fitness>1000);

		System.out.println("==== The result is: "+ea.getBest().toString());
	}

	@Ignore
	@Test
	public void twoThread(){
		// EA setup
		int len = 2;
		int popSize = 50;
		int gens = 70;
		float minw = -2, maxw = 2;	

		RealVectorMultiThreadEA ea = new RealVectorMultiThreadEA(len, false, gens, popSize, minw, maxw);

		ea.setNoThreads(1);
		
		ea.setProbabilities(0.05, 0.8);
		assertTrue(ea.wantsEval());
		assertTrue(ea.generation()==0);

		long ID = new Random().nextLong();	// unique ID
		
		while(ea.wantsEval()){

			Individual ind = ea.popIndividual(ID);
			
			if(ind == null){
				if(!ea.wantsEval()){
					System.out.println("Evolution ends now! exiting!");
					return;
				}
				waitForOthers();	// wait
				continue;			// try again the same
			}
			
			Float[] val = ((RealVector)ind.getGenome()).getVector();
			double f = Rosenbrock.eval(val[0], val[1]);
			((RealValFitness)ind.getFitness()).setValue(f);
		}

		// Should be something about 2-3000
		Double fitness = ((RealValFitness)ea.getBest().getFitness()).getValue();
		assertTrue(fitness>1000);

		System.out.println("==== The result is: "+ea.getBest().toString());
	}

	
	
	protected void waitForOthers(){
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
