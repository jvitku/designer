package design.ea.strategies;

import java.util.Random;

public abstract class AbstractCrossover implements Crossover{

	protected double pCross = 0.5;
	protected Random r;
	
	public AbstractCrossover(){
		r = new Random();
	}
	
	@Override
	public void setPCross(double p) {
		pCross = p;
	}
}
