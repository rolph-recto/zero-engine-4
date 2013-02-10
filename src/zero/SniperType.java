//SniperType.java
package zero;

import engine.Entity;

class Sniper extends Player {
	
	//define health, weapon, power based on structure of player
	 public Sniper(){}
}

class SniperType extends PlayerType {
	
	private SniperType(){
		super();
	}
	
	public Entity createEntity() {
		return new Sniper();
	}
	
	public String getName() {
		return "sniper";
	}
	
	public String getModelName() {
		return "sniper";
	}

	public double getMaxVelocity(){
		return 12;//arbitrary max velocity for now	
	}
}
