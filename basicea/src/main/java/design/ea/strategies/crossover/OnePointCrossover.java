package design.ea.strategies.crossover;

import design.ea.ind.genome.vector.VectorGenome;
import design.ea.ind.individual.Individual;
/**
 * Crosses two given genomes in randomly chosen point with a given probability.
 * 
 * @author Jaroslav Vitku
 *
 */
public class OnePointCrossover<E> extends AbstractCrossover {

	@SuppressWarnings("unchecked")
	@Override
	public void cross(Individual a, Individual b) {

		if(!(a.getGenome() instanceof VectorGenome) ||
				!(b.getGenome() instanceof VectorGenome)){
			System.err.println("OnePointCrossover: ERROR: (at least one) genome not "
					+ "an instance of VectorGenome!");
			return;
		}

		if(r.nextDouble()>pCross)
			return;

		VectorGenome<E> aa = (VectorGenome<E>) a.getGenome();
		VectorGenome<E> bb = (VectorGenome<E>) b.getGenome();

		if(aa.size()!= bb.size()){
			System.err.println("OnePointCrossover: ERROR: vectors have different lengths");
			return;
		}

		// genomes will change, discard values here
		a.getFitness().setValid(false);
		b.getFitness().setValid(false);

		// randomly select a crossover point 
		int point = r.nextInt(aa.size()-1)+1;
		VectorGenome<E> tmp = (VectorGenome<E>) aa.clone();

		for(int i=0; i<point; i++){
			// switch values in the first part of Genomes
			tmp.getVector()[i] = aa.getVector()[i];	
			aa.getVector()[i] = bb.getVector()[i];
			bb.getVector()[i] = tmp.getVector()[i];
		}
	}


	/*
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
	}*/

}
