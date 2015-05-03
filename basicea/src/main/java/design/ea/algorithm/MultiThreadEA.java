package design.ea.algorithm;

import design.ea.ind.individual.Individual;

/**
 * This EA supports asynchronous concurrent evaluation of individuals.
 * 
 * The single-thread version remains for backward compatibility.
 *  
 * @author Jaroslav Vitku
 *
 */
public interface MultiThreadEA extends EvolutionaryAlgorithm{

	/**
	 * Each thread can asynchronously pop own individual to be evaluated. 
	 * 
	 * After evaluation, thread sets the fitness and asks for new individual. 
	 * 
	 *  If there is no more individuals, method returns null. 
	 *  
	 *  If the wantsEval() returns false, threads are free to exit (end of evolution). 
	 *  Otherwise, they should wait for new individuals (next generation). 
	 * 
	 * If all threads are waiting for new individuals, the population is evaluated and new generation is triggered.
	 * 
	 * @param threadId each thread has to sign with unique ID (if all wait, new volution step is made)
	 * @return individual to be evaluated (evaluated ones are skipped) or null, if the population is evaluated.
	 */
	public Individual popIndividual(long threadId);

	/**
	 * The EA needs to know the total no. of threads that participate.
	 * 
	 * @param no
	 */
	public void setNoThreads(int no);

	/**
	 * @return current number of threads
	 */
	public int getNoThreads();
}
