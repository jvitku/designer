package design.ea.matrix;

import design.ea.matrix.individual.Individual;

public class Population implements Cloneable{
	
	Individual [] pop;
	int size;
	public Population(int size){
		this.size = size;
		pop = new Individual[size];
	}
	
	public void setInd(int no, Individual ind){
		pop[no] = ind;
	}
	public int size(){
		return size;
	}
	
	public Individual get(int i){
		return pop[i];
	}
	
	public void set(int i, Individual in){
		pop[i] = in;
	}
	
	public float[] getArrayOfFitnessVals(){
		float[] f = new float[pop.length];
		for(int i=0; i<pop.length; i++){
			f[i] = pop[i].getFitness().get();
		}
		return f;
	}

	public Population clone(){
		Population op = new Population(this.size);
		for(int i=0; i<pop.length; i++){
			op.setInd(i, pop[i].clone());
		}
		
		return op;
	}
}
