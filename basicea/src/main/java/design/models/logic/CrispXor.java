package design.models.logic;

import static org.junit.Assert.*;

import org.hanns.logic.crisp.gates.impl.AND;
import org.hanns.logic.crisp.gates.impl.NAND;
import org.hanns.logic.utils.evaluators.ros.DataGeneratorNode;
import org.hanns.logic.utils.evaluators.ros.MSENode;

import ca.nengo.model.StructuralException;
import ctu.nengoros.exceptions.ConnectionException;
import ctu.nengoros.model.transformMultiTermination.impl.BasicWeights;
import ctu.nengoros.network.common.exceptions.StartupDelayException;
import ctu.nengoros.testsuit.demo.nodes.gate.OR;
import ctu.nengorosHeadless.network.connections.Connection;
import ctu.nengorosHeadless.network.modules.NeuralModule;
import ctu.nengorosHeadless.simulator.InterLayerBuilder;
import ctu.nengorosHeadless.simulator.NodeBuilder;
import ctu.nengorosHeadless.simulator.impl.AbstractLayeredSimulator;


public class CrispXor {

	/**
	 * @author Jaroslav Vitku
	 */
	public static class CrispXorSim extends AbstractLayeredSimulator{


		public CrispXorSim() {
			/**
			 * TODO no interlayers
			 */
			super(4);
		}

		// change this to get more logging and less speed
		public static int log = 50; 	
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

			// generator -> OR
			w = this.getInterLayerNo(0).getWeightsBetween(
					dg.getOrigin(DataGeneratorNode.topicDataOut),
					or.getTermination(OR.inAT));
			BasicWeights.pseudoEye(w, 1);
			this.getInterLayerNo(0).setWeightsBetween(
					dg.getOrigin(DataGeneratorNode.topicDataOut),
					or.getTermination(OR.inAT),w);
			
			/*
			w = this.getInterLayerNo(0).getWeightsBetween(
					dg.getOrigin(DataGeneratorNode.topicDataOut),
					or.getTermination(OR.inBT));
			w[1][0] = 1;
			this.getInterLayerNo(0).setWeightsBetween(
					dg.getOrigin(DataGeneratorNode.topicDataOut),
					or.getTermination(OR.inBT),w);
			*/
			// generator -> NAND
			w = this.getInterLayerNo(0).getWeightsBetween(
					dg.getOrigin(DataGeneratorNode.topicDataOut),
					nand.getTermination(NAND.inAT));
			BasicWeights.pseudoEye(w, 1);
			this.getInterLayerNo(0).setWeightsBetween(
					dg.getOrigin(DataGeneratorNode.topicDataOut),
					nand.getTermination(NAND.inAT),w);
			/*
			w = this.getInterLayerNo(0).getWeightsBetween(
					dg.getOrigin(DataGeneratorNode.topicDataOut),
					nand.getTermination(NAND.inBT));
			w[1][0] = 1;
			this.getInterLayerNo(0).setWeightsBetween(
					dg.getOrigin(DataGeneratorNode.topicDataOut),
					nand.getTermination(NAND.inBT),w);
			*/
			
			// OR -> AND
			w = this.getInterLayerNo(1).getWeightsBetween(
					or.getOrigin(OR.outAT),
					and.getTermination(AND.inAT));
			w[0][0] = 1;
			this.getInterLayerNo(1).setWeightsBetween(
					or.getOrigin(OR.outAT),
					and.getTermination(AND.inAT),w);
			
			// NAND -> AND
			w = this.getInterLayerNo(1).getWeightsBetween(
					nand.getOrigin(NAND.outAT),
					and.getTermination(AND.inAT));
			w[0][1] = 1;
			this.getInterLayerNo(1).setWeightsBetween(
					nand.getOrigin(NAND.outAT),
					and.getTermination(AND.inAT),w);
			
			// AND -> MSENode
			w = this.getInterLayerNo(2).getWeightsBetween(
					and.getOrigin(AND.outAT),
					ev.getTermination(MSENode.topicDataIn));
			w[0][0] = 1;
			this.getInterLayerNo(2).setWeightsBetween(
					and.getOrigin(AND.outAT),
					ev.getTermination(MSENode.topicDataIn),w);
		}

		public void defineSupervisedDataSet(){
			
		}

		private Connection cd; 
		public NeuralModule ev, dg;
		private NeuralModule or, nand, and;
		
		
		@Override
		public void defineNetwork() {

			try {
				// add OR between interlayers no 0,1
				or = InterLayerBuilder.addSOR(0, 1, this);
				nand = InterLayerBuilder.addSNAND(0, 1, this);
				and = InterLayerBuilder.addSAND(1, 2, this);

				// add the MSE node, which has prosperity defined as 1-MSE (smaller MSE, better prosperity=fitness)
				ev = NodeBuilder.mseNode("mse", 1, 2,log);
				this.nodes.add(ev);
				
				this.registerTermination(ev.getTermination(MSENode.topicDataIn), 2);	// gates should be connected here
				
				int[] data = new int[]{0,0,0,1,1,0,1,1};
				int[] dataSol = new int[]{0,1,1,0};
				
				dg = NodeBuilder.dataGeneratorNode("generator",2,1,data, dataSol, log);
				this.nodes.add(dg);
				
				this.registerOrigin(dg.getOrigin(DataGeneratorNode.topicDataOut), 0);	// input to the gates
				this.registerTermination(dg.getTermination(DataGeneratorNode.topicDataIn), 3);
				
				// supervised data
				cd = this.connect(
						dg.getOrigin(DataGeneratorNode.topicDataSolution),
						ev.getTermination(MSENode.topicDataInSupervised),3);
				
				////////////////////
				this.designFinished();
				this.networkDefined = true;

				// three fully connected interlayers 
				this.makeFullConnections(0);		// generator -> gates
				this.makeFullConnections(1);		// gates -> gate
				this.makeFullConnections(2);		// gate -> evaluator
				
				float[][] w = cd.getWeights();
				BasicWeights.pseudoEye(w, 1);	// one to one connections (one dimension)
				cd.setWeights(w);
				

				this.setInitWeights();
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
		 * Defined as MSE on the data. 
		 */
		@Override
		public float getFitnessVal() {
			float fitness;
			try {
				fitness = ev.getOrigin(MSENode.topicProsperity).getValues()[0];
				//fitness = ms.getOrigin(BasicMotivation.topicProsperity).getValues()[0];
				return fitness;

			} catch (StructuralException e) {
				e.printStackTrace();
				return 0.0f;
			}
		}
	}
}

