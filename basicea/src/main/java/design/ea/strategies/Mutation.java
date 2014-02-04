package design.ea.strategies;

import design.ea.ind.individual.Individual;

/**
 * Mutation of {@link design.ea.ind.genome.Genome}.
 * 
 * @author Jaroslav Vitku
 *
 */
public interface Mutation {

	/**
	 * Probability of mutating each gene
	 * @param p value in [0,1] defining the probability of mutation for each gene
	 */
	public void setPMut(double p);		//set probability of changing one gene

	public double getPMut();

	/**
	 * Mutate all Individuals in the given array.
	 * @param individuals {@link design.ea.ind.individual.Individual} whose 
	 * {@link design.ea.ind.genome.Genome}s will be mutated. Each gene is
	 * mutated with the probability set by the {@link #setPMut(double)}
	 */
	public void mutate(Individual[] genomes);

	/*
	public void setStdev(double stdev);	//set standard deviation
	public Float[] mutate(Float[] genome, boolean isBinary);

	public TwoGenomes mutate(TwoGenomes genomes, boolean areBinary);
	 */
}
