package design.ea.ind.fitness;

import tools.utils.Resettable;

/**
 * The constructor should contain final information whether we are 
 * searching for the smallest value or for the biggest one.
 * 
 * @author Jaroslav Vitku
 *
 */
public interface Fitness extends Resettable, Cloneable{
	
	/**
	 * Compare this fitness to some other fitness value.
	 * If this fitness is not valid, it is not better. 
	 * If the other fitness is not valid and this is: this one is better.   
	 * @param f other Fitness
	 * @return true if this Fitness is better than a given one
	 */
	public boolean betterThan(Fitness f);
	
	/**
	 * The fitness can be marked as valid or not valid.
	 * If the fitness value is set, the {@link #setValid(boolean)} is called
	 * with the parameter true.
	 * 
	 * @param valid true if the fitness is valid now
	 */
	public void setValid(boolean valid);
	
	/**
	 * Indication whether the current fitness value is valid.
	 * @return true if the fitness value is valid
	 */
	public boolean isValid();
	
	public Fitness clone();
	
	/**
	 * Human-readable fitness value.
	 * @return string representing the fitness value (and state)
	 */
	public String toString();
}
