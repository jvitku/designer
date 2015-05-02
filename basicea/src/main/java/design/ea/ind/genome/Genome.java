package design.ea.ind.genome;

import java.io.Serializable;

import tools.utils.Resettable;

/**
 * Genome used by the AbstractSingleThreadEA. 
 * 
 * @author Jaroslav Vitku
 *
 */
public interface Genome extends Resettable, Cloneable, Serializable{

	public String toString();

	public Genome clone();

	public boolean equalsTo(Genome target);
}
