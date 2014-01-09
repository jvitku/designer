package design.ea.matrix.hnn.impl;

import java.util.Random;
import design.ea.matrix.encoding.matrix.MatrixEncoder;


/**
 * 1) represents weights in the matrix
 * 2) its custom matrix encoder does the actual encoding
 * 3) this is access point from the jython script (methods like: give me weights for..)
 *  
 * @author Jaroslav Vitku
 *
 */
public class HNNWMatrix implements /*MatrixEncoder,*/ Cloneable {
	
	protected final String me = "[WMatrix]: ";
	//public float[][]w;
	protected Random rand;
	protected int INdim, OUTdim;
	
	protected tools.utils.Logger l;
	private float min, max;
	private int numIns,numOuts;
	
	private TriangleEncoder te;
	
	/**
	 * constructor of first HNN encoding - triangle encoding - see ECMS paper
	 * @param INdim - num of inputs
	 * @param OUTdim - num of outputs
	 * @param N - alpha (sum of all inputs and outputs across all modules, excluding global IO)
	 * @param max - max weight
	 * @param min - min weight
	 */
	public HNNWMatrix(int INdim, int OUTdim, int numIns, int numOuts, float max, float min){
		l = new tools.utils.Logger("",true,false);
		
		this.INdim = INdim;
		this.OUTdim = OUTdim;
		
		rand = new Random();
		// build my custom encoder
		te = new TriangleEncoder(INdim,OUTdim,numIns,numOuts,max,min,false);
		te.randomize();
	}
	
	public void randomize(){
		this.te.randomize();
	}

	public HNNWMatrix clone(){
		HNNWMatrix out = new HNNWMatrix(INdim, OUTdim, numIns, numOuts, max, min);
		out.te = this.te.clone();
		out.te.setVector(this.te.getVector());
		return out;
	}
	
	public MatrixEncoder getEncoder(){
		return this.te;
	}
	
	public void setVector(float[] genome){
		this.te.setVector(genome);	
	}
	
	public float[][] getInW(){
		return this.te.getInWeights();
	}
	
	public float[][] getOutW(){
		return this.te.getOutWeights();
	}
	
	public float[][] getW(){
		return this.te.getWeights();
	}
	////////////// hardwired here!
	
	public float[][] getRecurrent(){
		float[][] w = new float[2][2];
		float[][] hiddenW = this.getW();
	    w[0][0] = hiddenW[3][4];
	    w[0][1] = hiddenW[4][5];
	    w[1][0] = hiddenW[3][6];
	    w[1][1] = hiddenW[5][6];
		return w;
	}
	/*
	
	public float[][] get2DInMatrixNo(int no){
		if(no>INdim)
			System.err.println(me+"too big no. of input dimension");
		
		float[][] in = new float[N][1];
		for(int i=0; i<N; i++)
			in[i][0] = w[no][i];
		l.pl("someone asked for in Matrix no: "+no+" it is: \n"+LU.toStr(in)+"\n------------------------");
		return in;
	}
	
	
	public float[][] get2DOutMatrixNo(int no){
		if(no>OUTdim)
			System.err.println(me+"too big no. of output dimension");
		float[][] out = new float[1][N];
		for(int i=0; i<N; i++)
			out[0][i] = w[i][INdim+no];
		l.pl("someone asked for out Matrix no: "+no+" it is: \n"+LU.toStr(out)+"\n------------------------");

		return out;
	}
	
	public float[] getInMatrixNo(int no){
		if(no>INdim)
			System.err.println(me+"too big no. of input dimension");
		
		float[] in = new float[N];
		for(int i=0; i<N; i++)
			in[i] = w[no][i];
		l.pl("someone asked for in Matrix no: "+no+" it is: \n"+LU.toStr(in)+"\n------------------------");
		return in;
	}
	
	public float[] getOutMatrixNo(int no){
		if(no>OUTdim)
			System.err.println(me+"too big no. of output dimension");
		float[] out = new float[N];
		for(int i=0; i<N; i++)
			out[i] = w[i][INdim+no];
		l.pl("someone asked for out Matrix no: "+no+" it is: \n"+LU.toStr(out)+"\n------------------------");

		return out;
	}
	
	public float[][] getWeights(){
		
		float[][] out = new float[N][N];
		for(int i=0; i<N; i++)
			for(int j=0; j<N; j++)
				out[i][j] = w[i][INdim+OUTdim+j];
		
		l.pl("someone asked for weight Matrix, it is: \n"+LU.toStr(out)+"\n------------------------");

		return out;
	}
	*/

}
