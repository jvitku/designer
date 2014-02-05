package design.ea.ind.individual;

import design.ea.encoding.Encoding;
import design.ea.ind.fitness.Fitness;
import design.ea.ind.genome.Genome;

public abstract class AbsIndividual implements Individual{

	private static final long serialVersionUID = 3515010047199787932L;
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

	@Override
	public void reset(boolean randomize) {
		genome.reset(randomize);
		fitness.reset(randomize);
	}

	@Override
	public abstract Individual clone();

	@Override
	public boolean equalsTo(Individual target){
		if(!genome.equalsTo(target.getGenome()))
			return false;
		if(fitness.equals(target.getFitness()))
			return false;
		return true;
	}
}
