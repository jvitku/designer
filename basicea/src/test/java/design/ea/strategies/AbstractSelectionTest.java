package design.ea.strategies;

import static org.junit.Assert.*;

import org.junit.Test;

import design.ea.TestUtil;
import design.ea.algorithm.Population;
import design.ea.algorithm.impl.SingleVectorPop;

public class AbstractSelectionTest {

	@Test
	public void testSorting(){

		// EA setup
		int len = 20;
		float max = 100; float min = -100;

		// pop setup
		int num = 10;
		Population p = new SingleVectorPop(num, len, min, max);
		TestUtil.randomizeFitness(p);
		
		T t = new T();
		t.resetSelection(p);
		int [] sorted = t.sort();
		this.ps(p, sorted);

		// check sorted individuals
		for(int i=0; i<sorted.length-1; i++){
			for(int j=i+1; j<sorted.length; j++){
				assertFalse(p.get(sorted[j]).getFitness().betterThan(
						p.get(sorted[i]).getFitness()));
			}
		}

		System.out.println("Sorting OK");
	}

	private void ps(Population p, int[] sorted){
		for(int i=0; i<p.size(); i++){
			System.out.println(i+" "+ p.get(sorted[i]).toString());
		}
	}

	private class T extends AbstractSelection{

		@Override
		public int[] select(int howMany) {
			// Auto-generated method stub
			return null;
		}
	}

}
