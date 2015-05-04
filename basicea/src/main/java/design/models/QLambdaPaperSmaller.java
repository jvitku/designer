package design.models;

/**
 * Similar map to the paper, but smaller 10x10. 
 * 
 * @author Jaroslav Vitku
 *
 */
public class QLambdaPaperSmaller extends QLambdaTestSim{
	
	@Override
	public void defineMap(){
		this.noValues = 10;						// 5x5 map
		size = new int[]{noValues,noValues};	
		pos = new int[]{4,4};					// agents init position
		obstacles = new int[]{2,0,2,1,5,8,5,9};	// list of obstacles in the world
		rewards = new int[]{1,1,0,1};			// list of rewards (x,y,type,value,   x,y,type,value..)
	}
}
