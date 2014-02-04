package design.ea.strategies;

import design.ea.ind.individual.Individual;

/**
 * Take two individuals and apply crossover on them, indicate whether the 
 * {@link design.ea.ind.fitness.Fitness} value is still valid (or genomes changed).
 * 
 * @author Jaroslav Vitku
 *
 */
public interface Crossover {
	
	/**
	 * Apply arbitrary crossover onto two Individuals' genomes
	 * @param a first individual 
	 * @param b second individual
	 */
	public void cross(Individual a, Individual b);

	/**
	 * Set the probability of applying crossover
	 * @param p in range of [0,1]
	 */
	public void setPCross(double p);
}
