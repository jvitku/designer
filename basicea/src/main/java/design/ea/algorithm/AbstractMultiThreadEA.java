package design.ea.algorithm;

import java.util.HashMap;

import design.ea.ind.individual.Individual;

/**
 * Multi-threaded version of the AbstractSingleThreadEA
 * 
 * @author Jaroslav Vitku
 *
 */
public class AbstractMultiThreadEA extends AbstractEA implements MultiThreadEA{

	public static int DEF_NO_THREADS = 1;
	private int noThreads;

	// indicates the first unevaluated individual which is not already taken by some thread  
	private int currentInd;

	// number of threads that are waiting (if all are waiting, all individuals are evaluated)
	//private int noWaitingThreads;
	private HashMap<Long, Long> waitingThreadIds;	// unique IDs of threads that are waiging

	public AbstractMultiThreadEA(int vectorLength, int generations, int popSize) {
		super(vectorLength, generations, popSize);
		this.init();
	}

	public AbstractMultiThreadEA(int vectorLength, boolean minimize, int generations, int popSize) {
		super(vectorLength, minimize, generations, popSize);
		this.init();
	}

	private void init(){
		this.noThreads = DEF_NO_THREADS;
		//this.noWaitingThreads = 0;
		this.currentInd = 0;			// first one to be evaluated
		this.waitingThreadIds = new HashMap<Long, Long>();
	}

	@Override
	public synchronized Individual popIndividual(long threadId) {
		int ind = findNext();

		if(ind == -1){
			// all threads waiting, can make one evolutionary step
			if(this.allWaiting(threadId)){

				this.waitingThreadIds.clear();
				this.currentInd = 0;

				// some error, not all evaluated, start again
				if(!this.allEvaluated()){
					System.err.println("some unevaluated missed! starting again!");
					return pop.get(this.findNext());
				}

				// evolution ended?
				if(gen >= maxGen){
					wantsEval = false;
					System.out.println(me+"evolution ended.");
					return null;
				}

				// new generation should be here
				System.out.println(me+"Generation ended here, apply operators now!");
				gen++;
				this.applyEAOperators();
				return pop.get(this.findNext());	// start again (possibly from the first one)

			}else{
				// some threads are still working, wait..
				return null;
			}
		}
		return pop.get(ind);
	}

	private boolean allEvaluated(){
		for(int i=0; i<pop.size; i++){
			if(!pop.get(i).getFitness().isValid()){
				System.err.println("Individual no: "+i+" not evaluated!");
				return false;
			}
		}
		return true;
	}


	private boolean allWaiting(Long threadId){
		if(this.waitingThreadIds.containsKey(threadId)){
			return false;
		}
		this.waitingThreadIds.put(threadId, threadId);
		if(this.waitingThreadIds.size() == this.noThreads){
			return true;
		}
		return false;
	}


	private int findNext(){
		// end of pop reached
		if(currentInd == pop.size){
			return -1;
		}
		// search for the first one
		for(int i=currentInd; i<pop.size; i++){
			if(!pop.get(i).getFitness().isValid()){
				this.currentInd = i+1;
				System.out.println("returning this: "+i);
				return i;
			}
		}
		// all evaluated
		return -1;
	}

	@Override
	public int getBestIndex() {
		int bestInd = 0;

		if(!pop.get(bestInd).getFitness().isValid()){
			System.out.println("First individual not evaluated, probably non of the population");
			return -1;
		}
		for(int i=1; i<this.popSize; i++){
			if(pop.get(i).getFitness().isValid()
					&& pop.get(i).getFitness().betterThan(pop.get(bestInd).getFitness())){
				bestInd = i;
			}
		}
		return bestInd;
	}

	@Override
	public void setNoThreads(int no) {
		if(no<0){
			System.err.println("Will not use negative no of threads, "
					+ "leaving the current one: "+this.noThreads);
			return;
		}
		this.noThreads = no;
	}

	@Override
	public int getNoThreads() { return this.noThreads; }

}
