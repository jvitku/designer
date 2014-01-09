package design.ea;

import java.util.Random;

import tools.utils.DU;

import design.ea.matrix.Population;
import design.ea.matrix.ann.impl.Ind;
import design.ea.matrix.hnn.impl.HNNInd;
import design.ea.matrix.individual.Individual;

public class TestUtil {
	
	public static Population initHNNPop(){
		// EA setup
		int INdim = 2; int OUTdim = 1;
		int numIns = 4;
		int numOuts = 3;
		
		float max = 100; float min = -100;

		// pop setup
		int num = 10;
		Population p = new Population(num);

		for(int i=0; i<num;i++){
			p.setInd(i, new HNNInd(INdim, OUTdim, numIns, numOuts, max, min));
		}

		TestUtil.randomizeFitness(p);
		return p;
	}
	
	public static Population initPop(){
		// EA setup
		int INdim = 1; int OUTdim = 2;
		int neurons=4;
		float max = 100; float min = -100;

		// pop setup
		int num = 10;
		Population p = new Population(num);

		for(int i=0; i<num;i++){
			p.setInd(i,new Ind(INdim, OUTdim, neurons, max, min));
		}

		TestUtil.randomizeFitness(p);
		return p;
	}

	public static void randomizeFitness(Population pop){
		int k = 1;
		Random r = new Random();
		for(int i=0;i<pop.size(); i++){
			pop.get(i).getFitness().set(k*r.nextFloat());
		}
	}
	
	public static boolean weightsAreEqual(Population a, Population b){
		if(a.size() != b.size())
			return false;
		for(int i=0; i<a.size(); i++){
			if(!weightsAreEqual((Ind)a.get(i) ,(Ind)b.get(i)))
				return false;
		}
		return true;
	}
	
	public static boolean genomesAreEqual(Population a, Population b){
		if(a.size() != b.size())
			return false;
		for(int i=0; i<a.size(); i++){
			if(!genomesAreEqual((Individual)a.get(i) ,(Individual)b.get(i)))
				return false;
		}
		return true;
	}

	public static boolean genomesAreEqual(Individual a, Individual b){
		float[] wa = a.getMatrixEncoder().getVector();
		float[] wb = b.getMatrixEncoder().getVector();
		for(int i=0; i<wa.length; i++){
			if(wa[i] != wb[i])
				return false;
		}
		return true;
	}
	
	public static boolean genesAllDiffer(Population a, Population b){
		if(a.size() != b.size())
			return false;
		for(int i=0; i<a.size(); i++){
			if(!genesAllDiffer((Individual)a.get(i) ,(Individual)b.get(i)))
				return false;
		}
		return true;
	}
	
	public static boolean genesAllDiffer(Individual a, Individual b){
		float[] wa = a.getMatrixEncoder().getVector();
		float[] wb = b.getMatrixEncoder().getVector();
		for(int i=0; i<wa.length; i++){
			if(wa[i] == wb[i])
				return false;
		}
		return true;
	}

	public static boolean weightsAreEqual(Individual a, Individual b){
		float[][] wa = a.getWeights();
		float[][] wb = b.getWeights();
		for(int i=0; i<wa.length; i++){
			for(int j=0; j<wa[0].length; j++){
				if(wa[i][j] != wb[i][j]){
					DU.pl("I do not like these two: "+wa[i][j]+" "+wb[i][j]+" i"+i+" "+j);
					return false;
				}
			}
		}
		return true;
	}
	
	public static boolean weightsAllDiffer(Individual a, Individual b){
		float[][] wa = a.getWeights();
		float[][] wb = b.getWeights();
		for(int i=0; i<wa.length; i++){
			for(int j=0; j<wa[0].length; j++){
				if(wa[i][j] == wb[i][j]){
					DU.pl("Found two identical weights: "+wa[i][j]+" "+wb[i][j]+" i"+i+" "+j);
					return false;
				}
			}
		}
		return true;
	}

	public static boolean weightsAllDiffer(Population a, Population b){
		if(a.size() != b.size())
			return false;
		for(int i=0; i<a.size(); i++){
			if(!weightsAllDiffer((Ind)a.get(i) ,(Ind)b.get(i)))
				return false;
		}
		return true;
	}
	
	
}
