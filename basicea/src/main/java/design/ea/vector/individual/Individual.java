package design.ea.vector.individual;

import design.ea.vector.encoding.matrix.MatrixEncoder;


public interface Individual extends Cloneable{

	public void init();
	
	//public AbstractWMatrix getMatrix();
	
	public MatrixEncoder getMatrixEncoder();
	
	public float[][] getWeights();
	
	public void setMatrix(float[][] m);
	
	public Fitness getFitness();
	
	public void randomize();

	public Individual clone();

}
