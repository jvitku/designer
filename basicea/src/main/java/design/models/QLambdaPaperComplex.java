package design.models;

/**
 * Similar map to the paper, but smaller 10x10. 
 * 
 * @author Jaroslav Vitku
 *
 */
public class QLambdaPaperComplex extends QLambdaTestSim{
	
	@Override
	public void defineMap(){
		this.noValues = 15;
		size = new int[]{noValues,noValues};	
		pos = new int[]{7,7};					// agents init position
		obstacles = new int[]{4,0,4,1,4,2,4,3,4,4,4,5,4,6,4,7,4,8,  3,8,2,8,
				10,10, 10,11,10,12,10,13,10,14};	// list of obstacles in the world
		rewards = new int[]{2,3,0,1};			// list of rewards (x,y,type,value,   x,y,type,value..)
	}
}
