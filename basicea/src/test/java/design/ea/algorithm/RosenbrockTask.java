package design.ea.algorithm;

import design.ea.algorithm.impl.SimpleEA;

public class RosenbrockTask {

	public void setup(){
		// EA setup
		int len = 20;
		float max = 100; float min = -100;
		int popSize = 10;
		int gens = 100;
		
		float minw = -2, maxw = 2;	
		
		SimpleEA ea = new SimpleEA(len, gens, popSize, minw, maxw);
		
		// TODO continue here 

	}

}
