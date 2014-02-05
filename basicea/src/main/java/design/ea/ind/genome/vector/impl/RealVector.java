package design.ea.ind.genome.vector.impl;

import ctu.nengoros.util.SL;
import design.ea.ind.genome.Genome;
import design.ea.ind.genome.vector.AbsVectorGenome;
/**
 * Vector of float values. 
 * 
 * @author Jaroslav Vitku
 *
 */
public class RealVector extends AbsVectorGenome<Float>{

	private static final long serialVersionUID = -2247234427088451531L;
	public static float DEF_VAL = 0.0f;
	private Float[] vals;
	private float minVal, maxVal;

	public RealVector(int size, float minVal, float maxVal) {
		super(size);

		this.minVal = minVal;
		this.maxVal = maxVal;
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

	@Override
	public Genome clone(){
		RealVector out = new RealVector(size, minVal, maxVal);
		for(int i=0; i<vals.length; i++){
			out.vals[i] = vals[i].floatValue();
		}
		return out;
	}

	public void setMaxVal(float maxVal) { this.maxVal = maxVal; }

	public void setMinVal(float minVal) { this.minVal = minVal; }

	public float getMaxVal() { return this.maxVal; }

	public float getMinVal() { return this.minVal; }

	@Override
	public boolean equalsTo(Genome target) {
		if(!(target instanceof RealVector))
			return false;
		float a,b;
		for(int i=0; i<vals.length; i++){
			a = ((RealVector)target).getVector()[i].floatValue();
			b = vals[i].floatValue();
			if(a!=b)
				return false;
		}
		return true;
	}
}
