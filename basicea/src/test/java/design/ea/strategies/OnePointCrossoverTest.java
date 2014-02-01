package design.ea.strategies;

import tools.utils.DU;
import tools.utils.LU;
import design.ea.TestUtil;
import design.ea.strategies.impl.OnePointCrossover;
import design.ea.vector.Population;

public class OnePointCrossoverTest {
	public static void main(String[] args){
		OnePointCrossoverTest mt = new OnePointCrossoverTest();
		if(mt.testUniformCrossover()){
			DU.pl("DONE all test successfully passed");
		}else{
			DU.pl("DONE, some tests failed");
		}
	}
	
	public boolean testUniformCrossover(){
		Population p = TestUtil.initPop();
		Population pp = p.clone();
		OnePointCrossover opc = new OnePointCrossover();
		
		// do not cross at all
		opc.setPCross(0);
		this.crossPopulation(p, opc);
		if(!TestUtil.genomesAreEqual(p, pp)){
			DU.pl("Genomes were changed during crossover with p=0..");
			return false;
		}
		if(!TestUtil.weightsAreEqual(p, pp)){
			DU.pl("Genomes (weights) were changed during crossover with p=0..");
			return false;
		}

		DU.pl("00000000000000000000000");
		// cross all
		opc.setPCross(1);
		this.crossPopulation(p, opc);
		
		return true;
	}
	
	
	private void crossPopulation(Population p,OnePointCrossover opc){
		float[] a;
		float[] b;
		for(int i=0; i<p.size()-1; i++){
			a = p.get(i).getMatrixEncoder().getVector();				// read genomes
			b = p.get(i+1).getMatrixEncoder().getVector();				// read genomes
			
			TwoGenomes cr = opc.cross(a, b);
			
			DU.pl("-------");
			DU.pl("Crossed this: "+LU.toStr(a));
			DU.pl("Wiiith this:  "+LU.toStr(b));
			
			DU.pl("and got this: "+LU.toStr(cr.a));
			DU.pl("and     this: "+LU.toStr(cr.b));
			
			p.get(i).getMatrixEncoder().setVector(cr.a);				// store them back
			p.get(i+1).getMatrixEncoder().setVector(cr.b);
		}
	}
}
