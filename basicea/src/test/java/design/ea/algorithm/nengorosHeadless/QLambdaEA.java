package design.ea.algorithm.nengorosHeadless;

import static org.junit.Assert.*;

import org.hanns.environments.discrete.ros.GridWorldNode;
import org.hanns.physiology.statespace.ros.BasicMotivation;
import org.hanns.rl.discrete.ros.srp.QLambda;
import org.junit.Test;

import ca.nengo.model.StructuralException;
import ctu.nengoros.exceptions.ConnectionException;
import ctu.nengoros.model.transformMultiTermination.impl.BasicWeights;
import ctu.nengoros.network.common.exceptions.StartupDelayException;
import ctu.nengorosHeadless.network.modules.NeuralModule;
import ctu.nengorosHeadless.network.modules.io.Connection;
import ctu.nengorosHeadless.simulator.NodeBuilder;
import ctu.nengorosHeadless.simulator.impl.AbstractSimulator;
import design.ea.algorithm.impl.RealVectorEA;
import design.ea.ind.fitness.simple.impl.RealValFitness;
import design.ea.ind.genome.vector.impl.RealVector;
import design.ea.ind.individual.Individual;
import design.ea.tasks.Rosenbrock;

/**
 * First attempt to design the QLambda network by the EA by use of pure java and NengorosHeadless. 
 * 
 * @author Jaroslav Vitku
 *
 */
public class QLambdaEA {

	//@Ignore
	@Test
	public void maximize(){

		System.out.println("instantiating the simulator");
		QLambdaTestSim sim = new QLambdaTestSim();
		sim.defineNetwork();

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
	
	public class QLambdaTestSim extends AbstractSimulator{

		public static final int log = 1; 
		public static final boolean file = false;

		@Override
		public void defineNetwork() {

			try {

				// Motivation source
				NeuralModule ms = NodeBuilder.basicMotivationSource("motSource", 1, 0.1f, log);
				this.nodes.add(ms);

				// Q-Learning
				NeuralModule ql = NodeBuilder.qlambdaASM("qLambda", 2, 4, 10, log, 1, 3);
				this.nodes.add(ql);

				// GridWorld
				int[] size = new int[]{10,10};
				int[] pos = new int[]{4,4};
				int[] obstacles = new int[]{1,1,2,2,3,3,5,5,6,6,7,7,8,8};
				int[] rewards = new int[]{7,6,0,1,9,0,0,1};

				NeuralModule gw = NodeBuilder.gridWorld("world", log, file, size, 4, pos, obstacles, rewards);
				this.nodes.add(gw);

				float[][] w;

				// world [r,state] ~> motivation [r]
				Connection cddd = this.connect(
						gw.getOrigin(GridWorldNode.topicDataIn),
						ms.getTermination(BasicMotivation.topicDataIn));
				w = cddd.getWeights();
				w[0][0] = 1;			// connect only reward to the source

				// motivation [R+mot] ~> importance [i] 
				Connection c = this.connect(
						ms.getOrigin(BasicMotivation.topicDataOut),
						ql.getTermination(QLambda.topicImportance));

				w = c.getWeights();
				w[0][0] = 1;			// connect only motivation (not reward) to the importance

				// Q-Learning [actions] ~> world [actions]
				Connection cd = this.connect(
						ql.getOrigin(QLambda.topicDataOut),
						gw.getTermination(GridWorldNode.topicDataOut));
				w = cd.getWeights();
				BasicWeights.pseudoEye(w,1);	// one to one connections

				// world [r, state] ~> Q-learning [state]
				Connection cdd = this.connect(
						gw.getOrigin(GridWorldNode.topicDataIn),
						ql.getTermination(QLambda.topicDataIn));
				w = cdd.getWeights();
				BasicWeights.pseudoEye(w,1);	// also one to one connections [r,x,y]

			} catch (ConnectionException e) {
				e.printStackTrace();
				fail();
			} catch (StartupDelayException e) {
				e.printStackTrace();
				fail();
			} catch (StructuralException e) {
				e.printStackTrace();
				fail();
			}
		}
	}
}


