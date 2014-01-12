package design.ea.strategies.impl;

import java.util.Random;
import design.ea.strategies.Mutation;
import design.ea.strategies.TwoGenomes;

public class UniformMutation implements Mutation{

	double p;
	double stdev = 1;	// standard deviation for float vectors
	Random r;
	
	public UniformMutation(){
		p = 0;
		r = new Random();
	}
	
	@Override
	public float[] mutate(float[] m, boolean isBinary) {
		float[] out = new float[m.length];
		float[] source = m;
		for(int i=0; i<source.length; i++){
			if(r.nextDouble()<p){
				if(isBinary){
					Math.abs(source[i]-1);	// flip the bit
				}else{
					out[i] = (float) (source[i] + r.nextGaussian()*stdev);
					//DU.pl("doing from this: "+source[i]+" this: "+out[i]);
				}
			}else{
				out[i] = source[i];
			}
		}
		return out;
	}

	@Override
	public void setPMut(double p) { this.p = p; }

	@Override
	public void setStdev(double stdev) { this.stdev = stdev; }

	@Override
	public TwoGenomes mutate(TwoGenomes genomes, boolean areBinary) {
		TwoGenomes tg = new TwoGenomes(this.mutate(genomes.a, areBinary),this.mutate(genomes.b, areBinary));
		return tg;
	}

}
