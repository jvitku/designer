package design.ea.strategies.crossover;

import static org.junit.Assert.*;

import org.junit.Test;

import tools.utils.DU;
import design.ea.TestUtil;
import design.ea.algorithm.Population;
import design.ea.algorithm.impl.SingleRealVectorPop;
import design.ea.ind.individual.Individual;
import design.ea.strategies.crossover.OnePointCrossover;

public class OnePointCrossoverTest {

	@Test
	public void testUniformCrossover(){
		// EA setup
		//int len = 20;
		int len = 5;
		float max = 100; float min = -100;
		int num = 5;
		Population p = new SingleRealVectorPop(num, len, min, max);
		TestUtil.randomizeFitness(p);			// note: this sets fitness valid to true

		OnePointCrossover<Double> opc = new OnePointCrossover<Double>();

		System.out.println("\n00000000000000000000000000 PCross = 0");
		// do not cross at all
		opc.setPCross(0);
		Population target = crossPopulation(p, opc);

		assertTrue(TestUtil.genomesAreEqual(p, target));

		// all Individuals remain unchanged, so their fitness values are valid
		for(int i=0; i<target.size(); i++){
			assertTrue(target.get(i).getFitness().isValid());
		}

		/*
		// TODO this could be implemented for testing encoding
		if(!TestUtil.weightsAreEqual(p, pp)){
			DU.pl("Genomes (weights) were changed during crossover with p=0..");
			fail();
		}*/

		System.out.println("\n00000000000000000000000000 PCross = 1");
		// cross all
		opc.setPCross(1);

		target = crossPopulation(p, opc);

		assertTrue(TestUtil.genesAllDiffer(p, target));

		// all Individuals are changed, so their fitness values are invalid
		for(int i=0; i<target.size(); i++){
			assertFalse(target.get(i).getFitness().isValid());
		}
	}

	public static Population crossPopulation(Population p, OnePointCrossover<?> opc){
		Population out = p.clone();			// init the target population

		Individual a,b;
		for(int i=0; i<p.size()-1; i++){
			a = p.get(i).clone();			// select two individuals and clone them (selection method)
			b = p.get(i+1).clone();

			DU.pl("-------");
			DU.pl("Crossed this: "+a.toString());
			DU.pl("Wiiith this:  "+b.toString());

			opc.cross(a, b);				// cross them with the pCross

			DU.pl("and got this: "+a.toString());
			DU.pl("and     this: "+b.toString());

			out.set(i, a);					// place them to the target population
			out.set(i+1, b);
		}
		return out;
	}
}
