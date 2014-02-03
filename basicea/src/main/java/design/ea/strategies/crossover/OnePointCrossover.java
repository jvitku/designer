package design.ea.strategies.crossover;

import design.ea.strategies.TwoGenomes;

/**
 * Crosses two given genomes in randomly chosen point with a given probability.
 * 
 * TODO: make this more general (use the Genome interface)
 * TODO: pass here entire individuals, if crossed, discard their fitness values
 * 
 * @author Jaroslav Vitku
 *
 */
public class OnePointCrossover extends AbstractCrossover {

	public TwoGenomes cross(Float[] a, Float[] b) {
		if(a.length != b.length){
			System.err.println("OnePointCrossover: ERROR: vectors have different lengths");
		}
		Float[] outA = a.clone();
		Float[] outB = b.clone();
		
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
