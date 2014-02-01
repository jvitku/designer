package design.ea.ind.fitness.simple;

import design.ea.ind.fitness.Fitness;

/**
 * Fitness with single objective. 
 * 
 * @author Jaroslav Vitku
 *
 * @param <E> Single value of the fitness
 */
public interface SingleObjectiveFitness<E> extends Fitness{
	
	public E getFitness();
	
	public void setFitness(E value);

}
