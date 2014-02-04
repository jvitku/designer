package design.ea.strategies.mutation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import design.ea.TestUtil;
import design.ea.algorithm.Population;
import design.ea.algorithm.impl.SingleBinaryVectorPop;

public class BinaryUniformMutationTest {

	@Test
	public void mutateNone(){

		// EA setup
		//int len = 20;
		int len = 5;
		int num = 5;
		Population p = new SingleBinaryVectorPop(num, len);
		TestUtil.randomizeFitness(p);			// note: this sets fitness valid to true

		//OnePointCrossover<Double> opc = new OnePointCrossover<Double>();
		BinaryUniformMutation m = new BinaryUniformMutation();

		System.out.println("\n00000000000000000000000000 PMut = 0");
		// do not cross at all
		m.setPMut(0);
		Population target = UniformMutationTest.mutatePopulation(p, m);

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
		m.setPMut(1);

		target = UniformMutationTest.mutatePopulation(p, m);

		// this checking is not suitable for binary genomes:
		assertTrue(TestUtil.binaryGenesAllDiffer(p, target));

		// all Individuals are changed, so their fitness values are invalid
		for(int i=0; i<target.size(); i++){
			assertFalse(target.get(i).getFitness().isValid());
		}

	}

}
