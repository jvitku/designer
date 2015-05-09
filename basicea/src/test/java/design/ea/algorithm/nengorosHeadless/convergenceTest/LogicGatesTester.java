package design.ea.algorithm.nengorosHeadless.convergenceTest;

import static org.junit.Assert.*;

import org.hanns.logic.utils.evaluators.ros.DataGeneratorNode;
import org.hanns.logic.utils.evaluators.ros.MSENode;
import org.hanns.physiology.statespace.ros.BasicMotivation;
import org.junit.Test;

import ctu.nengorosHeadless.simulator.impl.AbstractLayeredSimulator;
import ca.nengo.model.StructuralException;
import design.models.QLambdaPaper;
import design.models.QLambdaPaperComplex;
import design.models.QLambdaTestSim;
import design.models.QLambdaTestSimSmall;
import design.models.logic.CrispXor;

/**
 * How many simulation steps it is needed for a given map? 
 * @author Jaroslav Vitku
 *
 */
public class LogicGatesTester{


	@Test
	public void testConvergence(){
		int maxSteps = 4;
		int logPeriod = 1;

		CrispXor.CrispXorSim map= new CrispXor.CrispXorSim();

		float fitness = this.eval(map, maxSteps, logPeriod);

		assertTrue(fitness > 0.1);

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
				simulator.dg.getTermination(DataGeneratorNode.topicDataIn).sendValue(1, 0);
				
				simulator.makeStep();

				float mse = simulator.ev.getOrigin(MSENode.topicProsperity).getValues()[0];
				System.out.println("prosperity is "+mse);
				//float r = simulator.ms.getOrigin(BasicMotivation.topicDataOut).getValues()[0];
				//rewards += r;
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
