package design.ea.vector.real;

import design.ea.vector.SimpleEA;
import design.ea.vector.real.impl.Ind;

public class WMatrixEA extends SimpleEA{

	private final int N;
	
	public WMatrixEA(int INdim, int OUTdim, int N, int generations, int popSize, 
			float maxw, float minw) {
		super(INdim, OUTdim, generations, popSize, maxw,minw);
		this.N = N;
	}

	@Override
	public void initPop() {
		for(int i=0; i<pop.size(); i++){
			pop.setInd(i, new Ind(INdim, OUTdim, N, max, min));
		}
	}
	
	
	public Ind getInd(){
		return (Ind)pop.get(actual);
	}


	public float[][] getWeights(){
		return ((AbstractWMatrix)pop.get(actual).getMatrixEncoder()).getWeights();
		//return pop.get(actual).getMatrix();
	}
	
	public float[] getInMatrixFor(int who){
		//return ((AbstractWMatrix)pop.get(actual).getMatrix()).getInMatrixNo(who);
		return ((AbstractWMatrix)pop.get(actual).getMatrixEncoder()).getInMatrixNo(who);
	}
	
	public float[] getOutMatrixFor(int who){
		return ((AbstractWMatrix)pop.get(actual).getMatrixEncoder()).getOutMatrixNo(who);
	}
	
}
