package design.ea.strategies;

import design.ea.algorithm.Population;
import design.ea.ind.individual.Individual;

/**
 * <p>Selection strategy for the EA.</p> 
 * 
 * <p>Note that all EA strategies directly modify Individuals passed to them. 
 * The only actual copying of individuals should is done in the beginning
 * by the selection strategy in the method {@link #select(int)}.</p>    
 * 
 * @author Jaroslav Vitku
 *
 */
public interface Selection {

	/**
	 * Sort the population according to the fitness values
	 * return array of indexes pointing in order from bests ind to the worst
	 * 
	 * should be called from resetSelection()
	 * 
	 * @param pop AbsSingleObjPopulation of individuals
	 * @return array of indexes with sorted inds
	 */
	public int[] sortIndexes(); 

	/**
	 * Select given number of individuals from the population, clone them
	 * and return them in the newly created array. Note: cloning does not
	 * occur anywhere else, so the only copying could be done in this method.
	 * 
	 * @param howMany how many individuals to select
	 * @return array of Individuals cloned from the source population 
	 */
	public Individual[] select(int howMany);

	/**
	 * Call this each generation, before selecting individuals. This also 
	 * deletes the list of selected individuals.
	 * @see #selectedCanRepeat(boolean)
	 */
	public void resetSelection(Population pop);

	/**
	 * Should be selected individuals excluded from further selection?
	 * @param can if the selected individuals can repeat in further selections
	 * in the current generation 
	 */
	public void selectedCanRepeat(boolean can);
}
