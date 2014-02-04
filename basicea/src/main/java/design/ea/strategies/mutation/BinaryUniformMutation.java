package design.ea.strategies.mutation;

import design.ea.ind.genome.vector.impl.BinaryVector;
import design.ea.ind.individual.Individual;

/**
 * Uniform mutation of the {@link design.ea.ind.genome.vector.impl.BinaryVector} Genome.
 * 
 * @author Jaroslav Vitku
 *
 */
public class BinaryUniformMutation extends AbstractUniformMutation{

	@Override
	public void mutate(Individual[] individuals){
		Boolean[] vec;
		for(int j=0; j<individuals.length; j++){
			if(!(individuals[j].getGenome() instanceof BinaryVector)){
				System.err.println("ERROR Mutation: genome of the individual "+j+
						" not an instance of BinaryVector, ignoring!");
				continue;
			}
			vec = ((BinaryVector)individuals[j].getGenome()).getVector();
			// add a Gaussian for each gene with pMut
			for(int i=0; i<vec.length; i++){
				if(r.nextDouble() < super.pMut){
					if(vec[i].booleanValue()){
						vec[i] = false;
					}else{
						vec[i] = true;
					}
					// indicate that this individual needs to be re-evaluated
					individuals[i].getFitness().setValid(false);
				}
			}
		}
	}

	/*
	public Float[] mutate(Float[] a, boolean isBinary) {
		Float[] out = new Float[a.length];
		Float[] source = a;
		for(int i=0; i<source.length; i++){
			if(r.nextDouble()<pMut){
				if(isBinary){
					Math.abs(source[i]-1);	// flip the bit
				}else{
					out[i] = (float) (source[i] + r.nextGaussian()*stdev);
					//DU.pl("doing from this: "+source[i]+" this: "+out[i]);
				}
			}else{
				out[i] = source[i];
			}
		}
		return out;
	}
	 */

}
