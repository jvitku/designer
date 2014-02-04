package design.ea.strategies.mutation;

import design.ea.algorithm.Population;
import design.ea.ind.individual.Individual;
import design.ea.strategies.Mutation;

public class UniformMutationTest {

	public static Population mutatePopulation(Population p, Mutation m ){
		Population out = p.clone();
		// mutate inds directlly in the cloned population
		for(int i=0; i<p.size(); i++){
			m.mutate(new Individual[]{out.get(i)});
		}
		return out;
	}
	/*
		if(p.get(0).getGenome() instanceof BinaryVector){
			return binaryMutatePopulation(p,m);
		}else if(p.get(0).getGenome() instanceof RealVector){

			return null; //TODO
		}else{
			System.err.println("ERROR: unsupported type of genome");
			return null;
		}*/

	/*
	private static Population binaryMutatePopulation(Population p, Mutation m){
		Population out = p.clone();

		/*
		if(!(m instanceof BinaryUniformMutation)){
			System.err.println("ERR: unsupported mutation type!");
			return null;
		}
		BinaryUniformMutation mut = (BinaryUniformMutation)m;


	}*/
}
