package design.ea.strategies.impl;

import design.ea.strategies.AbstractCrossover;
import design.ea.strategies.TwoGenomes;

public class OnePointCrossover extends AbstractCrossover {

	public TwoGenomes cross(float[] a, float[] b) {
		if(a.length != b.length){
			System.err.println("OnePointCrossover: ERROR: vectors have different lengths");
		}
		float[] outA = a.clone();
		float[] outB = b.clone();
		
		// should apply crossover at all?
		if(r.nextDouble()<pCross){
			// randomly select crossover point 
			int point = r.nextInt(a.length);
			for(int i=0; i<point; i++){
				outA[i] = b[i];	// flip values
				outB[i] = a[i];
			}
		}
		return new TwoGenomes(outA,outB);
	}
}
