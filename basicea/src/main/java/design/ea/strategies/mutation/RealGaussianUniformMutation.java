package design.ea.strategies.mutation;

import design.ea.ind.genome.vector.impl.RealVector;
import design.ea.ind.individual.Individual;

/**
 * Mutate {@link design.ea.ind.genome.vector.impl.Realvector} Genomes.
 * 
 * @author Jaroslav Vitku
 *
 */
public class RealGaussianUniformMutation extends AbstractUniformMutation{

	// standard deviation for float vectors
	public static final double DEF_STDEV = 1.0;
	protected double stdev;

	private Float []vec;
	private RealVector genome;
	
	public RealGaussianUniformMutation(){
		this.stdev = DEF_STDEV;
	}

	public void setStdev(double stdev) {
		if(stdev<0){
			System.err.println("ERROR: will not set stdev<0");
			return;
		}	
		this.stdev = stdev;
	}

	public double getStdev(){ return this.stdev; }

	@Override
	public void mutate(Individual[] individuals){
		
		for(int j=0; j<individuals.length; j++){
			if(!(individuals[j].getGenome() instanceof RealVector)){
				System.err.println("ERROR Mutation: genome of the individual no "+j+
						" not an instance of RealVector, ignoring!");
				continue;
			}
			genome = (RealVector)individuals[j].getGenome();
			vec = genome.getVector();
			// add a Gaussian for each gene with pMut
			for(int i=0; i<vec.length; i++){
				if(r.nextDouble() < super.pMut){
					//vec[i] = (float) (vec[i] + r.nextGaussian()*stdev); // no boundary checking
					vec[i] = this.applyBoundaries(vec[i] + r.nextGaussian()*stdev, genome);
					individuals[j].getFitness().setValid(false);
				}
			}
		}
	}
	
	/**
	 * get new value of a gene, if value is out of boundaries, place it there.
	 * @param newValue new value to the gene
	 * @return value which is in boundaries 
	 */
	private float applyBoundaries(double newValue, RealVector genome){
		if(newValue>genome.getMaxVal())
			return genome.getMaxVal();
		
		if(newValue<genome.getMinVal())
			return genome.getMinVal();
		
		return (float)newValue;
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
	}*/
}
