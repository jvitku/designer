package design.ea.ind.individual;

import design.ea.encoding.Encoding;
import design.ea.ind.fitness.Fitness;
import design.ea.ind.genome.Genome;

public interface Individual {

	public Genome getGenome();
	public void setGenome();
	
	public Fitness getFitness();
	public void setFitness(Fitness f);
	
	public void setEncoding(Encoding e);
	public Encoding getEncoding();
}
