package design.ea.algorithm.nengorosHeadless;

import org.junit.Test;

import ctu.nengoros.comm.rosutils.RosUtils;
import ca.nengo.model.StructuralException;
import design.ea.algorithm.impl.RealVectorEA;
import design.ea.ind.fitness.simple.impl.RealValFitness;
import design.ea.ind.genome.vector.impl.RealVector;
import design.ea.ind.individual.Individual;
import design.models.QLambdaTestSim;

/**
 * First attempt to design the QLambda network by the EA by use of pure java and NengorosHeadless. 
 * 
 * @author Jaroslav Vitku
 *
 */
public class QLambdaEA {

	/**
	 * Evaluates one individual 
	 * @param sim simulator with the prepared model
	 * @param genome vector of connection weights of correct length proposed by EA
	 * @return fitness of the individual
	 */
	protected float eval(QLambdaTestSim sim, Float[] genome){

		sim.reset(false);		

		try {
			sim.getInterLayerNo(0).setVector(genome); 	// set the connection weights
		} catch (StructuralException e) {
			e.printStackTrace();
			System.err.println("Connection weights not set");
			return 0.0f;
		}	
		sim.run(0, 100);								// run for N steps

		float fitness = sim.getFitnessVal(); 			// read fitness (from <0,1>)
		System.out.println("Fitness read is this: "+fitness);
		return fitness;
	}

	
	/**
	 * This to be used in single-threaded HyperNEAT evolution
	 */
	@Test
	public void qLambdaExampleEvolution(){
		RosUtils.prefferJroscore(true);
		RosUtils.setRqtAutorun(false);
		
		System.out.println("instantiating the simulator");
		QLambdaTestSim sim = new QLambdaTestSim();
		sim.defineNetwork();

		int genomeLength = sim.getInterLayerNo(0).getVector().length;	// only interlayer 0 for the start

		// EA setup
		int len = genomeLength;
		int popSize = 30;//50
		int gens = 50;//70
		float minw = 0, maxw = 1;	

		RealVectorEA ea = new RealVectorEA(len, false, gens, popSize, minw, maxw);
		ea.setProbabilities(0.05, 0.8);

		while(ea.wantsEval()){

			Individual ind = ea.getCurrent();							// get current individual
			Float[] val = ((RealVector)ind.getGenome()).getVector();	// get its genome

			float f = this.eval(sim, val);								// evaluate the fitness

			((RealValFitness)ind.getFitness()).setValue((double)f);		// set the fitness in the EA

			ea.nextIndividual();										// generations and everything is hidden here
		}

		sim.cleanup();

		// results of the evolution
		Double fitness = ((RealValFitness)ea.getBest().getFitness()).getValue();
		System.out.println("==== The resulting genome is: "+ea.getBest().toString()+" fitness is: "+fitness);
	}
}

