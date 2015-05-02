package design.ea.algorithm;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import design.ea.algorithm.impl.RealVectorEA;
import design.ea.ind.fitness.simple.impl.RealValFitness;
import design.ea.ind.individual.Individual;

/**
 * It seemed that the getBest method did not work correctly, here, its behaviour is tested.
 * 
 * This is typical use-case from the jython script.
 * 
 * @author Jaroslav Vitku
 *
 */
public class CheckGetBestMethod {

	Random r = new Random();
	
	/**
	 * Try to find Rosenbrocks maximum (in given range), which is pretty simple.
	 */
	//@Ignore
	@Test
	public void maximize(){
		// EA setup
		int len = 2;
		int popSize = 50;
		int gens = 70;
		float minw = 0, maxw = 1;	

		RealVectorEA ea = new RealVectorEA(len, false, gens, popSize, minw, maxw);
		ea.setProbabilities(0.05, 0.8);
		assertTrue(ea.wantsEval());
		assertTrue(ea.generation()==0);
		assertTrue(ea.getCurrentIndex()==0);

		boolean shouldApply = false;
		
		// while the evolution not ended..
		while(ea.wantsEval()){

			Individual ind = ea.getCurrent();
			//Float[] val = ((RealVector)ind.getGenome()).getVector();
			double f = r.nextDouble();	// generate fitness randomly
			((RealValFitness)ind.getFitness()).setValue(f);

			shouldApply = false;
			if(ea.isTheLastOne()){
				System.out.println("this is the last ones fitness: "+((RealValFitness)ind.getFitness()).getValue());
				System.out.println("this is the best ones fitness: "+((RealValFitness)ea.getBest().getFitness()).getValue());
				
				// really the last one?
				assertTrue(ea.getCurrentIndex()==ea.popSize-1);
				
				// check whether the bestOne is better than the current one 
				assertFalse(ind.getFitness().betterThan(ea.getBest().getFitness()));
				
				// check that no individual in the population is better than the best one stored
				for(int i=0; i<ea.popSize; i++){
					assertFalse(ea.getIndNo(i).getFitness().betterThan(ea.getBest().getFitness()));
				}
				shouldApply = true;	// should apply operators now
			}
			ea.nextIndividual();
			
			// check whether the operators were applied and whether the current index is 0
			// the first one should be copied into this position and unchanged (fitness is valid, genome not changed)
			if(shouldApply){
				assertTrue(ea.getCurrentIndex()==0);
				System.out.println("fitness of the individual on the position 0 is: "+ea.getCurrent().getFitness().toString());
				assertTrue(ea.getCurrent().getFitness().isValid());
			}
		}

		System.out.println("==== The result is: "+ea.getBest().toString());
	}



}
