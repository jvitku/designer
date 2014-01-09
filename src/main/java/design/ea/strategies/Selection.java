package design.ea.strategies;

import design.ea.matrix.Population;

public interface Selection {
	
	/**
	 * Sort the population according to the fitness values
	 * return array of indexes pointing in order from bests ind to the worst
	 * 
	 * should be called from resetSelection()
	 * 
	 * @param pop Population of individuals
	 * @return array of indexes with sorted inds
	 */
	public int[] sort(); 
	
	public int[] select(int howMany);
	
	/**
	 * call this each generation, before selecting individuals
	 */
	public void resetSelection(Population pop);
	
	public void selectedCanRepeat(boolean can);
}
