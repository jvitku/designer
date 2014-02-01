package design.ea.ind.individual;

import tools.utils.Resettable;
import design.ea.encoding.Encoding;
import design.ea.ind.fitness.Fitness;
import design.ea.ind.genome.Genome;

/**
 * An individual holds own genome, corresponding fitness
 * and a way how to convert genotype to phenotype.
 * 
 * @author Jaroslav Vitku
 *
 */
public interface Individual extends Resettable, Cloneable{

	public Genome getGenome();
	public void setGenome(Genome g);
	
	public Fitness getFitness();
	public void setFitness(Fitness f);
	
	public void setEncoding(Encoding e);
	public Encoding getEncoding();
	
	public Individual clone();
}
