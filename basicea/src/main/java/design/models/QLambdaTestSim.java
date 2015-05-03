package design.models;

import static org.junit.Assert.*;

import org.hanns.environments.discrete.ros.GridWorldNode;
import org.hanns.physiology.statespace.ros.BasicMotivation;
import org.hanns.rl.discrete.ros.srp.QLambda;

import ca.nengo.model.StructuralException;
import ctu.nengoros.exceptions.ConnectionException;
import ctu.nengoros.model.transformMultiTermination.impl.BasicWeights;
import ctu.nengoros.network.common.exceptions.StartupDelayException;
import ctu.nengorosHeadless.network.connections.Connection;
import ctu.nengorosHeadless.network.connections.InterLayerWeights;
import ctu.nengorosHeadless.network.modules.NeuralModule;
import ctu.nengorosHeadless.simulator.EASimulator;
import ctu.nengorosHeadless.simulator.NodeBuilder;
import ctu.nengorosHeadless.simulator.impl.AbstractLayeredSimulator;


/**
 * This is the model together with the simulator. 
 * Use this in order to evaluate one individual, see the example how. 
 * 
 * The anticipated use of this simulator is as follows:
 * 
 * defineModel
 * get length of the genome
 * 
 * for(all genomes to be evaluated){
 * 	-reset
 *  -set weights
 * 	-run the simulation
 * 	-read fitness value
 *  }
 *  cleanup
 *  
 *  
 *  This model has two interLayer connections, but the evolution can use only the interlayer 0 for now.
 *  
 * @author Jaroslav Vitku
 */
public class QLambdaTestSim extends AbstractLayeredSimulator implements EASimulator{


	public QLambdaTestSim() {
		/**
		 * Three interlayers in this network, the first one is model to be optimized.
		 * 
		 * The second one holds connections QLearning -> world (these can be either static or optimized too).
		 * 
		 * The third one connects world to the motivation source -> should be only static (defines the fitness).
		 */
		super(3);
	}

	// change this to get more logging and less speed
	public static final int log = 500; 	
	public static final boolean file = false;

	public NeuralModule ms, ql, gw;

	@Override
	public void defineNetwork() {

		try {

			// Motivation source
			ms = NodeBuilder.basicMotivationSource("motSource", 1, 0.1f, log);
			this.nodes.add(ms);

			// Q-Learning
			ql = NodeBuilder.qlambdaASM("qLambda", 2, 4, 10, log, 1, 3);
			this.nodes.add(ql);

			// GridWorld
			int[] size = new int[]{10,10};
			int[] pos = new int[]{6,6};
			int[] obstacles = new int[]{1,1,2,2,7,7};
			int[] rewards = new int[]{7,6,0,1,5,5,0,1};

			gw = NodeBuilder.gridWorld("world", log, file, size, 4, pos, obstacles, rewards);
			this.nodes.add(gw);

			//float[][] w;

			// world [r,state] ~> motivation [r]
			Connection cddd = this.connect(
					gw.getOrigin(GridWorldNode.topicDataIn),
					ms.getTermination(BasicMotivation.topicDataIn), 2);	// this does not have to be changed
			//w = cddd.getWeights();
			//w[0][0] = 1;			// connect only reward to the source

			// motivation [R+mot] ~> importance [i] 
			Connection c = this.connect(
					ms.getOrigin(BasicMotivation.topicDataOut),
					ql.getTermination(QLambda.topicImportance), 0);

			//w = c.getWeights();
			//w[0][0] = 1;			// connect only motivation (not reward) to the importance

			// Q-Learning [actions] ~> world [actions]
			Connection cd = this.connect(
					ql.getOrigin(QLambda.topicDataOut),
					gw.getTermination(GridWorldNode.topicDataOut), 1);
			//w = cd.getWeights();
			//BasicWeights.pseudoEye(w,1);	// one to one connections

			// world [r, state] ~> Q-learning [state]
			Connection cdd = this.connect(
					gw.getOrigin(GridWorldNode.topicDataIn),
					ql.getTermination(QLambda.topicDataIn), 0);
			//w = cdd.getWeights();
			//BasicWeights.pseudoEye(w,1);	// also one to one connections [r,x,y]

			////////////////////
			this.designFinished();
			float[][] w;

			w = cddd.getWeights();
			w[0][0] = 1;
			cddd.setWeights(w);

			w = c.getWeights();
			w[0][0] = 1;
			c.setWeights(w);

			w = cd.getWeights();
			BasicWeights.pseudoEye(w, 1);
			cd.setWeights(w);

			w = cdd.getWeights();
			BasicWeights.pseudoEye(w, 1);
			cdd.setWeights(w);

			this.networkDefined = true;
			
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

	/**
	 * Defined as a prosperity of the MotivationSource (that is: MSD from optimal conditions), 
	 * the higher the better (interval <0,1>) 
	 */
	@Override
	public float getFitnessVal() {
		float fitness;
		try {
			fitness = ms.getOrigin(BasicMotivation.topicProsperity).getValues()[0];
			return fitness;

		} catch (StructuralException e) {
			e.printStackTrace();
			return 0.0f;
		}
	}


	/**
	 * Use either only interlayer 0 or both, 0 and 1.
	 * The InterLayer connects the motivation source to the reward, do not use it 
	 * (changes the fitness definition).
	 */
	@Override
	public InterLayerWeights getInterLayerNo(int no) {
		if(no<0 || no>this.interlayers.length){
			System.err.println("Incorrect no. of interlayer");
			return null;
		}
		return interlayers[no];
	}
}

