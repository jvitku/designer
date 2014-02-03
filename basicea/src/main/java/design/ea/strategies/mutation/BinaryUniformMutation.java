package design.ea.strategies.mutation;

import design.ea.ind.genome.Genome;
import design.ea.ind.genome.vector.impl.BinaryVector;

/**
 * Uniform mutation of the {@link design.ea.ind.genome.vector.impl.BinaryVector} Genome.
 * 
 * @author Jaroslav Vitku
 *
 */
public class BinaryUniformMutation extends AbstractUniformMutation{


	@Override
	public void mutate(Genome[] genomes){
		Boolean[] vec;
		for(int j=0; j<genomes.length; j++){
			if(genomes[j] instanceof BinaryVector){
				System.err.println("ERROR Mutation: genome "+j+
						" not an instance of RealVector, ignoring!");
				continue;
			}
			vec = ((BinaryVector)genomes[j]).getVector();
			// add a Gaussian for each gene with pMut
			for(int i=0; i<vec.length; i++){
				if(r.nextDouble() < super.pMut){
					if(vec[i].booleanValue()){
						vec[i] = false;
					}else{
						vec[i] = true;
					}
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
