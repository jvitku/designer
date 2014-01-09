package design.ea.matrix.hnn.simple;

import design.ea.matrix.SimpleEA;

public class HNNWMatrixEA extends SimpleEA{

	int len;
	public HNNWMatrixEA(int generations, int popSize, float maxw, float minw, int len) {
		super(0, 0, generations, popSize, maxw,minw);
		this.len = len;
	}

	@Override
	public void initPop() {
		for(int i=0; i<pop.size(); i++){
			pop.setInd(i, new HNNInd(max, min,len));
		}
	}
	
	public HNNInd getInd(){
		return (HNNInd)pop.get(actual);
	}
	

	public float[][] getWeights(){
		return ((HNNInd)pop.get(actual)).getWeights();
	}
	

	
}
