package design.ea.ind.individual;

import static org.junit.Assert.*;

import org.junit.Test;

import design.ea.ind.genome.vector.impl.RealVector;
import design.ea.ind.individual.impl.RealVectorSingleObj;

public class IndividualTest {
	
	@Test
	public void toStr(){
		int len=3;
		float min=0,max=1;
		
		Individual ind = new RealVectorSingleObj(len,true,min,max);
		Individual cloned = ind.clone();
		String str = cloned.toString();
		
		System.out.println("String received is: "+str);
		assertTrue(str.split(",").length==3);	// 2 commas
		assertTrue(str.contains("[xx]"));		// fitness not valid
		
		RealVector g = ((RealVector)ind.getGenome());
		g.setVector(new Float[]{0.111f, 1.222f, 3.4444f});
		ind.getFitness().setValid(true);
		
		str = cloned.toString();				// cloned one stays
		System.out.println("String received is: "+str);
		assertTrue(str.split(",").length==3);	// 2 commas
		assertTrue(str.contains("[xx]"));		// fitness not valid
		
		str = ind.toString();					// old one changes
		System.out.println("String received is: "+str);
		assertTrue(str.split(",").length==3);	// 2 commas
		assertTrue(str.contains("[ok]"));		// fitness is valid
		assertTrue(str.contains("0.111"));
		assertTrue(str.contains("1.222"));
		assertTrue(str.contains("3.4444"));
		
		ind.reset(true);						// randomize
		str = ind.toString();					// old one changes
		System.out.println("Randomized ind is: "+str);
		assertTrue(str.split(",").length==3);	// 2 commas
		assertTrue(str.contains("[xx]"));		// fitness not valid
		assertFalse(str.contains("0.111"));
		assertFalse(str.contains("1.222"));
		assertFalse(str.contains("3.4444"));
		
		ind.reset(false);						// randomize
		str = ind.toString();					// old one changes
		System.out.println("Reseted ind is: "+str);
		assertTrue(str.split(",").length==3);	// 2 commas
		assertTrue(str.contains("[xx]"));		// fitness not valid
		assertFalse(str.contains("0.111"));
		assertFalse(str.contains("1.222"));
		assertFalse(str.contains("3.4444"));
		
		System.out.println("Genome should be composed of: "+RealVector.DEF_VAL);
		assertTrue(str.contains(""+RealVector.DEF_VAL));
		
	}

}
