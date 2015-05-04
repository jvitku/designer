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
import ctu.nengorosHeadless.network.modules.NeuralModule;
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
public class QLambdaTestSim extends AbstractLayeredSimulator{


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
	public static int log = 500; 	
	public static final boolean file = false;

	public NeuralModule ms, ql, gw;


	/**
	 * This method sets the weights so that the model works as expected (evolution sould find this solution).
	 * @throws StructuralException 
	 */
	public void setInitWeights() throws StructuralException{
		if(!this.networkDefined){
			throw new StructuralException("network must be defined first!");
		}
		
		float[][] w;

		// world [r,state] ~> motivation [r] 						(3x1) interlayer 2 - do not change
		w = cddd.getWeights();
		w[0][0] = 1;			// connect only reward to the source
		cddd.setWeights(w);

		// fully connect the interlayer 0
		this.makeFullConnections(0);
		
		// motivation [R+mot] ~> importance [i] 	// input 0 -> output 0	(2x1) interlayer 0
		w = this.interlayers[0].getWeightsBetween(
				ms.getOrigin(BasicMotivation.topicDataOut),
				ql.getTermination(QLambda.topicImportance));
		//w = c.getWeights();
		w[0][0] = 1;			// connect only motivation (not reward) to the importance
		
		// motivation [R+mot] ~> importance [i] 	// input 0 -> output 0	(2x1) interlayer 0
		this.interlayers[0].setWeightsBetween(
				ms.getOrigin(BasicMotivation.topicDataOut),
				ql.getTermination(QLambda.topicImportance), w);
		
		// world [r, state] ~> Q-learning [r, state]	// input 1 -> output 1 (3x3) interlayer 0
		w = this.interlayers[0].getWeightsBetween(
				gw.getOrigin(GridWorldNode.topicDataIn),
				ql.getTermination(QLambda.topicDataIn));
		
		BasicWeights.pseudoEye(w, 1);	// also one to one connections [r,x,y]
		
		this.interlayers[0].setWeightsBetween(
				gw.getOrigin(GridWorldNode.topicDataIn),
				ql.getTermination(QLambda.topicDataIn), w);
		
		// Q-Learning [actions] ~> world [actions]					(4x4) interlayer 1 - can be changed too
		w = cd.getWeights();
		BasicWeights.pseudoEye(w, 1);	// one to one connections
		cd.setWeights(w);
	}
	
	protected int[] pos;
	protected int[] size;
	protected int noValues;		// world dimensions
	protected int[] obstacles;	// list of obstacles in the world
	protected int[] rewards;	// list of rewards in the world
	
	public void defineMap(){
		this.noValues = 10;
		size = new int[]{noValues,noValues};
		pos = new int[]{6,6};
		obstacles = new int[]{1,1,2,2,7,7};
		rewards = new int[]{7,6,0,1,5,5,0,1};
	}
	
	private Connection cddd, cd; 
	
	@Override
	public void defineNetwork() {

		try {
			// Motivation source
			ms = NodeBuilder.basicMotivationSource("motSource", 1, 0.1f, log);
			this.nodes.add(ms);

			this.defineMap();
			
			// Q-Learning
			ql = NodeBuilder.qlambdaASM("qLambda", 2, 4, this.noValues, log, 1, 3);
			this.nodes.add(ql);
			
			// GridWorld
			gw = NodeBuilder.gridWorld("world", log, file, size, 4, pos, obstacles, rewards);
			this.nodes.add(gw);

			// world [r,state] ~> motivation [r] 						(3x1) interlayer 2 - do not change
			cddd = this.connect(
					gw.getOrigin(GridWorldNode.topicDataIn),
					ms.getTermination(BasicMotivation.topicDataIn), 2);

			// motivation [R+mot] ~> importance [i] 	// input 0 -> output 0	(2x1) interlayer 0
			this.registerOrigin(ms.getOrigin(BasicMotivation.topicDataOut), 0);
			this.registerTermination(ql.getTermination(QLambda.topicImportance), 0);
			
			// Q-Learning [actions] ~> world [actions]					(4x4) interlayer 1 - can be changed too
			cd = this.connect(
					ql.getOrigin(QLambda.topicDataOut),
					gw.getTermination(GridWorldNode.topicDataOut), 1);

			// world [r, state] ~> Q-learning [r, state]	// input 1 -> output 1 (3x3) interlayer 0
			this.registerOrigin(gw.getOrigin(GridWorldNode.topicDataIn), 0);
			this.registerTermination(ql.getTermination(QLambda.topicDataIn), 0);

			////////////////////
			this.designFinished();
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
	
}

