package design.ea.ind.genome.vector;

import design.ea.ind.genome.Genome;

public interface VectorGenome<E> extends Genome{
	
	public int size();
	
	public void setVector(E[] values);
	
	public E[] getVector();
	
}
