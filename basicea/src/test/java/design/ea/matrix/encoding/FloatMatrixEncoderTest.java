package design.ea.matrix.encoding;

import tools.utils.DU;
import tools.utils.LU;
import design.ea.TestUtil;
import design.ea.vector.Population;
import design.ea.vector.real.impl.Ind;

public class FloatMatrixEncoderTest {

	public static void main(String [] args){
		FloatMatrixEncoderTest fmet = new FloatMatrixEncoderTest();
		
		if(!fmet.testTestClass())
			DU.pl("clonning test NOT passed");
		
		if(fmet.testEncoding())
			DU.pl("testing done");
		
	}


	public boolean testTestClass(){
		Population p = TestUtil.initPop();
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
	}


	public boolean testEncoding(){
		Population p = TestUtil.initPop();
		Population pp = p.clone();
		float[] tmp;
		for(int i=0; i<p.size(); i++){
			tmp = p.get(i).getMatrixEncoder().getVector(); // get genome
			//DU.pl("this matrix:    "+LU.toStr(p.get(i).getWeights()));
			//DU.pl("to this vector: "+LU.toStr(tmp));
			p.get(i).getMatrixEncoder().setVector(tmp);	// encode back into the matrix
			if(!TestUtil.weightsAreEqual((Ind)p.get(i),(Ind)pp.get(i))){
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
	}
	
}
