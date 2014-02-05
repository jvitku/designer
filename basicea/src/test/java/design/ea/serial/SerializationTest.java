package design.ea.serial;

import static org.junit.Assert.*;

import org.junit.Test;

import tools.io.ind.SerialUtil;

import design.ea.algorithm.Population;
import design.ea.algorithm.impl.SingleRealVectorPop;
import design.ea.ind.individual.Individual;
import design.ea.ind.individual.impl.RealVectorSingleObj;

public class SerializationTest {

	public static String filename = "testfile.obj"; 

	@Test
	public void indSerialization(){
		float minVal = 0, maxVal = 1;
		int genomeSize = 5;
		boolean minimize = false;

		Individual ind = new RealVectorSingleObj(genomeSize, 
				minimize, minVal,maxVal);

		SerialUtil.save(filename, ind);
		Individual indR = (Individual)SerialUtil.read(filename);

		System.out.println(indR.toString());
		System.out.println(ind.toString());

		assertTrue(indR.equalsTo(ind));
	}

	@Test
	public void popSerialization(){
		int size = 10;
		int vectorLength = 10;
		boolean minimize = false;
		float minVal = 0, maxVal = 1;
		String popName = "population.obj";

		Population p = new 
				SingleRealVectorPop(size,vectorLength, minimize, minVal, maxVal);

		SerialUtil.save(popName, p);
		Population pop = (Population)SerialUtil.read(popName);

		p.equalsTo(pop);
	}
}
