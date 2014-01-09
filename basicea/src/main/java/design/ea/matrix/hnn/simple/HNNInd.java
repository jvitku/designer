package design.ea.matrix.hnn.simple;

import design.ea.matrix.encoding.matrix.MatrixEncoder;
import design.ea.matrix.individual.Fitness;
import design.ea.matrix.individual.Individual;
import design.ea.matrix.individual.impl.DirectFitness;


public class HNNInd implements Individual{

	public HNNWMatrix m;
	Fitness f; 
	int INdim, OUTdim;
	float max, min;
	int numIns, numOuts;

	int len;
	public HNNInd(float max, float min, int len){
		this.INdim = 0;
		this.OUTdim = 0;
		this.numIns = 0; 
		this.numOuts = 0;
		this.max = max;
		this.min = min;
		this.len = len;

		this.init();
	}

	@Override
	public void init() {
		m = new HNNWMatrix(max,min,len);
		f = new DirectFitness();
	}

	@Override
	public void randomize() {
		m.randomize();
		f.reset();
	}



	@Override
	public Fitness getFitness() {
		return f;
	}

	public void setFitness(float val){
		f.set(val);
	}
	/*	
	public void printMatrix(){
		System.out.println(" "+LU.toStr(m.w));
	}
	 */
	@Override
	public HNNInd clone(){
		HNNInd out = new HNNInd(max,min,len);
		out.m = this.m.clone();
		out.setFitness(this.getFitness().get());
		return out;
	}

	@Override
	public MatrixEncoder getMatrixEncoder() {
		return this.m.getEncoder();
	}


	// TODO delete this also
	// This is for testing purposes ...
	@Override
	public float[][] getWeights() {
		System.err.println("Simple: will not give you weights");
		return null;
	}
	// TODO delete this too
	@Override
	public void setMatrix(float[][] m) {
		System.err.println("Simple: will not set let you set my matrix");
		//this.m.w = m;
	}
}
