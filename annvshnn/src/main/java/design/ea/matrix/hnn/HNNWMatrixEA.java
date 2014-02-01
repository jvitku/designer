package design.ea.matrix.hnn;

import design.ea.matrix.SimpleEA;
import design.ea.matrix.hnn.impl.HNNInd;

public class HNNWMatrixEA extends SimpleEA{

	private final int numIns,numOuts;
	
	public HNNWMatrixEA(int INdim, int OUTdim, int numIns,int numOuts, int generations, int popSize, 
			float maxw, float minw) {
		super(INdim, OUTdim, generations, popSize, maxw,minw);
		
		this.numIns = numIns;
		this.numOuts = numOuts;
	}

	@Override
	public void initPop() {
		for(int i=0; i<pop.size(); i++){
			pop.setInd(i, new HNNInd(INdim, OUTdim, numIns,numOuts, max, min));
		}
	}
	
	public HNNInd getInd(){
		return (HNNInd)pop.get(actual);
	}
	

	public float[][] getWeights(){
		return ((HNNInd)pop.get(actual)).getWeights();
	}
	
	public float[][] getInMatrix(){
		return ((HNNInd)pop.get(actual)).m.getInW();
	}
	
	public float[][] getOutMatrixFor(){
		return ((HNNInd)pop.get(actual)).m.getOutW();
	}

	
}
