package design.ea.matrix.hnn.simple;

import java.util.Random;
import design.ea.matrix.encoding.matrix.MatrixEncoder;


/**
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
	int len;

	private SimpleLineEncoder te;

	/**
	 * constructor of first HNN encoding - triangle encoding - see ECMS paper
	 * @param INdim - num of inputs
	 * @param OUTdim - num of outputs
	 * @param N - alpha (sum of all inputs and outputs across all modules, excluding global IO)
	 * @param max - max weight
	 * @param min - min weight
	 */
	public HNNWMatrix(float max, float min, int len){
		l = new tools.utils.Logger("",true,false);
		this.len = len;
		this.INdim = 0;
		this.OUTdim = 0;

		rand = new Random();
		// build my custom encoder
		te = new SimpleLineEncoder(0,0,0,0,max,min,false,len);
		te.randomize();
	}

	public void randomize(){
		this.te.randomize();
	}

	public HNNWMatrix clone(){
		HNNWMatrix out = new HNNWMatrix(max, min,len);
		out.te = this.te.clone();
		out.te.setVector(this.te.getVector());
		return out;
	}

	public MatrixEncoder getEncoder(){
		return this.te;
	}


	public float[] getVector(){
		return this.te.getVector();	
	}


	public void setVector(float[] genome){
		this.te.setVector(genome);	
	}

	public static float[] getRangeFrom(float[] vector, int from, int to){
		
		int range = to-from;
		System.out.println("range requested is from to range: "+from+" "+to+" "+range);
		System.out.println("and vec len is: "+vector.length);
		float[] out = new float[range];

		for(int i=0; i<range; i++)
			out[i] = vector[from+i];

		return out;
	}
	public float[] getRange(int from, int to){
		//TODO checking here..
		float[] vector = this.te.getVector();
		int range = to-from;
		System.out.println("range requested is from to range: "+from+" "+to+" "+range);
		System.out.println("and vec len is: "+vector.length);
		float[] out = new float[range];

		for(int i=0; i<range; i++)
			out[i] = vector[from+i];

		return out;
	}
}
