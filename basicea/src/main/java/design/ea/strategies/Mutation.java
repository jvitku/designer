package design.ea.strategies;


public interface Mutation {

	public void setPMut(double p);		//set probability of changing one gene
	public void setStdev(double stdev);	//set standard deviation
	public Float[] mutate(Float[] genome, boolean isBinary);
	public TwoGenomes mutate(TwoGenomes genomes, boolean areBinary);
}
