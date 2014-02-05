package design.ea.ind.genome.vector.impl;

import ctu.nengoros.util.SL;
import design.ea.ind.genome.Genome;
import design.ea.ind.genome.vector.AbsVectorGenome;

/**
 * Vector of boolean values.
 * 
 * @author Jaroslav Vitku
 *
 */
public class BinaryVector extends AbsVectorGenome<Boolean>{

	private static final long serialVersionUID = 5136290466672292081L;
	public static boolean DEF_VAL = false;
	private Boolean[] vals;

	public BinaryVector(int size) {
		super(size);

		this.vals = new Boolean[this.size];
		this.reset(true);
	}

	@Override
	public void setVector(Boolean[] values){
		if(values.length != this.size){
			System.err.println("ERROR: cannot change the size of the vector, " +
					"expected size is: "+this.size+", not "+values.length);
			return;
		}
		this.vals = values.clone();
	}

	@Override
	public Boolean[] getVector() { return this.vals; }

	@Override
	public void reset(boolean randomize) {
		if(randomize){
			for(int i=0; i<size; i++){
				vals[i] = r.nextBoolean();
			}
		}else{
			for(int i=0; i<size; i++){
				vals[i] = DEF_VAL;
			}
		}
	}

	@Override
	public String toString(){ return SL.toStr(vals); }

	@Override
	public Genome clone(){
		BinaryVector out = new BinaryVector(size);
		for(int i=0; i<vals.length; i++){
			out.vals[i] = vals[i].booleanValue();
		}
		return out;
	}

	@Override
	public boolean equalsTo(Genome target) {
		if(!(target instanceof BinaryVector))
			return false;
		boolean a,b;
		for(int i=0; i<vals.length; i++){
			a = ((BinaryVector)target).getVector()[i].booleanValue();
			b = vals[i].booleanValue();
			if(a!=b)
				return false;
		}
		return true;
	}
}
