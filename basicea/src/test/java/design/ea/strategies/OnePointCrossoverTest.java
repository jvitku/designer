package design.ea.strategies;

import static org.junit.Assert.*;

import org.junit.Test;

import tools.utils.DU;
import design.ea.TestUtil;
import design.ea.algorithm.Population;
import design.ea.algorithm.impl.SingleVectorPop;
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
		Population p = new SingleVectorPop(num, len, min, max);
		TestUtil.randomizeFitness(p);			// note: this sets fitness valid to true
		Population pp = p.clone();

		OnePointCrossover<Double> opc = new OnePointCrossover<Double>();

		System.out.println("\n00000000000000000000000000 PCross = 0");
		// do not cross at all
		opc.setPCross(0);
		this.crossPopulation(p, opc);

		assertTrue(TestUtil.genomesAreEqual(p, pp));

		// all Individuals remain unchanged, so their fitness values are valid
		for(int i=0; i<p.size(); i++){
			assertTrue(p.get(i).getFitness().isValid());
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
		this.crossPopulation(p, opc);

		assertTrue(TestUtil.genesAllDiffer(p, pp));

		// all Individuals are changed, so their fitness values are invalid
		for(int i=0; i<p.size(); i++){
			assertTrue(p.get(i).getFitness().isValid());
		}
	}

	private void crossPopulation(Population p, OnePointCrossover<?> opc){
		//Float[] a;
		//Float[] b;
		Individual a,b;
		for(int i=0; i<p.size()-1; i++){
			//a = ((RealVector)p.get(i).getGenome()).getVector();
			//b = ((RealVector)p.get(i+1).getGenome()).getVector();
			a = p.get(i);
			b = p.get(i+1);

			DU.pl("-------");
			DU.pl("Crossed this: "+a.toString());
			DU.pl("Wiiith this:  "+b.toString());

			/*TwoGenomes *cr =*/ opc.cross(a, b);

			DU.pl("and got this: "+a.toString());
			DU.pl("and     this: "+b.toString());

			//((RealVector)p.get(i).getGenome()).setVector(cr.a);
			//((RealVector)p.get(i+1).getGenome()).setVector(cr.b);
		}
	}
}
