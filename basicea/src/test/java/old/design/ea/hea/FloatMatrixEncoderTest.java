package old.design.ea.hea;

import old.design.ea.TestUtil;
import tools.utils.DU;
import tools.utils.LU;
import design.ea.vector.Population;
import design.ea.vector.individual.Individual;
import design.ea.vector.real.impl.Ind;

/**
 * 
 * @author Jaroslav Vitku
 *
 */
public class FloatMatrixEncoderTest {
/*
	public static void main(String[] args){
		FloatMatrixEncoderTest fmet = new FloatMatrixEncoderTest();
		Population p = TestUtil.initHNNPop();
		
		DU.pl("aaaaaaaaaaa: "+LU.toStr(p.get(0).getMatrixEncoder().getVector()));
		float[] vec = p.get(0).getMatrixEncoder().getVector();
		for(int i=0; i<vec.length; i++){
			vec[i] = i;
		}
		p.get(0).getMatrixEncoder().setVector(vec);
		DU.pl("bbbbbbbbb: "+LU.toStr(p.get(0).getMatrixEncoder().getVector()));
	}
	*/
	/*
	public static void main(String [] args){
		FloatMatrixEncoderTest fmet = new FloatMatrixEncoderTest();
		
		if(!fmet.testTestClass())
			DU.pl("clonning test NOT passed");
		
		if(fmet.testEncoding())
			DU.pl("testing done");
		
	}*/
/*
	//TODO: no testing so far..
	public boolean testTestClass(){
		Population p = TestUtil.initHNNPop();
		Population pp = p.clone();
		if(!TestUtil.weightsAreEqual(p,pp)){
			DU.pl("ERROR: clonning of weights does not work!");
			return false;
		}
		if(!TestUtil.genomesAreEqual(p,pp)){
			DU.pl("ERROR: clonning of genomes or encoding does not work!");
			return false;
		}
		return true;
	}*//*

	//TODO: no testing so far..
	public boolean testEncoding(){
		Population p = TestUtil.initHNNPop();
		Population pp = p.clone();
		float[] tmp;
		for(int i=0; i<p.size(); i++){
			tmp = p.get(i).getMatrixEncoder().getVector(); // get genome
			//DU.pl("this matrix:    "+LU.toStr(p.get(i).getWeights()));
			//DU.pl("to this vector: "+LU.toStr(tmp));
			p.get(i).getMatrixEncoder().setVector(tmp);	// encode back into the matrix
			if(!TestUtil.weightsAreEqual((Individual)p.get(i),(Individual)pp.get(i))){
				DU.pl("ERROR: encode/decode WM does not produce the same result!!");
				DU.pl(" p wm: "+LU.toStr(p.get(i).getWeights())+"\n");
				DU.pl("pp wm: "+LU.toStr(p.get(i).getWeights()));
				
				return false;
			}
			if(!TestUtil.genomesAreEqual((Ind)p.get(i),(Ind)pp.get(i))){
				DU.pl("ERROR: genomes differ!!");
				return false;
			}
		}
		return true;
	}*/
	
}
