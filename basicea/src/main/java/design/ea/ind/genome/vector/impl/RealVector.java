package design.ea.ind.genome.vector.impl;

import ctu.nengoros.util.SL;
import design.ea.ind.genome.vector.AbsVectorGenome;

public class RealVector extends AbsVectorGenome<Float>{

	public static float DEF_VAL = 0.0f;
	private Float[] vals;

	public RealVector(int size, Float minVal, Float maxVal) {
		super(size, minVal, maxVal);
		
		this.vals = new Float[this.size];
		this.reset(true);
	}

	@Override
	public void setVector(Float[] values) {
		if(values.length != this.size){
			System.err.println("ERROR: cannot change the size of the vector, " +
					"expected size is: "+this.size+", not "+values.length);
			return;
		}
		this.vals = values.clone();
	}

	@Override
	public Float[] getVector() { return this.vals; }

	@Override
	public void reset(boolean randomize) {
		if(randomize){
			for(int i=0; i<size; i++){
				vals[i] = this.getValidRandVal();
			}
		}else{
			for(int i=0; i<size; i++){
				vals[i] = DEF_VAL;
			}
		}
	}
	
	public float getValidRandVal(){
		double diff = (double)maxVal-(double)minVal;
		return (float) (minVal+r.nextDouble()*diff);
	}
	
	@Override
	public String toString(){ return SL.toStr(vals); }
}
