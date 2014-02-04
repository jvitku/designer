package design.ea.algorithm;

import static org.junit.Assert.*;

import org.junit.Test;

import design.ea.algorithm.impl.BinaryVectorEA;
import design.ea.ind.fitness.simple.impl.RealValFitness;
import design.ea.ind.individual.Individual;
import design.ea.ind.genome.vector.impl.BinaryVector;

/**
 * Just test whether the evolution goes the right direction. 
 * 
 * @author Jaroslav Vitku
 *
 */
public class BinaryVectorTask {

	public static Boolean[] solution = new Boolean[]{
		false, true, false, true, false, true, false, true,
		false, true, false, true, false, true, false, true,
		false, true, false, true, false, true, false, true,
		false, true, false, true, false, true, false, true
	};

	/**
	 * Try to find the boolean vector solution: {@link #solution}
	 */
	//@Ignore
	@Test
	public void maximize(){

		// EA setup
		int len = solution.length;
		int popSize = 50;
		int gens = 70;

		BinaryVectorEA ea = new BinaryVectorEA(len, false, gens, popSize);

		ea.setProbabilities(0.05, 0.8);
		assertTrue(ea.wantsEval());
		assertTrue(ea.generation()==0);
		assertTrue(ea.currentOne()==0);

		while(ea.wantsEval()){
			Individual ind = ea.getCurrentInd();
			Boolean[] val = ((BinaryVector)ind.getGenome()).getVector();

			double f = this.computeFitness(val, solution);
			((RealValFitness)ind.getFitness()).setValue(f);
			ea.nextIndividual();
		}
		// The optimum is 32
		Double fitness = ((RealValFitness)ea.getBestInd().getFitness()).getValue();
		assertTrue(fitness>25);

		System.out.println("==== The result is: "+ea.getBestInd().toString());
	}

	@Test
	public void minimize(){

		// EA setup
		int len = solution.length;
		int popSize = 50;
		int gens = 70;

		BinaryVectorEA ea = new BinaryVectorEA(len, true, gens, popSize);

		ea.setProbabilities(0.05, 0.8);
		assertTrue(ea.wantsEval());
		assertTrue(ea.generation()==0);
		assertTrue(ea.currentOne()==0);

		while(ea.wantsEval()){
			Individual ind = ea.getCurrentInd();
			Boolean[] val = ((BinaryVector)ind.getGenome()).getVector();

			double f = this.computeFitness(val, solution);
			((RealValFitness)ind.getFitness()).setValue(f);
			ea.nextIndividual();
		}
		// The optimum is 0 here
		Double fitness = ((RealValFitness)ea.getBestInd().getFitness()).getValue();
		assertTrue(fitness<10);

		System.out.println("==== The result is: "+ea.getBestInd().toString());
	}

	/**
	 * Fitness is given as number of correct binary values in the genome.
	 * @param genome genome to me evaluated
	 * @param solution target solution 
	 * @return number of genes with correct value in the genome
	 */
	private double computeFitness(Boolean[] genome, Boolean[] solution){
		if(genome.length !=solution.length){
			System.err.println("ERROR:the genome has incompatible length" +
					"with the solution!");
			return 0.0;
		}
		double out = 0;
		for(int i=0; i<genome.length; i++){
			if(genome[i]==solution[i]){
				out++;
			}
		}
		return out;
	}
}
