package design.ea.vector.real.impl;

import tools.utils.LU;
import design.ea.vector.encoding.matrix.MatrixEncoder;
import design.ea.vector.individual.Fitness;
import design.ea.vector.individual.Individual;
import design.ea.vector.individual.impl.InverseError;
import design.ea.vector.real.AbstractWMatrix;

public class Ind implements Individual{

	FloatWMatrix m;
	Fitness f; 
	int INdim, OUTdim, N;
	float max, min;
	
	public Ind(int INdim, int OUTdim, int N, float max, float min){
		this.INdim = INdim;
		this.OUTdim = OUTdim;
		this.N = N;
		this.max = max;
		this.min = min;
		
		this.init();
	}
	
	@Override
	public void init() {
		m = new FloatWMatrix(INdim,OUTdim,N,max,min);
		f = new InverseError();
	}
	
	@Override
	public void randomize() {
		m.randomize();
		f.reset();
	}

	
	@Override
	public void setMatrix(float[][] m) {
		this.m.w = m;
	}

	/*
	
	@Override
	public AbstractWMatrix getMatrix() {
		return this.m;
	}*/

	@Override
	public Fitness getFitness() {
		return f;
	}
	
	public void setFitness(float val){
		f.set(val);
	}
	
	public void printMatrix(){
		System.out.println(" "+LU.toStr(m.w));
	}

	@Override
	public Ind clone(){
		Ind out = new Ind(INdim,OUTdim,N,max,min);
		out.setMatrix(m.clone().w);
		out.setFitness(this.getFitness().get());
		return out;
	}

	@Override
	public float[][] getWeights() {
		return this.m.w;
	}

	@Override
	public MatrixEncoder getMatrixEncoder() {
		return this.m;
	}


}
