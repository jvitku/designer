package design.ea.strategies.mutation;

import java.util.Random;
import design.ea.ind.genome.Genome;
import design.ea.strategies.Mutation;

public abstract class AbstractUniformMutation implements Mutation{

	public static final double DEF_PMUT = 0.05;

	protected Random r = new Random();
	protected double pMut;

	public AbstractUniformMutation(){
		this.pMut = DEF_PMUT;
	}

	@Override
	public void setPMut(double p) {
		if(p<0){
			System.err.println("ERROR: will not set pMut<0");
			return;
		}else if(p>1){
			System.err.println("ERROR: will not set pMut>1");
			return;
		}
		this.pMut = p;
	}

	@Override
	public double getPMut() { return this.pMut; }

	@Override
	public abstract void mutate(Genome[] genomes);

}
