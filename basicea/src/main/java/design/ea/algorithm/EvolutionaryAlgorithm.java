package design.ea.algorithm;

import design.ea.ind.individual.Individual;

/**
 * Usage:
 * <ul>
 * <li>Initialize by the constructor</li> 
 * <li>{@link #setProbabilities(double, double)}</li>
 * <li>sequentially call the {@link #nextIndividual()}</li>
 * <li>obtain the current individual by {@link #getCurrent()}</li>
 * <li>evaluate</li>
 * <li>set the fitness by means of the {@link #getCurrent()}</li>
 * </ul>
 * Finally, obtain the best individual by calling the {@link #getBest()}.
 * 
 * Note: Generations are increased automatically, evolutionary operators
 * are applied between generations. To obtain the best individual in the 
 * current population, check the {@link #isTheLastOne()}, if true
 * is returned, all individuals have been evaluated in the population 
 * (in case that the current one has been evaluated too). 
 *  
 * @author Jaroslav Vitku
 *
 */
public interface EvolutionaryAlgorithm {

	/**
	 * Configure probabilities of the EA.
	 * @param pMut probability of mutation of each gene in genome
	 * @param pCross probability of crossing selected genomes
	 */
	public void setProbabilities(double pMut, double pCross);

	/**
	 * Return the current individual.
	 * @return instance of the current individual
	 */
	public Individual getCurrent();

	/**
	 * Best individual is stored each step (each call of {@link #nextIndividual()}),
	 * this method returns the best individual in the current population.
	 * If the current one is evaluated, also the current one is considered. 
	 * It is advised to call this when the {@link #isTheLastOne()} returns true,
	 * that is after evaluating all individuals in the population.
	 *  
	 * @return individual with the highest fitness in the population
	 */
	public Individual getBest();

	/**
	 * Get the best individual in the current population. 
	 * @see #getBest()
	 * @return individual with the best fitness in the current generation
	 */
	public int getBestIndex();
	
	/**
	 * Index of the current individual
	 * @return current individual to be processed
	 */
	public int getCurrentIndex();

	/**
	 * Indicates whether the evolution should continue.
	 * @return false if the current generation is the last one 
	 */
	public boolean wantsEval();

	/**
	 * Iterate to the next individual in the population. This
	 * typically serves for evaluating the fitness. 
	 * When the iterator points to the last individual in the 
	 * population and the {@link #nextIndividual()} is called, 
	 * one evolutionary step is taken. Operators are applied until 
	 * new population is filled, populations are swapped and 
	 * the iterator is pointed back to the first individual.
	 */
	public void nextIndividual();
	
	/**
	 * Return true if the current individual is the last one
	 * in the current generation. If this method returns true, 
	 * the following should be true:
	 * <ul>
	 * <li>all individuals in the current population(generation)
	 * are evaluated</li>
	 * <li>calling the method {@link #nextIndividual()} will trigger
	 * applying the EA operators on entire population (next generation)</li>
	 * <li>the method {@link #getBest()} returns the best individual 
	 * from the current generation.</li>
	 * </ul>
	 * 
	 * @return true if this is the last individual in the current population
	 */
	public boolean isTheLastOne();

	/**
	 * Return the current generation 
	 * @return number of the current generation
	 */
	public int generation();
	
	/**
	 * Get number of generations this evolution is supposed to run.
	 * @return max number of generations
	 */
	public int getMaxGenerations();

	/**
	 * Get the individual with the given fitness
	 * @param no index in the current population
	 * @return individual with the given index
	 */
	public Individual getIndNo(int no);

	
}
