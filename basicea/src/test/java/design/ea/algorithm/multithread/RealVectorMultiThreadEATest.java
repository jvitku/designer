package design.ea.algorithm.multithread;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import design.ea.algorithm.impl.RealVectorMultiThreadEA;
import design.ea.ind.fitness.simple.impl.RealValFitness;
import design.ea.ind.individual.Individual;
import design.ea.tasks.Rosenbrock;
import design.ea.ind.genome.vector.impl.RealVector;


public class RealVectorMultiThreadEATest {

	/**
	 * New multi-thread version of EA tested with a single thread
	 */
	//@Ignore
	@Test
	public void singleThread(){
		// EA setup
		int len = 2;
		int popSize = 50;
		int gens = 3000;

		float minw = -2, maxw = 2;	

		RealVectorMultiThreadEA ea = new RealVectorMultiThreadEA(len, false, gens, popSize, minw, maxw);

		ea.setNoThreads(1);

		ea.setProbabilities(0.05, 0.8);
		assertTrue(ea.wantsEval());
		assertTrue(ea.generation()==0);

		long ID = new Random().nextLong();	// unique ID

		while(ea.wantsEval()){

			Individual ind = ea.popIndividual(ID);

			if(ind == null){
				if(!ea.wantsEval()){
					System.out.println("Evolution ends now! exiting!");
					return;
				}
				waitForOthers();	// wait
				continue;			// try again the same
			}

			Float[] val = ((RealVector)ind.getGenome()).getVector();
			double f = Rosenbrock.eval(val[0], val[1]);
			((RealValFitness)ind.getFitness()).setValue(f);
		}

		// Should be something about 2-3000
		Double fitness = ((RealValFitness)ea.getBest().getFitness()).getValue();
		assertTrue(fitness>1000);

		System.out.println("==== The result is: "+ea.getBest().toString());
	}


	protected void waitForOthers(){
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	//@Ignore
	@Test
	public void multiThreadedEA(){

		// EA setup
		int len = 2;
		int popSize = 50;
		int gens = 3000;
		float minw = -2, maxw = 2;	

		RealVectorMultiThreadEA ea = new RealVectorMultiThreadEA(len, false, gens, popSize, minw, maxw);

		ea.setProbabilities(0.05, 0.8);
		assertTrue(ea.wantsEval());
		assertTrue(ea.generation()==0);

		// setup no of evaluator threads and run them all
		int noThreads = 12;
		ea.setNoThreads(noThreads);

		RosenbrockEvaluatorThread[] threads = new RosenbrockEvaluatorThread[noThreads];
		for(int i = 0; i < threads.length; i++){
			threads[i] = new RosenbrockEvaluatorThread(ea);
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
		assertTrue(fitness>1000);

		System.out.println("==== The result is: "+ea.getBest().toString());
	}


	public class RosenbrockEvaluatorThread extends Thread
	{
		private RealVectorMultiThreadEA ea;
		private final long myId;
		public static final int waitTimeNanos = 100;

		public RosenbrockEvaluatorThread(RealVectorMultiThreadEA ea)
		{
			this.myId  = new Random().nextLong();
			this.ea = ea;
		}

		public synchronized void run()
		{
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
					this.eval(ind);	
				}
			}
		}

		private void eval(Individual ind){
			Float[] val = ((RealVector)ind.getGenome()).getVector();
			double f = Rosenbrock.eval(val[0], val[1]);
			((RealValFitness)ind.getFitness()).setValue(f);
		}
	}
}
