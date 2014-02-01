package design.ea.algorithm;

import tools.utils.Resettable;
import design.ea.ind.individual.Individual;

/**
 * Population holds predefined number of Individuals. 
 * Resetting the population typically randomizes their genomes 
 * and discards fitness values.
 * 
 * @author Jaroslav Vitku
 *
 */
public interface Population extends Cloneable, Resettable{
	
	/**
	 * Place new individual into the population 
	 * @param i position in the population to be placed
	 * @param ind individual to be placed there
	 */
	public void set(int i, Individual ind);
	
	/**
	 * Get individual from a given position in the population
	 * @param i index in the population
	 * @return individual
	 */
	public Individual get(int i);
	
	public Population clone();
	
}
