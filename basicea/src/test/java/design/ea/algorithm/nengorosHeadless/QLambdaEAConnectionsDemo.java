package design.ea.algorithm.nengorosHeadless;

import java.util.ArrayList;

import org.junit.Test;

import ctu.nengoros.comm.rosutils.RosUtils;
import ctu.nengorosHeadless.network.connections.impl.IOGroup;
import ca.nengo.model.StructuralException;
import design.ea.algorithm.impl.RealVectorEA;
import design.ea.ind.fitness.simple.impl.RealValFitness;
import design.ea.ind.genome.vector.impl.RealVector;
import design.ea.ind.individual.Individual;
import design.models.QLambdaTestSim;

/**
 * How to use structured connections of the simulator for the HyperNEAT evolution. 
 * 
 * @author Jaroslav Vitku
 *
 */
public class QLambdaEAConnectionsDemo {

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
		
		QLambdaTestSim.log = 10000;		// completely disables the logging
		QLambdaTestSim sim = new QLambdaTestSim();
		sim.defineNetwork();

		int genomeLength = sim.getInterLayerNo(0).getVector().length;	// only interlayer 0 for the start
		
		// how to get list of inptus and outputs
		ArrayList<IOGroup> inputs = sim.getInterLayerNo(0).getInputs();
		ArrayList<IOGroup> outputs = sim.getInterLayerNo(0).getOutputs();
		
		// weight matrix representing full connections (no input units * no output units)
		// unit ~ neuron in classical ANN (one dimensional input/output)
		float[][] weightMatrix = sim.getInterLayerNo(0).getWeightMatrix();
		// just a flattened weight matrix
		float[] vector = sim.getInterLayerNo(0).getVector();
		
		// structured inputs outputs demo
		for(int i=0; i<inputs.size(); i++){
			
			int startIndex = inputs.get(i).getStartingIndex();
			int noUnits = inputs.get(i).getNoUnits();
			
			System.out.println("I am input no: "+i+" and I have starting index"
					+ " at the weight matrix: "+startIndex+" and I have "+noUnits+" units");
		}
		
		// OR the same matrix can be obtained by:
		try {
			float[][] submatrixII = 
					sim.getInterLayerNo(0).getWeightsBetween(inputs.get(1).getMyIndex(), outputs.get(0).getMyIndex());
		} catch (StructuralException e) {
			e.printStackTrace();
		}
		
		// how to use it to get connection weights between input(1) and output(0)
		int inStartInd = inputs.get(1).getStartingIndex();
		int inNoUnits = inputs.get(1).getNoUnits();
		
		int outStartInd = outputs.get(0).getStartingIndex();
		int outNoUnits = outputs.get(0).getNoUnits();
		
		System.out.println("OK, the input 1 has "+inNoUnits+" and is connected to "+
				" the output 0 by "+(inNoUnits+outNoUnits)+" connection weights");
		
		// this submatrix of the weightMatrix defines connections only between these two 
		float[][] submatrix = new float[inNoUnits][outNoUnits];
		
		int pi = 0, pj = 0;
		for(int i=inStartInd; i<inStartInd+inNoUnits; i++){
			pj = 0;
			for(int j=outStartInd; j<outStartInd+outNoUnits; j++){
				submatrix[pi][pj] = weightMatrix[i][j]; 
			}
		}
		

		// EA setup
		int len = genomeLength;
		int popSize = 30;//50
		int gens = 30;//70
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

