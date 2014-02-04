package design.ea.strategies.selection;

import design.ea.algorithm.AbsSingleObjPopulation;
import design.ea.algorithm.Population;
import design.ea.ind.individual.Individual;
import design.ea.strategies.Selection;


public abstract class AbstractSelection implements Selection{

	protected int[] sorted;
	protected boolean[] selected;
	protected Population pop;
	protected boolean repeatSelection = true;	// can be one ind selected more times?

	public void selectedCanRepeat(boolean can){
		this.repeatSelection = can;
	}

	public void resetSelection(Population pop){
		this.pop = pop;
		sorted = this.sortIndexes();
		selected = this.selectedNone();
	}
	/**
	 * we have got list of already selected individuals
	 * @return
	 */
	public boolean[] selectedNone(){
		selected = new boolean[pop.size()];
		for(int i=0; i<pop.size(); i++)
			selected[i] = false;
		return selected;
	}

	/**
	 * sort them according to their fitness values
	 */
	public int[] sortIndexes(){
		int[] sorted = new int[pop.size()];
		double[] fits = ((AbsSingleObjPopulation)pop).getArrayOfFitnessVals();

		boolean swapped = true;

		for( int i=0; i<pop.size(); i++){
			sorted[i]=i;
		}
		int tmp;
		while(swapped){
			swapped = false;
			for( int i=1; i<pop.size(); i++){
				double a = fits[sorted[i-1]];
				double b = fits[sorted[i]];

				if(a < b){

					swapped = true;
					tmp = sorted[i-1];
					sorted[i-1] = sorted[i];
					sorted[i] = tmp;
				}
			}
		}
		return sorted;
	}

	protected float[] getSortedVals(float[] fits, int[] sorted){
		float[] srt = new float[fits.length];
		for(int i=0; i<fits.length; i++){
			srt[i] = fits[sorted[i]];
		}
		return srt;
	}

	@Override
	public Individual[] select(int howMany) {
		int[] selected = this.selectIndexes(howMany);
		Individual[] out = new Individual[howMany];
		for(int i=0; i<howMany; i++){
			out[i] = pop.get(selected[i]).clone();
		}
		return out;
	}

	/**
	 * Select indexes of individuals in the population. The method 
	 * {@link #select(int)} is used to clone selected individuals 
	 * and return in one array.
	 * @param howMany how many individuals to select
	 * @return array of individuals' indexes in the population
	 */
	protected abstract int[] selectIndexes(int howMany);
}

