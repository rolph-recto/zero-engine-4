//HeavyType.java

package zero;

import engine.Entity;

class Heavy extends Player {
	
	//need to define health and weapon, and power based on the player 
	 public Heavy(){}
}

class HeavyType extends PlayerType {
	private HeavyType(){
		super();
	}
	
	public Entity createEntity() {
		return new Heavy();
	}
	
	public String getName() {
		return "heavy";
	}
	
	public String getModelName() {
		return "heavy";
	}
	
	public double getMaxVelocity(){
		return 12;//arbitrary max velocity for now
	}
}
