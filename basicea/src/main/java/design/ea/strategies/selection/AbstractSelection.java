package design.ea.strategies.selection;

import design.ea.algorithm.AbsSingleObjPopulation;
import design.ea.algorithm.Population;
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
		sorted = this.sort();
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
	public int[] sort(){
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

	public float[] getSortedVals(float[] fits, int[] sorted){
		float[] srt = new float[fits.length];
		for(int i=0; i<fits.length; i++){
			srt[i] = fits[sorted[i]];
		}
		return srt;
	}

	public abstract int[] select(int howMany);
}

