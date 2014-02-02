package design.ea.strategies;

public interface Crossover {
	
	// apply arbitrary crossover onto two genomes and return new ones 
	public TwoGenomes cross(Float[] a, Float[] b);

	public void setPCross(double p);
}
