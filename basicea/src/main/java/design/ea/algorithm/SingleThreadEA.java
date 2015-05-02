package design.ea.algorithm;

import design.ea.ind.individual.Individual;

public interface SingleThreadEA extends EvolutionaryAlgorithm{

	/**
	 * Return the current individual.
	 * @return instance of the current individual
	 */
	public Individual getCurrent();
	
	/**
	 * Index of the current individual
	 * @return current individual to be processed
	 */
	public int getCurrentIndex();

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
}
