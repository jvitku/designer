package design.ea.ind.genome.vector;

import design.ea.ind.genome.Genome;

/**
 * Genome represented by the vector of some variables of type E. 
 *  
 * @author Jaroslav Vitku
 *
 * @param <E> gene
 */
public interface VectorGenome<E> extends Genome{
	
	public int size();
	
	public void setVector(E[] values);
	
	public E[] getVector();
	
}
