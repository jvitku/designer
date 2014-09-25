package design.ea.algorithm;

import static org.junit.Assert.*;

import org.junit.Test;

import design.ea.algorithm.impl.RealVectorEA;
import design.ea.ind.fitness.simple.impl.RealValFitness;
import design.ea.ind.genome.vector.impl.RealVector;
import design.ea.ind.individual.Individual;
import design.ea.tasks.Rosenbrock;

import design.ea.strategies.mutation.RealGaussianUniformMutation;

/**
 * Just test whether the evolution goes the right direction. 
 * 
 * @author Jaroslav Vitku
 *
 */
public class RosenbrockTask {

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
		float minw = -2, maxw = 2;	

		RealVectorEA ea = new RealVectorEA(len, false, gens, popSize, minw, maxw);
		ea.setProbabilities(0.05, 0.8);
		assertTrue(ea.wantsEval());
		assertTrue(ea.generation()==0);
		assertTrue(ea.getCurrentIndex()==0);

		while(ea.wantsEval()){

			Individual ind = ea.getCurrent();
			Float[] val = ((RealVector)ind.getGenome()).getVector();
			double f = Rosenbrock.eval(val[0], val[1]);
			((RealValFitness)ind.getFitness()).setValue(f);

			ea.nextIndividual();
		}

		// Should be something about 2-3000
		Double fitness = ((RealValFitness)ea.getBest().getFitness()).getValue();
		assertTrue(fitness>1000);

		System.out.println("==== The result is: "+ea.getBest().toString());
	}

	/**
	 * This minimizes the Rosenbrock benchmark. The result is poor
	 * due to the fact that only two-valued genome is used. 
	 */
	//@Ignore
	@Test
	public void minimize(){
		// EA setup
		int len = 2;
		int popSize = 250;
		int gens = 70;
		float minw = -2, maxw = 2;	

		RealVectorEA ea = new RealVectorEA(len, true, gens, popSize, minw, maxw);
		((RealGaussianUniformMutation)ea.mutate).setStdev(2.5);
		ea.setProbabilities(0.05, 0.8);
		assertTrue(ea.wantsEval());
		assertTrue(ea.generation()==0);
		assertTrue(ea.getCurrentIndex()==0);

		while(ea.wantsEval()){

			Individual ind = ea.getCurrent();
			Float[] val = ((RealVector)ind.getGenome()).getVector();
			double f = Rosenbrock.eval(val[0], val[1]);
			((RealValFitness)ind.getFitness()).setValue(f);

			ea.nextIndividual();
		}

		//Float[] genome = ((RealVector)ea.getBestInd().getGenome()).getVector();
		Double fitness = ((RealValFitness)ea.getBest().getFitness()).getValue();

		// This should be 0, but typically something under 1.5
		assertTrue(fitness < 4);
		System.out.println("==== The result is: "+ea.getBest().toString());
	}

	/**
	 * Try to find Rosenbrock minimum with better encoding
	 */
	//@Ignore
	@Test
	public void minimizeBetter(){
		// EA setup
		int len = 4;
		int popSize = 250;
		int gens = 70;
		float minw = -2, maxw = 2;	

		RealVectorEA ea = new RealVectorEA(len, true, gens, popSize, minw, maxw);
		((RealGaussianUniformMutation)ea.mutate).setStdev(0.5);
		//ea.mutate.setStdev(2.5);
		ea.setProbabilities(0.5, 0.8);
		assertTrue(ea.wantsEval());
		assertTrue(ea.generation()==0);
		assertTrue(ea.getCurrentIndex()==0);

		while(ea.wantsEval()){

			Individual ind = ea.getCurrent();
			Float[] val = ((RealVector)ind.getGenome()).getVector();
			float[] decoded = decodeGenmoe(val);

			double f = Rosenbrock.eval(decoded[0], decoded[1]);
			((RealValFitness)ind.getFitness()).setValue(f);

			ea.nextIndividual();
		}

		Float[] genome = ((RealVector)ea.getBest().getGenome()).getVector();
		Double fitness = ((RealValFitness)ea.getBest().getFitness()).getValue();
		float[] decoded = decodeGenmoe(genome);

		assertTrue(fitness < 4);
		System.out.println("==== The result is: "+ea.getBest().toString());
		System.out.println("Found value is: "+decoded[0]+","+decoded[1]);
	}

	/**
	 * Two real-valued numbers are encoded as sum of first and 
	 * sum of second half of the genome (of real values).
	 */
	public float[] decodeGenmoe(Float[] genome){
		float[] out = new float[2];
		if(genome.length%2!=0){
			System.err.println("ERROR:the genome has to be dividible" +
					"into two parts of identical length!");
			return out;
		}
		out[0] = 0;
		out[1] = 0;
		int poc = genome.length/2;
		for(int i=0; i<genome.length; i++){
			if(i<=poc){
				out[0] = out[0]+genome[i];
			}else{
				out[1] = out[1]+genome[i];
			}
		}
		return out;
	}
}
