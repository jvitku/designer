package design.ea.algorithm.nengorosHeadless.convergenceTest;

import static org.junit.Assert.*;

import org.hanns.physiology.statespace.ros.BasicMotivation;
import org.junit.Test;

import ca.nengo.model.StructuralException;
import design.models.QLambdaPaperSmaller;
import design.models.QLambdaTestSim;
import design.models.QLambdaTestSimSmall;

/**
 * How many simulation steps it is needed for a given map? 
 * @author Jaroslav Vitku
 *
 */
public class ConvergenceTester{


	@Test
	public void testConvergence(){
		int maxSteps = 20000;
		int logPeriod = 100;

		QLambdaTestSim map= new QLambdaTestSimSmall();
		map.log = 50000;
		
		float fitness = this.eval(map, maxSteps, logPeriod);

		assertTrue(fitness > 0.1);

	}

	public float eval(QLambdaTestSim simulator, int maxSteps, int logPeriod){

		float rewards = 0;
		
		simulator.defineNetwork();
		try {
			simulator.setInitWeights();	// build the "optimal" model
		} catch (StructuralException e) {
			e.printStackTrace();
		}
		simulator.getInterLayerNo(0).printMatrix(simulator.getInterLayerNo(0).getWeightMatrix());
		
		simulator.reset(false);

		int step = 0;
		while(step++ < maxSteps){
			simulator.makeStep();
			
			try {
				float r = simulator.ms.getOrigin(BasicMotivation.topicDataOut).getValues()[0];
				rewards += r;
			} catch (StructuralException e) {
				e.printStackTrace();
			}

			if(step % logPeriod==0){
				System.out.println("step: "+step+"\tFitness value is: "+simulator.getFitnessVal()+
						" rps is: "+(rewards/step));
			}
			/*
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		}
		System.out.println("exiting");
		return simulator.getFitnessVal();
	}

	public class MyMap extends QLambdaTestSim{

		public void defineMap(){
			this.noValues = 10;
			size = new int[]{noValues,noValues};
			pos = new int[]{6,6};
			obstacles = new int[]{1,1,2,2,7,7};
			rewards = new int[]{7,6,0,1,5,5,0,1};
		}
	}
}
