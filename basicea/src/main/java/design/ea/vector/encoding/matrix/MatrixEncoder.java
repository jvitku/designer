package design.ea.vector.encoding.matrix;

public interface MatrixEncoder {
	
	public float[] getVector();	//fold the weight matrix into vector (genome) 
	public void setVector(float[] genome);	//build matrix of the vector (genome)
	public int vectorLength();			//get size of vector
	
	public float getMmaxVal();
	public float getMinVal();  
	
	public boolean isBinary();	// only binary weights allowed?
}
