package design.ea.matrix.hnn.simple;

import design.ea.matrix.encoding.matrix.AbstractMatrixEncoder;

public class SimpleLineEncoder extends AbstractMatrixEncoder implements Cloneable{

	private float[] vector;
	
	private final int INdim,OUTdim,alpha;
	private final int numIns, numOuts;
	private float[][] inW,outW,w;
	private final boolean isBinary;
	private int len;
	
	/**
	 * This class encodes linear genome into my custom encoding for hybrid networks,
	 * IN matrix, OUT matrix and upper triangle matrix encoding inner connections between
	 * explicitly represented inputs and outputs of MIMO modules, 
	 * encoding is well described in paper in ECMS 2013.
	 * 
	 * @param INdim - number of inputs to the architecture
	 * @param OUTdim - number of outputs of the architecture
	 * @param numIns - total number of inputs of modules (excluding global IO)
	 * @param numOuts - total number of outputs of all modules
	 * @param max - max value of weight
	 * @param min - min value of weight
	 */
	public SimpleLineEncoder(int INdim, int OUTdim, int numIns, int numOuts, float max, float min, boolean binary,int len){
		//@see article in ECMS2013 for encoding explanation
		super(min,max,len,false);
		
		this.alpha = numIns+numOuts;
		this.INdim = INdim;
		this.OUTdim = OUTdim;
		this.numIns = numIns;
		this.numOuts = numOuts;
		
		this.isBinary = binary;
		
		//this.inW = new float[numIns][INdim];	// input weights
		//this.outW = new float[numOuts][OUTdim]; // output weights
		//this.w = new float[alpha][alpha];		// upper triangle matrix encoding inner weights
		this.len = len;
		this.vector = new float[len];
	}
	
	public SimpleLineEncoder clone(){
		SimpleLineEncoder out = new SimpleLineEncoder(INdim, OUTdim, numIns, numOuts, INdim, numIns, isBinary,len); 
		out.setVector(this.getVector());
		return out;
	}
	
	public float[] getWeights(){
		return this.vector;
	}
	
	/** 
	 * translate my three matrixes into linear vector for EA
	 */
	@Override
	public float[] getVector() {
		return vector;
	}

	@Override
	public void setVector(float[] genome) {
		this.vector = genome.clone();
	}
}
