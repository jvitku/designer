package design.ea.ind.genome.vector;

import java.util.Random;

import design.ea.ind.genome.Genome;

/**
 * 
 * @author Jaroslav Vitku
 *
 * @param <E>
 */
public abstract class AbsVectorGenome<E> implements VectorGenome<E>{
	
	private static final long serialVersionUID = 3515095939681674018L;
	protected Random r = new Random();
	protected final int size;

	public AbsVectorGenome(int size){

		if(size<1){
			System.err.println("Error: could not initialize vectror shorter than 1!");
			size = 1;
		}
		this.size = size;
	}

	@Override
	public int size() { return this.size; }
	
	@Override
	public abstract Genome clone();

}
