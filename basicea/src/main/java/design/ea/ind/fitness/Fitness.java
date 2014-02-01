package design.ea.ind.fitness;

import tools.utils.Resettable;

public interface Fitness extends Resettable{
	
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

}
