package design.ea.algorithm.nengorosHeadless;


import java.io.IOException;
import java.util.Random;

import org.junit.Test;

import ctu.nengoros.comm.rosutils.RosUtils;
import ctu.nengoros.util.SL;
import ca.nengo.model.StructuralException;
import design.ea.algorithm.impl.RealVectorMultiThreadEA;
import design.ea.ind.fitness.simple.impl.RealValFitness;
import design.ea.ind.individual.Individual;
import design.ea.ind.genome.vector.impl.RealVector;
import design.models.logic.CrispXor.CrispXorSim;
import design.models.logic.CrispXor.CrispXorSimBig;
import design.models.logic.CrispXor.CrispXorSimBigger;
import design.models.logic.CrispXor.CrispXorSimMoreGates;

public class XorMultiThreadEABoxPlot {
	
	@Test
	public void runTenTimes(){
		for(int i=0; i<10; i++){
			
			System.err.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx eovlution no: "+i);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			this.multiThreadedEA(""+i);
			
			
		}
	}
	
	public void multiThreadedEA(String number){
		EvolutionLogger el;
		try {
			el = new EvolutionLogger(
					"../../data/xor/fitness_"+number+".txt",
					"../../data/xor/bestInds_"+number+".txt");
			
		} catch (IOException e2) {
			System.err.println("could not access files for writing!");
			return;
		}

		// run the core, the original (non-java) preferably
		RosUtils.prefferJroscore(true);
		RosUtils.setRqtAutorun(false);		// show the RQT window automatically? 
		RosUtils.utilsShallStart();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		// one instance of the simulator in order to get genome length
		CrispXorSim.log = 50000;		
		CrispXorSim sim = new CrispXorSim();
		//CrispXorSim sim = new CrispXorSimMoreGates();	// change the model HERE
		//CrispXorSim sim = new CrispXorSimBig();
		//CrispXorSim sim = new CrispXorSimBigger();
		sim.defineNetwork();

		
		// all interlayers are here
		int len = sim.getInterLayerNo(0).getVector().length+
				sim.getInterLayerNo(1).getVector().length+
				sim.getInterLayerNo(2).getVector().length;

		// EA setup
		int popSize = 50;
		int gens = 200;
		float minw = 0, maxw = 1;	

		RealVectorMultiThreadEA ea = new RealVectorMultiThreadEA(len, false, gens, popSize, minw, maxw);
		ea.setProbabilities(0.05, 0.8);

		// setup no of evaluator threads and run them all
		int noThreads = 1;
		ea.setNoThreads(noThreads);

		NengoRosEvaluatorThread[] threads = new NengoRosEvaluatorThread[noThreads];
		for(int i = 0; i < threads.length; i++){
			if(i==0){
				// the first one has already created instance..
				threads[i] = new NengoRosEvaluatorThread(ea, sim, el, true);
			}else{
				
				// communication asynchronous
				try {
					Thread.sleep(new Random().nextInt(100));
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}/**/

				threads[i] = new NengoRosEvaluatorThread(ea, new CrispXorSim(), el, false);
			}
			threads[i].start();
		}

		// wait for evolution to end
		System.out.println("---------- Waiting for the threads");
		for(int i = 0; i < threads.length; i++){
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Read the results, should be something about 2-3000
		Double fitness = ((RealValFitness)ea.getBest().getFitness()).getValue();

		//assertTrue(fitness>0.2);	// some reasonable fitness should be found
		RosUtils.utilsShallStop();
		System.out.println("==== The result is: "+ea.getBest().toString());

		try {
			sim.decode(((RealVector)ea.getBest().getGenome()).getVector());

			sim.getInterLayerNo(0).printMatrix(sim.getInterLayerNo(0).getWeightMatrix());
			sim.getInterLayerNo(1).printMatrix(sim.getInterLayerNo(1).getWeightMatrix());
			sim.getInterLayerNo(2).printMatrix(sim.getInterLayerNo(2).getWeightMatrix());
		} catch (StructuralException e) {
			e.printStackTrace();
		}
	}


	public class NengoRosEvaluatorThread extends Thread
	{
		private RealVectorMultiThreadEA ea;
		private CrispXorSim mySim;
		private final long myId;
		private EvolutionLogger el;
		public static final int waitTimeNanos = 100;

		public static final int DEF_STEPS = 20;	// Is more than ok
		private int steps = DEF_STEPS;

		private boolean iLogBest = false;

		public NengoRosEvaluatorThread(RealVectorMultiThreadEA ea, CrispXorSim sim, EvolutionLogger el, boolean iLogBest)
		{
			this.mySim = sim;
			this.myId  = new Random().nextLong();
			this.ea = ea;
			this.el = el;
			this.iLogBest = iLogBest;
		}

		public synchronized void run()
		{
			if(!mySim.networkDefined()){
				mySim.defineNetwork();

				/*
				// test if it does something
				try {
					((QLambdaTestSim)mySim).setInitWeights();
				} catch (StructuralException e) {
					e.printStackTrace();
				}/**/
			}

			int gen = 0;

			while(true)
			{
				Individual ind = ea.popIndividual(myId);
				if(ind == null){

					if(!ea.wantsEval()){
						System.out.println(myId+ " OK, evolution ends, exiting");
						return;
					}else{
						try {
							this.wait(0, waitTimeNanos);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}else{
					if(gen!=ea.generation() && iLogBest){

						Float[] g = ((RealVector)ea.getIndNo(0).getGenome()).getVector();
						try {
							mySim.decode(((RealVector)ea.getIndNo(0).getGenome()).getVector());
						} catch (StructuralException e) {
							e.printStackTrace();
						}

						el.writeBestInd(ea.generation()-1, 
								((RealValFitness)ea.getIndNo(0).getFitness()).getValue(), 
								g);

						gen = ea.generation();
					}
					// run the simulation
					Float[] genome = ((RealVector)ind.getGenome()).getVector();
					try {
						mySim.decode(genome);
					} catch (StructuralException e) {
						e.printStackTrace();
					}
					//System.out.println("genome: "+SL.toStr(genome));
					double fitness = this.eval(genome);
					// write the fitness
					((RealValFitness)ind.getFitness()).setValue(fitness);
					el.writefitness(ea.generation(), (float)fitness);
				}
			}
		}

		public void setSimulationTime(int steps){
			this.steps = steps;
		}

		/**
		 * Evaluates one individual
		 *  
		 * @param genome vector of connection weights of correct length proposed by EA
		 * @return fitness of the individual
		 */
		protected float eval(Float[] genome){
			mySim.reset(false);

			try {
				this.genomeToModel(genome);
			} catch (StructuralException e) {
				e.printStackTrace();
				System.err.println("Connection weights not set");
				return 0.0f;
			}	
			mySim.run(0, steps);								// run for N steps

			float fitness = mySim.getFitnessVal(); 			// read fitness (from <0,1>)
			System.out.println("Fitness read is this: "+fitness);
			return fitness;
		}

		public void genomeToModel(Float[] genome) throws StructuralException{
			//mySim.getInterLayerNo(0).setVector(genome); 	// set the connection weights
			mySim.decode(genome);
		}
	}
}