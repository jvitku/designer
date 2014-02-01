package design.ea.ind.genome.vector.impl;

import design.ea.ind.genome.vector.AbsVectorGenome;

/**
 * Genome represented by the vector of boolean variables.
 * 
 * @author Jaroslav Vitku
 *
 */
public class BinaryVector  extends AbsVectorGenome<Boolean>{

	public static boolean DEF_VAL = false;
	private Boolean[] vals;

	public BinaryVector(int size, Boolean minVal, Boolean maxVal) {
		super(size, minVal, maxVal);

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

}
