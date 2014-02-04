package design.ea;

import design.ea.algorithm.Population;
import design.ea.ind.genome.vector.impl.RealVector;
import design.ea.ind.individual.Individual;

public class TestUtil {

	public static void randomizeFitness(Population pop){
		for(int i=0;i<pop.size(); i++){
			pop.get(i).getFitness().reset(true);
			// just not to receive warnings:
			pop.get(i).getFitness().setValid(true);	
		}
	}
	
	/*
	// TODO this could be implemented for testing the encoding
	public static boolean weightsAreEqual(Population a, Population b){
		if(a.size() != b.size())
			return false;
		for(int i=0; i<a.size(); i++){
			if(!weightsAreEqual((Ind)a.get(i) ,(Ind)b.get(i)))
				return false;
		}
		return true;
	}
	*/
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
		Float[] wa = ((RealVector)a.getGenome()).getVector();
		Float[] wb = ((RealVector)b.getGenome()).getVector();
		
		for(int i=0; i<wa.length; i++){
			if(wa[i].floatValue() != wb[i].floatValue())
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
		Float[] wa = ((RealVector)a.getGenome()).getVector();
		Float[] wb = ((RealVector)b.getGenome()).getVector();
		
		for(int i=0; i<wa.length; i++){
			if(wa[i] == wb[i])
				return false;
		}
		return true;
	}

	/*
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
	}*/
	
	/*
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
	}*/
	
	
}
