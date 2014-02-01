package design.ea.encoding.matrix;

import java.util.Random;

import tools.utils.Logger;

public abstract class AbstractMatrixEncoder implements MatrixEncoder{

	protected final tools.utils.Logger l;
	protected final boolean isBinary;
	protected final float min, max;
	protected final int numWeights;
	private final Random r;
	protected final float[] vector; 
	
	public AbstractMatrixEncoder(float min, float max, int numWeights, boolean isBinary){
		this.isBinary = isBinary; 
		this.min = min;
		this.max = max;
		this.numWeights = numWeights;
		this.vector = new float[numWeights];
		
		this.l = new Logger();
		this.r = new Random();
	}
	
	@Override
	public abstract float[] getVector();

	@Override
	public abstract void setVector(float[] genome);
	
	
	@Override
	public int vectorLength() {
		return this.numWeights;
	}

	@Override
	public float getMmaxVal() {
		return this.max;
	}

	@Override
	public float getMinVal() {
		return this.min;
	}

	@Override
	public boolean isBinary() {
		return isBinary;
	}

	/**
	 * randomize my genome and encode into custom data type (setVector())
	 */
	public void randomize(){
		if(this.isBinary){
			for(int i=0; i<vector.length; i++)
				if(r.nextBoolean())
					vector[i] = 1;
				else
					vector[i] = 0;
		}else{
			for(int i=0; i<vector.length; i++)
				vector[i] = r.nextFloat();
		}
		this.setVector(vector);
	}


}
