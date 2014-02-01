package design.ea.ind.genome.vector;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import design.ea.ind.genome.vector.impl.RealVector;

public class Real {

	/**
	 * Test the range of generator
	 */
	@Test
	public void generator(){
		
		int len = 1000;
		
		int MAX =  100;
		int MIN = -100;
		
		Random r = new Random();
		
		float min = 0f, max = 1f, tmp;
		
		int attempts = 100; 
		
		for(int i=0; i<attempts; i++){
			RealVector v = new RealVector(len,max,min);
			assertTrue(len == v.size());
			this.checkRange(v, min, max);

			// randomly generate min/max boundaries
			min = MIN+r.nextInt(MAX-MIN);
			max = MIN+r.nextInt(MAX-MIN);
			
			if(min==max){
				min = MIN+r.nextInt(MAX-MIN);
				max = MIN+r.nextInt(MAX-MIN);	
			}
			if(min>max){
				tmp = max;
				max = min;
				min = tmp;
			}
		}
	}
	
	
	private void checkRange(RealVector v, float min, float max){
		for(int i=0; i<v.size(); i++)
			assertTrue(v.getVector()[i]>=min && v.getVector()[i]<max);
	}

}
