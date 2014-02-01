package design.ea.ind.genome;

import tools.utils.Resettable;

/**
 * Genome used by the AbstractEA. 
 * 
 * @author Jaroslav Vitku
 *
 */
public interface Genome extends Resettable, Cloneable{
	
	public String toString();
	
	public Genome clone();
	
}
