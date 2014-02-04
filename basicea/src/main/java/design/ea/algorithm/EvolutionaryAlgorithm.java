package design.ea.algorithm;

import design.ea.ind.individual.Individual;

/**
 * Usage:
 * <ul>
 * <li>Initialize by the constructor</li> 
 * <li>{@link #setProbabilities(double, double)}</li>
 * <li>sequentially call the {@link #nextIndividual()}</li>
 * <li>obtain genome by {@link #getCurrentInd()}</li>
 * <li>evaluate</li>
 * <li>set the fitness by means of the {@link #getCurrentInd()}</li>
 * </ul>
 * Finally, obtain the best individual by calling the {@link #getBestInd()}.
 * 
 * Note: Generations are increased automatically, evolutionary operators
 * are applied between generations.
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
	 * @return the current individual
	 */
	public Individual getCurrentInd();

	/**
	 * Get the best individual
	 * @return individual with the highest fitness in the population
	 */
	public Individual getBestInd();
	
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
	 * Return the current generation 
	 * @return number of the current generation
	 */
	public int generation();

	/**
	 * Index of the current individual
	 * @return current individual to be processed
	 */
	public int currentOne();

	/**
	 * Index of the best individual in the population
	 * @return index of individual with the best fitness in the pop. 
	 */
	public int bestOne();

	/**
	 * Get the best individual
	 * @return individual with the best fitness in the current generation
	 */
	public int getBest();

	/**
	 * Get the individual with the given fitness
	 * @param no index in the current population
	 * @return individual with the given index
	 */
	public Individual getIndNo(int no);

}
