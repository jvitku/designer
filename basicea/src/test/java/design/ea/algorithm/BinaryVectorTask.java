package design.ea.algorithm;

import static org.junit.Assert.*;

import org.junit.Test;

import design.ea.algorithm.impl.RealVectorEA;
import design.ea.ind.fitness.simple.impl.RealValFitness;
import design.ea.ind.genome.vector.impl.RealVector;
import design.ea.ind.individual.Individual;
import design.ea.tasks.Rosenbrock;

/**
 * Just test whether the evolution goes the right direction. 
 * 
 * @author Jaroslav Vitku
 *
 */
public class BinaryVectorTask {

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
		assertTrue(ea.currentOne()==0);

		while(ea.wantsEval()){

			Individual ind = ea.getCurrentInd();
			Float[] val = ((RealVector)ind.getGenome()).getVector();
			double f = Rosenbrock.eval(val[0], val[1]);
			((RealValFitness)ind.getFitness()).setValue(f);

			ea.nextIndividual();
		}

		// Should be something about 2-3000
		Double fitness = ((RealValFitness)ea.getBestInd().getFitness()).getValue();
		assertTrue(fitness>1000);

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
