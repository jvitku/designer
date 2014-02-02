package design.ea.strategies;

import static org.junit.Assert.*;

import org.junit.Test;

import design.ea.TestUtil;
import design.ea.algorithm.Population;
import design.ea.algorithm.impl.SingleVectorPop;
import design.ea.ind.fitness.simple.impl.RealValFitness;
import design.ea.strategies.impl.RouletteWheel;

/**
 * Note: precondition of this test is the AbstractSelectionTest passed
 * 
 * @author Jaroslav Vitku
 *
 */
public class RouletteWheelTest {

	/**
	 * Generate population of random individuals with random fitness vals.
	 * Select many times from the population. 
	 * Sort individuals according to the fitness and check:
	 * 	-fitness is decreasing 
	 *  -number of selections is decreasing
	 */
	@Test
	public void checkRoulette(){
		// EA setup
		int len = 20;
		float max = 100; float min = -100;

		// pop setup
		int num = 10;
		Population p = new SingleVectorPop(num, len, min, max);
		TestUtil.randomizeFitness(p);

		// make many selections
		RouletteWheel s = new RouletteWheel();
		s.resetSelection(p);
		int poc = 1000000;
		
		int[] numSelected = new int[p.size()];	// how many times is i selected?
		for(int i=0; i<numSelected.length; i++)
			numSelected[i] = 0;
		
		for(int i=0; i<=poc; i++){				// select someone poc-times
			numSelected[s.select(1)[0]]++;
		}
		
		// now sort the population according to the fitness
		int [] sorted = s.sort();
		
		for(int i=0; i<p.size()-1; i++){			// for all sorted individuals
			
			// just re-check whether fitness is decreasing
			assertFalse(
					p.get(sorted[i+1]).getFitness().betterThan(
					p.get(sorted[i]).getFitness()));
			
			// check that number of selections is decreasing too
			System.out.println("Fitness vals: "+
			((RealValFitness)p.get(sorted[i+1]).getFitness()).getFitness()+" <= "+
			((RealValFitness)p.get(sorted[i  ]).getFitness()).getFitness()+
			"\tnum selections: "+numSelected[sorted[i+1]] +" <= "+
			numSelected[sorted[i]]);
			
			assertTrue(
					numSelected[sorted[i+1]] <= numSelected[sorted[i]]);
			
		}
	}
}
