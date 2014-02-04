package design.ea.strategies.crossover;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import design.ea.TestUtil;
import design.ea.algorithm.Population;
import design.ea.algorithm.impl.SingleRealVectorPop;

public class OnePointBinaryCrossover {


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
		Population target = OnePointCrossoverTest.crossPopulation(p, opc);

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

		target = OnePointCrossoverTest.crossPopulation(p, opc);

		assertTrue(TestUtil.genesAllDiffer(p, target));

		// all Individuals are changed, so their fitness values are invalid
		for(int i=0; i<target.size(); i++){
			assertFalse(target.get(i).getFitness().isValid());
		}
	}

}
