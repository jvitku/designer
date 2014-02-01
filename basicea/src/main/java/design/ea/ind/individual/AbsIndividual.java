package design.ea.ind.individual;

import design.ea.encoding.Encoding;
import design.ea.ind.fitness.Fitness;
import design.ea.ind.genome.Genome;

public class AbsIndividual implements Individual{

	protected Genome genome;
	protected Fitness fitness;
	protected Encoding e;
	
	@Override
	public Genome getGenome() { return this.genome; }

	@Override
	public void setGenome(Genome g) { this.genome = g; }

	@Override
	public Fitness getFitness() { return this.fitness; }

	@Override
	public void setFitness(Fitness f) { this.fitness = f; }

	@Override
	public void setEncoding(Encoding e) { this.e = e; }

	@Override
	public Encoding getEncoding() { return this.e; }

}
