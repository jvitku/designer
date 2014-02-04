package design.ea.strategies.crossover;

import java.util.Random;

import design.ea.strategies.Crossover;

public abstract class AbstractCrossover implements Crossover{

	protected double pCross = 0.5;
	protected Random r;

	public AbstractCrossover(){
		r = new Random();
	}

	@Override
	public void setPCross(double p) {
		if(p<0){
			System.err.println("ERROR: will not set negative probability of mutation");
			p=0;
		}else if(p>1){
			System.err.println("ERROR: will not set probability of mutation > 1");
			p=1;
		}
		pCross = p;
	}
}
