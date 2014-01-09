package design.ea.matrix.hnn.impl;

import design.ea.matrix.encoding.matrix.AbstractMatrixEncoder;

public class TriangleEncoder extends AbstractMatrixEncoder implements Cloneable{

	private final int INdim,OUTdim,alpha;
	private final int numIns, numOuts;
	private float[][] inW,outW,w;
	private final boolean isBinary;
	
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
	public TriangleEncoder(int INdim, int OUTdim, int numIns, int numOuts, float max, float min, boolean binary){
		//@see article in ECMS2013 for encoding explanation
		super(min,max,((numIns+numOuts)*(numIns+numOuts)-(numIns+numOuts))/2 + INdim*numIns + OUTdim+numOuts,false);
		
		this.alpha = numIns+numOuts;
		this.INdim = INdim;
		this.OUTdim = OUTdim;
		this.numIns = numIns;
		this.numOuts = numOuts;
		this.isBinary = binary;
		
		this.inW = new float[numIns][INdim];	// input weights
		this.outW = new float[numOuts][OUTdim]; // output weights
		this.w = new float[alpha][alpha];		// upper triangle matrix encoding inner weights
	}
	
	public TriangleEncoder clone(){
		TriangleEncoder out = new TriangleEncoder(INdim, OUTdim, numIns, numOuts, INdim, numIns, isBinary); 
		out.setVector(this.getVector());
		return out;
	}
	
	public float[][] getWeights(){
		return this.w;
	}
	
	public float[][] getInWeights(){
		return this.inW;
	}
	
	public float[][] getOutWeights(){
		return this.outW;
	}
	
	
	/** 
	 * translate my three matrixes into linear vector for EA
	 */
	@Override
	public float[] getVector() {
		int poc = 0;	
		// encode inputs
		for(int i=0;i<INdim;i++){
			for(int j=0; j<numIns; j++){
				vector[poc++] = inW[j][i];
			}
		}
		// encode outputs
		for(int i=0;i<OUTdim;i++){
			for(int j=0; j<numOuts; j++){
				vector[poc++] = outW[j][i];
			}
		}
		// encode inner connections
		for(int i=0;i<alpha;i++){
			for(int j=0; j<alpha; j++){
				// upper triangle here
				if(i<j){
					vector[poc++] = w[i][j];
				}
			}
		}
		return vector;
	}

	@Override
	public void setVector(float[] genome) {
		if(genome.length != this.numWeights){
			System.err.println("TriangleEncoder: setVector(): error: vector length differ!");
			return;
		}
		int poc = 0;	
		// encode inputs
		for(int i=0;i<INdim;i++){
			for(int j=0; j<numIns; j++){
				inW[j][i] = genome[poc++];
			}
		}
		// encode outputs
		for(int i=0;i<OUTdim;i++){
			for(int j=0; j<numOuts; j++){
				outW[j][i] = genome[poc++];
			}
		}
		// encode inner connections
		for(int i=0;i<alpha;i++){
			for(int j=0; j<alpha; j++){
				// upper triangle here
				if(i<j){
					w[i][j] = genome[poc++];
				}else{
					w[i][j] = 0;
				}
			}
		}
	}
}
