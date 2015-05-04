package design.models;

/**
 * Similar map to the paper, but smaller 10x10. 
 * 
 * @author Jaroslav Vitku
 *
 */
public class QLambdaPaper extends QLambdaTestSim{
	
	@Override
	public void defineMap(){
		this.noValues = 15;
		size = new int[]{noValues,noValues};	
		pos = new int[]{7,7};					// agents init position
		obstacles = new int[]{4,0,4,1,4,2,4,3, 10, 11,10,12,10,13,10,14,10};	// list of obstacles in the world
		rewards = new int[]{2,3,0,1};			// list of rewards (x,y,type,value,   x,y,type,value..)
	}
}
