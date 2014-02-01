package old.design.ea.strategies;

import java.util.Random;

import tools.utils.DU;
import tools.utils.LU;
import design.ea.strategies.impl.RouletteWheel;
import design.ea.vector.Population;
import design.ea.vector.real.impl.Ind;

public class RouletteWheelTest {

	public static void main(String[] args){
		RouletteWheelTest rt = new RouletteWheelTest();
		rt.checkProbability();
	}
	
	public void checkProbability(){
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
			
			// make selection
			RouletteWheel s = new RouletteWheel();
			s.resetSelection(p);
			int poc = 100000;
			// all selected zero times
			int[] numSelected = new int[p.size()];
			for(int i=0; i<numSelected.length; i++)
				numSelected[i] = 0;
			for(int i=0; i<=poc; i++){
				numSelected[s.select(1)[0]]++;
			}
			DU.pl("inds with these fitness values: "+LU.toStr(p.getArrayOfFitnessVals()));
			DU.pl(" were selected these times:     "+LU.toStr(numSelected));
	}
	
	private void randomizeFitness(Population pop){
		int k = 1;
		Random r = new Random();
		for(int i=0;i<pop.size(); i++){
			pop.get(i).getFitness().set(k*r.nextFloat());
		}
	}
}
