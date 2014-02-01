package design.ea.ind.genome.vector;

import java.util.Random;

/**
 * 
 * @author Jaroslav Vitku
 *
 * @param <E>
 */
public abstract class AbsVectorGenome<E> implements VectorGenome<E>{
	
	protected Random r = new Random();
	protected E minVal, maxVal;
	protected final int size;

	public AbsVectorGenome(int size, E minVal, E maxVal){
		this.minVal = minVal;
		this.maxVal = maxVal;

		if(size<1){
			System.err.println("Error: could not initialize vectror shorter than 1!");
			size = 1;
		}
		this.size = size;
	}

	@Override
	public int size() { return this.size; }

	@Override
	public void setMaxVal(E maxVal) { this.maxVal = maxVal; }

	@Override
	public void setMinVal(E minVal) { this.minVal = minVal; }

	@Override
	public E getMaxVal() { return this.maxVal; }

	@Override
	public E getMinVal() { return this.minVal; }

}
