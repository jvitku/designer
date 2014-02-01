package design.ea.strategies;

import design.ea.algorithm.Population;

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
	public int[] sort(); 
	
	/**
	 * Select array of individuals by this selection method
	 * @param howMany how many individuals to select
	 * @return array of indexes in a given population
	 */
	public int[] select(int howMany);
	
	/**
	 * Call this each generation, before selecting individuals
	 */
	public void resetSelection(Population pop);
	
	public void selectedCanRepeat(boolean can);
}
