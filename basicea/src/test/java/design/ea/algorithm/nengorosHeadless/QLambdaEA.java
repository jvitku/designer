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
import ctu.nengorosHeadless.network.connections.Connection;
import ctu.nengorosHeadless.network.connections.InterLayerWeights;
import ctu.nengorosHeadless.network.modules.NeuralModule;
import ctu.nengorosHeadless.simulator.EASimulator;
import ctu.nengorosHeadless.simulator.NodeBuilder;
import ctu.nengorosHeadless.simulator.impl.AbstractLayeredSimulator;
import design.ea.algorithm.impl.RealVectorEA;
import design.ea.ind.fitness.simple.impl.RealValFitness;
import design.ea.ind.genome.vector.impl.RealVector;
import design.ea.ind.individual.Individual;

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

		System.out.println("instantiating the simulator");
		QLambdaTestSim sim = new QLambdaTestSim();
		sim.defineNetwork();

		int genomeLength = sim.getInterLayerNo(0).getVector().length;	// only interlayer 0 for the start

		// EA setup
		int len = genomeLength;
		int popSize = 30;//50
		int gens = 70;
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
	 * @author Jaroslav Vitku
	 */
	public class QLambdaTestSim extends AbstractLayeredSimulator implements EASimulator{


		public QLambdaTestSim() {
			/**
			 * Two interlayers in this network, the first one is model to be optimized,
			 * the second one holds connections QLearning -> world (these are static for now)
			 */
			super(2);
		}

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
						ms.getTermination(BasicMotivation.topicDataIn), 1);	// this does not have to be changed
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

		@Override
		public InterLayerWeights getInterLayerNo(int no) {
			if(no<0 || no>this.interlayers.length){
				System.err.println("Incorrect no. of interlayer");
				return null;
			}
			return interlayers[no];
		}
	}
}

