package design.ea.algorithm.nengorosHeadless.convergenceTest;

import static org.junit.Assert.*;

import org.hanns.logic.utils.evaluators.ros.DataGeneratorNode;
import org.hanns.logic.utils.evaluators.ros.MSENode;
import org.junit.Test;

import ca.nengo.model.StructuralException;
import design.models.logic.CrispXor;

/**
 * How many simulation steps it is needed for a given map? 
 * @author Jaroslav Vitku
 *
 */
public class LogicGatesTester{


	/**
	 * Runs the test with the target model (with setInitWeights called), so the fitness is 1.0,
	 * which means no sample missed.
	 */
	@Test
	public void testZeroErrorOnTargetModel(){
		int maxSteps = 20;
		int logPeriod = 1;

		CrispXor.CrispXorSim map= new CrispXor.CrispXorSimBig();
		map.log = 1;

		float fitness = this.eval(map, maxSteps, logPeriod);

		assertTrue(fitness == 1.0);
	}

	public float eval(CrispXor.CrispXorSim simulator, int maxSteps, int logPeriod){

		System.err.println("building the model");
		
		simulator.defineNetwork();
		
		System.err.println("network defined");

		try {
			simulator.setInitWeights();	// build the "optimal" model
		} catch (StructuralException e) {
			e.printStackTrace();
		}
		
		simulator.getInterLayerNo(0).printMatrix(simulator.getInterLayerNo(0).getWeightMatrix());
		simulator.getInterLayerNo(1).printMatrix(simulator.getInterLayerNo(1).getWeightMatrix());
		simulator.getInterLayerNo(2).printMatrix(simulator.getInterLayerNo(2).getWeightMatrix());
		
		System.err.println("model built");
		simulator.reset(false);

		int step = 0;
		while(step++ < maxSteps){
			System.out.println("making step now "+step);
			//simulator.dg.getOrigin(DataGeneratorNode.topicDataIn);
			try {
				simulator.dg.getTermination(DataGeneratorNode.topicDataIn).sendValue(11, 0);
				
				simulator.makeStep();

				float mse = simulator.ev.getOrigin(MSENode.topicProsperity).getValues()[0];
				System.out.println("prosperity is "+mse);
				
			} catch (StructuralException e) {
				e.printStackTrace();
			}

			/*
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		}
		System.err.println("exiting");
		return simulator.getFitnessVal();
	}

}
