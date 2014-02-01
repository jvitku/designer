package old.design.ea.matrix.encoding.MatrixEncoder;

import old.design.ea.TestUtil;
import tools.utils.DU;
import design.ea.strategies.impl.UniformMutation;
import design.ea.vector.Population;
import design.ea.vector.encoding.matrix.MatrixEncoder;

public class MutationTester {

	public static void main(String[] args){
		MutationTester mt = new MutationTester();
		if(mt.testFloatMuation()){
			DU.pl("DONE all test successfully passed");
		}else{
			DU.pl("DONE, some tests failed");
		}
	}
	
	public boolean testFloatMuation(){
		Population p = TestUtil.initPop();
		Population pp = p.clone();
		UniformMutation um = new UniformMutation();
		
		// do not mutate
		um.setPMut(0);
		this.mutatePopulation(p, um);
		if(!TestUtil.genomesAreEqual(p, pp)){
			DU.pl("Genomes were changed during mutation with p=0..");
			return false;
		}
		if(!TestUtil.weightsAreEqual(p, pp)){
			DU.pl("Genomes (weights) were changed during mutation with p=0..");
			return false;
		}
		
		// uniform mutation with 100% probability
		um.setPMut(1);
		p = pp.clone();
		this.mutatePopulation(p, um);
		
		if(!TestUtil.genesAllDiffer(p, pp)){
			DU.pl("All genes should differ while pMut=1..");
			return false;
		}
		if(!TestUtil.weightsAllDiffer(p, pp)){
			DU.pl("All weights should differ while pMut=1..");
			return false;
		}
		
		// test something between
		um.setPMut((float)0.5);
		p = pp.clone();
		this.mutatePopulation(p, um);
		if(TestUtil.genesAllDiffer(p, pp) || TestUtil.genomesAreEqual(p, pp)){
			DU.pl("Something is wrong with genomes..");
			return false;
		}
		if(TestUtil.weightsAllDiffer(p, pp) || TestUtil.weightsAreEqual(p, pp)){
			DU.pl("Something is wrong with weights..");
			return false;
		}
		return true;
	}
	
	private void mutatePopulation(Population p,UniformMutation um){
		MatrixEncoder in;
		float[] out;
		for(int i=0; i<p.size(); i++){
			in = p.get(i).getMatrixEncoder();				// read genome
			out = um.mutate(in.getVector(),false);					// mutate
			//DU.pl("in was : "+LU.toStr(in.getVector())+"\nout was : "+LU.toStr(out));
			p.get(i).getMatrixEncoder().setVector(out);	// write back to the matrix
			
			//DU.pl("new one: "+LU.toStr(p.get(i).getMatrix().getVector()));
		}
	}
	
}
