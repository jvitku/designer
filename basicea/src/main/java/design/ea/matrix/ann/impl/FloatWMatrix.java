package design.ea.matrix.ann.impl;

import design.ea.matrix.ann.AbstractWMatrix;

public class FloatWMatrix extends AbstractWMatrix {

	float max, min;
	int numWeights;	
	
	float[] vector;
	
	public FloatWMatrix(int INdim, int OUTdim, int N, float max, float min) {
		super(INdim, OUTdim, N);
		this.max = max;
		this.min = min;
		this.numWeights = N*(INdim+OUTdim+N);
		vector = new float[this.numWeights];
		this.randomize();

	}
	
	public void randomize(){
		w = new float[N][INdim+OUTdim+N];
		for(int i=0;i<w.length; i++){
			for(int j=0; j<w[i].length; j++){
				w[i][j] = rand.nextFloat()*(max-min) +min;
			}
		}
	}

	@Override
	public float[] getVector() {
		vector=new float[numWeights];
		for(int i=0; i<w.length; i++){
			for(int j=0; j<w[0].length; j++){
				vector[i*w[0].length+j]=w[i][j];
			}
		}
		return vector;
	}

	@Override
	public void setVector(float[] genome) {
		for(int i=0; i<w.length; i++){
			for(int j=0; j<w[0].length; j++){
				vector[i] = genome[i];
				w[i][j] = genome[i*w[0].length+j];
			}
		}
	}

	@Override
	public int vectorLength() { return numWeights; }

	@Override
	public float getMmaxVal() { return max; }

	@Override
	public float getMinVal() { return min; }

	@Override
	public boolean isBinary() { return false; }
	
	public FloatWMatrix clone(){
		FloatWMatrix out = new FloatWMatrix(INdim, OUTdim, N, max, min);
		for(int i=0; i<w.length; i++){
			for(int j=0; j<w[0].length; j++){
				out.w[i][j] = this.w[i][j];
			}
		}
		return out;
	}
	
}
