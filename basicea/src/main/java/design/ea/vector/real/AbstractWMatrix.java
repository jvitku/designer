package design.ea.vector.real;

import java.util.Random;

import design.ea.vector.encoding.matrix.MatrixEncoder;
import tools.utils.LU;

public abstract class AbstractWMatrix implements MatrixEncoder, Cloneable {
	
	protected final String me = "[HNN WMatrix]: ";
	public float[][]w;
	protected Random rand;
	protected int INdim, OUTdim,N;
	
	protected tools.utils.Logger l;
	
	public AbstractWMatrix(int INdim, int OUTdim, int N){
		l = new tools.utils.Logger("",true,false);
		
		this.INdim = INdim;
		this.OUTdim = OUTdim;
		this.N = N;
		System.out.println(" n is nowwwwwwwwwwwwwwwwwwww "+N);
		rand = new Random();
	}
	
	
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
	
	public int getN(){
		return this.N;
	}

}
