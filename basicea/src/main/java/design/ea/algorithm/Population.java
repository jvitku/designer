package design.ea.algorithm;

import java.io.Serializable;

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
public interface Population extends Cloneable, Resettable, Serializable{
	
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
	
	/**
	 * Number of individuals in the population.
	 * @return size of the population
	 */
	public int size();
	
	public Population clone();
	
	/**
	 * Compare two populations. Return true if values of all 
	 * fields are equal. This should include all individuals' genomes
	 * and fitness values and configuration of population.
	 *  
	 * @param target population to be compared with
	 * @return true if all fields are equal
	 */
	public boolean equalsTo(Population target);
	
	/**
	 * True if the minimum is searched for.
	 * @return false if the max is searched for.
	 */
	public boolean minimizes();
	
}
