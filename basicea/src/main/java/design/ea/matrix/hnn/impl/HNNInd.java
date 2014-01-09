package design.ea.matrix.hnn.impl;

import design.ea.matrix.encoding.matrix.MatrixEncoder;
import design.ea.matrix.individual.Fitness;
import design.ea.matrix.individual.Individual;
import design.ea.matrix.individual.impl.InverseError;

public class HNNInd implements Individual{

	public HNNWMatrix m;
	Fitness f; 
	int INdim, OUTdim;
	float max, min;
	int numIns, numOuts;

	public HNNInd(int INdim, int OUTdim, int numIns, int numOuts, float max, float min){
		this.INdim = INdim;
		this.OUTdim = OUTdim;
		this.numIns = numIns; 
		this.numOuts = numOuts;
		this.max = max;
		this.min = min;

		this.init();
	}

	@Override
	public void init() {
		m = new HNNWMatrix(INdim,OUTdim,numIns,numOuts,max,min);
		f = new InverseError();
	}

	@Override
	public void randomize() {
		m.randomize();
		f.reset();
	}

	public void printMatrix(){
		float[][] inw = this.m.getInW();
		float[][] outw = this.m.getOutW();
		float[][] w = this.m.getW();
		int alpha = numIns+numOuts;
		String str = "";
		
		System.out.println("sizes are: ni,no len inw len inw[0]"+numIns+" "+numOuts+" "+inw.length+" "+inw[0].length);
		for(int i=0; i<alpha; i++){	// rows
			for(int j=0; j<(alpha+INdim+OUTdim); j++){ //columns
				if(j<alpha){
					if(w[i][j]==0)
						str = str +" \t"+ 0.0011011;
					else
						str = str +" \t"+ w[i][j];
				}else if(j<alpha+INdim){
					if(i<numIns){
						str = str + " \t" +inw[i][j-alpha];
					}else
						str += " \t_________";
				}else{
					if(i<numOuts)
						str = str + " \t"+outw[i][j-alpha-INdim];
					else
						str += " \t_________";
				}/*
				if(j<numIns){
					str = str + inw[i][j];
					
				}else if(j<numOuts+numIns){
					str = str + outw[i][j-numIns];
				}else{
					str = str + w[i][j-numIns-numOuts];
				}*/
				if(j==alpha || j==(alpha+numIns))
					str += " \t";
			}
			str+="\n";
		}
		System.out.println("WEights are:: \n"+str);
	}


	@Override
	public Fitness getFitness() {
		return f;
	}

	public void setFitness(float val){
		f.set(val);
	}
	/*	
	public void printMatrix(){
		System.out.println(" "+LU.toStr(m.w));
	}
	 */
	@Override
	public HNNInd clone(){
		HNNInd out = new HNNInd(INdim,OUTdim,numIns,numOuts,max,min);
		out.m = this.m.clone();
		out.setFitness(this.getFitness().get());
		return out;
	}

	@Override
	public MatrixEncoder getMatrixEncoder() {
		return this.m.getEncoder();
	}


	// TODO delete this also
	// This is for testing purposes ...
	@Override
	public float[][] getWeights() {
		System.err.println("HNNInd: I do not support this, returning only inner weights..");
		return null;
	}
	// TODO delete this too
	@Override
	public void setMatrix(float[][] m) {
		//System.err.println("OK, setting my inner w matrix to these vals");
		System.err.println("HNNWmatrix: I dont support setting matrix");
		//this.m.w = m;
	}
}
