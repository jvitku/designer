package design.models.logic;

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


public class CrispXor {

	/**
	 * @author Jaroslav Vitku
	 */
	public class CrispXorSim extends AbstractLayeredSimulator{


		public CrispXorSim() {
			/**
			 * TODO no interlayers
			 */
			super(3);
		}

		// change this to get more logging and less speed
		public static final int log = 50; 	
		public static final boolean file = false;

		public NeuralModule ms, ql, gw;

		/**
		 * Build the target model. 
		 * 
		 * @throws StructuralException 
		 */
		public void setInitWeights() throws StructuralException{
			if(!this.networkDefined){
				throw new StructuralException("network must be defined first!");
			}

			float[][] w;

			// fully connect the interlayer 0
			this.makeFullConnections(0);

			// motivation [R+mot] ~> importance [i] 	// input 0 -> output 0	(2x1) interlayer 0
			w = this.interlayers[0].getWeightsBetween(
					ms.getOrigin(BasicMotivation.topicDataOut),
					ql.getTermination(QLambda.topicImportance));
			//w = c.getWeights();
			w[1][0] = 1;			// connect only motivation (not reward) to the importance

			this.interlayers[0].setWeightsBetween(
					ms.getOrigin(BasicMotivation.topicDataOut),
					ql.getTermination(QLambda.topicImportance), w);


			// world [r, state] ~> Q-learning [r, state]	// input 1 -> output 1 (3x3) interlayer 0
			w = this.interlayers[0].getWeightsBetween(
					gw.getOrigin(GridWorldNode.topicDataIn),
					ql.getTermination(QLambda.topicDataIn));

			BasicWeights.pseudoEye(w, 1);	// also one to one connections [r,x,y]
			w[0][0] = 0;

			this.interlayers[0].setWeightsBetween(
					gw.getOrigin(GridWorldNode.topicDataIn),
					ql.getTermination(QLambda.topicDataIn), w);

			// MS[r] -> QLambda[r]
			w = this.interlayers[0].getWeightsBetween(
					ms.getOrigin(BasicMotivation.topicDataOut),
					ql.getTermination(QLambda.topicDataIn));

			w[0][0] = 1;

			this.interlayers[0].setWeightsBetween(
					ms.getOrigin(BasicMotivation.topicDataOut),
					ql.getTermination(QLambda.topicDataIn),w);

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
		
		/**
		 * 
		 */
		public void defineSupervisedDataSet(){
			
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

				float[][] w;

				// world [r,state] ~> motivation [r] 						(3x1) interlayer 2 - do not change
				w = cddd.getWeights();
				w[0][0] = 1;			// connect only reward to the source
				cddd.setWeights(w);

				// Q-Learning [actions] ~> world [actions]					(4x4) interlayer 1 - can be changed too
				w = cd.getWeights();
				BasicWeights.pseudoEye(w, 1);	// one to one connections
				cd.setWeights(w);

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
		 * Defined as MSE on the test data. 
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
}
