package design.ea.strategies.impl;

import java.util.Random;

import design.ea.strategies.AbstractSelection;
//import old.design.ea.vector.Population;
//import old.design.ea.vector.Population;
import design.ea.vector.Population;

/**
 * Implements rouletteWheel selection. Call resetSelection each generation..
 * 
 * TODO: deal with case where user does not allow repetition and wants to select 
 * more individuals than is in the population 
 * 
 * @author Jaroslav Vitku
 *
 */
public class RouletteWheel extends AbstractSelection{

	float sumF;		// sum of all fitness values
	float[] fits; 	// array of unsorted fitness values
	Random r;
	
	public RouletteWheel(){
		r = new Random();
	}
	
	@Override
	public void resetSelection(Population pop){
		super.resetSelection(pop);
		
		fits = pop.getArrayOfFitnessVals();
		sumF = this.getSum(fits);
	}

	@Override
	public int[] select(int howMany) {
		int[] out = new int[howMany];
		float rand;
		int chosen = 0;
		while(chosen < out.length){
			rand = sumF * r.nextFloat(); // pin a finger into the space divided propor.to f 
			out[chosen] = this.findInd(rand, fits);
			// if not repeat selection, check if ind is already selected
			if(! (selected[out[chosen]] && !super.repeatSelection)){
				selected[out[chosen]] = true;
				chosen++;
			}
		}
		return out;
	}
	
	private float getSum(float[] fits){
		float sum = 0;
		for(int i=0; i<pop.size(); i++){
			sum+=fits[i];
		}
		return sum;
	}
	
	/**
	 * Selects from population proportionally to fitness values
	 * @param randNum give me number in range of sum of all fitness values
	 * @param sorted sorted indexes of individuals
	 * @param fits array of fitness values
	 * @return index of selected individual
	 */
	private int findInd(float randNum, float[] fits){
		float actual = 0;
		int poc = 0;
		
		while(actual < randNum){
			actual += fits[poc++];
		}
		if(poc>0)
			poc-=1;
		return poc;
	}


}
