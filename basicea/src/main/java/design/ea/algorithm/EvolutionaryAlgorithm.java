package design.ea.algorithm;

import design.ea.ind.individual.Individual;

public interface EvolutionaryAlgorithm {

	/**
	 * Configure probabilities of the EA.
	 * @param pMut probability of mutation of each gene in genome
	 * @param pCross probability of crossing selected genomes
	 */
	public void setProbabilities(double pMut, double pCross);

	/**
	 * Indicates whether the evolution should continue.
	 * @return false if the current generation is the last one 
	 */
	public boolean wantsEval();

	/**
	 * Iterate to the next individual in the population
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

	/**
	 * Get the best individual
	 * @return individual with the highest fitness in the population
	 */
	public Individual getBestInd();

}
