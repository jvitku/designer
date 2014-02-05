package design.ea.ind.individual;

import tools.utils.Resettable;
import design.ea.encoding.Encoding;
import design.ea.ind.fitness.Fitness;
import design.ea.ind.genome.Genome;
import java.io.*;

/**
 * An individual holds own genome, corresponding fitness
 * and a way how to convert genotype to phenotype.
 * 
 * @author Jaroslav Vitku
 */
public interface Individual extends Resettable, Cloneable, Serializable{

	public Genome getGenome();
	public void setGenome(Genome g);
	
	public Fitness getFitness();
	public void setFitness(Fitness f);
	
	public void setEncoding(Encoding e);
	public Encoding getEncoding();
	
	public Individual clone();
	
	/**
	 * Convert to human-readable String 
	 * @return human-readable String representation of this Individual
	 */
	public String toString();
	
	/**
	 * Return true if all fields of these two individuals have equal values.
	 * @param target individual to be compared with
	 * @return true if values of all fields are equal
	 */
	public boolean equalsTo(Individual target);
}
