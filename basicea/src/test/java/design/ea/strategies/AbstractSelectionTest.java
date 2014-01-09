package design.ea.strategies;

import static org.junit.Assert.*;

import java.util.Random;
import org.junit.Test;
import tools.utils.DU;
import design.ea.matrix.Population;
import design.ea.matrix.ann.impl.Ind;

public class AbstractSelectionTest {
	
	@Test
	public void testSorting(){

		// EA setup
		int INdim = 10;
		int OUTdim = 20;
		int neurons=100;
		float max = 100; float min = -100;

		// pop setup
		int num = 10;
		Population p = new Population(num);
		
		for(int i=0; i<num;i++){
			p.setInd(i,new Ind(INdim, OUTdim, neurons, max, min));
		}
		
		this.randomizeFitness(p);
		T t = new T();
		t.resetSelection(p);
		int [] sorted = t.sort();
		this.ps(p, sorted);
		
		// check sorted args
		for(int i=0; i<sorted.length-1; i++){
			for(int j=i+1; j<sorted.length; j++){
				assertTrue(p.get(sorted[i]).getFitness().get()
						>= p.get(sorted[j]).getFitness().get());
			}
		}
		
		System.out.println("tests done, OK:)");
	}
	
	private void ps(Population p, int[] sorted){
		for(int i=0; i<p.size(); i++){
			System.out.println(i+" "+p.get(sorted[i]).getFitness().get());
		}
	}
	
	private void randomizeFitness(Population pop){
		int k = 1;
		Random r = new Random();
		for(int i=0;i<pop.size(); i++){
			pop.get(i).getFitness().set(k*r.nextFloat());
		}
	}
	
	private class T extends AbstractSelection{

		@Override
		public int[] select(int howMany) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

	public static void main(String[] args){
		DU.p("checking");
		
		AbstractSelectionTest ast = new AbstractSelectionTest();
		ast.testSorting();
	}
	
}
