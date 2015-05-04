package design.models;

public class QLambdaTestSimSmall extends QLambdaTestSim{
	
	@Override
	public void defineMap(){
		this.noValues = 5;						// 5x5 map
		size = new int[]{noValues,noValues};	
		pos = new int[]{3,3};					// agents init position
		obstacles = new int[]{2,0,2,1,2,2};		// list of obstacles in the world
		rewards = new int[]{3,1,0,1,3,4,0,1};	// list of rewards (x,y,type,value,   x,y,type,value..)
	}
}
