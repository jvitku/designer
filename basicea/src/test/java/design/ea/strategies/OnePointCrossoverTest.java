package design.ea.strategies;

import static org.junit.Assert.*;

import org.junit.Test;

import ctu.nengoros.util.SL;
import tools.utils.DU;
import design.ea.TestUtil;
import design.ea.algorithm.Population;
import design.ea.algorithm.impl.SingleVectorPop;
import design.ea.ind.genome.vector.impl.RealVector;
import design.ea.strategies.crossover.OnePointCrossover;

public class OnePointCrossoverTest {

	@Test
	public void testUniformCrossover(){
		// EA setup
		int len = 20;
		float max = 100; float min = -100;
		int num = 10;
		Population p = new SingleVectorPop(num, len, min, max);
		TestUtil.randomizeFitness(p);		
		Population pp = p.clone();

		OnePointCrossover opc = new OnePointCrossover();

		// do not cross at all
		opc.setPCross(0);
		this.crossPopulation(p, opc);

		assertTrue(TestUtil.genomesAreEqual(p, pp));

		/*
		// TODO this could be implemented for testing encoding
		if(!TestUtil.weightsAreEqual(p, pp)){
			DU.pl("Genomes (weights) were changed during crossover with p=0..");
			fail();
		}*/

		// cross all
		opc.setPCross(1);
		this.crossPopulation(p, opc);

		assertTrue(TestUtil.genesAllDiffer(p, pp));
	}

	private void crossPopulation(Population p, OnePointCrossover opc){
		Float[] a;
		Float[] b;
		for(int i=0; i<p.size()-1; i++){
			a = ((RealVector)p.get(i).getGenome()).getVector();
			b = ((RealVector)p.get(i+1).getGenome()).getVector();

			TwoGenomes cr = opc.cross(a, b);

			DU.pl("-------");
			DU.pl("Crossed this: "+SL.toStr(a));
			DU.pl("Wiiith this:  "+SL.toStr(b));

			DU.pl("and got this: "+SL.toStr(cr.a));
			DU.pl("and     this: "+SL.toStr(cr.b));

			((RealVector)p.get(i).getGenome()).setVector(cr.a);
			((RealVector)p.get(i+1).getGenome()).setVector(cr.b);
		}
	}
}
